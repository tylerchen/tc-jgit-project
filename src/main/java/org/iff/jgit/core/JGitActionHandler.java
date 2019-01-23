/*******************************************************************************
 * Copyright (c) Sep 24, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.jgit.core;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.iff.infra.util.*;
import org.iff.infra.util.mybatis.service.RepositoryService;
import org.iff.jgit.core.servlet.bridge.NettyFilterChain;
import org.iff.jgit.core.servlet.bridge.NettyHttpServletRequest;
import org.iff.jgit.core.servlet.bridge.NettyHttpServletResponse;
import org.iff.jgit.core.servlet.bridge.WebApplication;
import org.iff.netty.server.ProcessContext;
import org.iff.netty.server.handlers.ActionHandler;
import org.iff.netty.server.handlers.BaseActionHandler;

import java.io.Closeable;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * <pre>
 *     rest path = /rest/:Namespace/:ModelName/:RestUri
 *     GET    /articles                       -> articles#index
 *     GET    /articles/find/:conditions      -> articles#find
 *     GET    /articles/:id                   -> articles#show
 *     POST   /articles                       -> articles#create
 *     PUT    /articles/:id                   -> articles#update
 *     DELETE /articles/:id                   -> articles#destroy
 *     GET    /articles/ex/name/:conditions   -> articles#extra
 *     POST   /articles/ex/name               -> articles#extra
 *     PUT    /articles/ex/name/:conditions   -> articles#extra
 *     DELETE /articles/ex/name/:conditions   -> articles#extra
 * </pre>
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 24, 2016
 */
public class JGitActionHandler extends BaseActionHandler {
    public static final String uriPrefix = "/git";

    public static final String sanitizeUri(String uri) {
        // Decode the path.
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                uri = URLDecoder.decode(uri, "ISO-8859-1");
            } catch (UnsupportedEncodingException e1) {
                throw new Error();
            }
        }
        // Convert file separators.
        uri = uri.replace('/', File.separatorChar);
        // Simplistic dumb security check.
        // You will have to do something serious in the production environment.
        if (uri.contains(File.separator + ".") || uri.contains("." + File.separator) || uri.startsWith(".") || uri.endsWith(".")) {
            return null;
        }

        return uri;
    }

    public boolean execute(ProcessContext ctx) {
        try {
            //String[] uris = StringUtils.split(ctx.getRequestPath(), "/");
            if (HttpUtil.is100ContinueExpected(ctx.getRequest())) {
                ctx.getCtx().channel().write(new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
            }
            NettyFilterChain chain = WebApplication.get().initializeChain(ctx.getUri());
            if (chain.isValid()) {
                handleHttpServletRequest(ctx, chain);
            } else if (WebApplication.get().getStaticResourcesFolder() != null) {
                handleStaticResourceRequest(ctx);
            } else {
                Exceptions.runtime("No handler found for uri: " + ctx.getUri());
            }
        } catch (Exception e) {
            Exceptions.runtime(FCS.get("JGitActionHandler error: ", uriPrefix), e);
        }
        return true;
    }

    protected void handleHttpServletRequest(ProcessContext ctx, NettyFilterChain chain) throws Exception {
        NettyHttpServletResponse response = new NettyHttpServletResponse(ctx);
        NettyHttpServletRequest request = new NettyHttpServletRequest(ctx, chain);
        {
            request.setAttribute(WebApplication.ATTR_WEB_APPLICATION, WebApplication.get());
        }
        chain.doFilter(request, response);
        response.getWriter().flush();
        boolean keepAlive = HttpUtil.isKeepAlive(ctx.getRequest());
        if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            ctx.getOutputHeaders().set(HttpHeaderNames.CONTENT_LENGTH, ctx.getOutputBuffer().readableBytes());
            // Add keep alive header as per:
            // -
            // http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
            ctx.getOutputHeaders().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        // write response...
        ctx.output(response.getContentType());
    }

    protected void handleStaticResourceRequest(ProcessContext ctx) throws Exception {
        if (!ctx.isGet()) {
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }
        String uri = sanitizeUri(ctx.getUri());
        final String path = (uri != null ? WebApplication.get().getStaticResourcesFolder().getAbsolutePath() + File.separator + uri : null);

        if (path == null) {
            sendError(ctx, HttpResponseStatus.FORBIDDEN);
            return;
        }
        File file = new File(path);
        if (file.isHidden() || !file.exists()) {
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }
        if (!file.isFile()) {
            sendError(ctx, HttpResponseStatus.FORBIDDEN);
            return;
        }
        ctx.getOutputBuffer().writeBytes(FileUtils.readFileToByteArray(file));
        ctx.output(ContentType.getContentType(file.getName()));
    }

    private void sendError(ProcessContext ctx, HttpResponseStatus status) {
        String text = "Failure: " + status.toString() + "\r\n";
        ByteBuf byteBuf = Unpooled.buffer();
        try {
            ctx.getOutputBuffer().writeBytes(text.getBytes("utf-8"));
        } catch (Exception e) {
        }
        ctx.getOutputHeaders().add(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8");
        ctx.getOutputHeaders().add(HttpHeaderNames.CACHE_CONTROL, "no-cache");
        ctx.getOutputHeaders().add(HttpHeaderNames.PRAGMA, "no-cache");
        ctx.getOutputHeaders().add(HttpHeaderNames.SERVER, "JGit Server");
        ctx.getOutputHeaders().add(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());
        ctx.output("text/plain;charset=utf-8");
    }

    private boolean isSslChannel(Channel ch) {
        return ch.pipeline().get(SslHandler.class) != null;
    }

    public boolean done() {
        Map<String, RepositoryService> map = ThreadLocalHelper.get(RepositoryService.class.getName());
        Map<String, Closeable> close = ThreadLocalHelper.get(Closeable.class.getName());
        if (MapUtils.isNotEmpty(close)) {
            for (Map.Entry<String, Closeable> entry : close.entrySet()) {
                SocketHelper.closeWithoutError(entry.getValue());
            }
        }
        ThreadLocalHelper.remove();
        return super.done();
    }

    public boolean matchUri(String uri) {
        return uriPrefix.equals(uri) || (uri.startsWith(uriPrefix) && (uri.charAt(uriPrefix.length()) == '/' || uri.charAt(uriPrefix.length()) == '?'));
    }

    public int getOrder() {
        return 100;
    }

    public ActionHandler create() {
        return new JGitActionHandler();
    }
}

/*******************************************************************************
 * Copyright (c) 2019-01-20 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core.servlet.bridge;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import org.iff.infra.util.Exceptions;
import org.iff.netty.server.ProcessContext;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Locale;

/**
 * HttpServletResponseImpl
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-01-20
 * auto generate by qdp.
 */
public class NettyHttpServletResponse implements HttpServletResponse {

    protected ServletOutputStream outputStream;
    protected PrintWriter writer;
    protected boolean responseCommited = false;
    protected Locale locale = null;
    protected ProcessContext ctx;

    public NettyHttpServletResponse(ProcessContext processContext) {
        this.ctx = processContext;
        this.outputStream = new ServletOutputStream() {
            public void write(int b) throws IOException {
                ctx.getOutputBuffer().writeByte(b);
            }
        };
        this.writer = new PrintWriter(this.outputStream);
    }

    public void addCookie(Cookie cookie) {
        ctx.addCookie(new DefaultCookie(cookie.getName(), cookie.getValue()));
    }

    public boolean containsHeader(String name) {
        return ctx.getOutputHeaders().contains(name);
    }

    public String encodeURL(String url) {
        try {
            return URLEncoder.encode(url, getCharacterEncoding());
        } catch (Exception e) {
            Exceptions.runtime("Error encoding url!", e);
        }
        return null;
    }

    public String encodeRedirectURL(String url) {
        return encodeURL(url);
    }

    public String encodeUrl(String url) {
        return encodeURL(url);
    }

    public String encodeRedirectUrl(String url) {
        return encodeURL(url);
    }

    public void sendError(int sc, String msg) throws IOException {
        //Fix the following exception
        /*
        java.lang.IllegalArgumentException: reasonPhrase contains one of the following prohibited characters: \r\n: FAILED - Cannot find View Map for null.
        at io.netty.handler.codec.http.HttpResponseStatus.<init>(HttpResponseStatus.java:514) ~[netty-all-4.1.0.Beta3.jar:4.1.0.Beta3]
        at io.netty.handler.codec.http.HttpResponseStatus.<init>(HttpResponseStatus.java:496) ~[netty-all-4.1.0.Beta3.jar:4.1.0.Beta3]
        */
        if (msg != null) {
            msg = msg.replace('\r', ' ');
            msg = msg.replace('\n', ' ');
        }
        setStatus(sc, msg);
    }

    public void sendError(int sc) throws IOException {
        setStatus(sc);
    }

    public void sendRedirect(String location) throws IOException {
        setStatus(SC_FOUND);
        setHeader(HttpHeaderNames.LOCATION.toString(), location);
    }

    public void setDateHeader(String name, long date) {
        ctx.getOutputHeaders().set(name, date);
    }

    public void addDateHeader(String name, long date) {
        ctx.getOutputHeaders().add(name, date);
    }

    public void setHeader(String name, String value) {
        ctx.getOutputHeaders().set(name, value);
    }

    public void addHeader(String name, String value) {
        ctx.getOutputHeaders().add(name, value);
    }

    public void setIntHeader(String name, int value) {
        ctx.getOutputHeaders().set(name, value);
    }

    public void addIntHeader(String name, int value) {
        ctx.getOutputHeaders().add(name, value);
    }

    public void setStatus(int sc) {
        ctx.getResponse().setStatus(HttpResponseStatus.valueOf(sc));
    }

    public void setStatus(int sc, String msg) {
        ctx.getResponse().setStatus(new HttpResponseStatus(sc, msg));
    }

    public String getCharacterEncoding() {
        return ctx.getOutputHeaders().get(HttpHeaderNames.CONTENT_ENCODING, "UTF-8");
    }

    public void setCharacterEncoding(String charset) {
        ctx.getOutputHeaders().set(HttpHeaderNames.CONTENT_ENCODING, charset);
    }

    public String getContentType() {
        return ctx.getOutputHeaders().get(HttpHeaderNames.CONTENT_TYPE);
    }

    public void setContentType(String type) {
        ctx.getOutputHeaders().set(HttpHeaderNames.CONTENT_TYPE, type);
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return outputStream;
    }

    public PrintWriter getWriter() throws IOException {
        return writer;
    }

    public void setContentLength(int len) {
        ctx.getOutputHeaders().set(HttpHeaderNames.CONTENT_LENGTH, len);
    }

    public int getBufferSize() {
        return ctx.getOutputBuffer().capacity();
    }

    public void setBufferSize(int size) {
        // we using always dynamic buffer for now
    }

    public void flushBuffer() throws IOException {
        // no need to flush
        responseCommited = true;
    }

    public void resetBuffer() {
        ctx.getOutputBuffer().clear();
    }

    public boolean isCommitted() {
        return responseCommited;
    }

    public void reset() {
        if (isCommitted()) {
            Exceptions.runtime("Response already commited!");
        }
        ctx.getOutputHeaders().clear();
        resetBuffer();
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale loc) {
        locale = loc;
    }
}

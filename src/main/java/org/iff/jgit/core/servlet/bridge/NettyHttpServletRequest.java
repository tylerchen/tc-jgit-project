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
import io.netty.handler.codec.http.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.Exceptions;
import org.iff.infra.util.NumberHelper;
import org.iff.infra.util.ThreadLocalHelper;
import org.iff.netty.server.ProcessContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.security.Principal;
import java.util.*;

/**
 * NettyHttpServletRequest
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-01-20
 * auto generate by qdp.
 */
public class NettyHttpServletRequest implements HttpServletRequest {

    private static final Locale DEFAULT_LOCALE = Locale.getDefault();

    protected ServletInputStream inputStream;

    protected BufferedReader reader;

    protected Map<String, Object> attributes = new HashMap<>();

    protected Principal userPrincipal;

    protected String characterEncoding;

    protected ProcessContext ctx;

    public NettyHttpServletRequest(ProcessContext ctx, NettyFilterChain chain) {
        this.ctx = ctx;
        this.inputStream = new ServletInputStream() {
            ByteArrayInputStream in = new ByteArrayInputStream(ctx.getContent());

            public int read() throws IOException {
                return in.read();
            }
        };
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
        this.characterEncoding = getCharsetFromContentType(getContentType());
    }

    /**
     * Parse the character encoding from the specified content type header. If
     * the content type is null, or there is no explicit character encoding,
     * <code>null</code> is returned.
     *
     * @param contentType a content type header
     */
    public static final String getCharsetFromContentType(String contentType) {
        if (contentType == null) {
            return null;
        }
        contentType = contentType.toLowerCase();
        String encoding = StringUtils.substringAfter(contentType, "charset=");
        encoding = StringUtils.substringBefore(encoding, ";").trim();
        if (encoding.length() > 2 && encoding.startsWith("\"") && encoding.endsWith("\"")) {
            encoding = encoding.substring(1, encoding.length() - 1);
        }
        return encoding.trim();
    }

    public static final Collection<Locale> parseAcceptLanguageHeader(String acceptLanguageHeader) {
        if (acceptLanguageHeader == null) {
            return null;
        }
        List<Locale> locales = new ArrayList<Locale>();
        for (String str : acceptLanguageHeader.split(",")) {
            String[] arr = str.trim().replace("-", "_").split(";");
            // Parse the locale
            Locale locale = null;
            String[] l = arr[0].split("_");
            switch (l.length) {
                case 2:
                    locale = new Locale(l[0], l[1]);
                    break;
                case 3:
                    locale = new Locale(l[0], l[1], l[2]);
                    break;
                default:
                    locale = new Locale(l[0]);
                    break;
            }
            locales.add(locale);
        }
        return locales;
    }

    public String getAuthType() {
        return getHeader(HttpHeaderNames.WWW_AUTHENTICATE.toString());
    }

    public Cookie[] getCookies() {
        List<Cookie> list = new ArrayList<>();
        for (io.netty.handler.codec.http.cookie.Cookie c : ctx.getCookies()) {
            Cookie cookie = new Cookie(c.name(), c.value());
            cookie.setDomain(c.domain());
            cookie.setMaxAge((int) c.maxAge());
            cookie.setPath(c.path());
            cookie.setSecure(c.isSecure());
            list.add(cookie);
        }
        return list.toArray(new Cookie[list.size()]);
    }

    public long getDateHeader(String name) {
        return NumberHelper.getLong(getHeader(name), -1);
    }

    public String getHeader(String name) {
        return ctx.getHeaders().get(name);
    }

    public Enumeration getHeaders(String name) {
        return Collections.enumeration(Arrays.asList(getHeader(name)));
    }

    public Enumeration getHeaderNames() {
        return Collections.enumeration(ctx.getHeaders().names());
    }

    public int getIntHeader(String name) {
        return NumberHelper.getInt(getHeader(name), -1);
    }

    public String getMethod() {
        return ctx.getHttpMethod();
    }

    public String getPathInfo() {
        String requestPath = ctx.getRequestPath();
        String contextPath = getContextPath();
        String servletPath = getServletPath();
        return StringUtils.substringAfter(requestPath, ("/".equals(contextPath) ? "" : contextPath) + servletPath);
    }

    public String getPathTranslated() {
        Exceptions.runtime("Method 'getPathTranslated' not yet implemented!");
        return null;
    }

    public String getContextPath() {
        return ((WebApplication) getAttribute(WebApplication.ATTR_WEB_APPLICATION)).getContext().getContextPath();
    }

    public String getQueryString() {
        return StringUtils.substringAfter(ctx.getUri(), "?");
    }

    public String getRemoteUser() {
        return getHeader(HttpHeaderNames.AUTHORIZATION.toString());
    }

    public boolean isUserInRole(String role) {
        Exceptions.runtime("Method 'isUserInRole' not yet implemented!");
        return false;
    }

    public Principal getUserPrincipal() {
        return userPrincipal;
    }

    public String getRequestedSessionId() {
        HttpSession session = ThreadLocalHelper.get(HttpSession.class.getName());
        return session == null ? null : session.getId();
    }

    /**
     * withou domain and port: /ckswzl/admin/login.jsp
     *
     * @return
     */
    public String getRequestURI() {
        return ctx.getUri();
    }

    /**
     * full path: http://localhost:8080/ckswzl/admin/login.jsp
     *
     * @return
     */
    public StringBuffer getRequestURL() {
        String scheme = getScheme();
        int port = getServerPort();
        String uri = ctx.getUri();
        StringBuffer url = new StringBuffer();
        url.append(scheme/*http, https*/).append("://").append(getServerName());
        if ((scheme.equals("http") && port != 80) || (scheme.equals("https") && port != 443)) {
            url.append(':').append(port);
        }
        url.append(uri);
        return url;
    }

    public String getServletPath() {
        String servletPath = (String) getAttribute(WebApplication.ATTR_SERVLET_PATH);
        if (servletPath != null) {
            return servletPath;
        }
        servletPath = StringUtils.substringAfter(getRequestURI(), getContextPath());
        return StringUtils.startsWith(servletPath, "/") ? servletPath : ("/" + servletPath);
    }

    public HttpSession getSession(boolean create) {
        if (create) {
            return ((WebApplication) getAttribute(WebApplication.ATTR_WEB_APPLICATION)).getOrCreateSession();
        }
        return ((WebApplication) getAttribute(WebApplication.ATTR_WEB_APPLICATION)).getSession();
    }

    public HttpSession getSession() {
        return ((WebApplication) getAttribute(WebApplication.ATTR_WEB_APPLICATION)).getOrCreateSession();
    }

    public boolean isRequestedSessionIdValid() {
        Exceptions.runtime("Method 'isRequestedSessionIdValid' not yet implemented!");
        return false;
    }

    public boolean isRequestedSessionIdFromCookie() {
        return true;
    }

    public boolean isRequestedSessionIdFromURL() {
        Exceptions.runtime("Method 'isRequestedSessionIdFromUrl' not yet implemented!");
        return false;
    }

    public boolean isRequestedSessionIdFromUrl() {
        Exceptions.runtime("Method 'isRequestedSessionIdFromURL' not yet implemented!");
        return false;
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public Enumeration getAttributeNames() {
        return Collections.enumeration(attributes.keySet());
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        characterEncoding = env;
    }

    public int getContentLength() {
        return HttpUtil.getContentLength(ctx.getRequest(), -1);
    }

    public String getContentType() {
        return getHeader(HttpHeaderNames.CONTENT_TYPE.toString());
    }

    public ServletInputStream getInputStream() throws IOException {
        return inputStream;
    }

    public String getParameter(String name) {
        String[] values = getParameterValues(name);
        return values != null ? values[0] : null;
    }

    public Enumeration getParameterNames() {
        return Collections.enumeration(ctx.getQueryParam().keySet());
    }

    public String[] getParameterValues(String name) {
        List<String> values = ctx.getQueryParam().get(name);
        if (values == null) {
            return null;
        }
        return values.toArray(new String[values.size()]);
    }

    public Map getParameterMap() {
        return ctx.getQueryParam();
    }

    public String getProtocol() {
        return ctx.getRequest().protocolVersion().toString();
    }

    public String getScheme() {
        return isSecure() ? "https" : "http";
    }

    public String getServerName() {
        return ctx.getHeaders().get(ProcessContext.ATTR_LOCAL_HOST);
    }

    public int getServerPort() {
        return NumberHelper.getInt(ctx.getHeaders().get(ProcessContext.ATTR_LOCAL_PORT), -1);
    }

    public BufferedReader getReader() throws IOException {
        return reader;
    }

    public String getRemoteAddr() {
        return ctx.getHeaders().get(ProcessContext.ATTR_REMOTE_ADDRESS);
    }

    public String getRemoteHost() {
        return ctx.getHeaders().get(ProcessContext.ATTR_REMOTE_HOST);
    }

    public void setAttribute(String name, Object o) {
        attributes.put(name, o);
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    public Locale getLocale() {
        String locale = getHeader(HttpHeaderNames.ACCEPT_LANGUAGE.toString());
        return StringUtils.isBlank(locale) ? DEFAULT_LOCALE : new Locale(locale);
    }

    public Enumeration getLocales() {
        String header = getHeader(HttpHeaderNames.ACCEPT_LANGUAGE.toString());
        Collection<Locale> locales = parseAcceptLanguageHeader(header);
        if (locales.isEmpty()) {
            locales.add(DEFAULT_LOCALE);
        }
        return Collections.enumeration(locales);
    }

    public boolean isSecure() {
        return "true".equals(ctx.getHeaders().get(ProcessContext.ATTR_IS_SECURE));
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        Exceptions.runtime("Method 'getRequestDispatcher' not yet implemented!");
        return null;
    }

    public String getRealPath(String path) {
        Exceptions.runtime("Method 'getRealPath' not yet implemented!");
        return null;
    }

    public int getRemotePort() {
        return NumberHelper.getInt(ctx.getHeaders().get(ProcessContext.ATTR_REMOTE_PORT), 0);
    }

    public String getLocalName() {
        return getServerName();
    }

    public String getLocalAddr() {
        return ctx.getHeaders().get(ProcessContext.ATTR_LOCAL_ADDRESS);
    }

    public int getLocalPort() {
        return NumberHelper.getInt(ctx.getHeaders().get(ProcessContext.ATTR_LOCAL_PORT), 0);
    }
}

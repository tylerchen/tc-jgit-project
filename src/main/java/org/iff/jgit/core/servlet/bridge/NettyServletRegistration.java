/*******************************************************************************
 * Copyright (c) 2019-01-20 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core.servlet.bridge;

import org.iff.infra.util.Exceptions;
import org.iff.infra.util.FCS;
import org.iff.infra.util.Logger;

import javax.servlet.http.HttpServlet;
import java.util.regex.Pattern;

/**
 * NettyHttpServletRegistration
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-01-20
 * auto generate by qdp.
 */
public class NettyServletRegistration {

    public static final String DEFAULT_URL_PATTERN = "/*";

    protected String[] sanitizedUrlPatterns;

    protected Pattern[] regexPatterns;

    protected HttpServlet servlet;

    protected NettyServletConfig config;

    protected boolean initialized = false;

    protected NettyServletContext context;

    public NettyServletRegistration(NettyServletContext context, Class<? extends HttpServlet> servletClazz, String... urlPatterns) {
        this(context, newInstance(servletClazz), urlPatterns);
    }

    public NettyServletRegistration(NettyServletContext context, Class<? extends HttpServlet> servletClazz) {
        this(context, newInstance(servletClazz), DEFAULT_URL_PATTERN);
    }

    public NettyServletRegistration(NettyServletContext context, HttpServlet servlet) {
        this(context, servlet, DEFAULT_URL_PATTERN);
    }

    public NettyServletRegistration(NettyServletContext context, HttpServlet servlet, String... urlPatterns) {
        if (urlPatterns == null || urlPatterns.length == 0) {
            Exceptions.runtime("No url patterns were assigned to http servlet: " + servlet);
        }

        this.context = context;
        this.regexPatterns = new Pattern[urlPatterns.length];
        this.sanitizedUrlPatterns = new String[urlPatterns.length];

        for (int i = 0; i < urlPatterns.length; i++) {
            String regex = urlPatterns[i].replaceAll("\\*", ".*");
            this.regexPatterns[i] = Pattern.compile(regex);
            this.sanitizedUrlPatterns[i] = urlPatterns[i].replaceAll("\\*", "");
            if (this.sanitizedUrlPatterns[i].endsWith("/")) {
                this.sanitizedUrlPatterns[i] = this.sanitizedUrlPatterns[i].substring(0, this.sanitizedUrlPatterns[i].length() - 1);
            }
        }

        this.servlet = servlet;
        this.config = new NettyServletConfig(servlet.getClass().getName(), context);
    }

    private static HttpServlet newInstance(Class<? extends HttpServlet> servletClazz) {
        try {
            return servletClazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            Exceptions.runtime("Can not new instance!" + servletClazz);
        }
        return null;
    }

    public boolean matchesUrlPattern(String uri) {
        return getMatchingUrlPattern(uri) != null;
    }

    public String getMatchingUrlPattern(String uri) {
        int indx = uri.indexOf('?');
        String path = indx != -1 ? uri.substring(0, indx) : uri.substring(0);
        if (!path.endsWith("/")) {
            path += "/";
        }
        for (int i = 0; i < regexPatterns.length; i++) {
            Pattern pattern = regexPatterns[i];
            if (pattern.matcher(path).matches()) {
                return sanitizedUrlPatterns[i];
            }
        }
        return null;
    }

    public NettyServletRegistration addInitParameter(String name, String value) {
        config.addInitParameter(name, value);
        return this;
    }

    public HttpServlet getHttpServlet() {
        return servlet;
    }

    public NettyServletConfig getConfig() {
        return config;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void init() {
        try {
            Logger.debug(FCS.get("Initializing http component: {}", servlet.getClass()));
            servlet.init(config);
            initialized = true;
        } catch (Exception e) {
            initialized = false;
            Logger.error("Http component '" + servlet.getClass() + "' was not initialized!", e);
        }
    }

    public void destroy() {
        try {
            Logger.debug(FCS.get("Destroying http component: {}", servlet.getClass()));
            servlet.destroy();
            initialized = false;
        } catch (Exception e) {
            initialized = false;
            Logger.error("Http component '" + servlet.getClass() + "' was not destroyed!", e);
        }
    }
}

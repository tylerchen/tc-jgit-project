/*******************************************************************************
 * Copyright (c) 2019-01-20 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core.servlet.bridge;

import org.iff.infra.util.ThreadLocalHelper;

import javax.servlet.http.HttpSession;
import java.io.Closeable;
import java.io.File;
import java.util.Map;

/**
 * WebApplication
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-01-20
 * auto generate by qdp.
 */
public class WebApplication implements AutoCloseable, Closeable {

    public static final String ATTR_WEB_APPLICATION = "WebApplication";
    public static final String ATTR_SERVLET_PATH = "ServletPath";
    private static WebApplication DEFAULT = new WebApplication();
    protected NettyHttpSessionStore sessionStore = new NettyHttpSessionStore();
    protected NettyServletContext context = new NettyServletContext();
    protected WebApplicationConfiguration config = new WebApplicationConfiguration(context);

    public static WebApplication get() {
        return DEFAULT;
    }

    public void init() {
        context.setAttribute("WebApplication", this);
        context.setContextPath("/");
        initServletContext();
        initContextListeners();
        initFilters();
        initServlets();
    }

    public void destroy() {
        destroyServlets();
        destroyFilters();
        destroyContextListeners();
    }

    private void initContextListeners() {
        for (NettyServletContextListenerRegistration ctx : config.getNettyServletContextListenerRegistrations()) {
            ctx.init();
        }
    }

    private void destroyContextListeners() {
        for (NettyServletContextListenerRegistration ctx : config.getNettyServletContextListenerRegistrations()) {
            ctx.destroy();
        }
    }

    private void destroyServlets() {
        for (NettyServletRegistration servlet : config.getServlets()) {
            servlet.destroy();
        }
    }

    private void destroyFilters() {
        for (NettyFilterRegistration filter : config.getFilters()) {
            filter.destroy();
        }
    }

    protected void initServletContext() {
        context.setServletContextName(config.getName());
        for (Map.Entry<String, String> entry : config.getContextParameters().entrySet()) {
            context.addInitParameter(entry.getKey(), entry.getValue());
        }
    }

    protected void initFilters() {
        for (NettyFilterRegistration filter : config.getFilters()) {
            filter.init();
        }
    }

    protected void initServlets() {
        for (NettyServletRegistration servlet : config.getServlets()) {
            servlet.init();
        }
    }

    public NettyFilterChain initializeChain(String uri) {
        NettyServletRegistration servlet = findServlet(uri);
        NettyFilterChain chain = new NettyFilterChain(servlet);
        for (NettyFilterRegistration filter : config.getFilters()) {
            if (!filter.matchesUrlPattern(uri)) {
                continue;
            }
            chain.addFilter(filter);
        }
        return chain;
    }

    private NettyServletRegistration findServlet(String uri) {
        if (!config.hasServlet()) {
            return null;
        }
        for (NettyServletRegistration servlet : config.getServlets()) {
            if (!servlet.matchesUrlPattern(uri)) {
                continue;
            }
            return servlet;
        }
        return null;
    }

    public File getStaticResourcesFolder() {
        return config.getStaticResourcesFolder();
    }

    public NettyHttpSessionStore getSessionStore() {
        return sessionStore;
    }

    public NettyServletContext getContext() {
        return context;
    }

    public WebApplicationConfiguration getConfig() {
        return config;
    }

    public synchronized NettyHttpSession getOrCreateSession() {
        if (ThreadLocalHelper.get(HttpSession.class.getName()) == null) {
            NettyHttpSession newSession = sessionStore.createSession();
            newSession.setMaxInactiveInterval(-1);
            ThreadLocalHelper.set(HttpSession.class.getName(), newSession);
        }
        return ThreadLocalHelper.get(HttpSession.class.getName());
    }

    public NettyHttpSession getSession() {
        return ThreadLocalHelper.get(HttpSession.class.getName());
    }

    public void close() {
        destroy();
    }
}

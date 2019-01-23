/*******************************************************************************
 * Copyright (c) 2019-01-21 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
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

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * NettyServletContextListenerRegistration
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-01-21
 * auto generate by qdp.
 */
public class NettyServletContextListenerRegistration {
    protected ServletContextListener listener;
    protected boolean initialized = false;
    protected NettyServletContext context;

    public NettyServletContextListenerRegistration(NettyServletContext context, Class<? extends ServletContextListener> clazz) {
        this(context, newInstance(clazz));
    }

    public NettyServletContextListenerRegistration(NettyServletContext context, ServletContextListener listener) {
        this.context = context;
        this.listener = listener;
    }

    private static ServletContextListener newInstance(Class<? extends ServletContextListener> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            Exceptions.runtime("Can not new instance!" + clazz);
        }
        return null;
    }

    public ServletContextListener getListener() {
        return listener;
    }

    public void init() {
        try {
            Logger.debug(FCS.get("Initializing listener: {}", listener.getClass()));
            listener.contextInitialized(new ServletContextEvent(context));
            initialized = true;
        } catch (Exception e) {
            initialized = false;
            Logger.error("Listener '" + listener.getClass() + "' was not initialized!", e);
        }
    }

    public void destroy() {
        try {
            Logger.debug(FCS.get("Destroying listener: {}", listener.getClass()));
            listener.contextDestroyed(new ServletContextEvent(context));
            initialized = false;
        } catch (Exception e) {
            initialized = false;
            Logger.error("Listener '" + listener.getClass() + "' was not destroyed!", e);
        }
    }

    public boolean isInitialized() {
        return initialized;
    }

    public NettyServletContext getContext() {
        return context;
    }
}

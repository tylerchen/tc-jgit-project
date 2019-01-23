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
import org.iff.infra.util.RSAHelper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSessionContext;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * NettyHttpSession
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-01-20
 * auto generate by qdp.
 */
public class NettyHttpSession implements HttpSession {

    public static final String SESSION_ID_KEY = "JSESSIONID";

    protected String id;

    protected long creationTime;

    protected long lastAccessedTime;

    protected int maxInactiveInterval = -1;

    protected Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();

    protected ServletContext servletContext;

    protected NettyHttpSession() {
    }

    protected NettyHttpSession(String id) {
        this.id = id;
        this.creationTime = System.currentTimeMillis();
        this.lastAccessedTime = this.creationTime;
    }

    public NettyHttpSession(String id, ServletContext servletContext) {
        this.id = id;
        this.creationTime = System.currentTimeMillis();
        this.lastAccessedTime = this.creationTime;
        this.servletContext = servletContext;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public String getId() {
        return id;
    }

    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public void setMaxInactiveInterval(int interval) {
        maxInactiveInterval = interval;
    }

    public HttpSessionContext getSessionContext() {
        Exceptions.runtime("As of Version 2.1, this method is deprecated and has no replacement.");
        return null;
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public Object getValue(String name) {
        return getAttribute(name);
    }

    public Enumeration getAttributeNames() {
        return Collections.enumeration(attributes.keySet());
    }

    public String[] getValueNames() {
        return attributes.keySet().toArray(new String[attributes.keySet().size()]);
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
        if (value != null && value instanceof HttpSessionBindingListener) {
            ((HttpSessionBindingListener) value).valueBound(new HttpSessionBindingEvent(this, name, value));
        }
    }

    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

    public void removeAttribute(String name) {
        Object value = attributes.get(name);
        if (value != null && value instanceof HttpSessionBindingListener) {
            ((HttpSessionBindingListener) value).valueUnbound(new HttpSessionBindingEvent(this, name, value));
        }
        attributes.remove(name);
    }

    public void removeValue(String name) {
        removeAttribute(name);
    }

    public void invalidate() {
        attributes.clear();
    }

    public boolean isNew() {
        Exceptions.runtime("Method 'isNew' not yet implemented!");
        return false;
    }

    public void touch() {
        this.lastAccessedTime = System.currentTimeMillis();
    }
}

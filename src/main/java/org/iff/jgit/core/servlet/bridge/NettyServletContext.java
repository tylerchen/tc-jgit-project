/*******************************************************************************
 * Copyright (c) 2019-01-20 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core.servlet.bridge;

import org.iff.infra.util.ContentType;
import org.iff.infra.util.Exceptions;
import org.iff.infra.util.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * NettyServletContext
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-01-20
 * auto generate by qdp.
 */
public class NettyServletContext implements ServletContext {

    protected String serverInfo;

    protected Map<String, String> initParameters = new HashMap<>();

    protected Map<String, Object> attributes = new HashMap<>();

    protected String servletContextName;

    protected String contextPath;

    public NettyServletContext() {
        this.serverInfo = "Netty Servlet Bridge";
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public ServletContext getContext(String uripath) {
        return this;
    }

    public int getMajorVersion() {
        return 2;
    }

    public int getMinorVersion() {
        return 4;
    }

    public String getMimeType(String file) {
        return ContentType.getContentType(file);
    }

    public Set getResourcePaths(String path) {
        Exceptions.runtime("Method 'getResourcePaths' not yet implemented!");
        return null;
    }

    public URL getResource(String path) throws MalformedURLException {
        return getClass().getResource(path);
    }

    public InputStream getResourceAsStream(String path) {
        return getClass().getResourceAsStream(path);
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        HttpServlet servlet = null;
        //TODO get servlet from WebApplication
        return new NettyRequestDispatcher(servlet.getServletName(), path, servlet);
    }

    public RequestDispatcher getNamedDispatcher(String name) {
        HttpServlet servlet = null;
        //TODO get servlet from WebApplication
        return new NettyRequestDispatcher(name, null, servlet);
    }

    public Servlet getServlet(String name) throws ServletException {
        Exceptions.runtime("Deprecated as of Java Servlet API 2.1, with no direct replacement!");
        return null;
    }

    public Enumeration getServlets() {
        Exceptions.runtime("Method 'getServlets' deprecated as of Java Servlet API 2.0, with no replacement.");
        return null;
    }

    public Enumeration getServletNames() {
        Exceptions.runtime("Method 'getServletNames' deprecated as of Java Servlet API 2.0, with no replacement.");
        return null;
    }

    public void log(String msg) {
        Logger.info(msg);
    }

    public void log(Exception exception, String msg) {
        log(msg, exception);
    }

    public void log(String message, Throwable throwable) {
        Logger.error(message, throwable);
    }

    public String getRealPath(String path) {
        if (!"/".equals(path)) {
            Exceptions.runtime("Method 'getRealPath' not yet implemented!");
        }
        try {
            File file = File.createTempFile("netty-servlet-bridge", "");
            file.mkdirs();
            return file.getAbsolutePath();
        } catch (Exception e) {
            Exceptions.runtime("Method 'getRealPath' not yet implemented!");
        }
        return null;
    }

    public String getServerInfo() {
        return serverInfo;
    }

    public String getInitParameter(String name) {
        return initParameters.get(name);
    }

    public Enumeration getInitParameterNames() {
        return Collections.enumeration(initParameters.keySet());
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public Enumeration getAttributeNames() {
        return Collections.enumeration(attributes.keySet());
    }

    public void setAttribute(String name, Object object) {
        attributes.put(name, object);
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    public String getServletContextName() {
        return servletContextName;
    }

    public void setServletContextName(String servletContextName) {
        this.servletContextName = servletContextName;
    }

    ///
    public void addInitParameter(String name, String value) {
        initParameters.put(name, value);
    }
}

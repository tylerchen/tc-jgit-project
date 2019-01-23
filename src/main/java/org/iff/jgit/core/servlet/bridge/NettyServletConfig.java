/*******************************************************************************
 * Copyright (c) 2019-01-20 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core.servlet.bridge;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * NettyFilterConfig
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-01-20
 * auto generate by qdp.
 */
public class NettyServletConfig implements ServletConfig {

    protected String servletName;

    protected Map<String, String> initParameters = new HashMap<>();

    protected ServletContext servletContext;

    protected NettyServletConfig() {
    }

    protected NettyServletConfig(String servletName) {
        this.servletName = servletName;
    }

    public NettyServletConfig(String servletName, ServletContext servletContext) {
        this.servletName = servletName;
        this.servletContext = servletContext;
    }

    public String getServletName() {
        return servletName;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public String getInitParameter(String name) {
        return initParameters.get(name);
    }

    public Enumeration getInitParameterNames() {
        return Collections.enumeration(initParameters.keySet());
    }

    public void addInitParameter(String name, String value) {
        initParameters.put(name, value);
    }
}

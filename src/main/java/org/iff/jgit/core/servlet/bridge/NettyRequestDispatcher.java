/*******************************************************************************
 * Copyright (c) 2019-01-20 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core.servlet.bridge;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * NettyRequestDispatcher
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-01-20
 * auto generate by qdp.
 */
public class NettyRequestDispatcher implements RequestDispatcher {

    /**
     * The servlet name for a named dispatcher.
     */
    protected String name = null;

    /**
     * The servlet path for this RequestDispatcher.
     */
    protected String servletPath = null;


    protected HttpServlet httpServlet;

    protected NettyRequestDispatcher() {
    }

    public NettyRequestDispatcher(String name, String servletPath, HttpServlet httpServlet) {
        this.name = name;
        this.servletPath = servletPath;
        this.httpServlet = httpServlet;
    }

    public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        if (httpServlet != null) {
            //TODO Wrap
            httpServlet.service(request, response);
        } else {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        if (httpServlet != null) {
            //TODO Wrap
            httpServlet.service(request, response);
        } else {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}

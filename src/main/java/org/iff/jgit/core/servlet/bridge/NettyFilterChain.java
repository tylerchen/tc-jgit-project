/*******************************************************************************
 * Copyright (c) 2019-01-20 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core.servlet.bridge;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.LinkedList;

/**
 * NettyFilterChain
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-01-20
 * auto generate by qdp.
 */
public class NettyFilterChain implements FilterChain {

    protected LinkedList<NettyFilterRegistration> filters = new LinkedList<>();
    protected NettyServletRegistration servlet;

    public NettyFilterChain() {
    }

    public NettyFilterChain(NettyServletRegistration servlet) {
        this.servlet = servlet;
    }

    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        NettyFilterRegistration filter = filters.poll();
        if (filter != null) {
            System.out.println(((HttpServletRequest) request).getMethod() + " " + ((HttpServletRequest) request).getRequestURI());
            request.setAttribute(WebApplication.ATTR_SERVLET_PATH, filter.getMatchingUrlPattern(((HttpServletRequest) request).getRequestURI()));
            filter.getFilter().doFilter(request, response, this);
        } else if (servlet != null) {
            request.setAttribute(WebApplication.ATTR_SERVLET_PATH, servlet.getMatchingUrlPattern(((HttpServletRequest) request).getRequestURI()));
            servlet.getHttpServlet().service(request, response);
        }
    }

    public NettyServletRegistration getServlet() {
        return servlet;
    }

    public void addFilter(NettyFilterRegistration filter) {
        filters.add(filter);
    }

    public boolean isValid() {
        return servlet != null || !this.filters.isEmpty();
    }
}

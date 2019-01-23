/*******************************************************************************
 * Copyright (c) 2019-01-20 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * HelloWorlFilter
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-01-20
 * auto generate by qdp.
 */
public class HelloWorlFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("===HelloWorlFilter.init===");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //GET http://localhost:8080/ckswzl/admin/login.jsp
        //getContextPath: /ckswzl
        //getServletPath: /admin/login.jsp
        //getRealPath: D:\document\EclipseWorkSpace2\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\ckswzl\
        //getRequestURL: http://localhost:8080/ckswzl/admin/login.jsp
        //getRequestURI: /ckswzl/admin/login.jsp
        HttpServletRequest hsr = (HttpServletRequest) request;
        System.out.println("===HelloWorlFilter.doFilter===");
        System.out.println("ServerName: " + request.getServerName());
        System.out.println("ContextPath: " + hsr.getContextPath());
        System.out.println("PathInfo: " + hsr.getPathInfo());
        System.out.println("ReqestURI: " + hsr.getRequestURI());
        System.out.println("ServletPath: " + hsr.getServletPath());
        System.out.println("RequestURL: " + hsr.getRequestURL());
        response.getWriter().write("===HelloWorlFilter.doFilter===");
    }

    @Override
    public void destroy() {
        System.out.println("===HelloWorlFilter.destroy===");
    }
}

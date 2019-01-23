/*
 * Copyright 2013 by Maxim Kalina
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.iff.jgit.core.servlet.bridge;

import org.iff.infra.util.Exceptions;

import javax.servlet.Filter;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.util.*;

public class WebApplicationConfiguration {

    protected String name;

    protected int sessionTimeout = 60 * 60; // 1 hour

    protected Map<String, String> contextParameters = new HashMap<>();

    protected Collection<NettyServletContextListenerRegistration> contextListeners = new ArrayList<>();

    protected Collection<NettyFilterRegistration> filters = new ArrayList<>();

    protected Collection<NettyServletRegistration> servlets = new ArrayList<>();

    protected File staticResourcesFolder;

    protected NettyServletContext context;

    protected WebApplicationConfiguration() {
    }

    public WebApplicationConfiguration(NettyServletContext context) {
        this.context = context;
    }

    public WebApplicationConfiguration addContextParameter(String name, String value) {
        contextParameters.put(name, value);
        return this;
    }

    public WebApplicationConfiguration addServletContextListener(Class<? extends ServletContextListener> listenerClass) {
        return addNettyServletContextListenerRegistrations(new NettyServletContextListenerRegistration(context, listenerClass));
    }

    public WebApplicationConfiguration addServletContextListener(ServletContextListener listener) {
        return addNettyServletContextListenerRegistrations(new NettyServletContextListenerRegistration(context, listener));
    }

    public WebApplicationConfiguration addNettyServletContextListenerRegistrations(NettyServletContextListenerRegistration... configs) {
        if (configs == null || configs.length == 0) {
            return this;
        }
        return addNettyServletContextListenerRegistrations(Arrays.asList(configs));
    }

    public WebApplicationConfiguration addNettyServletContextListenerRegistrations(List<NettyServletContextListenerRegistration> configs) {
        if (configs == null || configs.size() == 0) {
            return this;
        }
        contextListeners.addAll(configs);
        return this;
    }

    public Collection<NettyServletContextListenerRegistration> getNettyServletContextListenerRegistrations() {
        return Collections.unmodifiableCollection(contextListeners);
    }

    public Map<String, String> getContextParameters() {
        return Collections.unmodifiableMap(contextParameters);
    }

    public String getName() {
        return name;
    }

    public WebApplicationConfiguration setName(String name) {
        this.name = name;
        return this;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public WebApplicationConfiguration setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
        return this;
    }

    public WebApplicationConfiguration addFilter(Filter filter) {
        return addFilterRegistrations(new NettyFilterRegistration(context, filter));
    }

    public WebApplicationConfiguration addFilter(Filter filter, String... urlPatterns) {
        return addFilterRegistrations(new NettyFilterRegistration(context, filter, urlPatterns));
    }

    public WebApplicationConfiguration addFilter(Class<? extends Filter> filterClass) {
        return addFilterRegistrations(new NettyFilterRegistration(context, filterClass));
    }

    public WebApplicationConfiguration addFilter(Class<? extends Filter> filterClass, String... urlPatterns) {
        return addFilterRegistrations(new NettyFilterRegistration(context, filterClass, urlPatterns));
    }

    public WebApplicationConfiguration addFilterRegistrations(NettyFilterRegistration... filters) {
        if (filters == null || filters.length == 0) {
            return this;
        }
        return addFilterRegistrations(Arrays.asList(filters));
    }

    public WebApplicationConfiguration addFilterRegistrations(Collection<NettyFilterRegistration> configs) {
        if (configs == null || configs.size() == 0) {
            return this;
        }
        filters.addAll(configs);
        return this;
    }

    public Collection<NettyFilterRegistration> getFilters() {
        return Collections.unmodifiableCollection(filters);
    }

    public boolean hasFilter() {
        return !filters.isEmpty();
    }

    public WebApplicationConfiguration addHttpServlet(HttpServlet servlet) {
        return addNettyServletRegistrations(new NettyServletRegistration(context, servlet));
    }

    public WebApplicationConfiguration addHttpServlet(HttpServlet servlet, String... urlPatterns) {
        return addNettyServletRegistrations(new NettyServletRegistration(context, servlet, urlPatterns));
    }

    public WebApplicationConfiguration addHttpServlet(Class<? extends HttpServlet> servletClass) {
        return addNettyServletRegistrations(new NettyServletRegistration(context, servletClass));
    }

    public WebApplicationConfiguration addHttpServlet(Class<? extends HttpServlet> servletClass, String... urlPatterns) {
        return addNettyServletRegistrations(new NettyServletRegistration(context, servletClass, urlPatterns));
    }

    public WebApplicationConfiguration addNettyServletRegistrations(NettyServletRegistration... servlets) {
        if (servlets == null || servlets.length == 0) {
            return this;
        }
        return addNettyServletRegistrations(Arrays.asList(servlets));
    }

    public WebApplicationConfiguration addNettyServletRegistrations(Collection<NettyServletRegistration> configs) {
        if (configs == null || configs.size() == 0) {
            return this;
        }
        servlets.addAll(configs);
        return this;
    }

    public Collection<NettyServletRegistration> getServlets() {
        return Collections.unmodifiableCollection(servlets);
    }

    public boolean hasServlet() {
        return !servlets.isEmpty();
    }

    public File getStaticResourcesFolder() {
        return staticResourcesFolder;
    }

    public WebApplicationConfiguration setStaticResourcesFolder(String folder) {
        return setStaticResourcesFolder(new File(folder));
    }

    public WebApplicationConfiguration setStaticResourcesFolder(File folder) {
        if (folder == null) {
            Exceptions.runtime("Static resources folder must be not null!");
        }
        if (!folder.exists()) {
            Exceptions.runtime("Static resources folder '" + folder.getAbsolutePath() + "' was not found!");
        }
        if (!folder.isDirectory()) {
            Exceptions.runtime("Static resources folder '" + folder.getAbsolutePath() + "' must be a directory!");
        }
        this.staticResourcesFolder = folder;
        return this;
    }
}

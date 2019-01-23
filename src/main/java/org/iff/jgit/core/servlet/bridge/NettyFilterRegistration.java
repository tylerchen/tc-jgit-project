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

import javax.servlet.Filter;
import java.util.regex.Pattern;

/**
 * NettyFilterRegistration
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-01-20
 * auto generate by qdp.
 */
public class NettyFilterRegistration {

    public static final String DEFAULT_URL_PATTERN = "/*";

    protected String[] sanitizedUrlPatterns;

    protected Pattern[] regexPatterns;

    protected Filter filter;

    protected NettyFilterConfig config;

    protected boolean initialized = false;

    protected NettyServletContext context;

    public NettyFilterRegistration(NettyServletContext context, Class<? extends Filter> filterClazz, String... urlPatterns) {
        this(context, newInstance(filterClazz), urlPatterns);
    }

    public NettyFilterRegistration(NettyServletContext context, Class<? extends Filter> filterClazz) {
        this(context, newInstance(filterClazz), DEFAULT_URL_PATTERN);
    }

    public NettyFilterRegistration(NettyServletContext context, Filter filter) {
        this(context, filter, DEFAULT_URL_PATTERN);
    }

    public NettyFilterRegistration(NettyServletContext context, Filter filter, String... urlPatterns) {
        if (urlPatterns == null || urlPatterns.length == 0) {
            Exceptions.runtime("No url patterns were assigned to http servlet: " + filter);
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

        this.filter = filter;
        this.config = new NettyFilterConfig(filter.getClass().getName(), context);
    }

    private static Filter newInstance(Class<? extends Filter> filterClazz) {
        try {
            return filterClazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            Exceptions.runtime("Can not new instance!" + filterClazz);
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

    public NettyFilterRegistration addInitParameter(String name, String value) {
        config.addInitParameter(name, value);
        return this;
    }

    public Filter getFilter() {
        return filter;
    }

    public NettyFilterConfig getConfig() {
        return config;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void init() {
        try {
            Logger.debug(FCS.get("Initializing http component: {}", filter.getClass()));
            filter.init(config);
            initialized = true;
        } catch (Exception e) {
            initialized = false;
            Logger.error("Http component '" + filter.getClass() + "' was not initialized!", e);
        }
    }

    public void destroy() {
        try {
            Logger.debug(FCS.get("Destroying http component: {}", filter.getClass()));
            filter.destroy();
            initialized = false;
        } catch (Exception e) {
            initialized = false;
            Logger.error("Http component '" + filter.getClass() + "' was not destroyed!", e);
        }
    }
}

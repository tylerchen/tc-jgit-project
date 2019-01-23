/*******************************************************************************
 * Copyright (c) Sep 24, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.jgit.core;

import org.apache.commons.lang3.StringUtils;
import org.iff.jgit.core.service.ManagementControllerFactory;
import org.iff.jgit.core.service.QueryControllerFactory;
import org.iff.infra.util.Assert;
import org.iff.netty.server.ProcessContext;
import org.iff.netty.server.handlers.ActionHandler;
import org.iff.netty.server.handlers.BaseActionHandler;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 24, 2016
 */
public class DefaultRestHandler extends BaseActionHandler {

    public static final String URI_SPLIT = "uriSplit";
    public static final String uriPrefix = "/default";

    private static final Map<String, ActionHandler> services = new HashMap<String, ActionHandler>();

    public static void addService(String path, ActionHandler service) {
        Assert.notBlank(path);
        Assert.notNull(service);
        services.put(path, service);
    }

    public static void removeService(String path) {
        Assert.notBlank(path);
        services.remove(path);
    }

    public boolean execute(ProcessContext ctx) {
        String[] split = StringUtils.split(StringUtils.substringBefore(ctx.getUri(), "?"), '/');
        ctx.addAttribute(URI_SPLIT, split);
        ActionHandler restHandler = null;
        if (split == null || split.length < 2 || (restHandler = services.get("/" + split[1])) == null) {
            ctx.getOutputBuffer().writeCharSequence("[404]: no handler found!", Charset.forName("UTF-8"));
            ctx.outputText();
        } else {
            try {
                restHandler.create().process(ctx);
                DBConnectionHolder.commitAndCloseQuietly();
            } catch (Exception e) {
                DBConnectionHolder.rollbackAndCloseQuietly();
                throw e;
            }
        }
        return true;
    }

    public boolean matchUri(String uri) {
        return uriPrefix.equals(uri) || (uri.startsWith(uriPrefix)
                && (uri.charAt(uriPrefix.length()) == '/' || uri.charAt(uriPrefix.length()) == '?'));
    }

    public int getOrder() {
        return 100;
    }

    public ActionHandler create() {
        if (services.isEmpty()) {
            ManagementControllerFactory.addAllService();
            QueryControllerFactory.addAllService();
        }
        return this;
    }

}

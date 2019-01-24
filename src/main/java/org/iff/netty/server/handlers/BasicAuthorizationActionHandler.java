/*******************************************************************************
 * Copyright (c) 2019-01-24 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.netty.server.handlers;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.iff.jgit.util.SystemHelper;
import org.iff.netty.server.ProcessContext;

/**
 * BasicAuthorizationActionHandler
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2019-01-24
 * auto generate by qdp.
 */
public class BasicAuthorizationActionHandler extends BaseActionHandler {

    public ActionHandler create() {
        return new BasicAuthorizationActionHandler();
    }

    public boolean execute(ProcessContext ctx) {
        String authorizations = SystemHelper.getProps().getProperty("auth.basic.authorizations", "");
        if (StringUtils.isBlank(authorizations)) {
            return unAuthorization(ctx);
        }
        String authorization = ctx.getHeaders().get(HttpHeaderNames.AUTHORIZATION, "");
        if (StringUtils.isBlank(authorization)) {
            return unAuthorization(ctx);
        }
        String[] s = StringUtils.split(authorization.trim(), " ");
        if (s.length == 2) {
            String[] auths = StringUtils.split(authorizations, ",");
            String author = new String(Base64.decodeBase64(s[1]));
            if (StringUtils.isNotBlank(author) && ArrayUtils.contains(auths, author)) {
                return false;
            }
        }
        return unAuthorization(ctx);
    }

    private boolean unAuthorization(ProcessContext ctx) {
        ctx.getResponse().setStatus(HttpResponseStatus.UNAUTHORIZED);
        ctx.getOutputHeaders().set(HttpHeaderNames.WWW_AUTHENTICATE, "Basic realm");
        ctx.outputHtml();
        return true;
    }

    public boolean matchUri(String uri) {
        return true;
    }

    public int getOrder() {
        return -90;
    }
}

/*******************************************************************************
 * Copyright (c) Sep 30, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.jgit.core.service;

import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.Exceptions;
import org.iff.netty.server.handlers.ActionHandler;
import org.iff.netty.server.handlers.BaseActionHandler;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 30, 2016
 */
public abstract class BaseDefaultRestHandler extends BaseActionHandler {

    public static String urlDecode(String url) {
        if (url != null && url.length() > 0) {
            try {
                return URLDecoder.decode(url, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String urlEncode(String url) {
        if (url != null && url.length() > 0) {
            try {
                return URLEncoder.encode(url, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public ActionHandler create() {
        try {
            return getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            Exceptions.runtime("Fail to create ActionHandler!", e);
        }
        return null;
    }

    public boolean matchUri(String uri) {
        return true;
    }

    public int getOrder() {
        return 0;
    }

    public Map<String, String> pathParams(String[] pathSplit, int start, int end) {
        Map<String, String> conditionParams = new LinkedHashMap<String, String>();
        {
            start = Math.max(0, start);
            end = Math.min(Math.max(end, 0), pathSplit.length);
            for (int i = start; i < end; i++) {
                String tmp = pathSplit[i];
                String[] tmpSplit = StringUtils.split(tmp, "=");
                if (tmpSplit.length != 2) {
                    continue;
                }
                conditionParams.put(tmpSplit[0], urlDecode(tmpSplit[1]));
            }
        }
        return conditionParams;
    }

    public Map<String, String> postParams(Map<String, List<String>> map) {
        Map<String, String> conditionParams = new HashMap<String, String>();
        for (Entry<String, List<String>> entry : map.entrySet()) {
            String value = entry.getValue() != null && entry.getValue().size() > 0 ? entry.getValue().get(0) : null;
            if (value != null) {
                conditionParams.put(entry.getKey(), value);
            }
        }
        return conditionParams;
    }
}

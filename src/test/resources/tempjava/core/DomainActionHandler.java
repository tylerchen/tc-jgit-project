/*******************************************************************************
 * Copyright (c) Sep 24, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.jgit.core;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.*;
import org.iff.infra.util.mybatis.service.RepositoryService;
import org.iff.netty.server.ProcessContext;
import org.iff.netty.server.handlers.ActionHandler;
import org.iff.netty.server.handlers.BaseActionHandler;

import java.io.Closeable;
import java.util.Map;

/**
 * <pre>
 *     rest path = /rest/:Namespace/:ModelName/:RestUri
 *     GET    /articles                       -> articles#index
 *     GET    /articles/find/:conditions      -> articles#find
 *     GET    /articles/:id                   -> articles#show
 *     POST   /articles                       -> articles#create
 *     PUT    /articles/:id                   -> articles#update
 *     DELETE /articles/:id                   -> articles#destroy
 *     GET    /articles/ex/name/:conditions   -> articles#extra
 *     POST   /articles/ex/name               -> articles#extra
 *     PUT    /articles/ex/name/:conditions   -> articles#extra
 *     DELETE /articles/ex/name/:conditions   -> articles#extra
 * </pre>
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 24, 2016
 */
public class DomainActionHandler extends BaseActionHandler {

    public static final String URI_SPLIT = "uriSplit";
    public static final String uriPrefix = "/rest";
    public static final String RES_RECYCLE = "RES_RECYCLE";

    private String getNamespace(String[] uriSplit) {
        return PreRequiredHelper.requireNotBlank(PreRequiredHelper.requireMinLength(uriSplit, 2)[1]);
    }

    private String getModelName(String[] uriSplit) {
        return PreRequiredHelper.requireNotBlank(PreRequiredHelper.requireMinLength(uriSplit, 3)[2]);
    }

    private String[] getRestUri(String[] uriSplit) {
        PreRequiredHelper.requireMinLength(uriSplit, 3);
        if (uriSplit.length == 3) {
            return new String[0];
        }
        String[] restUri = new String[uriSplit.length - 3];
        System.arraycopy(uriSplit, 3, restUri, 0, restUri.length);
        return restUri;
    }

    public boolean execute(ProcessContext ctx) {
        String namespace = null;
        String modelName = null;
        String[] restUri = null;
        {
            String[] uris = StringUtils.split(ctx.getUri(), "/");
            if (uris.length < 3) {
                //TODO print rest path info.
                return true;
            }
            namespace = getNamespace(uris);
            modelName = getModelName(uris);
            restUri = getRestUri(uris);
        }
        if (restUri.length > 1 && "ex".equals(restUri[0])) {
            String name = restUri[1];
            String[] pathConditions = ArrayUtils.subarray(restUri, 1, restUri.length);
            // GET    /ex/name/:conditions   -> articles#extra
            // POST   /ex/name               -> articles#extra
            // PUT    /ex/name/:conditions   -> articles#extra
            // DELETE /ex/name/:conditions   -> articles#extra
            EventBusHelper.me().syncEvent(
                    DomainEventService.EVENT_NAME,
                    DomainEventService.Tuple.extra(namespace, modelName, name, pathConditions, ctx));
        } else if (ctx.isGet()) {
            if (restUri.length > 1 && "find".equals(restUri[0])) {
                // GET    /find/:conditions      -> articles#find
                String[] pathConditions = ArrayUtils.subarray(restUri, 1, restUri.length);
                EventBusHelper.me().syncEvent(
                        DomainEventService.EVENT_NAME,
                        DomainEventService.Tuple.find(namespace, modelName, pathConditions, ctx));
            } else if (restUri.length == 1) {
                // GET    /:id                   -> articles#show
                EventBusHelper.me().syncEvent(
                        DomainEventService.EVENT_NAME,
                        DomainEventService.Tuple.show(namespace, modelName, restUri[1], ctx));
            } else if (restUri.length == 0) {
                // GET    /                      -> articles#index
                EventBusHelper.me().syncEvent(
                        DomainEventService.EVENT_NAME,
                        DomainEventService.Tuple.index(namespace, modelName, ctx));
            } else {
                Exceptions.runtime(FCS.get("Request path not match: \nGET /model/find/:conditions\nGET /model/:id\nGET /model", uriPrefix), "REST-1001");
            }
        } else if (ctx.isPost()) {
            if (restUri.length == 0) {
                // POST   /                       -> articles#create
                EventBusHelper.me().syncEvent(
                        DomainEventService.EVENT_NAME,
                        DomainEventService.Tuple.create(namespace, modelName, ctx));
            } else {
                Exceptions.runtime(FCS.get("Request path not match: POST {0}/model/:id", uriPrefix), "REST-1001");
            }
        } else if (ctx.isPut()) {
            if (restUri.length == 1) {
                // PUT    /:id                   -> articles#update
                EventBusHelper.me().syncEvent(
                        DomainEventService.EVENT_NAME,
                        DomainEventService.Tuple.update(namespace, modelName, restUri[1], ctx));
            } else {
                Exceptions.runtime(FCS.get("Request path not match: PUT {0}/model/:id", uriPrefix), "REST-1001");
            }
        } else if (ctx.isDelete()) {
            if (restUri.length == 1) {
                // DELETE /:id                   -> articles#destroy
                String id = restUri[2];
                EventBusHelper.me().syncEvent(
                        DomainEventService.EVENT_NAME,
                        DomainEventService.Tuple.destroy(namespace, modelName, restUri[1], ctx));
            } else {
                Exceptions.runtime(FCS.get("Request path not match: DELETE {0}/model/:id", uriPrefix), "REST-1001");
            }
        }
        return true;
    }

    public boolean done() {
        Map<String, RepositoryService> map = ThreadLocalHelper.get(RepositoryService.class.getName());
        Map<String, Closeable> close = ThreadLocalHelper.get(Closeable.class.getName());
        if (hasError()) {
            if (MapUtils.isNotEmpty(map)) {
                for (Map.Entry<String, RepositoryService> entry : map.entrySet()) {
                    MyBatisSqlSessionFactory.rollback(entry.getValue());
                }
            }
        } else {
            if (MapUtils.isNotEmpty(map)) {
                for (Map.Entry<String, RepositoryService> entry : map.entrySet()) {
                    MyBatisSqlSessionFactory.commit(entry.getValue());
                }
            }
        }
        if (MapUtils.isNotEmpty(close)) {
            for (Map.Entry<String, Closeable> entry : close.entrySet()) {
                SocketHelper.closeWithoutError(entry.getValue());
            }
        }
        ThreadLocalHelper.remove();
        return super.done();
    }

    public boolean matchUri(String uri) {
        return uriPrefix.equals(uri) || (uri.startsWith(uriPrefix)
                && (uri.charAt(uriPrefix.length()) == '/' || uri.charAt(uriPrefix.length()) == '?'));
    }

    public int getOrder() {
        return 100;
    }

    public ActionHandler create() {
        return new DomainActionHandler();
    }

}

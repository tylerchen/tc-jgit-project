/*******************************************************************************
 * Copyright (c) 2018-10-14 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core;

import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.Exceptions;
import org.iff.infra.util.GsonHelper;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.mybatis.plugin.Page;
import org.iff.infra.util.mybatis.service.RepositoryService;
import org.iff.netty.server.ProcessContext;

import java.util.List;
import java.util.Map;

/**
 * DomainRestService
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
 * <pre>
 *     DSL struct:
 *     id:
 *     namespace:
 *     modelname:
 *     name:
 *     restpath:/rest/:Namespace/:ModelName/:RestUri@type[int|long|float|boolean|date|char|string|intArr|longArr|floatArr|booleanArr|dateArr|charArr|stringArr]
 *     content:
 *     createtime:
 *     updatetime:
 *     ---
 *     DSL Parameter struct:
 *     id:
 *     dslid:
 *     parameter:
 *     type:
 *     nullable:
 *     nullchar:
 *     defaultvalue:
 *     createtime:
 *     updatetime:
 * </pre>
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-10-14
 * auto generate by qdp.
 */
public class DomainRestService {

    public static DomainRestService create() {
        return new DomainRestService();
    }

    public Page index(String namespace, String modelName, ProcessContext ctx) {
        RepositoryService service = null;
        try {
            Object dsl = getDsl(namespace, modelName, "index", ctx);
            Page page = Page.pageable(10, 1, 0, null);
            service = service(namespace);
            page = service.queryPage("dsl", MapHelper.toMap("page", page));
            return page;
        } finally {
            MyBatisSqlSessionFactory.close(service);
        }
    }

    public Page find(String namespace, String modelName, String[] pathConditions, ProcessContext ctx) {
        RepositoryService service = null;
        try {
            Object dsl = getDsl(namespace, modelName, "find", ctx);
            String restPath = "";//get rest path: /find/:name/:age[int|intArray]/:gender
            //TODO get name:type map
            Map<String/*name*/, String/*type*/> paramsDef = null;
            //TODO parse params map
            Map<String/*name*/, Object/*value*/> params = null;
            Page page = Page.pageable(10, 1, 0, null);
            service = service(namespace);
            page = service.queryPage("dsl", MapHelper.toMap("page", page, "vo", params));
            return page;
        } finally {
            MyBatisSqlSessionFactory.close(service);
        }
    }

    public Object show(String namespace, String modelName, String id, ProcessContext ctx) {
        RepositoryService service = null;
        try {
            Object dsl = getDsl(namespace, modelName, "show", ctx);
            String restPath = "";//get rest path: /find/:name/:age[int|intArray]/:gender
            //TODO get name:type map
            Map<String/*name*/, String/*type*/> paramsDef = null;
            //TODO parse params map
            Map<String/*name*/, Object/*value*/> params = null;
            service = service(namespace);
            return service.queryOne("dsl", MapHelper.toMap("vo", params));
        } finally {
            MyBatisSqlSessionFactory.close(service);
        }
    }

    public Object create(String namespace, String modelName, ProcessContext ctx) {
        RepositoryService service = null;
        Throwable error = null;
        try {
            Object dsl = getDsl(namespace, modelName, "create", ctx);
            //POVOCopyHelper.copyTo(arg, Class.forName(key))
            List<String> list = ctx.getPostData().getFirst().values().iterator().next();
            String postData = list.isEmpty() ? null : list.get(0);
            Object vo = GsonHelper.toJson(postData);
            return service.save("dsl", MapHelper.toMap("vo", vo));
        } catch (Throwable t) {
            error = t;
            if (error instanceof RuntimeException) {
                throw (RuntimeException) error;
            }
            Exceptions.runtime("", error);
            return null;//Unreachable
        } finally {
            if (error != null) {
                MyBatisSqlSessionFactory.rollback(service);
            } else {
                MyBatisSqlSessionFactory.commit(service);
            }
        }
    }

    public Object update(String namespace, String modelName, String id, ProcessContext ctx) {
        RepositoryService service = null;
        Throwable error = null;
        try {
            Object dsl = getDsl(namespace, modelName, "update", ctx);
            //POVOCopyHelper.copyTo(arg, Class.forName(key))
            List<String> list = ctx.getPostData().getFirst().values().iterator().next();
            String postData = list.isEmpty() ? null : list.get(0);
            Object vo = GsonHelper.toJson(postData);
            return service.update("dsl", MapHelper.toMap("vo", vo));
        } catch (Throwable t) {
            error = t;
            if (error instanceof RuntimeException) {
                throw (RuntimeException) error;
            }
            Exceptions.runtime("", error);
            return null;//Unreachable
        } finally {
            if (error != null) {
                MyBatisSqlSessionFactory.rollback(service);
            } else {
                MyBatisSqlSessionFactory.commit(service);
            }
        }
    }

    public boolean destroy(String namespace, String modelName, String id, ProcessContext ctx) {
        RepositoryService service = null;
        Throwable error = null;
        try {
            Object dsl = getDsl(namespace, modelName, "destroy", ctx);
            Object ids = null;
            if (StringUtils.contains(id, ',')) {
                String[] split = StringUtils.split(id, ',');
                //TODO convert to id type
            }
            return service.remove("dsl", MapHelper.toMap("vo", MapHelper.toMap("ids", ids))) > 0;
        } catch (Throwable t) {
            error = t;
            if (error instanceof RuntimeException) {
                throw (RuntimeException) error;
            }
            Exceptions.runtime("", error);
            return false;//Unreachable
        } finally {
            if (error != null) {
                MyBatisSqlSessionFactory.rollback(service);
            } else {
                MyBatisSqlSessionFactory.commit(service);
            }
        }
    }

    public Object extra(String namespace, String modelName, String extraName, String[] pathConditions, ProcessContext ctx) {
        RepositoryService service = null;
        Throwable error = null;
        try {
            Object dsl = getDsl(namespace, modelName, extraName, ctx);
            service = service(namespace);
            if (ctx.isGet()) {
                Page page = Page.pageable(10, 1, 0, null);
                page = service.queryPage("dsl", MapHelper.toMap("page", page));
                return page;
            } else if (ctx.isPost()) {
                return service.save("dsl", MapHelper.toMap("vo", "vo"));
            } else if (ctx.isPut()) {
                return service.update("dsl", MapHelper.toMap("vo", "vo"));
            } else if (ctx.isDelete()) {
                return service.remove("dsl", MapHelper.toMap("vo", "vo")) > 0;
            }
            return null;
        } catch (Throwable t) {
            error = t;
            if (error instanceof RuntimeException) {
                throw (RuntimeException) error;
            }
            Exceptions.runtime("", error);
            return null;//Unreachable
        } finally {
            if (ctx.isGet()) {
                MyBatisSqlSessionFactory.close(service);
            } else if (error != null) {
                MyBatisSqlSessionFactory.rollback(service);
            } else {
                MyBatisSqlSessionFactory.commit(service);
            }
        }
    }

    public Object getDsl(String namespace, String modelName, String dslName, ProcessContext ctx) {
        //TODO get query dsl from system table.
        return extra("IFF_REST_SYSTEM",
                "Dsl",
                "getByNamespaceAndModelNameAndDslName",
                new String[]{namespace, modelName, dslName},
                ctx);
    }

    public RepositoryService service(String namespace) {
        return MyBatisSqlSessionFactory.service("");
    }
}

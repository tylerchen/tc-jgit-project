/*******************************************************************************
 * Copyright (c) 2018-10-20 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.iff.jgit.core.model.DomainModel;
import org.iff.jgit.core.model.DomainParamModel;
import org.iff.jgit.core.model.DomainQueryModel;
import org.iff.jgit.util.SystemHelper;
import org.iff.infra.util.*;
import org.iff.infra.util.mybatis.plugin.Page;
import org.iff.infra.util.mybatis.service.RepositoryService;
import org.iff.netty.server.ProcessContext;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DomainEventService
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
 *     queryContent:
 *     createtime:
 *     updatetime:
 *     ---
 *     DSL Parameter struct:
 *     id:
 *     dslid:
 *     name:
 *     type:
 *     nullable:
 *     nullchar:
 *     defaultvalue:
 *     createtime:
 *     updatetime:
 * </pre>
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-10-20
 * auto generate by qdp.
 */
public class DomainEventService implements EventBusHelper.EventProcess {

    public static final String EVENT_NAME = DomainEventService.class.getSimpleName();
    public static final String ACTION_index = "index";
    public static final String ACTION_find = "find";
    public static final String ACTION_show = "show";
    public static final String ACTION_create = "create";
    public static final String ACTION_update = "update";
    public static final String ACTION_destroy = "destroy";
    public static final String ACTION_extra = "extra";
    public static final String ACTION_queryPageByDsl = "queryPageByDsl";
    public static final String ACTION_queryOneByDsl = "queryOneByDsl";
    public static final String ACTION_queryListByDsl = "queryListByDsl";
    public static final String ACTION_querySizeByDsl = "querySizeByDsl";
    public static final String ACTION_removeByDsl = "removeByDsl";
    public static final String ACTION_updateByDsl = "updateByDsl";
    public static final String ACTION_saveByDsl = "saveByDsl";
    public static final String ACTION_queryPage = "queryPage";
    public static final String ACTION_queryOne = "queryOne";
    public static final String ACTION_queryList = "queryList";
    public static final String ACTION_querySize = "querySize";
    public static final String ACTION_removeDomain = "removeDomain";
    public static final String ACTION_updateDomain = "updateDomain";
    public static final String ACTION_saveDomain = "saveDomain";
    public static final String ACTION_service = "service";
    public static final String ACTION_getDomainParam = "getDomainParam";
    public static final String ACTION_getDomainQuery = "getDomainQuery";

    public String getName() {
        return EVENT_NAME;
    }

    public void listen(String name, Object args) {
        Tuple tuple = (Tuple) args;
        String action = tuple.action();
        Object result = null;
        try {
            if (ACTION_index.equals(action)) {
                result = index(tuple.namespace(), tuple.modelName(), tuple.ctx());
            } else if (ACTION_find.equals(action)) {
                result = find(tuple.namespace(), tuple.modelName(), tuple.pathConditions(), tuple.ctx());
            } else if (ACTION_show.equals(action)) {
                result = show(tuple.namespace(), tuple.modelName(), tuple.id(), tuple.ctx());
            } else if (ACTION_create.equals(action)) {
                result = create(tuple.namespace(), tuple.modelName(), tuple.ctx());
            } else if (ACTION_update.equals(action)) {
                result = update(tuple.namespace(), tuple.modelName(), tuple.id(), tuple.ctx());
            } else if (ACTION_destroy.equals(action)) {
                result = destroy(tuple.namespace(), tuple.modelName(), tuple.id(), tuple.ctx());
            } else if (ACTION_extra.equals(action)) {
                result = extra(tuple.namespace(), tuple.modelName(), tuple.queryName(), tuple.pathConditions(), tuple.ctx());
            } else if (ACTION_getDomainQuery.equals(action)) {
                result = getDomainQuery(tuple.namespace(), tuple.modelName(), tuple.queryName());
            } else if (ACTION_getDomainParam.equals(action)) {
                result = getDomainParam(tuple.namespace(), tuple.modelName(), tuple.domainQueryId());
            } else if (ACTION_service.equals(action)) {
                result = service(tuple.namespace());
            } else if (ACTION_saveDomain.equals(action)) {
                result = saveDomain(tuple.namespace(), tuple.modelName(), tuple.queryName(), tuple.params());
            } else if (ACTION_updateDomain.equals(action)) {
                result = updateDomain(tuple.namespace(), tuple.modelName(), tuple.queryName(), tuple.params());
            } else if (ACTION_removeDomain.equals(action)) {
                result = removeDomain(tuple.namespace(), tuple.modelName(), tuple.queryName(), tuple.params());
            } else if (ACTION_querySize.equals(action)) {
                result = querySize(tuple.namespace(), tuple.modelName(), tuple.queryName(), tuple.params());
            } else if (ACTION_queryList.equals(action)) {
                result = queryList(tuple.namespace(), tuple.modelName(), tuple.queryName(), tuple.params());
            } else if (ACTION_queryOne.equals(action)) {
                result = queryOne(tuple.namespace(), tuple.modelName(), tuple.queryName(), tuple.params());
            } else if (ACTION_queryPage.equals(action)) {
                result = queryPage(tuple.namespace(), tuple.modelName(), tuple.queryName(), tuple.params());
            } else if (ACTION_saveByDsl.equals(action)) {
                result = saveByDsl(tuple.namespace(), tuple.queryDsl(), tuple.params());
            } else if (ACTION_updateByDsl.equals(action)) {
                result = updateByDsl(tuple.namespace(), tuple.queryDsl(), tuple.params());
            } else if (ACTION_removeByDsl.equals(action)) {
                result = removeByDsl(tuple.namespace(), tuple.queryDsl(), tuple.params());
            } else if (ACTION_querySizeByDsl.equals(action)) {
                result = querySizeByDsl(tuple.namespace(), tuple.queryDsl(), tuple.params());
            } else if (ACTION_queryListByDsl.equals(action)) {
                result = queryListByDsl(tuple.namespace(), tuple.queryDsl(), tuple.params());
            } else if (ACTION_queryOneByDsl.equals(action)) {
                result = queryOneByDsl(tuple.namespace(), tuple.queryDsl(), tuple.params());
            } else if (ACTION_queryPageByDsl.equals(action)) {
                result = queryPageByDsl(tuple.namespace(), tuple.queryDsl(), tuple.params());
            } else {
                Exceptions.runtime("DomainEventService no event: " + tuple.action() + " found!");
            }
            tuple.result(result);
        } catch (Exception e) {
            tuple.result(e);
        }
    }

    public Page index(String namespace, String modelName, ProcessContext ctx) {
        Page page = Page.pageable(10, 1, 0, null);
        return queryPage(namespace, modelName, ACTION_index, MapHelper.toMap("vo", new DomainModel().init(namespace, modelName), "page", page));
    }

    protected DomainModel pathParamToVariable(String namespace, String modelName, String queryName, String[] pathConditions) {
        //get rest path: /find/:name/:age/:gender
        DomainQueryModel dqm = new DomainQueryModel().init(namespace, modelName);
        {
            dqm.setNamespace(namespace);
            dqm.setModelName(modelName);
            dqm.setQueryName(ACTION_find);
            SystemEventService.Tuple<DomainQueryModel> tuple = SystemEventService.Tuple.getDomainQueryModel(dqm);
            EventBusHelper.me().syncEvent(SystemEventService.EVENT_NAME, tuple);
            dqm = dqm.fromMap(tuple.result());
        }
        Map<String, DomainParamModel> paramMap = new HashMap<>();
        {
            DomainParamModel dpm = new DomainParamModel().init(namespace, modelName);
            dpm.setNamespace(namespace);
            dpm.setModelName(modelName);
            SystemEventService.Tuple<Page<DomainParamModel>> tuple = SystemEventService.Tuple.findDomainParamModel(dpm, Page.offsetPage(0, 1000, null));
            EventBusHelper.me().syncEvent(SystemEventService.EVENT_NAME, tuple);
            List<DomainParamModel> list = tuple.result().getRows();
            for (DomainParamModel tmp : list) {
                paramMap.put(tmp.getParamName(), tmp);
            }
        }
        String[] split = StringUtils.split(dqm.getRestPath(), '/');
        List<String> paramNames = new ArrayList<>();
        for (String str : split) {
            if (str.startsWith(":")) {
                paramNames.add(str.substring(1));
            }
        }
        DomainModel dm = new DomainModel().init(namespace, modelName);
        for (int i = 0; i < paramNames.size(); i++) {
            String paramName = paramNames.get(i);
            String paramValue = pathConditions.length > i ? pathConditions[i] : null;
            Object result = convert(paramName, paramValue, paramMap.get(paramName));
            dm.put(paramName, result);
        }
        return dm;
    }

    protected <T> T convert(String paramName, String paramValue, DomainParamModel dpm) {
        DomainParamModel found = dpm;
        if (found == null) {
            return (T) paramValue;
        }
        String paramType = found.getParamType();
        boolean isNullable = SystemHelper.isTrue(found.getIsNullable());
        String nullChar = found.getNullChar();
        String defaultValue = found.getDefaultValue();
        if (paramValue == null || paramValue.equals(nullChar)) {
            if (defaultValue == null || defaultValue.equals(nullChar)) {
                return null;
            }
            paramValue = defaultValue;
        }
        if ("String".equals(paramType)) {
            return (T) paramValue;
        } else if ("byte[]".equals(paramType)) {
            return (T) TypeConvertHelper.me().get(byte[].class.getName()).convert(byte[].class.getName(), paramValue, paramValue.getClass(), null);
        } else {
            return (T) TypeConvertHelper.me().get(paramType).convert(paramType, paramValue, paramValue.getClass(), null);
        }
    }

    public Page find(String namespace, String modelName, String[] pathConditions, ProcessContext ctx) {
        //get rest path: /find/:name/:age/:gender
        DomainModel params = pathParamToVariable(namespace, modelName, ACTION_find, pathConditions);
        Page page = Page.pageable(10, 1, 0, null);
        return queryPage(namespace, modelName, ACTION_find, MapHelper.toMap("page", page, "vo", params));
    }

    public DomainModel show(String namespace, String modelName, Object id, ProcessContext ctx) {
        DomainModel params = pathParamToVariable(namespace, modelName, ACTION_find, new String[]{id == null ? null : id.toString()});
        Page page = Page.pageable(10, 1, 0, null);
        return queryOne(namespace, modelName, ACTION_show, MapHelper.toMap("vo", params));
    }

    public DomainModel create(String namespace, String modelName, ProcessContext ctx) {
        List<String> list = ctx.getPostData().getFirst().values().iterator().next();
        String postData = list.isEmpty() ? null : list.get(0);
        Object vo = GsonHelper.toJson(postData);
        DomainModel dm = new DomainModel().init(namespace, modelName);
        dm.rePutAll((Map<String, Object>) vo);
        return dm.save();
    }

    public Object update(String namespace, String modelName, Object id, ProcessContext ctx) {
        List<String> list = ctx.getPostData().getFirst().values().iterator().next();
        String postData = list.isEmpty() ? null : list.get(0);
        Object vo = GsonHelper.toJson(postData);//TODO id
        DomainModel dm = pathParamToVariable(namespace, modelName, ACTION_update, new String[]{id == null ? null : id.toString()});
        dm.rePutAll((Map<String, Object>) vo);
        return dm.update();
    }

    public int destroy(String namespace, String modelName, Object id, ProcessContext ctx) {
        int count = 0;
        if (id instanceof String) {
            String[] ids = StringUtils.split((String) id, ',');
            for (String idStr : ids) {
                DomainModel dm = pathParamToVariable(namespace, modelName, ACTION_destroy, new String[]{id == null ? null : id.toString()});
                count += dm.removeById(id);
            }
        } else if (id.getClass().isArray()) {
            int len = Array.getLength(id);
            for (int i = 0; i < len; i++) {
                Object idValue = Array.get(id, i);
                DomainModel dm = pathParamToVariable(namespace, modelName, ACTION_destroy, new String[]{id == null ? null : idValue.toString()});
                count += dm.removeById(id);
            }
        }
        return count;
    }

    public Object extra(String namespace, String modelName, String queryName, String[] pathConditions, ProcessContext ctx) {
        DomainModel params = pathParamToVariable(namespace, modelName, ACTION_find, pathConditions);
        if (ctx.isGet()) {
            Page page = Page.pageable(10, 1, 0, null);
            page = queryPage(namespace, modelName, queryName, MapHelper.toMap("vo", params, "page", page));
            return page;
        } else if (ctx.isPost()) {
            List<String> list = ctx.getPostData().getFirst().values().iterator().next();
            String postData = list.isEmpty() ? null : list.get(0);
            Object vo = GsonHelper.toJson(postData);
            DomainModel dm = new DomainModel().init(namespace, modelName);
            dm.rePutAll((Map<String, Object>) vo);
            return saveDomain(namespace, modelName, queryName, dm);
        } else if (ctx.isPut()) {
            List<String> list = ctx.getPostData().getFirst().values().iterator().next();
            String postData = list.isEmpty() ? null : list.get(0);
            Object vo = GsonHelper.toJson(postData);
            DomainModel dm = new DomainModel().init(namespace, modelName);
            dm.rePutAll((Map<String, Object>) vo);
            return updateDomain(namespace, modelName, queryName, dm);
        } else if (ctx.isDelete()) {
            String id = pathConditions[0];
            int count = 0;
            String[] ids = StringUtils.split((String) id, ',');
            for (String idStr : ids) {
                DomainModel dm = pathParamToVariable(namespace, modelName, ACTION_destroy, new String[]{id == null ? null : id.toString()});
                count += dm.removeById(id);
            }
            return count;
        }
        return null;
    }

    public DomainQueryModel getDomainQuery(String namespace, String modelName, String queryName) {
        DomainQueryModel dqm = new DomainQueryModel().init(namespace, modelName);
        {
            dqm.setQueryName(queryName);
        }
        SystemEventService.Tuple<DomainQueryModel> tuple = SystemEventService.Tuple.getDomainQueryModel(dqm);
        EventBusHelper.me().syncEvent(SystemEventService.EVENT_NAME, tuple);
        return tuple.result();
    }

    public List<DomainParamModel> getDomainParam(String namespace, String modelName, String domainQueryId) {
        DomainParamModel dpm = new DomainParamModel().init(namespace, modelName);
        {
            dpm.setDomainQueryId(domainQueryId);
        }
        SystemEventService.Tuple<Page<DomainParamModel>> tuple = SystemEventService.Tuple.findDomainParamModel(dpm, Page.offsetPage(0, 1000, null));
        EventBusHelper.me().syncEvent(SystemEventService.EVENT_NAME, tuple);
        return tuple.result().getRows();
    }

    public RepositoryService service(String namespace) {
        Map<String, RepositoryService> map = ThreadLocalHelper.get(RepositoryService.class.getName());
        if (MapUtils.isEmpty(map)) {
            map = new HashMap<String, RepositoryService>();
            ThreadLocalHelper.set(RepositoryService.class.getName(), map);
        }
        RepositoryService service = map.get(namespace);
        if (service == null) {
            service = MyBatisSqlSessionFactory.service(namespace);
            map.put(RepositoryService.class.getName(), service);
        }
        return service;
    }

    public int saveDomain(String namespace, String modelName, String queryName, Object params) {
        DomainQueryModel domainQuery = getDomainQuery(namespace, modelName, queryName);
        return saveByDsl(namespace, domainQuery.getQueryContent(), params);
    }

    public int updateDomain(String namespace, String modelName, String queryName, Object params) {
        DomainQueryModel domainQuery = getDomainQuery(namespace, modelName, queryName);
        return updateByDsl(namespace, domainQuery.getQueryContent(), params);
    }

    public int removeDomain(String namespace, String modelName, String queryName, Object params) {
        DomainQueryModel domainQuery = getDomainQuery(namespace, modelName, queryName);
        return removeByDsl(namespace, domainQuery.getQueryContent(), params);
    }

    public long querySize(String namespace, String modelName, String queryName, Object params) {
        DomainQueryModel domainQuery = getDomainQuery(namespace, modelName, queryName);
        return querySizeByDsl(namespace, domainQuery.getQueryContent(), params);
    }

    public <T> List<T> queryList(String namespace, String modelName, String queryName, Object params) {
        DomainQueryModel domainQuery = getDomainQuery(namespace, modelName, queryName);
        return null;
    }

    public <T> T queryOne(String namespace, String modelName, String queryName, Object params) {
        DomainQueryModel domainQuery = getDomainQuery(namespace, modelName, queryName);
        return queryOneByDsl(namespace, domainQuery.getQueryContent(), params);
    }

    public <T> Page queryPage(String namespace, String modelName, String queryName, Object params) {
        DomainQueryModel domainQuery = getDomainQuery(namespace, modelName, queryName);
        return queryPageByDsl(namespace, domainQuery.getQueryContent(), params);
    }

    public int saveByDsl(String namespace, String queryDsl, Object params) {
        return service(namespace).save(queryDsl, params);
    }

    public int updateByDsl(String namespace, String queryDsl, Object params) {
        return service(namespace).update(queryDsl, params);
    }

    public int removeByDsl(String namespace, String queryDsl, Object params) {
        return service(namespace).remove(queryDsl, params);
    }

    public long querySizeByDsl(String namespace, String queryDsl, Object params) {
        return service(namespace).querySize(queryDsl, params);
    }

    public <T> List<T> queryListByDsl(String namespace, String queryDsl, Object params) {
        return service(namespace).queryList(queryDsl, params);
    }

    public <T> T queryOneByDsl(String namespace, String queryDsl, Object params) {
        return service(namespace).queryOne(queryDsl, params);
    }

    public <T> Page queryPageByDsl(String namespace, String queryDsl, Object params) {
        return service(namespace).queryPage(queryDsl, params);
    }

    public static class Tuple<T> implements Serializable, org.iff.jgit.util.Tuple.Result {
        private Object[] args = new Object[12];

        public Tuple() {
        }

        public Tuple(String action, String namespace, String modelName, String queryName, Object id, String[] pathConditions, ProcessContext ctx, String queryDsl, Object params, DomainModel domainModel, String domainQueryId) {
            args = new Object[]{action, namespace, modelName, queryName, id, pathConditions, ctx, queryDsl, params, domainModel, domainQueryId, null};
        }

        public static Tuple<Page> index(String namespace, String modelName, ProcessContext ctx) {
            return new Tuple<>(ACTION_index, namespace, modelName, null, null, null, ctx, null, null, null, null);
        }

        public static Tuple<Page> find(String namespace, String modelName, String[] pathConditions, ProcessContext ctx) {
            return new Tuple<>(ACTION_find, namespace, modelName, null, null, pathConditions, ctx, null, null, null, null);
        }

        public static Tuple<Object> show(String namespace, String modelName, Object id, ProcessContext ctx) {
            return new Tuple<>(ACTION_show, namespace, modelName, null, id, null, ctx, null, null, null, null);
        }


        public static Tuple<Object> create(String namespace, String modelName, ProcessContext ctx) {
            return new Tuple<>(ACTION_create, namespace, modelName, null, null, null, ctx, null, null, null, null);
        }

        public static Tuple<Object> update(String namespace, String modelName, Object id, ProcessContext ctx) {
            return new Tuple<>(ACTION_update, namespace, modelName, null, id, null, ctx, null, null, null, null);
        }

        public static Tuple<Integer> destroy(String namespace, String modelName, Object id, ProcessContext ctx) {
            return new Tuple<>(ACTION_destroy, namespace, modelName, null, id, null, ctx, null, null, null, null);
        }

        public static Tuple<Object> extra(String namespace, String modelName, String queryName, String[] pathConditions, ProcessContext ctx) {
            return new Tuple<>(ACTION_extra, namespace, modelName, queryName, null, pathConditions, ctx, null, null, null, null);
        }

        public static Tuple<DomainQueryModel> getDomainQuery(String namespace, String modelName, String queryName) {
            return new Tuple<>(ACTION_getDomainQuery, namespace, modelName, queryName, null, null, null, null, null, null, null);
        }

        public static Tuple<List<DomainParamModel>> getDomainParam(String namespace, String modelName, String domainQueryId) {
            return new Tuple<>(ACTION_getDomainParam, namespace, modelName, null, null, null, null, null, null, null, domainQueryId);
        }

        public static Tuple<RepositoryService> service(String namespace) {
            return new Tuple<>(ACTION_service, namespace, null, null, null, null, null, null, null, null, null);
        }

        public static Tuple<Integer> saveDomain(String namespace, String modelName, String queryName, Object params) {
            return new Tuple<>(ACTION_saveDomain, namespace, modelName, queryName, null, null, null, null, params, null, null);
        }

        public static Tuple<Integer> updateDomain(String namespace, String modelName, String queryName, Object params) {
            return new Tuple<>(ACTION_updateDomain, namespace, modelName, queryName, null, null, null, null, params, null, null);
        }

        public static Tuple<Integer> removeDomain(String namespace, String modelName, String queryName, Object params) {
            return new Tuple<>(ACTION_removeDomain, namespace, modelName, queryName, null, null, null, null, params, null, null);
        }

        public static Tuple<Long> querySize(String namespace, String modelName, String queryName, Object params) {
            return new Tuple<>(ACTION_querySize, namespace, modelName, queryName, null, null, null, null, params, null, null);
        }

        public static Tuple<List> queryList(String namespace, String modelName, String queryName, Object params) {
            return new Tuple<>(ACTION_queryList, namespace, modelName, queryName, null, null, null, null, params, null, null);
        }

        public static Tuple<Object> queryOne(String namespace, String modelName, String queryName, Object params) {
            return new Tuple<>(ACTION_queryOne, namespace, modelName, queryName, null, null, null, null, params, null, null);
        }

        public static Tuple<Page> queryPage(String namespace, String modelName, String queryName, Object params) {
            return new Tuple<>(ACTION_queryPage, namespace, modelName, queryName, null, null, null, null, params, null, null);
        }

        public static Tuple<Integer> saveByDsl(String namespace, String queryDsl, Object params) {
            return new Tuple<>(ACTION_saveByDsl, namespace, null, null, null, null, null, queryDsl, params, null, null);
        }

        public static Tuple<Integer> updateByDsl(String namespace, String queryDsl, Object params) {
            return new Tuple<>(ACTION_updateByDsl, namespace, null, null, null, null, null, queryDsl, params, null, null);
        }

        public static Tuple<Integer> removeByDsl(String namespace, String queryDsl, Object params) {
            return new Tuple<>(ACTION_removeByDsl, namespace, null, null, null, null, null, queryDsl, params, null, null);
        }

        public static Tuple<Long> querySizeByDsl(String namespace, String queryDsl, Object params) {
            return new Tuple<>(ACTION_querySizeByDsl, namespace, null, null, null, null, null, queryDsl, params, null, null);
        }

        public static Tuple<Page> queryListByDsl(String namespace, String queryDsl, Object params) {
            return new Tuple<>(ACTION_queryListByDsl, namespace, null, null, null, null, null, queryDsl, params, null, null);
        }

        public static Tuple<Object> queryOneByDsl(String namespace, String queryDsl, Object params) {
            return new Tuple<>(ACTION_queryOneByDsl, namespace, null, null, null, null, null, queryDsl, params, null, null);
        }

        public static Tuple<Page> queryPageByDsl(String namespace, String queryDsl, Object params) {
            return new Tuple<>(ACTION_queryPageByDsl, namespace, null, null, null, null, null, queryDsl, params, null, null);
        }

        public String action() {
            return (String) args[0];
        }

        public String namespace() {
            return (String) args[1];
        }

        public String modelName() {
            return (String) args[2];
        }

        public String queryName() {
            return (String) args[3];
        }

        public Object id() {
            return (Object) args[4];
        }

        public String[] pathConditions() {
            return (String[]) args[5];
        }

        public ProcessContext ctx() {
            return (ProcessContext) args[6];
        }

        public String queryDsl() {
            return (String) args[7];
        }

        public Object params() {
            return args[8];
        }

        public DomainModel domainModel() {
            return (DomainModel) args[9];
        }

        public String domainQueryId() {
            return (String) args[10];
        }

        public boolean hasError() {
            return args[args.length - 1] instanceof Throwable;
        }

        public Throwable error() {
            return (Throwable) args[args.length - 1];
        }

        public T result() {
            return (T) args[args.length - 1];
        }

        public void result(Object result) {
            args[args.length - 1] = result;
        }
    }
}

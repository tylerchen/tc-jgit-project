/*******************************************************************************
 * Copyright (c) 2018-11-04 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core;

import org.apache.commons.collections.MapUtils;
import org.iff.jgit.core.model.DataSourceModel;
import org.iff.jgit.core.model.DomainParamModel;
import org.iff.jgit.core.model.DomainQueryModel;
import org.iff.infra.util.*;
import org.iff.infra.util.mybatis.plugin.Page;
import org.iff.infra.util.mybatis.service.RepositoryService;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.iff.jgit.util.SystemHelper.NAMESPACE_SYS;
import static org.iff.jgit.util.SystemHelper.key;

/**
 * SystemEventService
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-04
 * auto generate by qdp.
 */
public class SystemEventService implements EventBusHelper.EventProcess {

    public static final String EVENT_NAME = SystemEventService.class.getSimpleName();
    public static final String ACTION_createTableDomainQueryModel = "createTableDomainQueryModel";
    public static final String ACTION_saveDomainQueryModel = "saveDomainQueryModel";
    public static final String ACTION_updateDomainQueryModel = "updateDomainQueryModel";
    public static final String ACTION_removeDomainQueryModel = "removeDomainQueryModel";
    public static final String ACTION_getDomainQueryModel = "getDomainQueryModel";
    public static final String ACTION_findDomainQueryModel = "findDomainQueryModel";
    public static final String ACTION_createTableDomainParamModel = "createTableDomainParamModel";
    public static final String ACTION_saveDomainParamModel = "saveDomainParamModel";
    public static final String ACTION_updateDomainParamModel = "updateDomainParamModel";
    public static final String ACTION_removeDomainParamModel = "removeDomainParamModel";
    public static final String ACTION_getDomainParamModel = "getDomainParamModel";
    public static final String ACTION_findDomainParamModel = "findDomainParamModel";
    public static final String ACTION_createTableDataSourceModel = "createTableDataSourceModel";
    public static final String ACTION_saveDataSourceModel = "saveDataSourceModel";
    public static final String ACTION_updateDataSourceModel = "updateDataSourceModel";
    public static final String ACTION_removeDataSourceModel = "removeDataSourceModel";
    public static final String ACTION_getDataSourceModel = "getDataSourceModel";
    public static final String ACTION_findDataSourceModel = "findDataSourceModel";

    public String getName() {
        return EVENT_NAME;
    }

    public void listen(String event, Object params) {
        Tuple tuple = (Tuple) params;
        String action = tuple.action();
        Object result = null;
        try {
            if (ACTION_createTableDomainQueryModel.equals(action)) {
                result = createTableDomainQueryModel();
            } else if (ACTION_saveDomainQueryModel.equals(action)) {
                result = saveDomainQueryModel(tuple.domainQueryModel());
            } else if (ACTION_updateDomainQueryModel.equals(action)) {
                result = updateDomainQueryModel(tuple.domainQueryModel());
            } else if (ACTION_removeDomainQueryModel.equals(action)) {
                result = removeDomainQueryModel(tuple.id());
            } else if (ACTION_getDomainQueryModel.equals(action)) {
                result = getDomainQueryModel(tuple.domainQueryModel());
            } else if (ACTION_findDomainQueryModel.equals(action)) {
                result = findDomainQueryModel(tuple.domainQueryModel(), tuple.page());
            } else if (ACTION_createTableDomainParamModel.equals(action)) {
                result = createTableDomainParamModel();
            } else if (ACTION_saveDomainParamModel.equals(action)) {
                result = saveDomainParamModel(tuple.domainParamModel());
            } else if (ACTION_updateDomainParamModel.equals(action)) {
                result = updateDomainParamModel(tuple.domainParamModel());
            } else if (ACTION_removeDomainParamModel.equals(action)) {
                result = removeDomainParamModel(tuple.id());
            } else if (ACTION_getDomainParamModel.equals(action)) {
                result = getDomainParamModel(tuple.domainParamModel());
            } else if (ACTION_findDomainParamModel.equals(action)) {
                result = findDomainParamModel(tuple.domainParamModel(), tuple.page());
            } else if (ACTION_createTableDataSourceModel.equals(action)) {
                result = createTableDataSourceModel();
            } else if (ACTION_saveDataSourceModel.equals(action)) {
                result = saveDataSourceModel(tuple.dataSourceModel());
            } else if (ACTION_updateDataSourceModel.equals(action)) {
                result = updateDataSourceModel(tuple.dataSourceModel());
            } else if (ACTION_removeDataSourceModel.equals(action)) {
                result = removeDataSourceModel(tuple.id());
            } else if (ACTION_getDataSourceModel.equals(action)) {
                result = getDataSourceModel(tuple.dataSourceModel());
            } else if (ACTION_findDataSourceModel.equals(action)) {
                result = findDataSourceModel(tuple.dataSourceModel(), tuple.page());
            } else {
                Exceptions.runtime("SystemEventService no event: " + action + " found!");
            }
            tuple.result(result);
        } catch (Exception e) {
            tuple.result(e);
        }
    }

    public int createTableDomainQueryModel() {
        String sql = CacheHelper.getCache().get(key("DomainQueryModel", "createTable", NAMESPACE_SYS));
        return service().update(sql, null);
    }

    public DomainQueryModel saveDomainQueryModel(DomainQueryModel model) {
        String queryDsl = CacheHelper.getCache().get(key("DomainQueryModel", "save", NAMESPACE_SYS));
        service().save(queryDsl, model);
        return model;
    }

    public DomainQueryModel updateDomainQueryModel(DomainQueryModel model) {
        String queryDsl = CacheHelper.getCache().get(key("DomainQueryModel", "update", NAMESPACE_SYS));
        service().update(queryDsl, model);
        return model;
    }

    public int removeDomainQueryModel(Object id) {
        String queryDsl = CacheHelper.getCache().get(key("DomainQueryModel", "remove", NAMESPACE_SYS));
        return service().remove(queryDsl, id);
    }

    public DomainQueryModel getDomainQueryModel(DomainQueryModel model) {
        String queryDsl = CacheHelper.getCache().get(key("DomainQueryModel", "get", NAMESPACE_SYS));
        Map<String, Object> vo = service().queryOne(queryDsl, MapHelper.toMap("vo", model));
        return new DomainQueryModel().init(NAMESPACE_SYS, "DomainQueryModel").fromMap(vo);
    }

    public Page<DomainQueryModel> findDomainQueryModel(DomainQueryModel model, Page page) {
        String queryDsl = CacheHelper.getCache().get(key("DomainQueryModel", "find", NAMESPACE_SYS));
        Page result = service().queryPage(queryDsl, MapHelper.toMap("vo", model, "page", page));
        return new DomainQueryModel().init(NAMESPACE_SYS, "DomainQueryModel").fromPage(result);
    }

    public int createTableDomainParamModel() {
        String sql = CacheHelper.getCache().get(key("DomainParamModel", "createTable", NAMESPACE_SYS));
        return service().update(sql, null);
    }

    public DomainParamModel saveDomainParamModel(DomainParamModel model) {
        String queryDsl = CacheHelper.getCache().get(key("DomainParamModel", "save", NAMESPACE_SYS));
        service().save(queryDsl, model);
        return model;
    }

    public DomainParamModel updateDomainParamModel(DomainParamModel model) {
        String queryDsl = CacheHelper.getCache().get(key("DomainParamModel", "update", NAMESPACE_SYS));
        service().update(queryDsl, model);
        return model;
    }

    public int removeDomainParamModel(Object id) {
        String queryDsl = CacheHelper.getCache().get(key("DomainParamModel", "remove", NAMESPACE_SYS));
        return service().remove(queryDsl, id);
    }

    public DomainParamModel getDomainParamModel(DomainParamModel model) {
        String queryDsl = CacheHelper.getCache().get(key("DomainParamModel", "get", NAMESPACE_SYS));
        Map<String, Object> vo = service().queryOne(queryDsl, MapHelper.toMap("vo", model));
        return new DomainParamModel().init(NAMESPACE_SYS, "DomainParamModel").rePutAll(vo);
    }

    public Page<DomainParamModel> findDomainParamModel(DomainParamModel model, Page page) {
        String queryDsl = CacheHelper.getCache().get(key("DomainParamModel", "find", NAMESPACE_SYS));
        Page result = service().queryPage(queryDsl, MapHelper.toMap("vo", model));
        return new DomainParamModel().init(NAMESPACE_SYS, "DomainParamModel").fromPage(result);
    }

    public int createTableDataSourceModel() {
        String sql = CacheHelper.getCache().get(key("DataSourceModel", "createTable", NAMESPACE_SYS));
        return service().update(sql, null);
    }

    public DataSourceModel saveDataSourceModel(DataSourceModel model) {
        String queryDsl = CacheHelper.getCache().get(key("DataSourceModel", "save", NAMESPACE_SYS));
        service().save(queryDsl, model);
        return model;
    }

    public DataSourceModel updateDataSourceModel(DataSourceModel model) {
        String queryDsl = CacheHelper.getCache().get(key("DataSourceModel", "update", NAMESPACE_SYS));
        service().update(queryDsl, model);
        return model;
    }

    public int removeDataSourceModel(Object id) {
        String queryDsl = CacheHelper.getCache().get(key("DataSourceModel", "remove", NAMESPACE_SYS));
        return service().remove(queryDsl, id);
    }

    public DataSourceModel getDataSourceModel(DataSourceModel model) {
        String queryDsl = CacheHelper.getCache().get(key("DataSourceModel", "get", NAMESPACE_SYS));
        Map<String, Object> vo = service().queryOne(queryDsl, MapHelper.toMap("vo", model));
        return new DataSourceModel().init(NAMESPACE_SYS, "DataSourceModel").fromMap(vo);
    }

    public Page<DataSourceModel> findDataSourceModel(DataSourceModel model, Page page) {
        String queryDsl = CacheHelper.getCache().get(key("DataSourceModel", "get", NAMESPACE_SYS));
        Page result = service().queryPage(queryDsl, page);
        return new DataSourceModel().init(NAMESPACE_SYS, "DataSourceModel").fromPage(result);
    }

    public RepositoryService service() {
        Map<String, RepositoryService> map = ThreadLocalHelper.get(RepositoryService.class.getName());
        if (MapUtils.isEmpty(map)) {
            map = new HashMap<String, RepositoryService>();
            ThreadLocalHelper.set(RepositoryService.class.getName(), map);
        }
        RepositoryService service = map.get(NAMESPACE_SYS);
        if (service == null) {
            service = MyBatisSqlSessionFactory.service(NAMESPACE_SYS);
        }
        return service;
    }

    public static class Tuple<T> implements Serializable, org.iff.jgit.util.Tuple.Result {
        private Object[] args = new Object[]{
                null /*0: action*/,
                null /*1: DomainQueryModel*/,
                null /*2: DomainParamModel*/,
                null /*3: DataSourceModel*/,
                null /*4: id*/,
                null /*5: Page*/,
                null /*10: result*/
        };

        public Tuple(String action, DomainQueryModel dqm, DomainParamModel dpm, DataSourceModel dsm, Object id, Page page) {
            args = new Object[]{action, dqm, dpm, dsm, id, page, null};
        }

        public static Tuple<Integer> createTableDomainQueryModel() {
            return new Tuple(ACTION_createTableDomainQueryModel, null, null, null, null, null);
        }

        public static Tuple<DomainQueryModel> saveDomainQueryModel(DomainQueryModel model) {
            return new Tuple(ACTION_saveDomainQueryModel, model, null, null, null, null);
        }

        public static Tuple<DomainQueryModel> updateDomainQueryModel(DomainQueryModel model) {
            return new Tuple(ACTION_updateDomainQueryModel, model, null, null, null, null);
        }

        public static Tuple<Integer> removeDomainQueryModel(Object id) {
            return new Tuple(ACTION_removeDomainQueryModel, null, null, null, id, null);
        }

        public static Tuple<DomainQueryModel> getDomainQueryModel(DomainQueryModel dqm) {
            return new Tuple(ACTION_getDomainQueryModel, dqm, null, null, null, null);
        }

        public static Tuple<Page<DomainQueryModel>> findDomainQueryModel(DomainQueryModel dqm, Page page) {
            return new Tuple(ACTION_findDomainQueryModel, dqm, null, null, null, page);
        }

        public static Tuple<Integer> createTableDomainParamModel() {
            return new Tuple(ACTION_createTableDomainParamModel, null, null, null, null, null);
        }

        public static Tuple<DomainParamModel> saveDomainParamModel(DomainParamModel model) {
            return new Tuple(ACTION_saveDomainParamModel, null, model, null, null, null);
        }

        public static Tuple<DomainParamModel> updateDomainParamModel(DomainParamModel model) {
            return new Tuple(ACTION_updateDomainParamModel, null, model, null, null, null);
        }

        public static Tuple<Integer> removeDomainParamModel(Object id) {
            return new Tuple(ACTION_removeDomainParamModel, null, null, null, id, null);
        }

        public static Tuple<DomainParamModel> getDomainParamModel(DomainParamModel dpm) {
            return new Tuple(ACTION_getDomainParamModel, null, dpm, null, null, null);
        }

        public static Tuple<Page<DomainParamModel>> findDomainParamModel(DomainParamModel dpm, Page page) {
            return new Tuple(ACTION_findDomainParamModel, null, dpm, null, null, page);
        }

        public static Tuple<Integer> createTableDataSourceModel() {
            return new Tuple(ACTION_createTableDataSourceModel, null, null, null, null, null);
        }

        public static Tuple<DataSourceModel> saveDataSourceModel(DataSourceModel model) {
            return new Tuple(ACTION_saveDataSourceModel, null, null, model, null, null);
        }

        public static Tuple<DataSourceModel> updateDataSourceModel(DataSourceModel model) {
            return new Tuple(ACTION_updateDataSourceModel, null, null, model, null, null);
        }

        public static Tuple<Integer> removeDataSourceModel(Object id) {
            return new Tuple(ACTION_removeDataSourceModel, null, null, null, id, null);
        }

        public static Tuple<DataSourceModel> getDataSourceModel(DataSourceModel dsm) {
            return new Tuple(ACTION_getDataSourceModel, null, null, dsm, null, null);
        }

        public static Tuple<Page<DataSourceModel>> findDataSourceModel(DataSourceModel dsm, Page page) {
            return new Tuple(ACTION_findDataSourceModel, null, null, dsm, null, page);
        }

        public String action() {
            return (String) args[0];
        }

        public DomainQueryModel domainQueryModel() {
            return (DomainQueryModel) args[1];
        }

        public DomainParamModel domainParamModel() {
            return (DomainParamModel) args[2];
        }

        public DataSourceModel dataSourceModel() {
            return (DataSourceModel) args[3];
        }

        public Object id() {
            return args[4];
        }

        public Page page() {
            return (Page) args[5];
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


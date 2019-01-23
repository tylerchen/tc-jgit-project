/*******************************************************************************
 * Copyright (c) 2018-10-16 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core;

import org.iff.infra.util.MapHelper;
import org.iff.infra.util.PreRequiredHelper;
import org.iff.infra.util.mybatis.service.RepositoryService;

import java.io.Serializable;
import java.util.Map;

/**
 * SuperModel
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-10-16
 * auto generate by qdp.
 */
public class SuperModel implements Serializable {

//    public static Map<String, Object> getBy(RepositoryService dao, String modelName, String fieldName, Object fieldValue) {
//        String dslName = modelName + ".get" + modelName + "By" + StringUtils.capitalize(fieldName);
//        return dao.queryOne(dslName, MapHelper.toMap("vo", MapHelper.toMap("id", fieldValue)));
//    }
//
//    public static int removeBy(RepositoryService dao, String modelName, String fieldName, Object fieldValue) {
//        String dslName = modelName + ".delete" + modelName + "By" + StringUtils.capitalize(fieldName);
//        return dao.remove(dslName, MapHelper.toMap("vo", MapHelper.toMap("id", fieldValue)));
//    }

    private RepositoryService dao;
    private String namespace;
    private String modelName;
    private Map<String, Object> data;

    public static SuperModel create(RepositoryService dao, String namespace, String modelName) {
        SuperModel sm = new SuperModel();
        sm.dao = PreRequiredHelper.requireNotNull(dao);
        sm.namespace = PreRequiredHelper.requireNotBlank(namespace);
        sm.modelName = PreRequiredHelper.requireNotBlank(modelName);
        return sm;
    }

    public static SuperModel create(RepositoryService dao, String namespace, String modelName, Map<String, Object> data) {
        SuperModel sm = new SuperModel();
        sm.dao = PreRequiredHelper.requireNotNull(dao);
        sm.namespace = PreRequiredHelper.requireNotBlank(namespace);
        sm.modelName = PreRequiredHelper.requireNotBlank(modelName);
        sm.data = PreRequiredHelper.requireNotNull(data);
        return sm;
    }

    public SuperModel get(Object id) {
        Object dsl = getDsl(dao, namespace, modelName, "getById");
        Map<String, Object> newData = dao.queryOne("dsl", MapHelper.toMap("vo", MapHelper.toMap("id", id)));
        return create(dao, namespace, modelName, newData);
    }

//    public SuperModel create(){
//
//    }

    private Object getDsl(RepositoryService dao, String namespace, String modelName, String dslName) {
        //TODO get query dsl from system table.
        return null;
    }
}

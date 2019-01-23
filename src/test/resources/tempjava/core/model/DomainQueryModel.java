/*******************************************************************************
 * Copyright (c) 2018-11-04 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core.model;

import javax.validation.constraints.NotNull;
import java.beans.Transient;
import java.io.Serializable;

/**
 * DomainQueryModel
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-04
 * auto generate by qdp.
 */
public class DomainQueryModel extends DomainModel<DomainQueryModel> implements Serializable {
    /*private String id;
    private String namespace;
    private String modelName;
    private String queryName;
    private String dbType;
    private String tableName;
    private String restPath;
    private String isPublic;
    private String queryContent;
    private String createTime;
    private String upateTime;*/

    public DomainQueryModel() {
    }

    @Override
    public DomainQueryModel init(@NotNull String namespace, @NotNull String modelName) {
        super.init(namespace, modelName);
        setNamespace(namespace());
        setModelName(modelName);
        return this;
    }

    @Override
    public DomainQueryModel newInstance() {
        return getClass() == DomainQueryModel.class ? new DomainQueryModel() : super.newInstance();
    }

    @Transient
    public String getId() {
        return (String) get("ID");
    }

    @Transient
    public void setId(String id) {
        put("ID", id);
    }

    @Transient
    public String getNamespace() {
        return (String) get("NAMESPACE");
    }

    @Transient
    public void setNamespace(String namespace) {
        put("NAMESPACE", namespace);
    }

    @Transient
    public String getModelName() {
        return (String) get("MODEL_NAME");
    }

    @Transient
    public void setModelName(String modelName) {
        put("MODEL_NAME", modelName);
    }

    @Transient
    public String getQueryName() {
        return (String) get("QUERY_NAME");
    }

    @Transient
    public void setQueryName(String queryName) {
        put("QUERY_NAME", queryName);
    }

    @Transient
    public String getDbType() {
        return (String) get("DB_TYPE");
    }

    @Transient
    public void setDbType(String dbType) {
        put("DB_TYPE", dbType);
    }

    @Transient
    public String getTableName() {
        return (String) get("TABLE_NAME");
    }

    @Transient
    public void setTableName(String tableName) {
        put("TABLE_NAME", tableName);
    }

    @Transient
    public String getRestPath() {
        return (String) get("REST_PATH");
    }

    @Transient
    public void setRestPath(String restPath) {
        put("REST_PATH", restPath);
    }

    @Transient
    public String getIsPublic() {
        return (String) get("IS_PUBLIC");
    }

    @Transient
    public void setIsPublic(String isPublic) {
        put("IS_PUBLIC", isPublic);
    }

    @Transient
    public String getQueryContent() {
        return (String) get("QUERY_CONTENT");
    }

    @Transient
    public void setQueryContent(String queryContent) {
        put("QUERY_CONTENT", queryContent);
    }

    @Transient
    public String getCreateTime() {
        return (String) get("CREATE_TIME");
    }

    @Transient
    public void setCreateTime(String createTime) {
        put("CREATE_TIME", createTime);
    }

    @Transient
    public String getUpdateTime() {
        return (String) get("UPDATE_TIME");
    }

    @Transient
    public void setUpdateTime(String updateTime) {
        put("UPDATE_TIME", updateTime);
    }
}

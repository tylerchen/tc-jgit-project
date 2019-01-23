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
 * DomainParamModel
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-04
 * auto generate by qdp.
 */
public class DomainParamModel extends DomainModel<DomainParamModel> implements Serializable {
    /*  private String id;
        private String namespace;
        private String modelName;
        private String domainQueryId;
        private String paramName;
        private String colName;
        private String paramType;
        private String isNullable;
        private String nullChar;
        private String defaultValue;
        private String createTime;
        private String updateTime;
    */
    public DomainParamModel() {
    }

    @Override
    public DomainParamModel init(@NotNull String namespace, @NotNull String modelName) {
        super.init(namespace, modelName);
        setNamespace(namespace);
        setModelName(modelName);
        return this;
    }

    @Override
    public DomainParamModel newInstance() {
        return getClass() == DomainParamModel.class ? new DomainParamModel() : super.newInstance();
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
    public String getDomainQueryId() {
        return (String) get("DOMAIN_QUERY_ID");
    }

    @Transient
    public void setDomainQueryId(String domainQueryId) {
        put("DOMAIN_QUERY_ID", domainQueryId);
    }

    @Transient
    public String getParamName() {
        return (String) get("PARAM_NAME");
    }

    @Transient
    public void setParamName(String paramName) {
        put("PARAM_NAME", paramName);
    }

    @Transient
    public String getColName() {
        return (String) get("COL_NAME");
    }

    @Transient
    public void setColName(String colName) {
        put("COL_NAME", colName);
    }

    @Transient
    public String getParamType() {
        return (String) get("PARAM_TYPE");
    }

    @Transient
    public void setParamType(String paramType) {
        put("PARAM_TYPE", paramType);
    }

    @Transient
    public String getIsNullable() {
        return (String) get("IS_NULLABLE");
    }

    @Transient
    public void setIsNullable(String isNullable) {
        put("IS_NULLABLE", isNullable);
    }

    @Transient
    public String getNullChar() {
        return (String) get("NULL_CHAR");
    }

    @Transient
    public void setNullChar(String nullChar) {
        put("NULL_CHAR", nullChar);
    }

    @Transient
    public String getDefaultValue() {
        return (String) get("DEFAULT_VALUE");
    }

    @Transient
    public void setDefaultValue(String defaultValue) {
        put("DEFAULT_VALUE", defaultValue);
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

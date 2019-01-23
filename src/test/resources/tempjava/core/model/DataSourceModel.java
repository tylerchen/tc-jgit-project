/*******************************************************************************
 * Copyright (c) Oct 9, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.jgit.core.model;

import javax.validation.constraints.NotNull;
import java.beans.Transient;
import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Oct 9, 2016
 */
@SuppressWarnings("serial")
public class DataSourceModel extends DomainModel<DataSourceModel> implements Serializable {
    /*private String id;
    private String namespace;
    private String name;
    private String user;
    private String password;
    private String url;
    private String driver;
    private String validationQuery;
    private int initConnection;
    private int maxConnection;
    private String description;
    private Date createTime;
    private Date updateTime;*/

    public DataSourceModel() {
    }

    @Override
    public DataSourceModel newInstance() {
        return getClass() == DataSourceModel.class ? new DataSourceModel() : super.newInstance();
    }

    @Override
    public DataSourceModel init(@NotNull String namespace, @NotNull String modelName) {
        super.init(namespace, modelName);
        setNamespace(namespace);
        return this;
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
    public String getName() {
        return (String) get("NAME");
    }

    @Transient
    public void setName(String name) {
        put("NAME", name);
    }

    @Transient
    public String getUser() {
        return (String) get("USER");
    }

    @Transient
    public void setUser(String user) {
        put("USER", user);
    }

    @Transient
    public String getPassword() {
        return (String) get("PASSWORD");
    }

    @Transient
    public void setPassword(String password) {
        put("PASSWORD", password);
    }

    @Transient
    public String getUrl() {
        return (String) get("URL");
    }

    @Transient
    public void setUrl(String url) {
        put("URL", url);
    }

    @Transient
    public String getDriver() {
        return (String) get("DRIVER");
    }

    @Transient
    public void setDriver(String driver) {
        put("DRIVER", driver);
    }

    @Transient
    public String getValidationQuery() {
        return (String) get("VALIDATION_QUERY");
    }

    @Transient
    public void setValidationQuery(String validationQuery) {
        put("VALIDATION_QUERY", validationQuery);
    }

    @Transient
    public Integer getInitConnection() {
        return (Integer) get("INIT_CONNECTION");
    }

    @Transient
    public void setInitConnection(int initConnection) {
        put("INIT_CONNECTION", initConnection);
    }

    @Transient
    public Integer getMaxConnection() {
        return (Integer) get("MAX_CONNECTION");
    }

    @Transient
    public void setMaxConnection(Integer maxConnection) {
        put("MAX_CONNECTION", maxConnection);
    }

    @Transient
    public String getDecryptKey() {
        return (String) get("DECRYPT_KEY");
    }

    @Transient
    public void setDdecryptKey(String decryptKey) {
        put("DECRYPT_KEY", decryptKey);
    }

    @Transient
    public String getDescription() {
        return (String) get("DESCRIPTION");
    }

    @Transient
    public void setDescription(String description) {
        put("DESCRIPTION", description);
    }

    @Transient
    public Date getCreateTime() {
        return (Date) get("CREATE_TIME");
    }

    @Transient
    public void setCreateTime(Date createTime) {
        put("CREATE_TIME", createTime);
    }

    @Transient
    public Date getUpdateTime() {
        return (Date) get("UPDATE_TIME");
    }

    @Transient
    public void setUpdateTime(Date updateTime) {
        put("UPDATE_TIME", updateTime);
    }
}

/*******************************************************************************
 * Copyright (c) Oct 8, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.jgit.core.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Oct 8, 2016
 */
@SuppressWarnings("serial")
public class QueryStatementModel implements Serializable {

    private String id;
    private String name;
    private String selectBody;
    private String fromBody;
    private String whereBody;
    private String description;
    private Date createTime;
    private Date updateTime;

    public QueryStatementModel() {
        super();
    }

    public static QueryStatementModel create(String name, String selectBody, String fromBody, String whereBody,
                                             String description) {
        QueryStatementModel query = new QueryStatementModel();
        query.name = name;
        query.selectBody = selectBody;
        query.fromBody = fromBody;
        query.whereBody = whereBody;
        query.description = description;
        query.createTime = new Date();
        query.updateTime = new Date();
        return query;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelectBody() {
        return selectBody;
    }

    public void setSelectBody(String selectBody) {
        this.selectBody = selectBody;
    }

    public String getFromBody() {
        return fromBody;
    }

    public void setFromBody(String fromBody) {
        this.fromBody = fromBody;
    }

    public String getWhereBody() {
        return whereBody;
    }

    public void setWhereBody(String whereBody) {
        this.whereBody = whereBody;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}

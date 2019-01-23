/*******************************************************************************
 * Copyright (c) Sep 30, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.jgit.core.service;

import org.iff.jgit.core.DataSourceFactory;
import org.iff.jgit.core.model.DataSourceModel;
import org.iff.jgit.core.model.QueryStatementModel;
import org.iff.jgit.core.service.SqlConditionProcessHelper.SqlAndParams;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.iff.infra.util.Exceptions;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.jdbc.dialet.Dialect;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 1. find
 * 2. pageFind
 * 3. get
 * 4. count
 * 5. update
 * 6. insert
 * 7. delete
 * </pre>
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 30, 2016
 */
@SuppressWarnings("unchecked")
public class QueryService {

    ManagementService managementService = new ManagementService();

    public Map<String, Object> get(String dataSourceName, String queryStatementName, Map<String, Object> params) {
        try {
            DataSourceModel dataSourceModel = managementService.getDataSourceModelByName(dataSourceName);
            QueryStatementModel queryStatementModel = managementService
                    .getQueryStatementModelByName(queryStatementName);

            QueryRunner queryRunner = new QueryRunner(DataSourceFactory.getOrCreate(dataSourceModel));
            String sql = "select " + queryStatementModel.getSelectBody() + " from " + queryStatementModel.getFromBody()
                    + " where " + queryStatementModel.getWhereBody();
            SqlAndParams sqlAndParams = SqlConditionProcessHelper.processCondition(sql, params);
            Map<String, Object> map = queryRunner.query(sqlAndParams.getSql(), new MapHandler(),
                    sqlAndParams.getParams().toArray());
            return map;
        } catch (Exception e) {
            Exceptions.runtime("get sql error!", e);
        }
        return new HashMap<String, Object>();
    }

    public List<Map<String, Object>> find(String dataSourceName, String queryStatementName,
                                          Map<String, Object> params) {
        try {
            DataSourceModel dataSourceModel = managementService.getDataSourceModelByName(dataSourceName);
            QueryStatementModel queryStatementModel = managementService
                    .getQueryStatementModelByName(queryStatementName);

            QueryRunner queryRunner = new QueryRunner(DataSourceFactory.getOrCreate(dataSourceModel));
            String sql = "select " + queryStatementModel.getSelectBody() + " from " + queryStatementModel.getFromBody()
                    + " where " + queryStatementModel.getWhereBody();
            SqlAndParams sqlAndParams = SqlConditionProcessHelper.processCondition(sql, params);
            List<Map<String, Object>> list = queryRunner.query(sqlAndParams.getSql(), new MapListHandler(),
                    sqlAndParams.getParams().toArray());
            return list;
        } catch (Exception e) {
            Exceptions.runtime("find sql error!", e);
        }
        return new ArrayList<Map<String, Object>>();
    }

    public Map<String, Object> page(String dataSourceName, String queryStatementName, Map<String, Object> params,
                                    int currentPage, int pageSize) {
        try {

            DataSourceModel dataSourceModel = managementService.getDataSourceModelByName(dataSourceName);
            QueryStatementModel queryStatementModel = managementService
                    .getQueryStatementModelByName(queryStatementName);

            DataSource dataSource = DataSourceFactory.getOrCreate(dataSourceModel);
            if (dataSource instanceof BasicDataSource) {
                QueryRunner queryRunner = new QueryRunner(dataSource);
                String sql = "select " + queryStatementModel.getSelectBody() + " from "
                        + queryStatementModel.getFromBody() + " where 1=1 " + queryStatementModel.getWhereBody();
                SqlAndParams sqlAndParams = SqlConditionProcessHelper.processCondition(sql, params);

                String countSql = "select count(*) from (" + sqlAndParams.getSql() + ") tmp_count";
                Number count = (Number) queryRunner.query(countSql, new ScalarHandler<Number>(1),
                        sqlAndParams.getParams().toArray());

                BasicDataSource bds = (BasicDataSource) dataSource;
                String pageSql = Dialect.getInstanceByUrl(bds.getUrl()).getLimitString(sqlAndParams.getSql(),
                        Math.max((currentPage - 1) * pageSize, 0), pageSize);
                List<Map<String, Object>> list = queryRunner.query(pageSql, new MapListHandler(),
                        sqlAndParams.getParams().toArray());

                return MapHelper.toMap("currentPage", currentPage, "pageSize", pageSize, "totalCount",
                        count.longValue(), "rows", list);
            }
        } catch (Exception e) {
            Exceptions.runtime("pageFind sql error!", e);
        }
        return MapHelper.toMap("currentPage", currentPage, "pageSize", pageSize, "totalCount", 0, "rows",
                new ArrayList<QueryStatementModel>());
    }

    public long count(String dataSourceName, String queryStatementName, Map<String, Object> params) {
        try {
            DataSourceModel dataSourceModel = managementService.getDataSourceModelByName(dataSourceName);
            QueryStatementModel queryStatementModel = managementService
                    .getQueryStatementModelByName(queryStatementName);

            QueryRunner queryRunner = new QueryRunner(DataSourceFactory.getOrCreate(dataSourceModel));
            String sql = "select " + queryStatementModel.getSelectBody() + " from " + queryStatementModel.getFromBody()
                    + " where " + queryStatementModel.getWhereBody();
            SqlAndParams sqlAndParams = SqlConditionProcessHelper.processCondition(sql, params);
            Number count = queryRunner.query(sqlAndParams.getSql(), new ScalarHandler<Number>(1),
                    sqlAndParams.getParams().toArray());
            return count.longValue();
        } catch (Exception e) {
            Exceptions.runtime("count sql error!", e);
        }
        return 0;
    }
}

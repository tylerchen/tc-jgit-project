/*******************************************************************************
 * Copyright (c) Sep 26, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.jgit.core;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.iff.jgit.core.model.DataSourceModel;
import org.iff.infra.util.*;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.iff.jgit.util.SystemHelper.NAMESPACE_SYS;
import static org.iff.jgit.util.SystemHelper.getProps;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 26, 2016
 */
public class DataSourceFactory {
    private static Map<String, DataSource> dataSourceCacheMap = new HashMap<String, DataSource>();

    static {
        ShutdownHookHelper.register("DataSourceFactory.dataSourceCacheMap", new Closeable() {
            public void close() throws IOException {
                for (Map.Entry<String, DataSource> entry : dataSourceCacheMap.entrySet()) {
                    Logger.info("DataSourceFactory shutdown datasource: " + entry.getKey());
                    try {
                        ((BasicDataSource) entry.getValue()).close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static DataSource get(String name) {
        return dataSourceCacheMap.get(name);
    }

    public static DataSource getSystemDS() {
        DataSource dataSource = dataSourceCacheMap.get(NAMESPACE_SYS);
        if (dataSource == null) {
            dataSource = create(NAMESPACE_SYS,
                    getProps().getProperty("db.user"),
                    getProps().getProperty("db.password"),
                    getProps().getProperty("db.url"),
                    getProps().getProperty("db.driver"),
                    getProps().getProperty("db.validationQuery"),
                    NumberHelper.getInt(getProps().getProperty("db.initConnection"), 3),
                    NumberHelper.getInt(getProps().getProperty("db.maxConnection"), 10));
        }
        return dataSource;
    }

    public static DataSource create(String name, String user, String password, String url, String driver,
                                    String validationQuery, int initConnection, int maxConnection) {
        Assert.notBlank(name);
        Assert.notBlank(user);
        Assert.notBlank(url);
        Assert.notBlank(driver);
        Assert.notBlank(validationQuery);
        Properties props = new Properties();
        {
            Map map = getDataSourceConfig(user, password, url, driver, validationQuery, initConnection, maxConnection);
            props.putAll(map);
        }
        try {
            DataSource dataSource = org.apache.commons.dbcp.BasicDataSourceFactory.createDataSource(props);
            {
                DataSource ds = dataSourceCacheMap.get(name);
                if (ds != null && ds instanceof BasicDataSource) {
                    ((BasicDataSource) ds).close();
                }
            }
            dataSourceCacheMap.put(name, dataSource);
            return dataSource;
        } catch (Exception e) {
            Exceptions.runtime("Error invoke BasicDataSourceFactory.createDataSource", e);
        }
        return null;
    }

    public static Map<String, String> getDataSourceConfig(String user, String password, String url, String driver, String validationQuery, int initConnection, int maxConnection) {
        return MapHelper.toMap(/**/
                "username", user, /**/
                "password", password, /**/
                "url", url, /**/
                "driverClassName", driver, /**/
                "defaultAutoCommit", "false", /**/
                "initialSize", String.valueOf(Math.max(initConnection, 3)), /**/
                "maxActive", String.valueOf(Math.max(maxConnection, 3)), /**/
                "maxWait", "60000", /**/
                "validationQuery", validationQuery, /**/
                "testWhileIdle", "true", /**/
                "testOnBorrow", "false", /**/
                "testOnReturn", "false" /**/
        );
    }

    public static DataSource create(DataSourceModel model) {
        Assert.notNull(model);
        Assert.notBlank(model.getName());
        Assert.notBlank(model.getUser());
        Assert.notBlank(model.getUrl());
        Assert.notBlank(model.getDriver());
        Assert.notBlank(model.getValidationQuery());
        Properties props = new Properties();
        {
            Map map = getDataSourceConfig(model.getUser(), model.getPassword(), model.getUrl(), model.getDriver(), model.getValidationQuery(), model.getInitConnection(), model.getMaxConnection());
            props.putAll(map);
        }
        try {
            DataSource dataSource = org.apache.commons.dbcp.BasicDataSourceFactory.createDataSource(props);
            {
                DataSource ds = dataSourceCacheMap.get(model.getName());
                if (ds != null && ds instanceof BasicDataSource) {
                    ((BasicDataSource) ds).close();
                }
            }
            dataSourceCacheMap.put(model.getName(), dataSource);
            return dataSource;
        } catch (Exception e) {
            e.printStackTrace();
            Exceptions.runtime("Error invoke BasicDataSourceFactory.createDataSource", e);
        }
        return null;
    }

    public static DataSource create(String name) {
        Assert.notBlank(name);
        if ("IFF_REST_SYSTEM".equals(name)) {
            return getSystemDS();
        }
        DomainEventService.Tuple params = DomainEventService.Tuple.queryOne("IFF_REST_SYSTEM", "DataSource", "getByNamespace", name);
        EventBusHelper.me().syncEvent(DomainEventService.EVENT_NAME, params);

        if (params.hasError()) {
            Exceptions.runtime("DataSourceFactory cannot load datasource by name: " + name, (Throwable) params.result());
        }
        //jdbc:mysql://localhost:3306/database?useUnicode=true&characterEncoding=utf8[username=&password=&...]
        DataSourceModel dsm = new DataSourceModel().init("IFF_REST_SYSTEM", "DataSource").rePutAll((Map<String, Object>) params.result());

        Properties props = new Properties();
        {
            String jdbcUrl = StringUtils.substringBeforeLast(dsm.getUrl(), "[");
            String paramStr = StringUtils.substringBeforeLast(StringUtils.substringAfterLast(dsm.getUrl(), "["), "]");
            String[] pairs = StringUtils.split(paramStr, '&');
            for (String pair : pairs) {
                String[] keyValue = StringUtils.split(pair, '=');
                if (keyValue.length != 2) {
                    Logger.warn("DataSourceFactory datasource created by illegal parameters! Datasource " + name + " parameters contains illegal format: " + paramStr);
                }
                props.put(keyValue[0], keyValue[1]);
            }
        }
        try {
            DataSource dataSource = org.apache.commons.dbcp.BasicDataSourceFactory.createDataSource(props);
            {
                DataSource ds = dataSourceCacheMap.get(name);
                if (ds != null && ds instanceof BasicDataSource) {
                    ((BasicDataSource) ds).close();
                }
            }
            dataSourceCacheMap.put(name, dataSource);
            return dataSource;
        } catch (Exception e) {
            e.printStackTrace();
            Exceptions.runtime("Error invoke BasicDataSourceFactory.createDataSource", e);
        }
        return null;
    }

    public static DataSource getOrCreate(DataSourceModel model) {
        DataSource dataSource = get(model.getName());
        if (dataSource == null) {
            dataSource = create(model);
        }
        return dataSource;
    }

    public static DataSource getOrCreate(String name) {
        DataSource dataSource = get(name);
        if (dataSource == null) {
            dataSource = create(name);
        }
        return dataSource;
    }
}

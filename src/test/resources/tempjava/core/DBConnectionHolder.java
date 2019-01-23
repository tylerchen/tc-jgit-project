/*******************************************************************************
 * Copyright (c) Oct 16, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.jgit.core;

import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Oct 16, 2016
 */
public class DBConnectionHolder {
    private static final ThreadLocal<Map<DataSource, Connection>> local = new ThreadLocal<Map<DataSource, Connection>>();

    public static Connection getConnection(DataSource ds) {
        Connection connection = null;
        try {
            Map<DataSource, Connection> map = local.get();
            if (map != null) {
                if (map.size() > 0) {
                    return map.get(0);
                }
            } else {
                map = new HashMap<DataSource, Connection>();
            }
            connection = ds.getConnection();
            connection.setAutoCommit(false);
            map.put(ds, connection);
            local.set(map);
        } catch (Exception e) {
            DbUtils.closeQuietly(connection);
            e.printStackTrace();
        }
        return connection;
    }

    public static void commitAndCloseQuietly() {
        Map<DataSource, Connection> map = local.get();
        if (map != null) {
            for (Connection conn : map.values()) {
                DbUtils.commitAndCloseQuietly(conn);
            }
            map.clear();
        }
    }

    public static void rollbackAndCloseQuietly() {
        Map<DataSource, Connection> map = local.get();
        if (map != null) {
            for (Connection conn : map.values()) {
                DbUtils.rollbackAndCloseQuietly(conn);
            }
            map.clear();
        }
    }
}

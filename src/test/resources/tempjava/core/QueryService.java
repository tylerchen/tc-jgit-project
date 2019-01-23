/*******************************************************************************
 * Copyright (c) Sep 26, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.jgit.core;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 26, 2016
 */
public class QueryService {

    public static void main(String[] args) {
        new QueryService().table();
    }

    public void test() {
        DataSource dataSource = DataSourceFactory.create("test", "iff", "iff",
                "jdbc:mysql://localhost:3306/new_qdp_0201?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false",
                "com.mysql.jdbc.Driver", "select 1", 3, 10);
        try {
            QueryRunner queryRunner = new QueryRunner(dataSource);
            System.out.println("处理多行记录！");
            List<Map<String, Object>> list = queryRunner.query("select * from sys_openreport", new MapListHandler(),
                    (Object[]) null);

            for (Iterator<Map<String, Object>> li = list.iterator(); li.hasNext(); ) {
                System.out.println("--------------");
                Map<String, Object> m = li.next();
                for (Iterator<Entry<String, Object>> mi = m.entrySet().iterator(); mi.hasNext(); ) {
                    Entry<String, Object> e = mi.next();
                    System.out.println(e.getKey() + "=" + e.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void table() {
        DataSource ds = DataSourceFactory.create("test", "iff", "iff",
                "jdbc:mysql://localhost:3306/new_qdp_0201?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false",
                "com.mysql.jdbc.Driver", "select 1", 3, 10);
        Map<String, String> table2Desc = new LinkedHashMap<String, String>();
        Connection conn = null;
        try {
            String catalog = "";
            String schema = "";
            conn = ds.getConnection();
            {
                DatabaseMetaData dbmd = conn.getMetaData();
                ResultSet rs = dbmd.getTables(catalog, schema, null, new String[]{"TABLE", "VIEW"});
                while (rs.next()) {
                    //String catalog = rs.getString("TYPE_CAT");
                    //String schema = rs.getString("TYPE_SCHEM");
                    String name = rs.getString("TABLE_NAME");
                    String type = rs.getString("TABLE_TYPE");
                    String remarks = rs.getString("REMARKS");
                    System.out.println(rs.getMetaData());
                    table2Desc.put(name, remarks);
                }
                rs.close();
            }
            {
                DatabaseMetaData dbmd = conn.getMetaData();
                for (Entry<String, String> entry : table2Desc.entrySet()) {
                    ResultSet rs = dbmd.getPrimaryKeys(catalog, schema, entry.getKey());
                    while (rs.next()) {
                        String idName = rs.getString("COLUMN_NAME");
                        System.out.println("table=" + entry.getKey() + ", pk=" + idName);
                    }
                    rs.close();

                    rs = dbmd.getColumns(catalog, schema, entry.getKey(), "%");
                    while (rs.next()) {
                        String colName = rs.getString("COLUMN_NAME");
                        Integer sqlType = rs.getInt("DATA_TYPE");
                        Integer size = rs.getInt("COLUMN_SIZE");
                        Object o = rs.getObject("DECIMAL_DIGITS");
                        Integer digit = null;
                        if (o != null) {
                            digit = ((Number) o).intValue();
                        }
                        int nullable = rs.getInt("NULLABLE");
                        String defaultValue = rs.getString("COLUMN_DEF");
                        String isAutoIncrement = rs.getString("IS_AUTOINCREMENT");
                        String remark = rs.getString("REMARKS");
                        System.out.println("table=" + entry.getKey() + ", COL=" + colName + ",Type=" + sqlType
                                + ",size=" + size + ",digit=" + digit + ",nullable=" + (nullable == 0 ? "N" : "Y")
                                + ",defaultValue=" + defaultValue + ",isAutoIncrement=" + isAutoIncrement + ",desc="
                                + remark);
                    }
                    rs.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
    }
}

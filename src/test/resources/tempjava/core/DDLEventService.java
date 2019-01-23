/*******************************************************************************
 * Copyright (c) 2018-10-29 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.lang3.StringUtils;
import org.iff.jgit.core.model.DescColumnModel;
import org.iff.jgit.core.model.DescTableModel;
import org.iff.jgit.util.SqlTypeMappingHelper;
import org.iff.jgit.util.FieldNameHelper;
import org.iff.jgit.util.SystemHelper;
import org.iff.infra.util.*;
import org.iff.infra.util.validation.ValidationMethods;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * DDLEventService
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-10-29
 * auto generate by qdp.
 */
public class DDLEventService implements EventBusHelper.EventProcess {

    public static final String EVENT_NAME = DDLEventService.class.getSimpleName();
    public static final String ACTION_findAllCatalog = "findAllCatalog";
    public static final String ACTION_findAllSchema = "findAllSchema";
    public static final String ACTION_findAllTableDesc = "findAllTableDesc";
    public static final String ACTION_getTableDesc = "getTableDesc";
    public static final String ACTION_createTable = "createTable";
    public static final String ACTION_dropTable = "dropTable";
    public static final String ACTION_createDatabase = "createDatabase";

    public String getName() {
        return EVENT_NAME;
    }

    public void listen(String name, Object args) {
        Tuple tuple = (Tuple) args;
        String action = tuple.action();
        Object result = null;
        try {
            if (action.equals(ACTION_findAllCatalog)) {
                result = findAllCatalog(tuple.namespace());
            } else if (action.equals(ACTION_findAllSchema)) {
                result = findAllSchema(tuple.namespace());
            } else if (action.equals(ACTION_findAllTableDesc)) {
                result = findAllTableDesc(tuple.namespace(), tuple.catalog(), tuple.schema());
            } else if (action.equals(ACTION_getTableDesc)) {
                result = getTableDesc(tuple.namespace(), tuple.catalog(), tuple.schema(), tuple.table());
            } else if (action.equals(ACTION_createTable)) {
                result = createTable(tuple.namespace(), tuple.createSql());
            } else if (action.equals(ACTION_dropTable)) {
                result = dropTable(tuple.namespace(), tuple.table());
            } else if (action.equals(ACTION_createDatabase)) {
                result = createDatabase(tuple.namespace(), tuple.database());
            } else {
                Exceptions.runtime("DDLEventService no event: " + action + " found!");
            }
            tuple.result(result);
        } catch (Exception e) {
            tuple.result(e);
        }
    }

    public boolean createTable(String namespace, String createSql) {
        boolean result = false;
        Connection conn = null;
        try {
            conn = dataSource(namespace).getConnection();
            QueryRunner queryRunner = new QueryRunner();
            String sql = createSql;
            result = queryRunner.update(conn, sql) > 0;
        } catch (Exception e) {
            Exceptions.runtime("DDLEventService createTable error!", e);
        } finally {
            SocketHelper.closeWithoutError(conn);
        }
        return result;
    }

    public boolean dropTable(String namespace, String tableName) {
        boolean result = false;
        Connection conn = null;
        try {
            PreRequiredHelper.requireTrue(ValidationMethods.tableName(tableName), "DDLEventService dropTable tableName: " + tableName + ", is invalid!");
            conn = dataSource(namespace).getConnection();
            QueryRunner queryRunner = new QueryRunner();
            String sql = "drop table " + tableName;
            result = queryRunner.update(conn, sql) > 0;
        } catch (Exception e) {
            Exceptions.runtime("DDLEventService dropTable error!", e);
        } finally {
            SocketHelper.closeWithoutError(conn);
        }
        return result;
    }

    public boolean createDatabase(String namespace, String database) {
        boolean result = false;
        Connection conn = null;
        try {
            PreRequiredHelper.requireTrue(ValidationMethods.tableName(database), "DDLEventService createDatabase database: " + database + ", is invalid!");
            conn = dataSource(namespace).getConnection();
            QueryRunner queryRunner = new QueryRunner();
            String sql = "create database " + database + " default charset utf8";
            result = queryRunner.update(conn, sql) > 0;
        } catch (Exception e) {
            Exceptions.runtime("DDLEventService createDatabase error!", e);
        } finally {
            SocketHelper.closeWithoutError(conn);
        }
        return result;
    }

    public List<String> findAllCatalog(String namespace) {
        String cacheKey = StringUtils.join(
                new String[]{getClass().getSimpleName(), "findAllCatalog", namespace},
                '/');
        {
            List<String> list = CacheHelper.getCache().get(cacheKey);
            if (list != null) {
                return list;
            }
        }
        List<String> list = new ArrayList<String>();
        Connection conn = null;
        try {
            conn = dataSource(namespace).getConnection();
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet rs = dbmd.getCatalogs();
            while (rs.next()) {
                list.add(rs.getString("TABLE_CAT"));
            }
            rs.close();
        } catch (Exception e) {
            Exceptions.runtime("DDLEventService findAllCatalog error!", e);
        } finally {
            SocketHelper.closeWithoutError(conn);
        }
        {
            CacheHelper.set(cacheKey, list);
        }
        return list;
    }

    public List<String> findAllSchema(String namespace) {
        String cacheKey = StringUtils.join(
                new String[]{getClass().getSimpleName(), "findAllSchema", namespace},
                '/');
        {
            List<String> list = CacheHelper.getCache().get(cacheKey);
            if (list != null) {
                return list;
            }
        }
        List<String> list = new ArrayList<String>();
        Connection conn = null;
        try {
            conn = dataSource(namespace).getConnection();
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet rs = dbmd.getSchemas();
            while (rs.next()) {
                list.add(rs.getString("TYPE_SCHEM"));
            }
            rs.close();
        } catch (Exception e) {
            Exceptions.runtime("DDLEventService findAllCatalog error!", e);
        } finally {
            SocketHelper.closeWithoutError(conn);
        }
        {
            CacheHelper.set(cacheKey, list);
        }
        return list;
    }

    public DescTableModel getTableDesc(String namespace, String catalog, String schema, String table) {
        Map<String, DescTableModel> map = findAllTableDesc(namespace, catalog, schema);
        return map.get(FieldNameHelper.classToTable(table));
    }

    public Map<String, DescTableModel> findAllTableDesc(String namespace, String catalog, String schema) {
        String cacheKey = SystemHelper.cacheKey(getClass().getSimpleName(), "findAllTableDesc", namespace, catalog, schema);
        {
            Map<String, DescTableModel> table2Desc = CacheHelper.getCache().get(cacheKey);
            if (table2Desc != null) {
                return table2Desc;
            }
        }

        Map<String, DescTableModel> table2Desc = new LinkedHashMap<String, DescTableModel>();
        Connection conn = null;
        DatabaseMetaData dbmd = null;
        try {
            conn = dataSource(namespace).getConnection();
            dbmd = conn.getMetaData();
        } catch (Throwable e) {
            SocketHelper.closeWithoutError(conn);
            Exceptions.runtime("DDLEventService findAllTableDesc error!", e);
        }
        try {
            descTables(catalog, schema, table2Desc, dbmd);
        } catch (Throwable e) {
            SocketHelper.closeWithoutError(conn);
            Exceptions.runtime("DDLEventService findAllTableDesc error!", e);
        }
        try {
            for (Map.Entry<String, DescTableModel> entry : table2Desc.entrySet()) {
                String tableName = entry.getKey();
                DescTableModel table = entry.getValue();
                descTable(catalog, schema, dbmd, tableName, table);
            }
        } finally {
            SocketHelper.closeWithoutError(conn);
        }
        {
            CacheHelper.set(cacheKey, table2Desc);
        }
        return table2Desc;
    }

    protected void descTables(String catalog, String schema, Map<String, DescTableModel> table2Desc, DatabaseMetaData dbmd) throws SQLException {
        //获得“表”及“视图”
        /*1.TABLE_CAT        (String)   => 表所在的编目(可能为空)*/
        /*2.TABLE_SCHEM (String)   => 表所在的模式(可能为空) */
        /*3.TABLE_NAME    (String)   => 表的名称*/
        /*4.TABLE_TYPE     (String)    => 表的类型。*/
        /*5.REMARKS          (String)       => 解释性的备注*/
        /*6.TYPE_CAT          (String)      =>编目类型(may be null) */
        /*7.TYPE_SCHEM   (String)      => 模式类型(may be null) */
        /*8.TYPE_NAME      (String)      => 类型名称(may be null) */
        /*9.SELF_REFERENCING_COL_NAME    (String) => name of the designated "identifier" column of a typed table (may be null) */
        /*10.REF_GENERATION   (String)    => specifies how values in SELF_REFERENCING_COL_NAME are created.它的值有："SYSTEM"、"USER"、"DERIVED"，也可能为空。*/
        ResultSet rs = dbmd.getTables(catalog, schema, null, new String[]{"TABLE", "VIEW"});
        while (rs.next()) {
            String cat = rs.getString("TYPE_CAT");
            String schem = rs.getString("TYPE_SCHEM");
            String name = rs.getString("TABLE_NAME").toUpperCase();
            String type = rs.getString("TABLE_TYPE");
            String remarks = rs.getString("REMARKS");
            table2Desc.put(name, DescTableModel.create(StringUtils.defaultString(cat), StringUtils.defaultString(schem), name, type, remarks));
        }
        rs.close();
    }

    protected void descTable(String catalog, String schema, DatabaseMetaData dbmd, String tableName, DescTableModel table) {
        Map<String, String> pkMap = new HashMap<String, String>();
        Map<String, String[]> fkMap = new HashMap<String, String[]>();
        try {
            DescColumnModel columnModel = descColumn(catalog, schema, dbmd, tableName, table);
            descPrimaryKey(catalog, schema, dbmd, table, pkMap, columnModel);
            descForeignKey(catalog, schema, dbmd, table, fkMap, columnModel);
        } catch (Exception e) {
            Exceptions.runtime("DDLEventService findAllTableDesc error!", e);
        }
    }

    protected DescColumnModel descColumn(String catalog, String schema, DatabaseMetaData dbmd, String tableName, DescTableModel table) throws SQLException {
        //获得“列”信息
        /*1. TABLE_CAT String => table catalog (may be null), 表类别（可为 null）*/
        /*2. TABLE_SCHEM String => table schema (may be null), 表模式（可为 null）*/
        /*3. TABLE_NAME String =>table name, 表名称*/
        /*4. COLUMN_NAME String =>column name, 列名称*/
        /*5. DATA_TYPE int =>SQL type from java.sql.Types, java.sql.Types 的 SQL 类型*/
        /*6. TYPE_NAME String =>Data source dependent type name,for a UDT the type name is fully qualified, 数据源依赖的类型名称，对于 UDT，该类型名称是完全限定的*/
        /*7. COLUMN_SIZE int =>column size, 列的大小*/
        /*8. BUFFER_LENGTH is not used, 未被使用*/
        /*9. DECIMAL_DIGITS int =>the number of fractional digits.Null is returned for data types where DECIMAL_DIGITS is not applicable, 小数部分的位数。对于 DECIMAL_DIGITS 不适用的数据类型，则返回 Null*/
        /*10. NUM_PREC_RADIX int =>Radix(typically either 10or 2), 基数（通常为 10 或 2）*/
        /*11. NULLABLE int =>is NULL allowed, 是否允许使用 NULL*/
        /*12. REMARKS String =>comment describing column(may be null), 描述列的注释（可为 null）*/
        /*13. COLUMN_DEF String =>default value for the column, (may be null), 该列的默认值，当值在单引号内时应被解释为一个字符串（可为 null）*/
        /*14. SQL_DATA_TYPE int =>unused, 未使用*/
        /*15. SQL_DATETIME_SUB int =>unused, 未使用*/
        /*16. CHAR_OCTET_LENGTH int =>for char types the maximum number of bytes in the column, 对于 char 类型，该长度是列中的最大字节数*/
        /*17. ORDINAL_POSITION int =>index of column in table(starting at 1), 表中的列的索引（从 1 开始）*/
        /*18. IS_NULLABLE String =>ISO rules are used to determine the nullability for a column, ISO 规则用于确定列是否包括 null, YES/NO/Empty*/
        /*19. SCOPE_CATLOG String =>catalog of table that is the scope of a reference attribute(null if DATA_TYPE isn 't REF), 表的类别，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为 null）*/
        /*20. SCOPE_SCHEMA String =>schema of table that is the scope of a reference attribute(null if the DATA_TYPE isn 't REF), 表的模式，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为 null）*/
        /*21. SCOPE_TABLE String =>table name that this the scope of a reference attribure (null if the DATA_TYPE isn 't REF), 表名称，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为 null）*/
        /*22. SOURCE_DATA_TYPE short =>source type of a distinct type or user -generated Ref type, SQL type from java.sql.Types, 不同类型或用户生成 Ref 类型、来自 java.sql.Types 的 SQL 类型的源类型（如果 DATA_TYPE 不是 DISTINCT 或用户生成的 REF，则为 null）*/
        /*23. IS_AUTOINCREMENT String =>Indicates whether this column is auto incremented, 指示此列是否自动增加, YES/NO/Empty*/
        DescColumnModel columnModel = null;
        ResultSet rs = dbmd.getColumns(catalog, schema, tableName, "%");
        while (rs.next()) {
            String colName = rs.getString("COLUMN_NAME").toUpperCase();
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
            colName = StringUtils.upperCase(colName);
            columnModel = DescColumnModel.create(colName, SqlTypeMappingHelper.getSqlType(sqlType), SqlTypeMappingHelper.getMybatisJavaType(sqlType), size,
                    digit, defaultValue, remark,
                    StringUtils.equalsIgnoreCase(isAutoIncrement, "NO") ? "N" : "Y",
                    nullable == 0 ? "N" : "Y");
            table.add(columnModel);
        }
        rs.close();
        return columnModel;
    }

    protected void descPrimaryKey(String catalog, String schema, DatabaseMetaData dbmd, DescTableModel table, Map<String, String> pkMap, DescColumnModel columnModel) throws SQLException {
        //获得“主键”信息
        /*1.TABLE_CAT String => tableName catalog (may be null), 表类别（可为 null）*/
        /*2.TABLE_SCHEM String => tableName schema (may be null), 表模式（可为 null）*/
        /*3.TABLE_NAME String => tableName name, 表名称*/
        /*4.COLUMN_NAME String => column name, 列名称*/
        /*5.KEY_SEQ short => sequence number within primary key, 主键中的序列号（值 1 表示主键中的第一列，值 2 表示主键中的第二列）。*/
        /*6.PK_NAME String => primary key name (may be null), 主键的名称（可为 null）*/
        if (pkMap.isEmpty()) {
            ResultSet rs = dbmd.getPrimaryKeys(catalog, schema, table.getName());
            while (rs.next()) {
                String idName = rs.getString("COLUMN_NAME");
                pkMap.put(StringUtils.upperCase(idName), idName);
            }
            rs.close();
        }
        if (pkMap.containsKey(columnModel.getName())) {
            columnModel.setIsPrimaryKey("Y");
            table.getPks().put(columnModel.getName(), columnModel);
        }
    }

    protected void descForeignKey(String catalog, String schema, DatabaseMetaData dbmd, DescTableModel table, Map<String, String[]> fkMap, DescColumnModel columnModel) throws SQLException {
        //获得“外键”信息
        /*1.PKTABLE_CAT String => primary key tableName catalog (may be null), 被导入的主键表类别（可为 null）*/
        /*2.PKTABLE_SCHEM String => primary key tableName schema (may be null), 被导入的主键表模式（可为 null）*/
        /*3.PKTABLE_NAME String => primary key tableName name, 被导入的主键表名称*/
        /*4.PKCOLUMN_NAME String => primary key column name, 被导入的主键列名称*/
        /*5.FKTABLE_CAT String => foreign key tableName catalog (may be null) being exported (may be null), 外键表类别（可为 null）*/
        /*6.FKTABLE_SCHEM String => foreign key tableName schema (may be null) being exported (may be null), 外键表模式（可为 null）*/
        /*7.FKTABLE_NAME String => foreign key tableName name being exported, 外键表名称*/
        /*8.FKCOLUMN_NAME String => foreign key column name being exported, 外键列名称*/
        /*9.KEY_SEQ short => sequence number within foreign key, 外键中的序列号（值 1 表示外键中的第一列，值 2 表示外键中的第二列）*/
        /*10.UPDATE_RULE short => What happens to foreign key when primary is updated:, 更新主键时外键发生的变化*/
        /*11.DELETE_RULE short => What happens to the foreign key when primary is deleted., 删除主键时外键发生的变化*/
        /*importedKeyNoAction (3) importedKeyCascade (0) importedKeySetNull (2) importedKeySetDefault (4) importedKeyRestrict (1)*/
        /*PK_NAME String => 主键的名称（可为 null）*/
        /*FK_NAME String => 外键的名称（可为 null）*/
        /*DEFERRABILITY short => 是否可以将对外键约束的评估延迟到提交时间*/
        //https://docs.microsoft.com/zh-cn/sql/connect/jdbc/reference/getimportedkeys-method-sqlserverdatabasemetadata?view=sql-server-2017
        if (fkMap.isEmpty()) {
            ResultSet rs = dbmd.getImportedKeys(catalog, schema, table.getName());
            while (rs.next()) {
                String fkColumnName = rs.getString("FKCOLUMN_NAME");
                String pkColumnName = rs.getString("PKCOLUMN_NAME");
                String pkTableCat = rs.getString("PKTABLE_CAT");
                String pkTableSchem = rs.getString("PKTABLE_SCHEM");
                String pkTableName = rs.getString("PKTABLE_NAME");
                short deleteRule = rs.getShort("DELETE_RULE");
                fkMap.put(StringUtils.upperCase(fkColumnName), new String[]{pkTableCat, pkTableSchem, pkTableName, pkColumnName, deleteRule == DatabaseMetaData.importedKeyCascade ? "Y" : "N"});
                if ("DOMAIN_QUERY_ID".equals(pkColumnName) || "DOMAIN_QUERY_ID".equals(fkColumnName)) {
                    System.out.println("DOMAIN_QUERY_ID");
                }
            }
            rs.close();
        }
        if (fkMap.containsKey(columnModel.getName())) {
            String[] strings = fkMap.get(columnModel.getName());
            columnModel.setFkCatalog(strings[0]);
            columnModel.setFkSchema(strings[1]);
            columnModel.setFkTable(strings[2]);
            columnModel.setFkColumn(strings[3]);
            columnModel.setIsCascadeDelete(strings[4]);
            table.getFks().put(columnModel.getName(), columnModel);
        }
    }

    public DataSource dataSource(String namespace) {
        return MyBatisSqlSessionFactory.getDataSource(namespace);
    }

    public static class Tuple<T> extends org.iff.jgit.util.Tuple.Seven<
            /*0:result*/T,
            /*1:action*/String,
            /*2:namespace*/String,
            /*3:catalog*/String,
            /*4:schema*/String,
            /*5:table*/String,
            /*6:createSql*/String,
            /*7:database*/String> {

        public Tuple(String action, String namespace, String catalog, String schema, String table, String createSql, String database) {
            super(action, namespace, catalog, schema, table, createSql, database);
        }

        public static Tuple<List<String>> findAllCatalog(String namespace) {
            return new Tuple<>(ACTION_findAllCatalog, namespace, null, null, null, null, null);
        }

        public static Tuple<List<String>> findAllSchema(String namespace) {
            return new Tuple<>(ACTION_findAllSchema, namespace, null, null, null, null, null);
        }

        public static Tuple<Map<String, DescTableModel>> findAllTableDesc(String namespace, String catalog, String schema) {
            return new Tuple<>(ACTION_findAllTableDesc, namespace, catalog, schema, null, null, null);
        }

        public static Tuple<DescTableModel> getTableDesc(String namespace, String catalog, String schema, String table) {
            return new Tuple<>(ACTION_getTableDesc, namespace, catalog, schema, table, null, null);
        }

        public static Tuple<Boolean> createTable(String namespace, String createSql) {
            return new Tuple<>(ACTION_createTable, namespace, null, null, null, createSql, null);
        }

        public static Tuple<Boolean> dropTable(String namespace, String table) {
            return new Tuple<>(ACTION_dropTable, namespace, null, null, table, null, null);
        }

        public static Tuple<Boolean> createDatabase(String namespace, String database) {
            return new Tuple<>(ACTION_createDatabase, namespace, null, null, null, null, database);
        }

        public String action() {
            return first();
        }

        public String namespace() {
            return second();
        }

        public String catalog() {
            return third();
        }

        public String schema() {
            return fourth();
        }

        public String table() {
            return fifth();
        }

        public String createSql() {
            return sixth();
        }

        public String database() {
            return seventh();
        }
    }
}

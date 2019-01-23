/*******************************************************************************
 * Copyright (c) Sep 30, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.jgit.core.service;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;
import org.iff.jgit.core.DBConnectionHolder;
import org.iff.jgit.core.DataSourceFactory;
import org.iff.jgit.core.model.DataSourceModel;
import org.iff.jgit.core.model.DescColumnModel;
import org.iff.jgit.core.model.DescTableModel;
import org.iff.jgit.core.model.QueryStatementModel;
import org.iff.jgit.util.SqlTypeMappingHelper;
import org.iff.infra.util.*;
import org.iff.infra.util.jdbc.dialet.Dialect;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.*;
import java.util.Map.Entry;

/**
 * <pre>
 * 1. get table description.
 * 2. getQuery
 * 3. findQuery
 * 4. pageFindQuery
 * 5. updateQuery
 * 6. deleteQuery
 * 7. getDataSource
 * 8. findDataSource
 * 9. pageFindDataSource
 * 10. updateDataSource
 * 11. deleteDataSource
 * 12. testDataSource
 * </pre>
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 30, 2016
 */
@SuppressWarnings("unchecked")
public class ManagementService {

    public static void main(String[] args) {
        Map<String, DescTableModel> allTableDesc = new ManagementService().findAllTableDesc(DataSourceFactory.getSystemDS());
    }

    public DescTableModel getTableDescByDataSourceId(String datasourceId, String table) {
        DataSourceModel dataSourceModel = getDataSourceModelById(datasourceId);
        DataSource ds = DataSourceFactory.getOrCreate(dataSourceModel);
        return getTableDesc(ds, table);
    }

    public DescTableModel getTableDescByDataSourceName(String datasourceName, String table) {
        DataSourceModel dataSourceModel = getDataSourceModelByName(datasourceName);
        DataSource ds = DataSourceFactory.getOrCreate(dataSourceModel);
        return getTableDesc(ds, table);
    }

    protected DescTableModel getTableDesc(DataSource ds, String table) {
        //		DataSource ds = DataSourceFactory.create("test", "iff", "iff",
        //				"jdbc:mysql://localhost:3306/new_qdp_0201?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false",
        //				"com.mysql.jdbc.Driver", "select 1", 3, 10);
        Map<String, DescTableModel> table2Desc = new LinkedHashMap<String, DescTableModel>();
        Connection conn = null;
        //        供应商	Catalog支持	Schema支持
        //        Oracle	不支持	Oracle User ID
        //        MySQL	不支持	数据库名
        //        MS SQL Server	数据库名	对象属主名，2005版开始有变
        //        DB2	指定数据库对象时，Catalog部分省略	Catalog属主名
        //        Sybase	数据库名	数据库属主名
        //        Informix	不支持	不需要
        //        PointBase	不支持	数据库名
        try {
            String catalog = "";
            String schema = "";
            conn = ds.getConnection();
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
            DatabaseMetaData dbmd = conn.getMetaData();
            {//获得“Catalog”
                ResultSet rs = dbmd.getCatalogs();
                while (rs.next()) {
                    String temp = rs.getString("TABLE_CAT");
                    System.out.println("Catalog: " + temp);
                }
                rs.close();
            }
            {//获得“Schema”
                ResultSet rs = dbmd.getSchemas();
                while (rs.next()) {
                    String temp = rs.getString("TYPE_SCHEM");
                    System.out.println("Schema: " + temp);
                }
                rs.close();
            }
            {//获得“表”及“视图”
                ResultSet rs = dbmd.getTables(catalog, schema, "%", new String[]{"TABLE", "VIEW"});
                while (rs.next()) {
                    catalog = rs.getString("TYPE_CAT");
                    schema = rs.getString("TYPE_SCHEM");
                    String name = rs.getString("TABLE_NAME");
                    String type = rs.getString("TABLE_TYPE");
                    String remarks = rs.getString("REMARKS");
                    if (StringUtils.equalsIgnoreCase(name, table)) {
                        table2Desc.put(table, DescTableModel.create(catalog, schema, name, type, remarks));
                    }
                }
                rs.close();
            }
            /** getColumns **/
            /*1. TABLE_CAT String => table catalog (may be null)*/
            /*2. TABLE_SCHEM String => table schema (may be null)*/
            /*3. TABLE_NAME String =>table name (表名称)*/
            /*4. COLUMN_NAME String =>column name (列名)*/
            /*5. DATA_TYPE int =>SQL type from java.sql.Types(列的数据类型)*/
            /*6. TYPE_NAME String =>Data source dependent type name,for a UDT the type name is fully qualified*/
            /*7. COLUMN_SIZE int =>column size.*/
            /*8. BUFFER_LENGTH is not used.*/
            /*9. DECIMAL_DIGITS int =>the number of fractional digits.Null is returned for data types where DECIMAL_DIGITS is not applicable.*/
            /*10. NUM_PREC_RADIX int =>Radix(typically either 10or 2)*/
            /*11. NULLABLE int =>is NULL allowed.*/
            /*12. REMARKS String =>comment describing column(may be null)*/
            /*13. COLUMN_DEF String =>default value for the column, (may be null)*/
            /*14. SQL_DATA_TYPE int =>unused*/
            /*15. SQL_DATETIME_SUB int =>unused*/
            /*16. CHAR_OCTET_LENGTH int =>for char types the maximum number of bytes in the column*/
            /*17. ORDINAL_POSITION int =>index of column in table(starting at 1)*/
            /*18. IS_NULLABLE String =>ISO rules are used to determine the nullability for a column.*/
            /*19. SCOPE_CATLOG String =>catalog of table that is the scope of a reference attribute(null if DATA_TYPE isn 't REF)*/
            /*20. SCOPE_SCHEMA String =>schema of table that is the scope of a reference attribute(null if the DATA_TYPE isn 't REF)*/
            /*21. SCOPE_TABLE String =>table name that this the scope of a reference attribure (null if the DATA_TYPE isn 't REF)*/
            /*22. SOURCE_DATA_TYPE short =>source type of a distinct type or user -generated Ref type, SQL type from java.sql.Types*/
            /*23. IS_AUTOINCREMENT String =>Indicates whether this column is auto incremented*/
            /** getPrimaryKeys **/
            /*1.TABLE_CAT String => table catalog (may be null)*/
            /*2.TABLE_SCHEM String => table schema (may be null)*/
            /*3.TABLE_NAME String => table name*/
            /*4.COLUMN_NAME String => column name*/
            /*5.KEY_SEQ short => sequence number within primary key*/
            /*6.PK_NAME String => primary key name (may be null)*/
            /** getExportedKeys **/
            /*1.PKTABLE_CAT String => primary key table catalog (may be null)*/
            /*2.PKTABLE_SCHEM String => primary key table schema (may be null)*/
            /*3.PKTABLE_NAME String => primary key table name*/
            /*4.PKCOLUMN_NAME String => primary key column name*/
            /*5.FKTABLE_CAT String => foreign key table catalog (may be null) being exported (may be null)*/
            /*6.FKTABLE_SCHEM String => foreign key table schema (may be null) being exported (may be null)*/
            /*7.FKTABLE_NAME String => foreign key table name being exported*/
            /*8.FKCOLUMN_NAME String => foreign key column name being exported*/
            /*9.KEY_SEQ short => sequence number within foreign key*/
            /*10.UPDATE_RULE short => What happens to foreign key when primary is updated:*/
            /*11.DELETE_RULE short => What happens to the foreign key when primary is deleted.*/
            {//获得“表”的字段
                for (Entry<String, DescTableModel> entry : table2Desc.entrySet()) {
                    //获得“主键”信息
                    Map<String, String> pkMap = new HashMap<String, String>();
                    {
                        ResultSet rs = dbmd.getPrimaryKeys(catalog, schema, entry.getKey());
                        while (rs.next()) {
                            String idName = rs.getString("COLUMN_NAME");
                            pkMap.put(StringUtils.upperCase(idName), idName);
                            System.out.println("table=" + entry.getKey() + ", pk=" + idName);
                        }
                        rs.close();
                    }
                    //获得“索引”信息
//                    {
//                        ResultSet rs = dbmd.getIndexInfo(entry.getValue().getCatalog(), entry.getValue().getSchema(), entry.getValue().getName(), true, true);
//                        Map<String, String> pkMap = new HashMap<String, String>();
//                        while (rs.next()) {
//                            String idName = rs.getString("COLUMN_NAME");
//                            pkMap.put(StringUtils.upperCase(idName), idName);
//                            System.out.println("table=" + entry.getKey() + ", pk=" + idName);
//                        }
//                        rs.close();
//                    }
                    //获得“列”信息
                    ResultSet rs = dbmd.getColumns(catalog, schema, entry.getKey(), "%");
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
                        colName = StringUtils.upperCase(colName);
                        DescColumnModel column = DescColumnModel.create(colName,
                                SqlTypeMappingHelper.getSqlType(sqlType),
                                SqlTypeMappingHelper.getMybatisJavaType(sqlType), size,
                                digit, defaultValue, remark,
                                StringUtils.equalsIgnoreCase(isAutoIncrement, "NO") ? "N" : "Y",
                                nullable == 0 ? "N" : "Y");
                        column.setIsPrimaryKey(pkMap.containsKey(colName) ? "Y" : "N");
                        entry.getValue()
                                .add(column);
                        System.out.println("table=" + entry.getKey() + ", COL=" + colName + ",Type=" + sqlType
                                + ",size=" + size + ",digit=" + digit + ",nullable=" + (nullable == 0 ? "N" : "Y")
                                + ",defaultValue=" + defaultValue + ",isAutoIncrement=" + isAutoIncrement + ",desc="
                                + remark);
                    }
                    rs.close();
                }
            }
            for (Entry<String, DescTableModel> entry : table2Desc.entrySet()) {
                System.out.println(entry.getValue());
            }
        } catch (Exception e) {
            Exceptions.runtime("getTableDesc error!", e);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
        return table2Desc.get(table);
    }

    public Map<String, DescTableModel> findAllTableDescByDataSourceId(String datasourceId) {
        DataSourceModel dataSourceModel = getDataSourceModelById(datasourceId);
        DataSource ds = DataSourceFactory.getOrCreate(dataSourceModel);
        return findAllTableDesc(ds);
    }

    public Map<String, DescTableModel> findAllTableDescByDataSourceName(String datasourceName) {
        DataSourceModel dataSourceModel = getDataSourceModelByName(datasourceName);
        DataSource ds = DataSourceFactory.getOrCreate(dataSourceModel);
        return findAllTableDesc(ds);
    }

    protected Map<String, DescTableModel> findAllTableDesc(DataSource ds) {
        return findAllTableDesc(ds, "", "");
    }

    protected Map<String, DescTableModel> findAllCatalogs(DataSource ds) {
        Connection conn = null;
        try {
            conn = ds.getConnection();
            DatabaseMetaData dbmd = conn.getMetaData();
        } catch (Exception e) {
            Exceptions.runtime("ManagementService findAllCatalogs error!", e);
        } finally {
            SocketHelper.closeWithoutError(conn);
        }
        return findAllTableDesc(ds, "", "");
    }

    protected Map<String, DescTableModel> findAllSchemas(DataSource ds) {
        return findAllTableDesc(ds, "", "");
    }

    protected Map<String, DescTableModel> findAllTableDesc(DataSource ds, String catalog, String schema) {
        Map<String, DescTableModel> table2Desc = new LinkedHashMap<String, DescTableModel>();
        Connection conn = null;
        try {
            conn = ds.getConnection();
            DatabaseMetaData dbmd = conn.getMetaData();
            {//获得“Catalog”
                ResultSet rs = dbmd.getCatalogs();
                while (rs.next()) {
                    String temp = rs.getString("TABLE_CAT");
                    System.out.println("Catalog: " + temp);
                }
                rs.close();
            }
            {//获得“Schema”
                ResultSet rs = dbmd.getSchemas();
                while (rs.next()) {
                    String temp = rs.getString("TYPE_SCHEM");
                    System.out.println("Schema: " + temp);
                }
                rs.close();
            }
            {
                ResultSet rs = dbmd.getTables(catalog, schema, null, new String[]{"TABLE", "VIEW"});
                while (rs.next()) {
                    catalog = rs.getString("TYPE_CAT");
                    schema = rs.getString("TYPE_SCHEM");
                    String name = rs.getString("TABLE_NAME");
                    String type = rs.getString("TABLE_TYPE");
                    String remarks = rs.getString("REMARKS");
                    table2Desc.put(name, DescTableModel.create(catalog, schema, name, type, remarks));
                }
                rs.close();
            }
            {
                for (Entry<String, DescTableModel> entry : table2Desc.entrySet()) {
                    ResultSet rs = dbmd.getPrimaryKeys(catalog, schema, entry.getKey());
                    Map<String, String> pkMap = new HashMap<String, String>();
                    while (rs.next()) {
                        String idName = rs.getString("COLUMN_NAME");
                        pkMap.put(StringUtils.upperCase(idName), idName);
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
                        colName = StringUtils.upperCase(colName);
                        DescColumnModel column = DescColumnModel.create(colName,
                                SqlTypeMappingHelper.getSqlType(sqlType),
                                SqlTypeMappingHelper.getMybatisJavaType(sqlType), size,
                                digit, defaultValue, remark,
                                StringUtils.equalsIgnoreCase(isAutoIncrement, "NO") ? "N" : "Y",
                                nullable == 0 ? "N" : "Y");
                        column.setIsPrimaryKey(pkMap.containsKey(colName) ? "Y" : "N");
                        entry.getValue()
                                .add(column);
                        System.out.println("table=" + entry.getKey() + ", COL=" + colName + ",Type=" + sqlType
                                + ",size=" + size + ",digit=" + digit + ",nullable=" + (nullable == 0 ? "N" : "Y")
                                + ",defaultValue=" + defaultValue + ",isAutoIncrement=" + isAutoIncrement + ",desc="
                                + remark);
                    }
                    rs.close();
                }
            }
            for (Entry<String, DescTableModel> entry : table2Desc.entrySet()) {
                System.out.println(entry.getValue());
            }
        } catch (Exception e) {
            Exceptions.runtime("findAllTableDesc error!", e);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
        return table2Desc;
    }

    public QueryStatementModel getQueryStatementModelById(String id) {
        try {
            QueryRunner queryRunner = new QueryRunner(DataSourceFactory.getSystemDS());
            Map<String, Object> result = queryRunner.query("select * from DS_QUERY_STMT where ID=?", new MapHandler(),
                    new Object[]{id});
            return BeanHelper.copyProperties(QueryStatementModel.class, result);
        } catch (Exception e) {
            Exceptions.runtime("getQueryStatementModelById error!", e);
        }
        return null;
    }

    public QueryStatementModel getQueryStatementModelByName(String name) {
        try {
            QueryRunner queryRunner = new QueryRunner(DataSourceFactory.getSystemDS());
            Map<String, Object> result = queryRunner.query("select * from DS_QUERY_STMT where NAME=?", new MapHandler(),
                    new Object[]{name});
            return BeanHelper.copyProperties(QueryStatementModel.class, result);
        } catch (Exception e) {
            Exceptions.runtime("getQueryStatementModelByName error!", e);
        }
        return null;
    }

    public List<QueryStatementModel> findQueryStatementModel(QueryStatementModel model) {
        try {
            QueryRunner queryRunner = new QueryRunner(DataSourceFactory.getSystemDS());
            StringBuilder sql = new StringBuilder();
            List<Object> params = new ArrayList<Object>();
            {
                sql.append("select * from DS_QUERY_STMT where 1=1 ");
                if (StringUtils.isNotBlank(model.getId())) {
                    sql.append(" and ID=? ");
                    params.add(model.getId());
                }
                if (StringUtils.isNotBlank(model.getName())) {
                    sql.append(" and NAME like ? ");
                    params.add("%" + model.getName() + "%");
                }
                if (model.getCreateTime() != null) {
                    sql.append(" and CREATE_TIME >= ? ");
                    params.add(model.getCreateTime());
                }
                if (model.getUpdateTime() != null) {
                    sql.append(" and UPDATE_TIME >= ? ");
                    params.add(model.getUpdateTime());
                }
            }
            List<QueryStatementModel> models = new ArrayList<QueryStatementModel>();
            {
                List<Map<String, Object>> list = queryRunner.query(sql.toString(), new MapListHandler(),
                        params.toArray());
                for (Map<String, Object> map : list) {
                    models.add(BeanHelper.copyProperties(QueryStatementModel.class, map));
                }
            }
            return models;
        } catch (Exception e) {
            Exceptions.runtime("findQueryStatementModel error!", e);
        }
        return new ArrayList<QueryStatementModel>();
    }

    public Map<String, Object> pageFindQueryStatementModel(QueryStatementModel model, int currentPage, int pageSize) {
        try {
            DataSource dataSource = DataSourceFactory.getSystemDS();
            QueryRunner queryRunner = new QueryRunner(dataSource);
            StringBuilder sql = new StringBuilder();
            List<Object> params = new ArrayList<Object>();
            {
                sql.append("select * from DS_QUERY_STMT where 1=1 ");
                if (StringUtils.isNotBlank(model.getId())) {
                    sql.append(" and ID=? ");
                    params.add(model.getId());
                }
                if (StringUtils.isNotBlank(model.getName())) {
                    sql.append(" and NAME like ? ");
                    params.add("%" + model.getName() + "%");
                }
                if (model.getCreateTime() != null) {
                    sql.append(" and CREATE_TIME >= ? ");
                    params.add(model.getCreateTime());
                }
                if (model.getUpdateTime() != null) {
                    sql.append(" and UPDATE_TIME >= ? ");
                    params.add(model.getUpdateTime());
                }
            }
            if (dataSource instanceof BasicDataSource) {
                String countSql = "select count(*) from (" + sql.toString() + ") tmp_count";
                Number count = (Number) queryRunner.query(countSql, new ScalarHandler<Number>(1), params.toArray());
                BasicDataSource bds = (BasicDataSource) dataSource;
                String pageSql = Dialect.getInstanceByUrl(bds.getUrl()).getLimitString(sql.toString(),
                        Math.max((currentPage - 1) * pageSize, 0), pageSize);
                List<QueryStatementModel> models = new ArrayList<QueryStatementModel>();
                {
                    List<Map<String, Object>> list = queryRunner.query(pageSql, new MapListHandler(), params.toArray());
                    for (Map<String, Object> map : list) {
                        models.add(BeanHelper.copyProperties(QueryStatementModel.class, map));
                    }
                }
                return MapHelper.toMap("currentPage", currentPage, "pageSize", pageSize, "totalCount",
                        count.longValue(), "rows", models);
            }
        } catch (Exception e) {
            Exceptions.runtime("pageFindQueryStatementModel error!", e);
        }
        return MapHelper.toMap("currentPage", currentPage, "pageSize", pageSize, "totalCount", 0, "rows",
                new ArrayList<QueryStatementModel>());
    }

    public long addOrUpdateQueryStatementModel(QueryStatementModel model) {
        try {
            Connection connection = DBConnectionHolder.getConnection(DataSourceFactory.getSystemDS());
            QueryRunner queryRunner = new QueryRunner();
            if (StringUtils.isBlank(model.getId())) {
                String sql = "insert into DS_QUERY_STMT( ID, NAME, SELECT_BODY, FROM_BODY, WHERE_BODY, DESCRIPTION, UPDATE_TIME, CREATE_TIME ) values (?, ?, ?, ?, ?, ?, ?, ?)";
                {
                    model.setId(StringHelper.uuid());
                    if (model.getCreateTime() == null) {
                        model.setCreateTime(new Date());
                    }
                    if (model.getUpdateTime() == null) {
                        model.setUpdateTime(new Date());
                    }
                }
                Map<?, ?> map = queryRunner.insert(connection, sql, new MapHandler(),
                        new Object[]{model.getId(), model.getName(), model.getSelectBody(), model.getFromBody(),
                                model.getWhereBody(), model.getDescription(), model.getCreateTime(),
                                model.getUpdateTime()});
                return 0;
            } else {
                {
                    model.setUpdateTime(new Date());
                }
                String sql = "update DS_QUERY_STMT set SELECT_BODY=?, FROM_BODY=?, WHERE_BODY=?, DESCRIPTION=?, UPDATE_TIME=? where ID=?";
                if (StringUtils.isBlank(model.getId())) {//TODO
                    sql = "update DS_QUERY_STMT set SELECT_BODY=?, FROM_BODY=?, WHERE_BODY=?, DESCRIPTION=?, UPDATE_TIME=? where NAME=?";
                }
                int count = queryRunner.update(connection, sql, model.getSelectBody(), model.getFromBody(),
                        model.getWhereBody(), model.getDescription(), model.getUpdateTime(),
                        StringUtils.defaultIfBlank(model.getId(), model.getName()));
                return count;
            }
        } catch (Exception e) {
            Exceptions.runtime("addOrUpdateQueryStatementModel error!", e);
        }
        return -1;
    }

    public long deleteQueryStatementModelById(String id) {
        try {
            Connection connection = DBConnectionHolder.getConnection(DataSourceFactory.getSystemDS());
            QueryRunner queryRunner = new QueryRunner();
            String sql = "delete from DS_QUERY_STMT where ID=?";
            int count = queryRunner.update(connection, sql, id);
            return count;
        } catch (Exception e) {
            Exceptions.runtime("deleteQueryStatementModelById error!", e);
        }
        return -1;
    }

    public long deleteQueryStatementModelByName(String name) {
        try {
            Connection connection = DBConnectionHolder.getConnection(DataSourceFactory.getSystemDS());
            QueryRunner queryRunner = new QueryRunner();
            String sql = "delete from DS_QUERY_STMT where NAME=?";
            int count = queryRunner.update(connection, sql, name);
            return count;
        } catch (Exception e) {
            Exceptions.runtime("deleteQueryStatementModelByName error!", e);
        }
        return -1;
    }

    public DataSourceModel getDataSourceModelById(String id) {
        try {
            QueryRunner queryRunner = new QueryRunner(DataSourceFactory.getSystemDS());
            Map<String, Object> result = queryRunner.query("select * from DS_DATA_SOURCE where ID=?", new MapHandler(),
                    new Object[]{id});
            return BeanHelper.copyProperties(DataSourceModel.class, result);
        } catch (Exception e) {
            Exceptions.runtime("getDataSourceModelById error!", e);
        }
        return null;
    }

    public DataSourceModel getDataSourceModelByName(String name) {
        try {
            QueryRunner queryRunner = new QueryRunner(DataSourceFactory.getSystemDS());
            Map<String, Object> result = queryRunner.query("select * from DS_DATA_SOURCE where NAME=?",
                    new MapHandler(), new Object[]{name});
            return BeanHelper.copyProperties(DataSourceModel.class, result);
        } catch (Exception e) {
            Exceptions.runtime("getDataSourceModelByName error!", e);
        }
        return null;
    }

    public List<DataSourceModel> findDataSourceModel(DataSourceModel model) {
        Assert.notNull(model, "DataSource is required!");
        try {
            QueryRunner queryRunner = new QueryRunner(DataSourceFactory.getSystemDS());
            StringBuilder sql = new StringBuilder();
            List<Object> params = new ArrayList<Object>();
            {
                sql.append("select * from DS_DATA_SOURCE where 1=1 ");
                if (StringUtils.isNotBlank(model.getId())) {
                    sql.append(" and ID=? ");
                    params.add(model.getId());
                }
                if (StringUtils.isNotBlank(model.getName())) {
                    sql.append(" and NAME like ? ");
                    params.add("%" + model.getName() + "%");
                }
                if (model.getCreateTime() != null) {
                    sql.append(" and CREATE_TIME >= ? ");
                    params.add(model.getCreateTime());
                }
                if (model.getUpdateTime() != null) {
                    sql.append(" and UPDATE_TIME >= ? ");
                    params.add(model.getUpdateTime());
                }
            }
            List<DataSourceModel> models = new ArrayList<DataSourceModel>();
            {
                List<Map<String, Object>> list = queryRunner.query(sql.toString(), new MapListHandler(),
                        params.toArray());
                for (Map<String, Object> map : list) {
                    models.add(BeanHelper.copyProperties(DataSourceModel.class, map));
                }
            }

            return models;
        } catch (Exception e) {
            Exceptions.runtime("findDataSourceModel error!", e);
        }
        return new ArrayList<DataSourceModel>();
    }

    public Map<String, Object> pageFindDataSourceModel(DataSourceModel model, int currentPage, int pageSize) {
        Assert.notNull(model, "DataSource is required!");
        try {
            DataSource dataSource = DataSourceFactory.getSystemDS();
            QueryRunner queryRunner = new QueryRunner(dataSource);
            StringBuilder sql = new StringBuilder();
            List<Object> params = new ArrayList<Object>();
            {
                sql.append("select * from DS_DATA_SOURCE where 1=1 ");
                if (StringUtils.isNotBlank(model.getId())) {
                    sql.append(" and ID=? ");
                    params.add(model.getId());
                }
                if (StringUtils.isNotBlank(model.getName())) {
                    sql.append(" and NAME like ? ");
                    params.add("%" + model.getName() + "%");
                }
                if (model.getCreateTime() != null) {
                    sql.append(" and CREATE_TIME >= ? ");
                    params.add(model.getCreateTime());
                }
                if (model.getUpdateTime() != null) {
                    sql.append(" and UPDATE_TIME >= ? ");
                    params.add(model.getUpdateTime());
                }
            }
            if (dataSource instanceof BasicDataSource) {
                String countSql = "select count(*) from (" + sql.toString() + ") tmp_count";
                Number count = (Number) queryRunner.query(countSql, new ScalarHandler<Number>(1), params.toArray());
                BasicDataSource bds = (BasicDataSource) dataSource;
                String pageSql = Dialect.getInstanceByUrl(bds.getUrl()).getLimitString(sql.toString(),
                        Math.max((currentPage - 1) * pageSize, 0), pageSize);

                List<DataSourceModel> models = new ArrayList<DataSourceModel>();
                {
                    List<Map<String, Object>> list = queryRunner.query(pageSql, new MapListHandler(), params.toArray());
                    for (Map<String, Object> map : list) {
                        models.add(BeanHelper.copyProperties(DataSourceModel.class, map));
                    }
                }
                return MapHelper.toMap("currentPage", currentPage, "pageSize", pageSize, "totalCount",
                        count.longValue(), "rows", models);
            }
        } catch (Exception e) {
            Exceptions.runtime("findDataSourceModel error!", e);
        }
        return MapHelper.toMap("currentPage", currentPage, "pageSize", pageSize, "totalCount", 0, "rows",
                new ArrayList<DataSourceModel>());
    }

    public long addOrUpdateDataSourceModel(DataSourceModel model) {
        Assert.notNull(model, "DataSource is required!");
        try {
            Connection connection = DBConnectionHolder.getConnection(DataSourceFactory.getSystemDS());
            QueryRunner queryRunner = new QueryRunner();
            if (StringUtils.isBlank(model.getId())) {
                String sql = "insert into DS_DATA_SOURCE( ID, NAME, USER, PASSWORD, URL, DRIVER, VALIDATION_QUERY, INIT_CONNECTION, MAX_CONNECTION, DESCRIPTION, UPDATE_TIME, CREATE_TIME ) "
                        + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                {
                    model.setId(StringHelper.uuid());
                    if (model.getCreateTime() == null) {
                        model.setCreateTime(new Date());
                    }
                    if (model.getUpdateTime() == null) {
                        model.setUpdateTime(new Date());
                    }
                }
                queryRunner.insert(connection, sql, new MapHandler(),
                        new Object[]{model.getId(), model.getName(), model.getUser(), model.getPassword(),
                                model.getUrl(), model.getDriver(), model.getValidationQuery(),
                                model.getInitConnection(), model.getMaxConnection(), model.getDescription(),
                                model.getUpdateTime(), model.getCreateTime()});
                return 0;
            } else {
                {
                    model.setUpdateTime(new Date());
                }
                String sql = "update DS_DATA_SOURCE set USER=?, PASSWORD=?, URL=?, DRIVER=?, VALIDATION_QUERY=?, INIT_CONNECTION=?, MAX_CONNECTION=?, DESCRIPTION=?, UPDATE_TIME=? where ID=?";
                if (StringUtils.isBlank(model.getId())) {//TODO
                    sql = "update DS_DATA_SOURCE set USER=?, PASSWORD=?, URL=?, DRIVER=?, VALIDATION_QUERY=?, INIT_CONNECTION=?, MAX_CONNECTION=?, DESCRIPTION=?, UPDATE_TIME=? where NAME=?";
                }
                int count = queryRunner.update(connection, sql, model.getUser(), model.getPassword(), model.getUrl(),
                        model.getDriver(), model.getValidationQuery(), model.getInitConnection(),
                        model.getMaxConnection(), model.getDescription(), model.getUpdateTime(),
                        StringUtils.defaultIfBlank(model.getId(), model.getName()));
                return count;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Exceptions.runtime("addOrUpdateDataSourceModel error!", e);
        }
        return -1;
    }

    public long deleteDataSourceModelById(String id) {
        Assert.notBlank(id, "DataSource id is required!");
        try {
            Connection connection = DBConnectionHolder.getConnection(DataSourceFactory.getSystemDS());
            QueryRunner queryRunner = new QueryRunner();
            String sql = "delete from DS_DATA_SOURCE where ID=?";
            int count = queryRunner.update(connection, sql, id);
            return count;
        } catch (Exception e) {
            Exceptions.runtime("deleteDataSourceModelById error!", e);
        }
        return -1;
    }

    public long deleteDataSourceModelByName(String name) {
        Assert.notBlank(name, "DataSource name is required!");
        try {
            Connection connection = DBConnectionHolder.getConnection(DataSourceFactory.getSystemDS());
            QueryRunner queryRunner = new QueryRunner();
            String sql = "delete from DS_DATA_SOURCE where NAME=?";
            int count = queryRunner.update(connection, sql, name);
            return count;
        } catch (Exception e) {
            Exceptions.runtime("deleteDataSourceModelByName error!", e);
        }
        return -1;
    }

    public boolean testDataSourceModelById(String id) {
        Assert.notBlank(id, "DataSource id is required!");
        try {
            DataSourceModel ds = getDataSourceModelById(id);
            DataSource source = DataSourceFactory.create(ds);
            QueryRunner queryRunner = new QueryRunner(source);
            Object result = queryRunner.query(ds.getValidationQuery(), new ScalarHandler<Object>(1));
            return result != null;
        } catch (Exception e) {
            Exceptions.runtime("testDataSourceModelById error!", e);
        }
        return false;
    }

    public boolean testDataSourceModelByName(String name) {
        Assert.notBlank(name, "DataSource name is required!");
        try {
            DataSourceModel ds = getDataSourceModelByName(name);
            DataSource source = DataSourceFactory.create(ds);
            QueryRunner queryRunner = new QueryRunner(source);
            Object result = queryRunner.query(ds.getValidationQuery(), new ScalarHandler<Object>(1));
            return result != null;
        } catch (Exception e) {
            Exceptions.runtime("testDataSourceModelById error!", e);
        }
        return false;
    }
}

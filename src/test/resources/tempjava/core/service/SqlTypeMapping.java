/*******************************************************************************
 * Copyright (c) Oct 8, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.jgit.core.service;

import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.MapHelper;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Oct 8, 2016
 */
public class SqlTypeMapping {

    public static final Map<String, String> columnTypeToMybatisType = Collections.unmodifiableMap(MapHelper.toMap(
            "CHAR", "CHAR",
            "VARCHAR", "VARCHAR",
            "LONGVARCHAR", "LONGVARCHAR",
            "NCHAR", "VARCHAR",
            "NVARCHAR", "VARCHAR",
            "LONGNVARCHAR", "VARCHAR",
            "SQLXML", "VARCHAR",
            "NUMERIC", "NUMERIC",
            "DECIMAL", "DECIMAL",
            "BIT", "BIT",
            "BOOLEAN", "BOOLEAN",
            "TINYINT", "TINYINT",
            "INTEGER", "INTEGER",
            "BIGINT", "BIGINT",
            "REAL", "REAL",
            "FLOAT", "FLOAT",
            "DOUBLE", "DOUBLE",
            "BINARY", "BINARY",
            "VARBINARY", "VARBINARY",
            "LONGVARBINARY", "LONGVARBINARY",
            "DATE", "TIMESTAMP",
            "TIME", "TIME",
            "TIMESTAMP", "TIMESTAMP",
            "CLOB", "CLOB",
            "NCLOB", "CLOB",
            "BLOB", "BLOB",
            "ARRAY", "ARRAY",
            "STRUCT", "STRUCT",
            "REF", "REF",
            "DATALINK", "DATALINK"
    ));
    public static final Map<Integer, String> jdbcTypeToJavaType = Collections.unmodifiableMap(MapHelper.toMap(
            Types.CHAR, "String",
            Types.VARCHAR, "String",
            Types.LONGVARCHAR, "String",
            Types.NCHAR, "String",
            Types.NVARCHAR, "String",
            Types.LONGNVARCHAR, "String",
            Types.SQLXML, "String",
            Types.NUMERIC, BigDecimal.class.getName(),
            Types.DECIMAL, BigDecimal.class.getName(),
            Types.BIT, "Boolean",
            Types.BOOLEAN, "Boolean",
            Types.TINYINT, "Byte",
            Types.SMALLINT, "Short",
            Types.INTEGER, "Integer",
            Types.BIGINT, "Long",
            Types.REAL, "Float",
            Types.FLOAT, "Double",
            Types.DOUBLE, "Double",
            Types.BINARY, "byte[]",
            Types.VARBINARY, "byte[]",
            Types.LONGVARBINARY, "byte[]",
            Types.DATE, Date.class.getName(),
            Types.TIME, Time.class.getName(),
            Types.TIMESTAMP, Date.class.getName(),
            Types.CLOB, Clob.class.getName(),
            Types.NCLOB, Clob.class.getName(),
            Types.BLOB, Blob.class.getName(),
            Types.ARRAY, Array.class.getName(),
            Types.STRUCT, Struct.class.getName(),
            Types.REF, Ref.class.getName(),
            Types.DATALINK, URL.class.getName()
    ));
    public static final Map<Integer, String> jdbcTypeToSqlType = Collections.unmodifiableMap(MapHelper.toMap(
            Types.CHAR, "CHAR",
            Types.VARCHAR, "VARCHAR",
            Types.LONGVARCHAR, "LONGVARCHAR",
            Types.NCHAR, "NCHAR",
            Types.NVARCHAR, "NVARCHAR",
            Types.LONGNVARCHAR, "LONGNVARCHAR",
            Types.SQLXML, "SQLXML",
            Types.NUMERIC, "NUMERIC",
            Types.DECIMAL, "DECIMAL",
            Types.BIT, "BIT",
            Types.BOOLEAN, "BOOLEAN",
            Types.TINYINT, "TINYINT",
            Types.SMALLINT, "SMALLINT",
            Types.INTEGER, "INTEGER",
            Types.BIGINT, "BIGINT",
            Types.REAL, "REAL",
            Types.FLOAT, "FLOAT",
            Types.DOUBLE, "DOUBLE",
            Types.BINARY, "BINARY",
            Types.VARBINARY, "VARBINARY",
            Types.LONGVARBINARY, "LONGVARBINARY",
            Types.DATE, "DATE",
            Types.TIME, "TIME",
            Types.TIMESTAMP, "TIMESTAMP",
            Types.CLOB, "CLOB",
            Types.NCLOB, "NCLOB",
            Types.BLOB, "BLOB",
            Types.ARRAY, "ARRAY",
            Types.STRUCT, "STRUCT",
            Types.REF, "REF",
            Types.DATALINK, "DATALINK"
    ));

    public static String getMybatisJavaType(int jdbcType) {
        return StringUtils.defaultString(jdbcTypeToJavaType.get(jdbcType), "String");
    }

    public static String getMybatisJdbcTypeByColumnType(String columnType) {
        return StringUtils.defaultString(columnTypeToMybatisType.get(columnType.toUpperCase()), "VARCHAR");
    }

    public static String getSqlType(int jdbcType) {
        return StringUtils.defaultString(jdbcTypeToSqlType.get(jdbcType), "VARCHAR");
    }
}

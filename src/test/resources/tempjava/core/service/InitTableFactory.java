/*******************************************************************************
 * Copyright (c) Oct 13, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.jgit.core.service;

import org.iff.jgit.core.DataSourceFactory;
import org.iff.jgit.core.model.DescTableModel;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.lang3.StringUtils;
import org.iff.infra.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Oct 13, 2016
 */
public class InitTableFactory {

    private static InitTableFactory me = new InitTableFactory();

    private static Map<String, InitTable> map = new HashMap<String, InitTable>();

    public static InitTableFactory me() {
        if (map.isEmpty()) {
            register("mysql", me.new MySqlTable());
        }
        return me;
    }

    public static InitTableFactory register(String databaseType, InitTable initTable) {
        Assert.notBlank(databaseType);
        Assert.notNull(initTable);
        map.put(StringUtils.lowerCase(databaseType), initTable);
        return me();
    }

    public static InitTable get(String databaseType) {
        Assert.notBlank(databaseType);
        me();
        return map.get(StringUtils.lowerCase(databaseType));
    }

    public static void initByDatabaseType(String databaseType) {
        Assert.notBlank(databaseType);
        InitTable initTable = get(databaseType);
        Assert.notNull(initTable);
        initTable.init();
    }

    public class MySqlTable implements InitTable {
        @Override
        public void init() {
            QueryRunner queryRunner = new QueryRunner(DataSourceFactory.getSystemDS());
            {/*create table DS_DATA_SOURCE*/

                String dataSourceTable = ""/**/
                        + "CREATE TABLE `DS_DATA_SOURCE` (                                   \n"/**/
                        + "    `ID` VARCHAR(40) NOT NULL   COMMENT '主键',                    \n"/**/
                        + "    `NAME` VARCHAR(100) NOT NULL  COMMENT '数据源名称',             \n"/**/
                        + "    `USER` VARCHAR(1024) NOT NULL  COMMENT '用户名',               \n"/**/
                        + "    `PASSWORD` VARCHAR(2048) NOT NULL  COMMENT '密码',             \n"/**/
                        + "    `URL` VARCHAR(2048) NOT NULL  COMMENT '连接URL',               \n"/**/
                        + "    `DRIVER` VARCHAR(1024) NOT NULL  COMMENT '驱动',               \n"/**/
                        + "    `DESCRIPTION` VARCHAR(200)   COMMENT '描述',                   \n"/**/
                        + "    `VALIDATION_QUERY` VARCHAR(1024) NOT NULL  COMMENT '测试语句',  \n"/**/
                        + "    `INIT_CONNECTION` INT NOT NULL  COMMENT '初始连接数',           \n"/**/
                        + "    `MAX_CONNECTION` INT NOT NULL  COMMENT '最大连接数',            \n"/**/
                        + "    `UPDATE_TIME` DATETIME NOT NULL  COMMENT '修改时间',            \n"/**/
                        + "    `CREATE_TIME` DATETIME NOT NULL  COMMENT '创建时间',            \n"/**/
                        + "    PRIMARY KEY (`ID`),                                           \n"/**/
                        + "    UNIQUE KEY (`NAME`)                                           \n"/**/
                        + ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据源';             \n"/**/;
                try {
                    ManagementService managementService = new ManagementService();
                    DescTableModel tableDesc = managementService.getTableDesc(DataSourceFactory.getSystemDS(),
                            "DS_DATA_SOURCE");
                    if (tableDesc == null) {
                        queryRunner.update(dataSourceTable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            {/*create table DS_DATA_SOURCE*/

                String queryStatement = ""/**/
                        + "CREATE TABLE `DS_QUERY_STMT` (                               \n"/**/
                        + "    `ID` VARCHAR(40) NOT NULL   COMMENT '主键',               \n"/**/
                        + "    `NAME` VARCHAR(100) NOT NULL  COMMENT '查询语句名称',       \n"/**/
                        + "    `SELECT_BODY` VARCHAR(4000) NOT NULL  COMMENT '查询列',    \n"/**/
                        + "    `FROM_BODY` VARCHAR(4000) NOT NULL  COMMENT '查询表',      \n"/**/
                        + "    `WHERE_BODY` VARCHAR(4000) NOT NULL  COMMENT '查询条件',    \n"/**/
                        + "    `DESCRIPTION` VARCHAR(200)   COMMENT '描述',               \n"/**/
                        + "    `UPDATE_TIME` DATETIME NOT NULL  COMMENT '修改时间',        \n"/**/
                        + "    `CREATE_TIME` DATETIME NOT NULL  COMMENT '创建时间',        \n"/**/
                        + "    PRIMARY KEY (`ID`),                                        \n"/**/
                        + "    UNIQUE KEY (`NAME`)                                        \n"/**/
                        + ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='查询语句';        \n"/**/;
                try {
                    ManagementService managementService = new ManagementService();
                    DescTableModel tableDesc = managementService.getTableDesc(DataSourceFactory.getSystemDS(),
                            "DS_QUERY_STMT");
                    if (tableDesc == null) {
                        queryRunner.update(queryStatement);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

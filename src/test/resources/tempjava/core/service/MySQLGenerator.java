/*******************************************************************************
 * Copyright (c) 2018-12-02 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core.service;

import org.apache.commons.lang3.StringUtils;
import org.iff.jgit.util.FieldNameHelper;
import org.iff.jgit.util.SqlTypeMappingHelper;
import org.iff.infra.util.EventBusHelper;
import org.iff.infra.util.Exceptions;
import org.iff.infra.util.FCS;
import org.iff.infra.util.NumberHelper;

import java.util.ArrayList;
import java.util.List;

import static org.iff.jgit.core.model.CrudModel.BusinessModel;
import static org.iff.jgit.core.model.CrudModel.FieldModel;
import static org.iff.jgit.core.service.CrudService.ProjectTuple;

/**
 * GenerateEventService
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-12-02
 * auto generate by qdp.
 */
public class MySQLGenerator implements EventBusHelper.EventProcess {
    public static final String EVENT_NAME = MySQLGenerator.class.getSimpleName();
    public static final String ACTION_tableRestrict = "tableRestrict";
    public static final String ACTION_tableDrop = "tableDrop";
    public static final String ACTION_tableCreate = "tableCreate";
    public static final String ACTION_tablePk = "tablePk";
    public static final String ACTION_tableColumn = "tableColumn";
    public static final String ACTION_tablePkIndex = "tablePkIndex";
    public static final String ACTION_tableColumnIndex = "tableColumnIndex";
    public static final String ACTION_tableUniqueIndex = "tableUniqueIndex";
    public static final String ACTION_tableForeignKey = "tableForeignKey";
    public static final String ACTION_tableCreateClose = "tableCreateClose";
    public static final String ACTION_tableRestrictClose = "tableRestrictClose";
    public static final String ACTION_mappingGet = "mappingGet";
    public static final String ACTION_mappingFind = "mappingFind";
    public static final String ACTION_mappingUpdate = "mappingUpdate";
    public static final String ACTION_mappingSave = "mappingSave";
    public static final String ACTION_mappingRemove = "mappingRemove";
    public static final String ACTION_mappingIndex = "mappingIndex";
    public static final String ACTION_mappingShow = "mappingShow";
    public static final String ACTION_mappingCreate = "mappingCreate";
    public static final String ACTION_mappingDestroy = "mappingDestroy";

    public String getName() {
        return EVENT_NAME;
    }

    public void listen(String event, Object args) {
        Tuple tuple = (Tuple) args;
        String action = tuple.action();
        Object result = null;
        try {
            if (action.equals(ACTION_tableRestrict)) {
                tableRestrict(tuple.projectTuple());
            } else if (action.equals(ACTION_tableDrop)) {
                tableDrop(tuple.projectTuple());
            } else if (action.equals(ACTION_tableCreate)) {
                tableCreate(tuple.projectTuple());
            } else if (action.equals(ACTION_tablePk)) {
                tablePk(tuple.projectTuple());
            } else if (action.equals(ACTION_tableColumn)) {
                tableColumn(tuple.projectTuple());
            } else if (action.equals(ACTION_tablePkIndex)) {
                tablePkIndex(tuple.projectTuple());
            } else if (action.equals(ACTION_tableColumnIndex)) {
                tableColumnIndex(tuple.projectTuple());
            } else if (action.equals(ACTION_tableUniqueIndex)) {
                tableUniqueIndex(tuple.projectTuple());
            } else if (action.equals(ACTION_tableForeignKey)) {
                tableForeignKey(tuple.projectTuple());
            } else if (action.equals(ACTION_tableCreateClose)) {
                tableCreateClose(tuple.projectTuple());
            } else if (action.equals(ACTION_tableRestrictClose)) {
                tableRestrictClose(tuple.projectTuple());
            } else if (action.equals(ACTION_mappingGet)) {
                mappingGet(tuple.projectTuple());
            } else if (action.equals(ACTION_mappingFind)) {
                mappingFind(tuple.projectTuple());
            } else if (action.equals(ACTION_mappingUpdate)) {
                mappingUpdate(tuple.projectTuple());
            } else if (action.equals(ACTION_mappingSave)) {
                mappingSave(tuple.projectTuple());
            } else if (action.equals(ACTION_mappingRemove)) {
                mappingRemove(tuple.projectTuple());
            } else if (action.equals(ACTION_mappingIndex)) {
                mappingIndex(tuple.projectTuple());
            } else if (action.equals(ACTION_mappingShow)) {
                mappingShow(tuple.projectTuple());
            } else if (action.equals(ACTION_mappingCreate)) {
                mappingCreate(tuple.projectTuple());
            } else if (action.equals(ACTION_mappingDestroy)) {
                mappingDestroy(tuple.projectTuple());
            } else {
                Exceptions.runtime("GenerateEventService no event: " + action + " found!");
            }
            tuple.result(result);
        } catch (Exception e) {
            tuple.result(e);
        }
    }

    /**
     * 1/9: SELECT * FROM xxx WHERE
     *
     * @param pt
     */
    public void mappingGet(ProjectTuple pt) {
        pt.contents().add("<script>");
        pt.contents().add("  SELECT * FROM " + pt.table().getStr(BusinessModel.tableName).toUpperCase());
        pt.contents().add("  <where>");
        List<FieldModel> list = new ArrayList<>(pt.pk());
        list.addAll(pt.columns());
        String templ = "    <if test=\"vo!=null and vo.{fieldName} != null  and vo.{fieldName} !='' \"> AND {columnName} = #{vo.{fieldName},jdbcType={jdbcType}}</if>";
        String templLike = "    <if test=\"vo!=null and vo.{fieldName} != null  and vo.{fieldName} !='' \"> AND {columnName} LIKE CONCAT('%',#{vo.{fieldName},jdbcType={jdbcType}},'%')</if>";
        for (FieldModel fm : list) {
            String fieldName = FieldNameHelper.columnToField(fm.getStr(FieldModel.fieldName));
            String columnName = fm.getStr(FieldModel.fieldName).toUpperCase();
            String jdbcType = SqlTypeMappingHelper.getMybatisJdbcTypeByColumnType(fm.getStr(FieldModel.fieldColType));
            String template = "Y".equals(fm.getStr(FieldModel.ableSearch)) && "String".equals(fm.getStr(FieldModel.fieldJavaType)) ? templLike : templ;
            String content = FCS.get(template, fieldName, fieldName, columnName, fieldName, jdbcType).toString();
            pt.contents().add(content);
        }
        pt.contents().add("  </where>");
        pt.contents().add("</script>");
    }

    /**
     * 2/9: SELECT * FROM xxx WHERE xxx ORDER BY xxx
     *
     * @param pt
     */
    public void mappingFind(ProjectTuple pt) {
        pt.contents().add("<script>");
        pt.contents().add("  SELECT * FROM " + pt.table().getStr(BusinessModel.tableName).toUpperCase());
        pt.contents().add("  <where>");
        List<FieldModel> list = new ArrayList<>(pt.pk());
        list.addAll(pt.columns());
        String templ = "    <if test=\"vo!=null and vo.{fieldName} != null  and vo.{fieldName} !='' \"> AND {columnName} = #{vo.{fieldName},jdbcType={jdbcType}}</if>";
        String templLike = "    <if test=\"vo!=null and vo.{fieldName} != null  and vo.{fieldName} !='' \"> AND {columnName} LIKE CONCAT('%',#{vo.{fieldName},jdbcType={jdbcType}},'%')</if>";
        StringBuilder fieldColList = new StringBuilder();
        for (FieldModel fm : list) {
            String fieldName = FieldNameHelper.columnToField(fm.getStr(FieldModel.fieldName));
            String columnName = fm.getStr(FieldModel.fieldName).toUpperCase();
            String jdbcType = SqlTypeMappingHelper.getMybatisJdbcTypeByColumnType(fm.getStr(FieldModel.fieldColType));
            String template = "Y".equals(fm.getStr(FieldModel.ableSearch)) && "String".equals(fm.getStr(FieldModel.fieldJavaType)) ? templLike : templ;
            String content = FCS.get(template, fieldName, fieldName, columnName, fieldName, jdbcType).toString();
            pt.contents().add(content);
            fieldColList.append('\'').append(columnName).append("',");
        }
        if (fieldColList.length() > 0) {
            fieldColList.setLength(fieldColList.length() - 1);
        }
        pt.contents().add("  </where>");
        pt.contents().add("  <bind name=\"orderList\" value=\"{'ASC', 'DESC'}\"/>");
        pt.contents().add("  <bind name=\"fieldColList\" value=\"{" + fieldColList.toString() + "}\"/>");
        pt.contents().add("  <if test=\"page!=null and page.orderBy!=null\">");
        pt.contents().add("    <trim prefix=\"order by \" suffixOverrides=\",\">");
        pt.contents().add("      <foreach item=\"item\" index=\"index\" collection=\"page.orderBy\" separator=\",\">");
        pt.contents().add("        <bind name=\"orderName\" value=\"@org.iff.datarest.util.FieldNameHelper@fieldToColumn(item.name)\"/>");
        pt.contents().add("        <bind name=\"orderType\" value=\"(item.order.toUpperCase() in orderList) ? item.order.toUpperCase() : null\"/>");
        pt.contents().add("        <if test=\"item!=null and item.name!=null and orderType!=null and (orderName in fieldColList)\">${orderName} ${orderType}</if>");
        pt.contents().add("      </foreach>");
        pt.contents().add("    </trim>");
        pt.contents().add("  </if>");
        pt.contents().add("</script>");
    }

    /**
     * 3/9: UPDATE * SET xxx WHERE xx
     *
     * @param pt
     */
    public void mappingUpdate(ProjectTuple pt) {
        pt.contents().add("<script>");
        pt.contents().add("  UPDATE " + pt.table().getStr(BusinessModel.tableName).toUpperCase());
        pt.contents().add("  <set>");
        String templSet = "    {fieldName}{columnName} = #{{fieldName},jdbcType={jdbcType}},";
        String templSetNotNull = "    <if test=\"{fieldName} != null\">{columnName} = #{{fieldName},jdbcType={jdbcType}},</if>";
        for (FieldModel fm : pt.columns()) {
            String fieldName = FieldNameHelper.columnToField(fm.getStr(FieldModel.fieldName));
            String columnName = fm.getStr(FieldModel.fieldName).toUpperCase();
            String jdbcType = SqlTypeMappingHelper.getMybatisJdbcTypeByColumnType(fm.getStr(FieldModel.fieldColType));
            String template = "Y".equals(fm.getStr(FieldModel.isNull)) ? templSet : templSetNotNull;
            String content = FCS.get(template, fieldName, columnName, fieldName, jdbcType).toString();
            pt.contents().add(content);
        }
        pt.contents().add("  </set>");
        pt.contents().add("  <where>");
        String templ = "    AND {columnName} = #{{fieldName},jdbcType={jdbcType}}";
        for (FieldModel fm : pt.pk()) {
            String fieldName = FieldNameHelper.columnToField(fm.getStr(FieldModel.fieldName));
            String columnName = fm.getStr(FieldModel.fieldName).toUpperCase();
            String jdbcType = SqlTypeMappingHelper.getMybatisJdbcTypeByColumnType(fm.getStr(FieldModel.fieldColType));
            String content = FCS.get(templ, columnName, fieldName, jdbcType).toString();
            pt.contents().add(content);
        }
        pt.contents().add("  </where>");
        pt.contents().add("</script>");
    }

    /**
     * 4/9: INSERT INTO * (Column,...) VALUES(?,...)
     *
     * @param pt
     */
    public void mappingSave(ProjectTuple pt) {
        pt.contents().add("<script>");
        pt.contents().add("  INSERT INTO " + pt.table().getStr(BusinessModel.tableName).toUpperCase());
        List<FieldModel> list = new ArrayList<>(pt.pk());
        list.addAll(pt.columns());
        StringBuilder fieldColList = new StringBuilder();
        {// add (ColumnName,...)
            for (FieldModel fm : list) {
                String columnName = fm.getStr(FieldModel.fieldName).toUpperCase();
                fieldColList.append(columnName).append(',');
            }
            if (fieldColList.length() > 0) {
                fieldColList.setLength(fieldColList.length() - 1);
            }
            pt.contents().add("  ( " + fieldColList.toString() + " )");
        }
        {// add VALUES(?,...)
            fieldColList.setLength(0);
            String templ = "#{{fieldName},jdbcType={jdbcType}},";
            for (FieldModel fm : list) {
                String fieldName = FieldNameHelper.columnToField(fm.getStr(FieldModel.fieldName));
                String jdbcType = SqlTypeMappingHelper.getMybatisJdbcTypeByColumnType(fm.getStr(FieldModel.fieldColType));
                String content = FCS.get(templ, fieldName, jdbcType).toString();
                fieldColList.append(content);
            }
            if (fieldColList.length() > 0) {
                fieldColList.setLength(fieldColList.length() - 1);
            }
            pt.contents().add("  VALUES( " + fieldColList.toString() + " )");
        }
        pt.contents().add("</script>");
    }

    /**
     * 5/9: DELETE FROM * WHERE xxx
     *
     * @param pt
     */
    public void mappingRemove(ProjectTuple pt) {
        pt.contents().add("<script>");
        pt.contents().add("  DELETE FROM " + pt.table().getStr(BusinessModel.tableName).toUpperCase());
        pt.contents().add("  <where>");
        String templ = "    AND {columnName} = #{{fieldName},jdbcType={jdbcType}}";
        for (FieldModel fm : pt.pk()) {
            String fieldName = FieldNameHelper.columnToField(fm.getStr(FieldModel.fieldName));
            String columnName = fm.getStr(FieldModel.fieldName).toUpperCase();
            String jdbcType = SqlTypeMappingHelper.getMybatisJdbcTypeByColumnType(fm.getStr(FieldModel.fieldColType));
            String content = FCS.get(templ, columnName, fieldName, jdbcType).toString();
            pt.contents().add(content);
        }
        pt.contents().add("  </where>");
        pt.contents().add("</script>");
    }

    /**
     * 6/9: for domain event service: index
     *
     * @param pt
     */
    public void mappingIndex(ProjectTuple pt) {
        mappingFind(pt);
    }

    /**
     * 7/9: for domain event service: show
     *
     * @param pt
     */
    public void mappingShow(ProjectTuple pt) {
        mappingGet(pt);
    }

    /**
     * 8/9: for domain event service: create
     *
     * @param pt
     */
    public void mappingCreate(ProjectTuple pt) {
        mappingSave(pt);
    }

    /**
     * 9/9: for domain event service: destroy
     *
     * @param pt
     */
    public void mappingDestroy(ProjectTuple pt) {
        mappingRemove(pt);
    }

    /**
     * 1/11: SET xxx, call once, at start
     *
     * @param pt
     */
    public void tableRestrict(ProjectTuple pt) {
        if (pt.contents().isEmpty()) {
            pt.contents().add("SET CHARACTER SET UTF8;");
            pt.contents().add("SET FOREIGN_KEY_CHECKS = 0;");
        }
    }

    /**
     * 2/11: DROP TABLE IF EXISTS xxx
     *
     * @param pt
     */
    public void tableDrop(ProjectTuple pt) {
        pt.contents().add("DROP TABLE IF EXISTS " + pt.table().getStr(BusinessModel.tableName).toUpperCase() + ";");
    }

    /**
     * 3/11: CREATE TABLE xxx
     *
     * @param pt
     */
    public void tableCreate(ProjectTuple pt) {
        pt.contents().add("CREATE TABLE " + pt.table().getStr(BusinessModel.tableName).toUpperCase() + "(");
    }

    /**
     * 4/11: Generate Primary Key column
     *
     * @param pt
     */
    public void tablePk(ProjectTuple pt) {
        for (FieldModel fm : (List<FieldModel>) pt.pk()) {
            String content = "    {col} {colType}{colLen}{colScale} {colNull} {colAutoPk} COMMENT {colComment},";
            String fieldColType = fm.getStr(FieldModel.fieldColType).toUpperCase();
            String fieldScale = fm.getStr(FieldModel.fieldScale);
            boolean isStr = "String".equals(fm.getStr(FieldModel.fieldJavaType));
            boolean isScale = "DECIMAL".equals(fieldColType) || "NUMERIC".equals(fieldColType);
            int len = NumberHelper.getInt(fm.getStr(FieldModel.fieldLength), 0);
            boolean hasScale = StringUtils.isNotBlank(fieldScale);
            boolean lenAble = (isStr || isScale) && len > 0;
            String colLen = (lenAble && !hasScale) ? ("(" + len + ")") : "";
            String colScale = (lenAble && isScale && hasScale) ? ("(" + len + "," + fieldScale + ")") : "";
            content = FCS.get(content,
                    /*col*/fm.getStr(FieldModel.fieldName).toUpperCase(),
                    /*colType*/fieldColType,
                    /*colLen*/colLen,
                    /*colScale*/colScale,
                    /*colNull*/"Y".equals(fm.getStr(FieldModel.isNull)) ? "" : "NOT NULL",
                    /*colAutoPk*/"Y".equals(fm.getStr(FieldModel.isAutoIncrease)) ? "AUTO_INCREMENT" : "",
                    /*colComment*/(String) fm.getOrDefault(FieldModel.fieldDesc, fm.getOrDefault(FieldModel.fieldAlias, fm.get(FieldModel.fieldColumn)))
            ).toString();
            pt.contents().add(content);
        }
    }

    /**
     * 5/11: Generate normal column (not-include pk)
     *
     * @param pt
     */
    public void tableColumn(ProjectTuple pt) {
        for (FieldModel fm : (List<FieldModel>) pt.columns()) {
            String content = "    {col} {colType}{colLen}{colScale} {colNull} {colAutoPk} {colDefault} COMMENT {colComment},";
            String fieldColType = fm.getStr(FieldModel.fieldColType).toUpperCase();
            String fieldScale = fm.getStr(FieldModel.fieldScale);
            String fieldJavaType = fm.getStr(FieldModel.fieldJavaType);
            boolean isStr = "String".equals(fieldJavaType);
            boolean isScale = "DECIMAL".equals(fieldColType) || "NUMERIC".equals(fieldColType);
            int len = NumberHelper.getInt(fm.getStr(FieldModel.fieldLength), 0);
            boolean hasScale = StringUtils.isNotBlank(fieldScale);
            boolean lenAble = (isStr || isScale) && len > 0;
            String colLen = (lenAble && !hasScale) ? ("(" + len + ")") : "";
            String colScale = (lenAble && isScale && hasScale) ? ("(" + len + "," + fieldScale + ")") : "";
            String defVal = StringUtils.defaultString(fm.getStr(FieldModel.fieldValue));
            boolean hasDef = StringUtils.isNotBlank(defVal);
            String colDefault = (hasDef && "NULL".equals(defVal)) ? "DEFAULT NULL" : "";
            boolean isNumberType = "|Integer|Long|Double|BigDecimal|Boolean|Float|Short|Byte|byte[]|".contains(fieldJavaType);
            colDefault = (hasDef && !"NULL".equals(defVal)) ? ("DEFAULT " + (isNumberType ? defVal : ("'" + defVal + "'"))) : "";
            content = FCS.get(content,
                    /*col*/fm.getStr(FieldModel.fieldName).toUpperCase(),
                    /*colType*/fieldColType,
                    /*colLen*/colLen,
                    /*colScale*/colScale,
                    /*colNull*/"Y".equals(fm.getStr(FieldModel.isNull)) ? "" : "NOT NULL",
                    /*colAutoPk*/"Y".equals(fm.getStr(FieldModel.isAutoIncrease)) ? "AUTO_INCREMENT" : "",
                    /*colDefault*/colDefault,
                    /*colComment*/(String) fm.getOrDefault(FieldModel.fieldDesc, fm.getOrDefault(FieldModel.fieldAlias, fm.get(FieldModel.fieldColumn)))
            ).toString();
            pt.contents().add(content);
        }
    }

    /**
     * 6/11: PRIMARY KEY xxx, 设置主键
     *
     * @param pt
     */
    public void tablePkIndex(ProjectTuple pt) {
        for (FieldModel fm : pt.pk()) {
            String content = "    PRIMARY KEY ({column}),";
            content = FCS.get(content, fm.getStr(FieldModel.fieldName).toUpperCase()).toString();
            pt.contents().add(content);
        }
    }

    /**
     * 7/11: KEY xxx, 设置索引
     *
     * @param pt
     */
    public void tableColumnIndex(ProjectTuple pt) {
        for (FieldModel fm : pt.index()) {
            String content = "    INDEX ({column}),";
            content = FCS.get(content, fm.getStr(FieldModel.fieldName).toUpperCase()).toString();
            pt.contents().add(content);
        }
    }

    /**
     * 8/11: UNIQUE KEY xxx, 设置唯一约束
     *
     * @param pt
     */
    public void tableUniqueIndex(ProjectTuple pt) {
        for (FieldModel fm : pt.unique()) {
            String content = "    UNIQUE KEY ({column}),";
            content = FCS.get(content, fm.getStr(FieldModel.fieldName).toUpperCase()).toString();
            pt.contents().add(content);
        }
    }

    /**
     * 9/11: FOREIGN KEY xxx REFERENCES xxx, 设置外键
     *
     * @param pt
     */
    public void tableForeignKey(ProjectTuple pt) {
        for (FieldModel fm : pt.foreign()) {
            String content = "    FOREIGN KEY ({column}) REFERENCES {refTable} ({refField}),";
            content = FCS.get(content,
                    fm.getStr(FieldModel.fieldName).toUpperCase(),
                    fm.getStr(FieldModel.refTable).toUpperCase(),
                    fm.getStr(FieldModel.refField).toUpperCase()
            ).toString();
            pt.contents().add(content);
        }
    }

    /**
     * 10/11: ENGINE=InnoDB DEFAULT
     *
     * @param pt
     */
    public void tableCreateClose(ProjectTuple pt) {
        String removeComma = pt.contents().get(pt.contents().size() - 1);
        pt.contents().set(pt.contents().size() - 1, StringUtils.removeEnd(removeComma, ","));
        pt.contents().add(") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='" + StringUtils.defaultString(pt.table().getStr(BusinessModel.name), pt.table().getStr(BusinessModel.tableName)) + "';\n");
    }

    /**
     * 11/11: SET xxx, call once, must be sure call at end.
     *
     * @param pt
     */
    public void tableRestrictClose(ProjectTuple pt) {
        pt.contents().add("SET FOREIGN_KEY_CHECKS = 1;");
    }

    public static class Tuple extends org.iff.jgit.util.Tuple.Two</*0:result*/Object,
            /*1:action*/String,
            /*2:namespace*/ProjectTuple
            > {
        public Tuple(String action, ProjectTuple projectTuple) {
            super(action, projectTuple);
        }

        public static Tuple tableRestrict(ProjectTuple pt) {
            return new Tuple(ACTION_tableRestrict, pt);
        }

        public static Tuple tableDrop(ProjectTuple pt) {
            return new Tuple(ACTION_tableDrop, pt);
        }

        public static Tuple tableCreate(ProjectTuple pt) {
            return new Tuple(ACTION_tableCreate, pt);
        }

        public static Tuple tablePk(ProjectTuple pt) {
            return new Tuple(ACTION_tablePk, pt);
        }

        public static Tuple tableColumn(ProjectTuple pt) {
            return new Tuple(ACTION_tableColumn, pt);
        }

        public static Tuple tablePkIndex(ProjectTuple pt) {
            return new Tuple(ACTION_tablePkIndex, pt);
        }

        public static Tuple tableColumnIndex(ProjectTuple pt) {
            return new Tuple(ACTION_tableColumnIndex, pt);
        }

        public static Tuple tableUniqueIndex(ProjectTuple pt) {
            return new Tuple(ACTION_tableUniqueIndex, pt);
        }

        public static Tuple tableForeignKey(ProjectTuple pt) {
            return new Tuple(ACTION_tableForeignKey, pt);
        }

        public static Tuple tableCreateClose(ProjectTuple pt) {
            return new Tuple(ACTION_tableCreateClose, pt);
        }

        public static Tuple tableRestrictClose(ProjectTuple pt) {
            return new Tuple(ACTION_tableRestrictClose, pt);
        }

        public static Tuple mappingGet(ProjectTuple pt) {
            return new Tuple(ACTION_mappingGet, pt);
        }

        public static Tuple mappingFind(ProjectTuple pt) {
            return new Tuple(ACTION_mappingFind, pt);
        }

        public static Tuple mappingUpdate(ProjectTuple pt) {
            return new Tuple(ACTION_mappingUpdate, pt);
        }

        public static Tuple mappingSave(ProjectTuple pt) {
            return new Tuple(ACTION_mappingSave, pt);
        }

        public static Tuple mappingRemove(ProjectTuple pt) {
            return new Tuple(ACTION_mappingRemove, pt);
        }

        public static Tuple mappingIndex(ProjectTuple pt) {
            return new Tuple(ACTION_mappingIndex, pt);
        }

        public static Tuple mappingShow(ProjectTuple pt) {
            return new Tuple(ACTION_mappingShow, pt);
        }

        public static Tuple mappingCreate(ProjectTuple pt) {
            return new Tuple(ACTION_mappingCreate, pt);
        }

        public static Tuple mappingDestroy(ProjectTuple pt) {
            return new Tuple(ACTION_mappingDestroy, pt);
        }

        public String action() {
            return first();
        }

        public ProjectTuple projectTuple() {
            return second();
        }
    }
}

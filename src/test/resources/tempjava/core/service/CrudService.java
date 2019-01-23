/*******************************************************************************
 * Copyright (c) 2018-11-21 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core.service;

import org.apache.commons.lang3.StringUtils;
import org.iff.jgit.util.Tuple;
import org.iff.infra.util.EventBusHelper;

import java.util.ArrayList;
import java.util.List;

import static org.iff.jgit.core.model.CrudModel.*;

/**
 * CrudService
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-21
 * auto generate by qdp.
 */
public class CrudService {

    static List<FieldModel> filter(List<FieldModel> fields, String fieldName, String expectValue, boolean isEqual) {
        List<FieldModel> list = new ArrayList<>();
        for (FieldModel fm : fields) {
            if (isEqual == expectValue.equals(fm.getStr(fieldName))) {
                list.add(fm);
            }
        }
        return list;
    }

    static List<FieldModel> foreign(List<FieldModel> fields) {
        List<FieldModel> list = new ArrayList<>();
        for (FieldModel fm : fields) {
            if (StringUtils.isNotBlank(fm.getStr(FieldModel.refTable))
                    && StringUtils.isNotBlank(fm.getStr(FieldModel.refField))
                    && StringUtils.isNotBlank(fm.getStr(FieldModel.refLabelField))) {
                list.add(fm);
            }
        }
        return list;
    }

    static interface Compare {
        boolean compare(String value);
    }

    public static class GenMappedSql {
        public String gen(ProjectModel projectModel) {
            ArrayList<String> contents = new ArrayList<>();
            List<ModuleModel> modules = projectModel.modules();
            for (ModuleModel module : modules) {
                List<BusinessModel> businesses = module.businesses();
                for (BusinessModel business : businesses) {
                    List<FunctionModel> functions = business.functions();
                    for (FunctionModel function : functions) {
                        String type = function.getStr(FunctionModel.type);
                        if (!("grid-model".equals(type) || "tree-model".equals(type))) {
                            continue;
                        }
                        List<FieldModel> fields = function.fields();
                        ProjectTuple pt = new ProjectTuple(projectModel, module, business, function
                                , filter(fields, FieldModel.isPk, "Y", true)
                                , filter(fields, FieldModel.isNull, "Y", true)
                                , filter(fields, FieldModel.isIndex, "Y", true)
                                , filter(fields, FieldModel.isUnique, "Y", true)
                                , filter(filter(fields, FieldModel.isNonTableColumn, "Y", false), FieldModel.isPk, "Y", false)
                                , foreign(fields)
                                , contents);
                        /*1/9: SELECT * FROM xxx WHERE*/
                        EventBusHelper.me().syncEvent(MySQLGenerator.EVENT_NAME, MySQLGenerator.Tuple.mappingGet(pt));
                        /*2/9: SELECT * FROM xxx WHERE xxx ORDER BY xxx*/
                        EventBusHelper.me().syncEvent(MySQLGenerator.EVENT_NAME, MySQLGenerator.Tuple.mappingFind(pt));
                        /*3/9: UPDATE * SET xxx WHERE xx*/
                        EventBusHelper.me().syncEvent(MySQLGenerator.EVENT_NAME, MySQLGenerator.Tuple.mappingUpdate(pt));
                        /*4/9: INSERT INTO * (Column,...) VALUES(?,...)*/
                        EventBusHelper.me().syncEvent(MySQLGenerator.EVENT_NAME, MySQLGenerator.Tuple.mappingSave(pt));
                        /*5/9: DELETE FROM * WHERE xxx*/
                        EventBusHelper.me().syncEvent(MySQLGenerator.EVENT_NAME, MySQLGenerator.Tuple.mappingRemove(pt));
                        /*6/9: for domain event service: index*/
                        EventBusHelper.me().syncEvent(MySQLGenerator.EVENT_NAME, MySQLGenerator.Tuple.mappingIndex(pt));
                        /*7/9: for domain event service: show*/
                        EventBusHelper.me().syncEvent(MySQLGenerator.EVENT_NAME, MySQLGenerator.Tuple.mappingShow(pt));
                        /*8/9: for domain event service: create*/
                        EventBusHelper.me().syncEvent(MySQLGenerator.EVENT_NAME, MySQLGenerator.Tuple.mappingCreate(pt));
                        /*9/9: for domain event service: destroy*/
                        EventBusHelper.me().syncEvent(MySQLGenerator.EVENT_NAME, MySQLGenerator.Tuple.mappingDestroy(pt));
                    }
                }
            }
            return StringUtils.join(contents, "\n");
        }
    }

    public static class GenTable {
        public String gen(ProjectModel projectModel) {
            ArrayList<String> contents = new ArrayList<>();
            List<ModuleModel> modules = projectModel.modules();
            for (ModuleModel module : modules) {
                List<BusinessModel> businesses = module.businesses();
                for (BusinessModel business : businesses) {
                    List<FunctionModel> functions = business.functions();
                    for (FunctionModel function : functions) {
                        String type = function.getStr(FunctionModel.type);
                        if (!("grid-model".equals(type) || "tree-model".equals(type))) {
                            continue;
                        }
                        List<FieldModel> fields = function.fields();
                        ProjectTuple pt = new ProjectTuple(projectModel, module, business, function
                                , filter(fields, FieldModel.isPk, "Y", true)
                                , filter(fields, FieldModel.isNull, "Y", true)
                                , filter(fields, FieldModel.isIndex, "Y", true)
                                , filter(fields, FieldModel.isUnique, "Y", true)
                                , filter(filter(fields, FieldModel.isNonTableColumn, "Y", false), FieldModel.isPk, "Y", false)
                                , foreign(fields)
                                , contents);
                        /*1/11: SET xxx*/
                        EventBusHelper.me().syncEvent(MySQLGenerator.EVENT_NAME, MySQLGenerator.Tuple.tableRestrict(pt));
                        /*2/11: DROP TABLE IF EXISTS xxx*/
                        EventBusHelper.me().syncEvent(MySQLGenerator.EVENT_NAME, MySQLGenerator.Tuple.tableDrop(pt));
                        /*3/11: CREATE TABLE xxx*/
                        EventBusHelper.me().syncEvent(MySQLGenerator.EVENT_NAME, MySQLGenerator.Tuple.tableCreate(pt));
                        /*4/11: Generate Primary Key column*/
                        EventBusHelper.me().syncEvent(MySQLGenerator.EVENT_NAME, MySQLGenerator.Tuple.tablePk(pt));
                        /*5/11: Generate normal column (not-include pk)*/
                        EventBusHelper.me().syncEvent(MySQLGenerator.EVENT_NAME, MySQLGenerator.Tuple.tableColumn(pt));
                        /*6/11: PRIMARY KEY xxx, 设置主键*/
                        EventBusHelper.me().syncEvent(MySQLGenerator.EVENT_NAME, MySQLGenerator.Tuple.tablePkIndex(pt));
                        /*7/11: KEY xxx, 设置索引*/
                        EventBusHelper.me().syncEvent(MySQLGenerator.EVENT_NAME, MySQLGenerator.Tuple.tableColumnIndex(pt));
                        /*8/11: UNIQUE KEY xxx, 设置唯一约束*/
                        EventBusHelper.me().syncEvent(MySQLGenerator.EVENT_NAME, MySQLGenerator.Tuple.tableUniqueIndex(pt));
                        /*9/11: FOREIGN KEY xxx REFERENCES xxx, 设置外键*/
                        EventBusHelper.me().syncEvent(MySQLGenerator.EVENT_NAME, MySQLGenerator.Tuple.tableForeignKey(pt));
                        /*10/11: ENGINE=InnoDB DEFAULT*/
                        EventBusHelper.me().syncEvent(MySQLGenerator.EVENT_NAME, MySQLGenerator.Tuple.tableCreateClose(pt));
                        if (business == businesses.get(business.size() - 1)) {
                            /*11/11: SET xxx, must be sure call at end.*/
                            EventBusHelper.me().syncEvent(MySQLGenerator.EVENT_NAME, MySQLGenerator.Tuple.tableRestrictClose(pt));
                        }
                    }
                }
            }
            return StringUtils.join(contents, "\n");
        }
    }

    public static class ProjectTuple extends Tuple.Eleven<Object, ProjectModel, ModuleModel, BusinessModel, FunctionModel, List<FieldModel>, List<FieldModel>, List<FieldModel>, List<FieldModel>, List<FieldModel>, List<FieldModel>, List<String>> {

        public ProjectTuple(ProjectModel project, ModuleModel module, BusinessModel table, FunctionModel function, List<FieldModel> pk, List<FieldModel> notNull, List<FieldModel> index, List<FieldModel> unique, List<FieldModel> columns, List<FieldModel> foreign, List<String> contents) {
            super(project, module, table, function, pk, notNull, index, unique, columns, foreign, contents);
        }

        public ProjectModel project() {
            return first();
        }

        public ModuleModel module() {
            return second();
        }

        public BusinessModel table() {
            return third();
        }

        public FunctionModel function() {
            return fourth();
        }

        public List<FieldModel> pk() {
            return fifth();
        }

        public List<FieldModel> notNull() {
            return sixth();
        }

        public List<FieldModel> index() {
            return seventh();
        }

        public List<FieldModel> unique() {
            return eighth();
        }

        public List<FieldModel> columns() {
            return ninth();
        }

        public List<FieldModel> foreign() {
            return tenth();
        }

        public List<String> contents() {
            return eleventh();
        }
    }
}

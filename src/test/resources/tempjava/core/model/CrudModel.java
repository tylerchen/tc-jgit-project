/*******************************************************************************
 * Copyright (c) 2018-11-18 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core.model;

import org.apache.commons.lang3.StringUtils;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * CrudModel
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-18
 * auto generate by qdp.
 */
public class CrudModel extends DomainModel<CrudModel> {
    @Override
    public CrudModel newInstance() {
        return getClass() == CrudModel.class ? new CrudModel() : super.newInstance();
    }

    public static class ProjectModel extends DomainModel<ProjectModel> {
        public static final String id = "id";
        public static final String groupId = "groupId";
        public static final String artifactId = "artifactId";
        public static final String version = "version";
        public static final String templateVersion = "templateVersion";
        public static final String modules = "modules";

        @Override
        public ProjectModel newInstance() {
            return getClass() == ProjectModel.class ? new ProjectModel() : super.newInstance();
        }

        public List<ModuleModel> modules() {
            List<? extends Map<String, Object>> moduleModels = (List<? extends Map<String, Object>>) get(modules);
            if (moduleModels == null) {
                put(modules, (moduleModels = new ArrayList<>()));
            } else if (moduleModels.size() > 0 && !(moduleModels.get(0) instanceof ModuleModel)) {
                List<ModuleModel> newList = new ArrayList<>();
                for (Map<String, Object> map : moduleModels) {
                    newList.add(new ModuleModel().fromMap(map));
                }
                put(modules, (moduleModels = newList));
            }
            return (List<ModuleModel>) moduleModels;
        }
    }

    public static class ModuleModel extends DomainModel<ModuleModel> {
        public static final String id = "id";
        public static final String name = "name";
        public static final String packageName = "packageName";
        public static final String alias = "alias";
        public static final String businesses = "businesses";

        @Override
        public ModuleModel newInstance() {
            return getClass() == ModuleModel.class ? new ModuleModel() : super.newInstance();
        }

        public List<BusinessModel> businesses() {
            List<? extends Map<String, Object>> businessModels = (List<? extends Map<String, Object>>) get(businesses);
            if (businessModels == null) {
                put(businesses, (businessModels = new ArrayList<>()));
            } else if (businessModels.size() > 0 && !(businessModels.get(0) instanceof BusinessModel)) {
                List<BusinessModel> newList = new ArrayList<>();
                for (Map<String, Object> map : businessModels) {
                    newList.add(new BusinessModel().fromMap(map));
                }
                put(businesses, (businessModels = newList));
            }
            return (List<BusinessModel>) businessModels;
        }
    }

    public static class BusinessModel extends DomainModel<BusinessModel> {
        public static final String id = "id";
        public static final String name = "name";
        public static final String packageName = "packageName";
        public static final String alias = "alias";
        public static final String tableName = "tableName";
        public static final String functions = "functions";
        public static final String actions = "actions";

        @Override
        public BusinessModel newInstance() {
            return getClass() == BusinessModel.class ? new BusinessModel() : super.newInstance();
        }

        public List<FunctionModel> functions() {
            List<? extends Map<String, Object>> functionModels = (List<? extends Map<String, Object>>) get(functions);
            if (functionModels == null) {
                put(functions, (functionModels = new ArrayList<>()));
            } else if (functionModels.size() > 0 && !(functionModels.get(0) instanceof FunctionModel)) {
                List<FunctionModel> newList = new ArrayList<>();
                for (Map<String, Object> map : functionModels) {
                    newList.add(new FunctionModel().fromMap(map));
                }
                put(functions, (functionModels = newList));
            }
            return (List<FunctionModel>) functionModels;
        }

        public Map<String, ActionModel> actions() {
            Map<String, ? extends Map<String, Object>> actionModels = (Map<String, ? extends Map<String, Object>>) get(actions);
            if (actionModels == null) {
                put(actions, (actionModels = new LinkedHashMap<>()));
            } else if (actionModels.size() > 0 && !(actionModels.values().iterator().next() instanceof ActionModel)) {
                Map<String, ActionModel> newMap = new LinkedHashMap<>();
                for (Map.Entry<String, ? extends Map<String, Object>> entry : actionModels.entrySet()) {
                    newMap.put(entry.getKey(), new ActionModel().fromMap(entry.getValue()));
                }
                put(actions, newMap);
            }
            return (Map<String, ActionModel>) actionModels;
        }
    }

    public static class FunctionModel extends DomainModel<FunctionModel> {
        public static final String id = "id";
        public static final String name = "name";
        public static final String fileName = "fileName";
        public static final String alias = "alias";
        public static final String type = "type";
        public static final String fields = "fields";

        @Override
        public FunctionModel newInstance() {
            return getClass() == FunctionModel.class ? new FunctionModel() : super.newInstance();
        }

        @Transient
        public boolean isPage() {
            return StringUtils.contains(getStr(type), "page");
        }

        public List<FieldModel> fields() {
            List<? extends Map<String, Object>> fieldModels = (List<? extends Map<String, Object>>) get(fields);
            if (fieldModels == null) {
                put(fields, (fieldModels = new ArrayList<>()));
            } else if (fieldModels.size() > 0 && !(fieldModels.get(0) instanceof FieldModel)) {
                List<FieldModel> newList = new ArrayList<>();
                for (Map<String, Object> map : fieldModels) {
                    newList.add(new FieldModel().fromMap(map));
                }
                put(fields, (fieldModels = newList));
            }
            return (List<FieldModel>) fieldModels;
        }
    }

    public static class ActionModel extends DomainModel<ActionModel> {
        public static final String id = "id";
        public static final String actionName = "actionName";
        public static final String eventName = "eventName";
        public static final String actionType = "actionType";
        public static final String shortCut = "shortCut";
        public static final String actionIcon = "actionIcon";

        @Override
        public ActionModel newInstance() {
            return getClass() == ActionModel.class ? new ActionModel() : super.newInstance();
        }
    }

    public static class FieldModel extends DomainModel<FieldModel> {
        public static final String id = "id";
        public static final String fieldName = "fieldName";
        public static final String fieldAlias = "fieldAlias";
        public static final String fieldColumn = "fieldColumn";
        public static final String fieldColType = "fieldColType";
        public static final String fieldJavaType = "fieldJavaType";
        public static final String fieldLength = "fieldLength";
        public static final String fieldScale = "fieldScale";
        public static final String fieldValue = "fieldValue";
        public static final String fieldDesc = "fieldDesc";
        public static final String fieldSeq = "fieldSeq";
        public static final String isNull = "isNull";
        public static final String isPk = "isPk";
        public static final String isIndex = "isIndex";
        public static final String isUnique = "isUnique";
        public static final String isAutoIncrease = "isAutoIncrease";
        public static final String isNonTableColumn = "isNonTableColumn";
        public static final String ableAdd = "ableAdd";
        public static final String ableEdit = "ableEdit";
        public static final String ableList = "ableList";
        public static final String ableInfo = "ableInfo";
        public static final String ableSearch = "ableSearch";
        public static final String ableSort = "ableSort";
        public static final String htmlWidth = "htmlWidth";
        public static final String htmlWordbreak = "htmlWordbreak";
        public static final String dataUrl = "dataUrl";
        public static final String dataJson = "dataJson";
        public static final String dataJsonSelect = "dataJsonSelect";
        public static final String typeAll = "typeAll";
        public static final String typeAdd = "typeAdd";
        public static final String typeEdit = "typeEdit";
        public static final String typeGrid = "typeGrid";
        public static final String typeInfo = "typeInfo";
        public static final String typeSearch = "typeSearch";
        public static final String refTable = "refTable";
        public static final String refField = "refField";
        public static final String refLabelField = "refLabelField";
        public static final String midTable = "midTable";
        public static final String midMainField = "midMainField";
        public static final String midSecondField = "midSecondField";
        public static final String uniqueFields = "uniqueFields";
        public static final String ruleType = "ruleType";
        public static final String ruleRequired = "ruleRequired";
        public static final String ruleRequiredMsg = "ruleRequiredMsg";
        public static final String ruleRegex = "ruleRegex";
        public static final String ruleRegexMsg = "ruleRegexMsg";
        public static final String ruleRangeMin = "ruleRangeMin";
        public static final String ruleRangeMax = "ruleRangeMax";
        public static final String ruleRangeMsg = "ruleRangeMsg";
        public static final String ruleLength = "ruleLength";
        public static final String ruleLengthMsg = "ruleLengthMsg";
        public static final String ruleEnums = "ruleEnums";
        public static final String ruleEnumsMsg = "ruleEnumsMsg";
        public static final String ruleNotBlank = "ruleNotBlank";
        public static final String ruleNotBlankMsg = "ruleNotBlankMsg";
        public static final String exJsonData = "exJsonData";

        @Override
        public FieldModel newInstance() {
            return getClass() == FieldModel.class ? new FieldModel() : super.newInstance();
        }
    }
}

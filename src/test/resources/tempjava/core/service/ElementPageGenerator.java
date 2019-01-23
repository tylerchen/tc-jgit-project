/*******************************************************************************
 * Copyright (c) 2018-12-03 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core.service;

import org.apache.commons.lang3.StringUtils;
import org.iff.jgit.util.FieldNameHelper;
import org.iff.infra.util.*;

import java.util.*;

import static org.iff.jgit.core.model.CrudModel.*;

/**
 * ElementPageGenerator
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-12-03
 * auto generate by qdp.
 */
public class ElementPageGenerator implements EventBusHelper.EventProcess {
    public static final String EVENT_NAME = ElementPageGenerator.class.getSimpleName();


    public String getName() {
        return EVENT_NAME;
    }

    public void listen(String event, Object args) {
    }

    /**
     * 1/9: Index grid page
     *
     * @param pt
     */
    public void indexGrid(CrudService.ProjectTuple pt) {
        String className = FieldNameHelper.tableToClass(pt.table().getStr(BusinessModel.tableName));
        String uncapClassName = StringUtils.uncapitalize(className);
        List<FieldModel> allFields = new ArrayList<>(pt.pk());
        allFields.addAll(pt.columns());
        int count = 0;
        pt.contents().add("<template><!-- template-1.0.0 -->");
        pt.contents().add("  <div class=\"qdp-list-layout\">");
        pt.contents().add("  <!-- 搜索 -->");
        for (FieldModel fm : allFields) {
            if (!"Y".equals(fm.getStr(FieldModel.ableSearch))) {
                continue;
            }
            if ((count += 1) == 1) {
                pt.contents().add("  <el-row class=\"qdp-list-content\" type=\"flex\" justify=\"start\" align=\"top\">");
                pt.contents().add("    <el-form class=\"qdp-search-form\" :model=\"searchForm\" label-width=\"100px\" inline ref=\"searchForm\" @keydown.13.native.stop=\"onPost('searchForm', 'search')\">");
            }
            pt.contents().add("      <@searchForm field=field/>");
        }
        if (count > 0) {
            pt.contents().add("      <el-form-item>");
            pt.contents().add("        <el-button size=\"small\" type=\"info\" @click=\"onPost('searchForm','reset')\"><i class=\"el-icon-ion-backspace-outline\"></i></el-button>");
            pt.contents().add("        <el-button size=\"small\" type=\"info\" @click=\"onPost('searchForm','search')\"><i class=\"el-icon-ion-search\"></i></el-button>");
            pt.contents().add("      </el-form-item>");
            pt.contents().add("    </el-form>");
            pt.contents().add("  </el-row>");
        }
        pt.contents().add("  <!-- 搜索-END -->");
        pt.contents().add("  <!-- 操作栏 -->");
        count = 0;
        Map<String, ActionModel> actions = pt.table().actions();
        for (Map.Entry<String, ActionModel> entry : actions.entrySet()) {
            if ((count += 1) == 1) {
                pt.contents().add("  <el-row class=\"qdp-list-content qdp-el-action\" type=\"flex\" justify=\"end\" align=\"top\">");
            }
            String actionName = entry.getValue().getStr(ActionModel.actionName);
            String shortCut = entry.getValue().getStr(ActionModel.shortCut);
            shortCut = StringUtils.isBlank(shortCut) ? "" : ("(" + shortCut + ")");
            String eventName = entry.getValue().getStr(ActionModel.eventName);
            String actionIcon = entry.getValue().getStr(ActionModel.actionIcon);
            pt.contents().add("  <el-button size=\"small\" type=\"text\" title=\"" + actionName + shortCut + "\"    @click=\"onPost('action-bar','" + eventName + "')\"><i class=\"" + actionIcon + "\"></i></el-button>");
        }
        if (count > 0) {
            pt.contents().add("  </el-row>");
        }
        pt.contents().add("  <!-- 操作栏-END- -->");
        pt.contents().add("  <!-- 列表 -->");
        count = 0;
        for (FieldModel fm : allFields) {
            if (!"Y".equals(fm.getStr(FieldModel.ableList))) {
                continue;
            }
            if ((count += 1) == 1) {
                pt.contents().add("  <el-row class=\"qdp-list-content qdp-grid\" type=\"flex\" justify=\"center\" align=\"top\">");
                pt.contents().add("    <el-table :data=\"grid.data.rows\" style=\"width: 100%;\" border highlight-current-row ref=\"grid\" height=\"auto\" stripe @selection-change=\"onPost('grid-table', 'selection-change', arguments[0])\" @row-click=\"onPost('grid-table', 'row-click', arguments[0], arguments[1], arguments[2])\" @sort-change=\"onPost('grid-table', 'sort-change', arguments[0])\">");
                pt.contents().add("      <el-table-column type=\"selection\" width=\"35\"></el-table-column>");
            }
            String javaField = FieldNameHelper.columnToField(fm.getStr(FieldModel.fieldName));
            String javaFieldEnName = FieldNameHelper.tableToClass(StringUtils.defaultString(fm.getStr(FieldModel.fieldAlias), StringUtils.defaultString(fm.getStr(FieldModel.fieldDesc), javaField)));
            if ("radio|checkbox|switch|select".contains(fm.getStr(FieldModel.typeGrid))) {
                pt.contents().add("        <el-table-column :fixed=\"true\" prop=\"" + javaField + "\" label=\"" + javaFieldEnName + "\" width=\"100\">");
                pt.contents().add("          <template scope=\"scope\"><el-tag>{{getOption(scope.row, scope.column)}}</el-tag></template>");
                pt.contents().add("        </el-table-column>");
            } else if ("modal".equals(fm.getStr(FieldModel.typeAdd))) {
                boolean isForeign = StringUtils.isNotBlank(fm.getStr(FieldModel.refField)) && StringUtils.isNotBlank(fm.getStr(FieldModel.refTable)) && StringUtils.isNotBlank(fm.getStr(FieldModel.refLabelField));
                if (isForeign) {
                    pt.contents().add("        <el-table-column :fixed=\"true\" prop=\"" + javaField + "\" label=\"" + javaFieldEnName + "\">");
                    if ("Y".equals(fm.getStr(FieldModel.isNonTableColumn))) {
                        pt.contents().add("          <template scope=\"scope\"><el-tag v-for=\"value in split(scope.row, scope.column.property+'Name')\" v-if=\"value\" :key=\"value\">{{value}}</el-tag></template>");
                    } else {
                        pt.contents().add("          <template scope=\"scope\"><el-tag>{{scope.row[scope.column.property+'Name']||\"\"}}</el-tag></template>");
                    }
                    pt.contents().add("        </el-table-column>");
                }
            } else {
                pt.contents().add("        <el-table-column :fixed=\"true\" prop=\"" + javaField + "\" label=\"" + javaFieldEnName + "\" :sortable=\"" + ("Y".equals(fm.getStr(FieldModel.ableSort)) ? "true" : "false") + "\"></el-table-column>");
            }
        }
        if (count > 0) {
            pt.contents().add("    </el-table>");
            pt.contents().add("  </el-row>");
        }
        pt.contents().add("  <!-- 列表-END -->");
        pt.contents().add("  <!-- 分页 -->");
        pt.contents().add("  <el-row class=\"qdp-list-content qdp-page\" type=\"flex\" justify=\"end\" align=\"top\">");
        pt.contents().add("    <el-pagination :current-page=\"grid.data.currentPage\" :total=\"grid.data.totalCount\" :page-size=\"grid.data.pageSize\" :page-sizes=\"[ 5, 10, 20, 50, 100 ]\" layout=\"total, sizes, prev, pager, next, jumper\" @size-change=\"onPost('grid-page', 'size-change', arguments[0])\" @current-change=\"onPost('grid-page', 'current-change', arguments[0])\"></el-pagination>");
        pt.contents().add("  </el-row>");
        pt.contents().add("  <!-- 分页-END -->");
        pt.contents().add("  <!-- 添加 -->");
        count = 0;
        for (FieldModel fm : allFields) {
            if (!"Y".equals(fm.getStr(FieldModel.ableAdd))) {
                continue;
            }
            if ((count += 1) == 1) {
                pt.contents().add("  <el-dialog title=\"添加\" :visible.sync=\"addFormVisible\" :close-on-press-escape=\"false\" :close-on-click-modal=\"false\">");
                pt.contents().add("    <el-row type=\"flex\" justify=\"center\" align=\"top\">");
                pt.contents().add("      <el-form class=\"qdp-add-form\" :model=\"addForm\" :rules=\"addFormRules\" label-width=\"100px\" inline ref=\"addForm\" @keydown.13.native.stop=\"onPost('addForm', 'submit')\">");
            }
            pt.contents().add("        <@genForm field=field formType='add'/>");
        }
        if (count > 0) {
            pt.contents().add("      <el-form>");
            pt.contents().add("    </el-row>");
            pt.contents().add("    <el-row type=\"flex\" justify=\"center\" align=\"top\">");
            pt.contents().add("      <el-button type=\"primary\" @click=\"onPost('addForm','reset') \" icon=\"ion-backspace-outline\">重置</el-button>");
            pt.contents().add("      <el-button type=\"primary\" @click=\"onPost('addForm','submit')\" icon=\"ion-checkmark\">提交</el-button>");
            pt.contents().add("    </el-row>");
            pt.contents().add("  </el-dialog>");
        }
        pt.contents().add("  <!-- 添加-END -->");
        pt.contents().add("  <!-- 修改 -->");
        count = 0;
        for (FieldModel fm : allFields) {
            if (!"Y".equals(fm.getStr(FieldModel.ableEdit))) {
                continue;
            }
            if ((count += 1) == 1) {
                pt.contents().add("  <el-dialog title=\"修改\" :visible.sync=\"editFormVisible\" :close-on-press-escape=\"false\" :close-on-click-modal=\"false\">");
                pt.contents().add("    <el-row type=\"flex\" justify=\"center\" align=\"top\">");
                pt.contents().add("      <el-form class=\"qdp-edit-form\" :model=\"editForm\" :rules=\"editFormRules\" label-width=\"100px\" inline ref=\"editForm\" @keydown.13.native.stop=\"onPost('editForm', 'submit')\">");
            }
            pt.contents().add("        <@genForm field=field formType='edit'/>");
        }
        if (count > 0) {
            pt.contents().add("      <el-form>");
            pt.contents().add("    </el-row>");
            pt.contents().add("    <el-row type=\"flex\" justify=\"center\" align=\"top\">");
            pt.contents().add("      <el-button type=\"primary\" @click=\"onPost('editForm','reset') \" icon=\"ion-backspace-outline\">重置</el-button>");
            pt.contents().add("      <el-button type=\"primary\" @click=\"onPost('editForm','submit')\" icon=\"ion-checkmark\">提交</el-button>");
            pt.contents().add("    </el-row>");
            pt.contents().add("  </el-dialog>");
        }
        pt.contents().add("  <!-- 修改-END -->");
        pt.contents().add("  <!-- 详情 -->");
        count = 0;
        for (FieldModel fm : allFields) {
            if (!"Y".equals(fm.getStr(FieldModel.ableInfo))) {
                continue;
            }
            if ((count += 1) == 1) {
                pt.contents().add("  <el-dialog title=\"详情\" :visible.sync=\"infoFormVisible\">");
                pt.contents().add("    <el-row type=\"flex\" justify=\"center\" align=\"top\" class=\"qdp-info-form\">");
                pt.contents().add("      <table>");
                pt.contents().add("        <tbody>");
            }
            String javaField = FieldNameHelper.columnToField(fm.getStr(FieldModel.fieldName));
            String javaFieldEnName = FieldNameHelper.tableToClass(StringUtils.defaultString(fm.getStr(FieldModel.fieldAlias), StringUtils.defaultString(fm.getStr(FieldModel.fieldDesc), javaField)));
            if ("radio|checkbox|switch|select".contains(fm.getStr(FieldModel.typeGrid))) {
                pt.contents().add("          <tr><td class=\"qdp-infoform-title\">" + javaFieldEnName + "</td><td class=\"qdp-infoform-content\"><div><el-tag>{{getOption(infoForm, '" + javaField + "')}}</el-tag></div></td></tr>");
            } else if ("modal".equals(fm.getStr(FieldModel.typeAdd))) {
                boolean isForeign = StringUtils.isNotBlank(fm.getStr(FieldModel.refField)) && StringUtils.isNotBlank(fm.getStr(FieldModel.refTable)) && StringUtils.isNotBlank(fm.getStr(FieldModel.refLabelField));
                if (isForeign) {
                    if ("Y".equals(fm.getStr(FieldModel.isNonTableColumn))) {
                        pt.contents().add("          <tr><td class=\"qdp-infoform-title\">" + javaFieldEnName + "</td><td class=\"qdp-infoform-content\"><div><el-tag v-for=\"value in split(infoForm, '" + javaField + "Name')\" v-if=\"value\" :key=\"value\">{{value}}</el-tag></div></td></tr>");
                    } else {
                        pt.contents().add("          <tr><td class=\"qdp-infoform-title\">" + javaFieldEnName + "</td><td class=\"qdp-infoform-content\"><div><el-tag>{{infoForm." + javaField + "Name||\"\"}}</el-tag></div></td></tr>");
                    }
                }
            } else {
                pt.contents().add("          <tr><td class=\"qdp-infoform-title\">" + javaFieldEnName + "</td><td class=\"qdp-infoform-content\"><div>{{infoForm." + javaField + "}}</div></td></tr>");
            }
        }
        if (count > 0) {
            pt.contents().add("        </tbody>");
            pt.contents().add("      </table>");
            pt.contents().add("    </el-row>");
            pt.contents().add("  </el-dialog>");
        }
        pt.contents().add("  <!-- 详情-END -->");
        for (FunctionModel fum : pt.table().functions()) {
            if (!"form".equals(fum.getStr(FunctionModel.type))) {
                continue;
            }
            String formName = StringUtils.defaultString(fum.getStr(FunctionModel.alias), fum.getStr(FunctionModel.name));
            String fileName = StringUtils.defaultString(fum.getStr(FunctionModel.fileName));
            pt.contents().add("  <!-- 弹出表单: " + formName + " --><#--如果是外置表单，则使用弹出窗-->");
            pt.contents().add("  <el-dialog class=\"qdp-form-dialog\" title=\"" + formName + "\" :visible.sync=\"" + fileName + "Visible\" v-if=\"" + fileName + "Visible\">");
            pt.contents().add("    <el-row type=\"flex\" justify=\"center\" align=\"top\">");
            pt.contents().add("      <el-col :span=\"24\">");
            pt.contents().add("        <" + StringUtils.capitalize(fileName.toLowerCase()) + " v-if=\"currentFormName\" :edit-form.sync=\"this[currentFormName]\" :edit-form-rules=\"this[currentFormName+'Rules']\" :enums=\"enums\" ref=\"" + fileName + "Form\"></" + StringUtils.capitalize(fileName.toLowerCase()) + ">");
            pt.contents().add("      </el-col>");
            pt.contents().add("    </el-row>");
            pt.contents().add("    <el-row type=\"flex\" justify=\"center\" align=\"top\">");
            pt.contents().add("      <el-button type=\"primary\" @click=\"onPost('" + fileName + "Form','reset') \" icon=\"ion-backspace-outline\">重置</el-button>");
            pt.contents().add("      <el-button type=\"primary\" @click=\"onPost('" + fileName + "Form','submit')\" icon=\"ion-checkmark\">提交</el-button>");
            pt.contents().add("    </el-row>");
            pt.contents().add("  </el-dialog>");
            pt.contents().add("  <!-- 弹出表单: " + formName + "-END -->");
        }
        for (FunctionModel fum : pt.table().functions()) {
            if ("noShow".equals(fum.getStr(FunctionModel.type))) {
                continue;
            }
            boolean multi = false;
            String formName = StringUtils.defaultString(fum.getStr(FunctionModel.alias), fum.getStr(FunctionModel.name));
            String fileName = StringUtils.defaultString(fum.getStr(FunctionModel.fileName));
            for (FieldModel fm : allFields) {
                if (FieldNameHelper.columnToField(fm.getStr(FieldModel.fieldName)).equals(fileName)) {
                    multi = "Y".equals(fm.getStr(FieldModel.isNonTableColumn));
                    break;
                }
            }
            pt.contents().add("  <!-- 弹出选择：" + formName + " --><#--如果是其他页面，如选择页面，则弹出窗-->");
            pt.contents().add("  <el-dialog class=\"qdp-form-dialog\" title=\"" + formName + "\" :visible.sync=\"" + fileName + "Visible\" v-if=\"" + fileName + "Visible\">");
            pt.contents().add("    <el-row type=\"flex\" justify=\"center\" align=\"top\">");
            pt.contents().add("      <el-col :span=\"24\">");
            pt.contents().add("        <" + StringUtils.capitalize(fileName.toLowerCase()) + " v-if=\"currentFormName\" :value.sync=\"this[currentFormName]\" :multi=\"" + multi + "\" :field=\"'" + fileName + "'\" :id-field=\"'id'\" :enums=\"enums\"></" + StringUtils.capitalize(fileName.toLowerCase()) + ">");
            pt.contents().add("      </el-col>");
            pt.contents().add("    </el-row>");
            pt.contents().add("    <el-row type=\"flex\" justify=\"center\" align=\"top\" class=\"qdp-dialog-btns\">");
            pt.contents().add("      <el-button type=\"primary\" @click=\"resetSelectionLabel(currentFormName, '" + fileName + "')," + fileName + "Visible=false\" icon=\"ion-backspace-outline\">重置</el-button>");
            pt.contents().add("      <el-button type=\"primary\" @click=\"getSelectionLabel(currentFormName, '" + fileName + "', 'name')," + fileName + "Visible=false\" icon=\"ion-checkmark\">选择</el-button>");
            pt.contents().add("    </el-row>");
            pt.contents().add("  </el-dialog>");
            pt.contents().add("  <!-- 弹出选择：" + formName + "-END -->");
        }
        pt.contents().add("  </div>");
        pt.contents().add("</template>");
        //===============================Script===============================
        pt.contents().add("<script type=\"text/javascript\">");
        count = 0;
        List<String> comsPath = new ArrayList<>(Arrays.asList(""));// component path to load in define.
        List<String> comsName = new ArrayList<>(Arrays.asList(""));// component name to use.
        List<String> coms = new ArrayList<>();// system components such as IconPanel.
        List<String> names = new ArrayList<>();
        for (FieldModel fm : allFields) {//add system component
            if ("icon".equals(fm.getStr(FieldModel.typeAdd)) || "icon".equals(fm.getStr(FieldModel.typeEdit))) {
                names.add(FieldNameHelper.columnToField(fm.getStr(FieldModel.fieldName)));
            }
            if (!coms.isEmpty()) {
                continue;
            }
            comsPath.add("vuel!pages/components/IconPanel.html");
            comsName.add("IconPanel");
            coms.add("IconPanel: IconPanel");
        }
        for (FunctionModel fum : pt.table().functions()) {
            if (!("page".equals(fum.getStr(FunctionModel.type)) && "index".equals(fum.getStr(FunctionModel.fileName)))) {
                continue;
            }
            String fileName = StringUtils.defaultString(fum.getStr(FunctionModel.fileName));
            String capitalizeFilename = StringUtils.capitalize(fileName.toLowerCase());
            comsPath.add("'vuel!pages/" + FieldNameHelper.packageToPath(pt.table().getStr(BusinessModel.packageName), fum.getStr(FunctionModel.name).toLowerCase(), className.toLowerCase(), fileName) + ".html'");
            comsName.add(capitalizeFilename);
            coms.add(capitalizeFilename + ": " + capitalizeFilename);
            names.add(fileName);
        }
        pt.contents().add("define([ 'vue'" + StringUtils.join(comsPath, ", ") + "], function(Vue" + StringUtils.join(comsName, ", ") + ") {");
        pt.contents().add("  return {");
        pt.contents().add("    template : template, //");
        pt.contents().add("    components : {" + StringUtils.join(coms, ", ") + "}");
        pt.contents().add("  },");
        pt.contents().add("  data : function() {");
        pt.contents().add("    return {");
        pt.contents().add("      " + StringUtils.join(names, "Visible : false,") + (names.isEmpty() ? "" : "Visible : false,"));
        for (FunctionModel fum : pt.table().functions()) {
            if (!("selectMultiGrid|selectMultiTree".contains(fum.getStr(FunctionModel.type)) && !"index".equals(fum.getStr(FunctionModel.fileName)))) {
                continue;
            }
            String fileName = StringUtils.defaultString(fum.getStr(FunctionModel.fileName));
            pt.contents().add("      " + fileName + "LoadData : {");
            pt.contents().add("        url : '/ws/json/" + StringUtils.uncapitalize(className) + "Application/pageFindAssign" + className + "/arg0={{d.vo}}/arg1={{d.page}}',");
            pt.contents().add("        query : qdpObj({}, 'vo', {}, 'page', qdpObj({}, 'pageSize', 1000, 'currentPage', 1, 'orderBy', [])),");
            pt.contents().add("        callback : function(data, callback, formName) {");
            pt.contents().add("          var checkedAll = data || [], checkedArr = qdpIsArray(checkedAll) ? checkedAll : (checkedAll.rows || []);//要考虑返回是一个Page对象的情况");
            pt.contents().add("          var checked = [], ids = [], names = [], form = this[formName];");
            pt.contents().add("          qdpMap(checkedArr, function(value) {");
            pt.contents().add("            checked.push(value.id), ids.push(value.id), names.push(value.name);");
            pt.contents().add("          });");
            pt.contents().add("          this.cache('" + className.toLowerCase() + StringUtils.capitalize(fileName) + "', checked);");
            pt.contents().add("          if (form) {");
            pt.contents().add("            form." + fileName + " = ids.join(','), form." + fileName + "Name = names.join(',');");
            pt.contents().add("          }");
            pt.contents().add("          typeof (callback) == 'function' && callback.call(this);");
            pt.contents().add("        }");
            pt.contents().add("      },");
        }
        List<String> hotKeys = new ArrayList<>();
        for (Entry<String, ActionModel> entry : pt.table().actions().entrySet()) {
            hotKeys.add("'" + entry.getValue().getStr(ActionModel.shortCut).toLowerCase() + "' : '" + entry.getValue().getStr(ActionModel.eventName) + "'");
        }
        pt.contents().add("      hotKey : {");
        pt.contents().add("        controlKey : 0, keyAction : {");
        pt.contents().add("          " + StringUtils.join(hotKeys, ","));
        pt.contents().add("        }");
        pt.contents().add("      },");
        pt.contents().add("      currentFormName : '',//addForm, editForm");
        pt.contents().add("      searchForm : {");
        pt.contents().add("        " + formFields(pt.function(), FieldModel.ableSearch));
        pt.contents().add("      },");
        pt.contents().add("      addFormUrl : '/ws/json/" + uncapClassName + "Application/add" + className + "',");
        pt.contents().add("      addFormVisible : false,");
        pt.contents().add("      addForm : {");
        pt.contents().add("        " + formFields(pt.function(), FieldModel.ableAdd));
        pt.contents().add("      },");
        pt.contents().add("      addFormRules : " + genFormRule(pt.function(), FieldModel.ableAdd) + ",");
        pt.contents().add("      editFormUrl : '/ws/json/" + uncapClassName + "Application/update" + className + "',");
        pt.contents().add("      editFormVisible : false,");
        pt.contents().add("      editForm : {");
        pt.contents().add("        " + formFields(pt.function(), FieldModel.ableEdit));
        pt.contents().add("      },");
        pt.contents().add("      editFormRules : " + genFormRule(pt.function(), FieldModel.ableEdit) + ",");
        pt.contents().add("      infoFormVisible : false,");
        pt.contents().add("      infoForm : {");
        pt.contents().add("        " + formFields(pt.function(), FieldModel.ableInfo));
        pt.contents().add("      },");
        pt.contents().add("      deleteFormUrl : '/ws/json/" + uncapClassName + "Application/remove" + className + "ById',");
        for (FunctionModel fum : pt.table().functions()) {
            if (!("form".equals(fum.getStr(FunctionModel.type)) && !"index".equals(fum.getStr(FunctionModel.fileName)))) {
                continue;
            }
            String fileName = StringUtils.defaultString(fum.getStr(FunctionModel.fileName));
            pt.contents().add("      " + fileName + "FormUrl : '/ws/json/" + uncapClassName + "Application/" + fileName + "',");
            pt.contents().add("      " + fileName + "FormVisible : false,");
            pt.contents().add("      " + fileName + "Form : {");
            pt.contents().add("        " + formFields(fum, FieldModel.ableEdit));
            pt.contents().add("      },");
            pt.contents().add("      " + fileName + "FormRules : " + genFormRule(pt.function(), FieldModel.ableEdit) + "},");
        }
        pt.contents().add("      enums : ${genEnums(func,project)},");
        pt.contents().add("      grid : {");
        pt.contents().add("        url : '/ws/json/" + uncapClassName + "Application/pageFind" + className + "Map/arg0={{d.vo}}/arg1={{d.page}}',");
        pt.contents().add("        query : qdpObj({}, 'vo', {}, 'page', qdpObj({}, 'pageSize', 10, 'currentPage', 1, 'orderBy', [])), oneSelection : null,");
        pt.contents().add("        multiSelection : [], data : {");
        pt.contents().add("          currentPage : 1, totalCount : 0, pageSize : 5, rows : []");
        pt.contents().add("        }");
        pt.contents().add("      }");
        pt.contents().add("    };//END-OF-DATA-RETURN");
        pt.contents().add("  },//END-OF-DATA");
        pt.contents().add("  created : function() {//创建的时候加载列表数据");
        pt.contents().add("    this.loadGrid();");
        pt.contents().add("  },");
        pt.contents().add("  mounted : function() {");
        pt.contents().add("    document.addEventListener('keyup', this.onHotKey);//快捷键");
        pt.contents().add("    document.addEventListener('keydown', this.onHotKey);//快捷键");
        pt.contents().add("  },");
        pt.contents().add("  beforeDestroy : function() {");
        pt.contents().add("    document.removeEventListener('keyup', this.onHotKey);//快捷键");
        pt.contents().add("    document.removeEventListener('keydown', this.onHotKey);//快捷键");
        pt.contents().add("  },");
        pt.contents().add("  methods : {");
        pt.contents().add("    cache : getters.cache,");
        pt.contents().add("    accountId : getters.accountId, ");
        pt.contents().add("    loadGrid : function() {// 加载Grid");
        pt.contents().add("      loadGrid : function() {");
        pt.contents().add("        var root = this, grid = root.grid;");
        pt.contents().add("        var query = grid.query, url = grid.url, params = params || {}, prefix='" + uncapClassName + "';");
        pt.contents().add("        query.page && params.orderBy && (query.page.orderBy = params.orderBy);");
        pt.contents().add("        query.page && qdpIsNumber(params.pageSize) && (query.page.pageSize = params.pageSize || 1) && (query.page.currentPage = 1);");
        pt.contents().add("        query.page && qdpIsNumber(params.currentPage) && (query.page.currentPage = params.currentPage);");
        pt.contents().add("        query = qdpCombine(root.queryContext(), query);");
        pt.contents().add("        qdpMap(root.queryContext(), function(value, key){//把当前上下文参数传给VO");
        pt.contents().add("          (query.vo||{})[key] = value;");
        pt.contents().add("        });");
        pt.contents().add("        url = qdpFormatUrl(ctx + url, query);");
        pt.contents().add("        LOG(\"FN: Grid.grid-load-data.url=\" + url);");
        pt.contents().add("        root.$http.get(url).then(function(data) {");
        pt.contents().add("          Vue.set(grid, 'data', qdpAjaxDataBody(data));");
        pt.contents().add("        }, function(response) {");
        pt.contents().add("          root.$notify({");
        pt.contents().add("            title : 'Http=>Error', desc : response.url + '\\n<br/>' + qdpToString(response), duration : 0");
        pt.contents().add("          });");
        pt.contents().add("        });");
        pt.contents().add("      },//END-OF-loadGrid");
        pt.contents().add("      getOption : function(data, field) {// 返回选择项的值");
        pt.contents().add("        var fieldName = field.property || field, enums = ((this.enums || {}).index||{})[fieldName] || {};");
        pt.contents().add("        var value = data[fieldName];");
        pt.contents().add("        var option = enums[value == null ? '' : value];");
        pt.contents().add("        return option == null ? (value == null ? '' : value) : option;");
        pt.contents().add("      },");
        pt.contents().add("      getSelectionLabel : function(formName, field, labelField) {// 返回选择数据的值");
        pt.contents().add("        var form = this[formName], names = [], ids = [], datas = form[field + 'Selections'];");
        pt.contents().add("        qdpMap(datas, function(value) {");
        pt.contents().add("          names.push(value[labelField]);");
        pt.contents().add("          ids.push(value.id);");
        pt.contents().add("        });");
        pt.contents().add("        form[field + 'Name'] = names.join(',');");
        pt.contents().add("        form[field] = ids.join(',');");
        pt.contents().add("      },");
        pt.contents().add("      resetSelectionLabel : function(formName, field) {// 重置选择数据的值");
        pt.contents().add("        var ref = this.$refs[formName + '-' + field], ref2 = this.$refs[formName + '-' + field + 'Name'];");
        pt.contents().add("        ref && ref.resetField();");
        pt.contents().add("        ref2 && ref2.resetField();");
        pt.contents().add("        this[formName][field + 'Selections'] = [];");
        pt.contents().add("      },");
        pt.contents().add("      split : function(value, fieldName) {// 分割字符串，预防空值");
        pt.contents().add("        return ((value||{})[fieldName]||',').split(',');");
        pt.contents().add("      },");
        pt.contents().add("      removeSelect : function(form, field, value) {// 删除选择项");
        pt.contents().add("        var root = this, names = root.split(form, field+'Name'), ids = root.split(form, field);");
        pt.contents().add("        var index = names.indexOf(value);");
        pt.contents().add("        if(index > -1 && names.length == ids.length){");
        pt.contents().add("          names.splice(index,1), ids.splice(index,1);");
        pt.contents().add("          form[field+'Name']=names.join(','), form[field]=ids.join(',');");
        pt.contents().add("        }");
        pt.contents().add("      },");
        pt.contents().add("      resetSelectionLabel : function(formName, field) {// 重置选择数据的值");
        pt.contents().add("        var ref = this.$refs[formName + '-' + field], ref2 = this.$refs[formName + '-' + field + 'Name'];");
        pt.contents().add("        ref && ref.resetField();");
        pt.contents().add("        ref2 && ref2.resetField();");
        pt.contents().add("        this[formName][field + 'Selections'] = [];");
        pt.contents().add("      },");
        pt.contents().add("      onHotKey : function(event) {// 热键，默认就是Ctrl+其他键");
        pt.contents().add("        var root = this;");
        pt.contents().add("        if (event.type == 'keyup') { root.hotKey.controlKey = null; return; }");
        pt.contents().add("        if (event.type != 'keydown') { return; }");
        pt.contents().add("        var keyCode = event.keyCode;// 获取键值, Ctrl=17, Alt=18, Shit=16, Command=224");
        pt.contents().add("        if (keyCode == 17) { root.hotKey.controlKey = 17; return; }");
        pt.contents().add("        var keyValue = String.fromCharCode(event.keyCode).toLowerCase();");
        pt.contents().add("        if (root.hotKey.controlKey < 1) { return; }");
        pt.contents().add("        var map = root.hotKey.keyAction, value = map[keyValue];");
        pt.contents().add("        if (!value) { return; }");
        pt.contents().add("        LOG('FN: onHotKey.Ctrl+' + keyValue);");
        pt.contents().add("        event.stopPropagation(), event.preventDefault(); // 阻止事件冒泡 // 阻止该元素默认的keyup事件");
        pt.contents().add("        root.onPost('action-bar', value);");
        pt.contents().add("      },");
        pt.contents().add("      openDialog : function(formName, field) {// 打开弹窗");
        pt.contents().add("        var root = this;");
        pt.contents().add("        root.currentFormName = formName, root[field + 'Visible'] = true;");
        pt.contents().add("      },");
        pt.contents().add("      queryContext : function(value) {// 查询上下文");
        pt.contents().add("        var qc = qdpSGetItem('queryContext', {});");
        pt.contents().add("        if (value === null || value != null) {");
        pt.contents().add("          qc." + uncapClassName + "Id = value;");
        pt.contents().add("          qdpSSetItem('queryContext', qc);");
        pt.contents().add("        }");
        pt.contents().add("        return qc;");
        pt.contents().add("      },");
        pt.contents().add("      clearForm : function(formJson) {// 清除表单多余项");
        pt.contents().add("        var keys = [];");
        pt.contents().add("        qdpMap(formJson, function(value, key) {");
        pt.contents().add("          if (((key + 'Selections') in formJson) && (key + 'Name') in formJson) {");
        pt.contents().add("            keys.push(key + 'Selections');");
        pt.contents().add("            keys.push(key + 'Name');");
        pt.contents().add("          }");
        pt.contents().add("        });");
        pt.contents().add("        qdpMap(keys, function(value, key) {");
        pt.contents().add("          delete formJson[value];");
        pt.contents().add("        });");
        pt.contents().add("        return formJson;");
        pt.contents().add("      },");
        pt.contents().add("      onPost : function(eventType, arg0, arg1, arg2, arg3) {//事件处理");
        pt.contents().add("        LOG('EV: onPost.' + eventType);");
        pt.contents().add("        var root = this;");
        pt.contents().add("        if ('searchForm' == eventType) {//搜索表单");
        pt.contents().add("          LOG('searchForm:' + arg0);");
        pt.contents().add("          var action = arg0;");
        pt.contents().add("          if ('reset' == arg0) {");
        pt.contents().add("            root.$refs.searchForm.resetFields();");
        pt.contents().add("            qdpMap(root.searchForm, function(value, key) {");
        pt.contents().add("              root.grid.query.vo[key] = value;");
        pt.contents().add("            }");
        pt.contents().add("          }");
        pt.contents().add("          if ('search' == arg0) {");
        pt.contents().add("            qdpMap(root.searchForm, function(value, key) {");
        pt.contents().add("              root.grid.query.vo[key] = value;");
        pt.contents().add("            }");
        pt.contents().add("          }");
        pt.contents().add("          root.loadGrid();");
        pt.contents().add("        }");
        pt.contents().add("      }//END-OF-onPost");
        pt.contents().add("    }//END-OF-methods");
        pt.contents().add("  }//END-OF-return");
        pt.contents().add("});");
        pt.contents().add("</script>");
    }

    protected String genEnums(BusinessModel bm){
        Map<String, Object> rules = new LinkedHashMap<>();
        String fieldTypes="radio,checkbox,switch,select";
        for (FunctionModel fum: bm.functions()) {
            if(!fum.isPage()){
                continue;
            }
            for(FieldModel fm:fum.fields()){
                if(!(StringUtils.contains(fieldTypes,fm.getStr(FieldModel.typeAdd))||StringUtils.contains(fieldTypes,fm.getStr(FieldModel.typeEdit)))){
                    continue;
                }

            }
        }
        return GsonHelper.toJsonString(rules);
    }

    protected String genFormRule(FunctionModel fum, String formTypeFieldName) {
        Map<String, List<Map<String, Object>>> rules = new LinkedHashMap<>();
        for (FieldModel fm : fum.fields()) {
            if (!"Y".equals(fm.getStr(formTypeFieldName))) {
                continue;
            }
            List<Map<String, Object>> rule = new ArrayList<>();
            String javaField = FieldNameHelper.columnToField(fm.getStr(FieldModel.fieldName));
            String javaFieldEnName = FieldNameHelper.tableToClass(StringUtils.defaultString(fm.getStr(FieldModel.fieldAlias), StringUtils.defaultString(fm.getStr(FieldModel.fieldDesc), javaField)));
            // rule = {type, required, requiredMsg, regex, regexMsg, rangeMin, rangeMax, rangeMsg, length, lengthMsg, enums, enumsMsg, noBlank, noBlankMsg}
            String ruleType = fm.getStr(FieldModel.ruleType);
            String ruleRequired = fm.getStr(FieldModel.ruleRequired);
            String ruleRequiredMsg = fm.getStr(FieldModel.ruleRequiredMsg);
            String ruleRegex = fm.getStr(FieldModel.ruleRegex);
            String ruleRegexMsg = fm.getStr(FieldModel.ruleRegexMsg);
            String ruleRangeMin = fm.getStr(FieldModel.ruleRangeMin);
            String ruleRangeMax = fm.getStr(FieldModel.ruleRangeMax);
            String ruleRangeMsg = fm.getStr(FieldModel.ruleRangeMsg);
            String ruleLength = fm.getStr(FieldModel.ruleLength);
            String ruleLengthMsg = fm.getStr(FieldModel.ruleLengthMsg);
            String ruleEnums = fm.getStr(FieldModel.ruleEnums);
            String ruleEnumsMsg = fm.getStr(FieldModel.ruleEnumsMsg);
            String ruleNotBlank = fm.getStr(FieldModel.ruleNotBlank);
            String ruleNotBlankMsg = fm.getStr(FieldModel.ruleNotBlankMsg);
            if (StringUtils.isNotBlank(ruleType)) {
                rule.add(MapHelper.toMap("type", ruleType, "message", javaFieldEnName + "必须为" + ruleType + "类型"));
            }
            if (StringUtils.equals("Y", ruleRequired)) {
                rule.add(MapHelper.toMap("required", true, "message", StringUtils.defaultString(ruleRequiredMsg, javaFieldEnName + "不能为空")));
            }
            if (StringUtils.isNotBlank(ruleRegex)) {
                //rule.add(MapHelper.toMap("required",true, "message", StringUtils.defaultString(ruleRequiredMsg, javaFieldEnName + "不能为空")));
            }
            if (!StringUtils.equals(ruleRangeMin, ruleRangeMax)) {
                boolean isDouble = StringUtils.contains(ruleRangeMin, '.') || StringUtils.contains(ruleRangeMax, '.');
                try {
                    Number min = isDouble ? Double.valueOf(ruleRangeMin) : Long.valueOf(ruleRangeMin);
                    Number max = isDouble ? Double.valueOf(ruleRangeMax) : Long.valueOf(ruleRangeMax);
                    rule.add(MapHelper.toMap("min", min, "max", max, "message", StringUtils.defaultString(ruleRangeMsg, javaFieldEnName + "范围" + ruleRangeMin + "至" + ruleRangeMax)));
                } catch (Exception e) {
                    Logger.warn("ElementPageGenerator can't parse rage rule for field: " + javaField);
                }
            }
            if (NumberHelper.getInt(ruleLength, 0) > 0) {
                rule.add(MapHelper.toMap("len", NumberHelper.getInt(ruleLength, 0), "message", StringUtils.defaultString(ruleLengthMsg, javaFieldEnName + "长度为" + ruleLength)));
            }
            if (StringUtils.isNotBlank(ruleEnums)) {
                try {
                    ruleEnums = GsonHelper.toJsonString(GsonHelper.toJsonList(ruleEnums));
                    rule.add(MapHelper.toMap("enum", ruleEnums, "message", StringUtils.defaultString(ruleEnumsMsg, javaFieldEnName + "为数组")));
                } catch (Exception e) {
                    Logger.warn("ElementPageGenerator can't parse enum rule for field: " + javaField);
                }
            }
            if (StringUtils.equals("Y", ruleNotBlank)) {
                rule.add(MapHelper.toMap("whitespace", true, "message", StringUtils.defaultString(ruleNotBlankMsg, javaFieldEnName + "不能为空")));
            }
            rules.put(javaField, rule);
        }
        return GsonHelper.toJsonString(rules);
    }

    protected String formFields(FunctionModel fum, String formTypeFieldName) {
        List<String> formFields = new ArrayList<>();
        for (FieldModel fm : fum.fields()) {
            if (!"Y".equals(fm.getStr(formTypeFieldName))) {
                continue;
            }
            String javaField = FieldNameHelper.columnToField(fm.getStr(FieldModel.fieldName));
            formFields.add(javaField + ": null");
            if ("modal".equals(fm.getStr(FieldModel.typeAll)) && !FieldModel.typeSearch.equals(formTypeFieldName)) {
                formFields.add(javaField + "Name : ''\", \"" + javaField + "Selections : []");
            }
        }
        return StringUtils.join(formFields, ", ");
    }
}

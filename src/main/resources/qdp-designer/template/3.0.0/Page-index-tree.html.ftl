<#include "/_functions.ftl" />
<#include "/_page_util.ftl" />
<#macro page date package class module table pk notNull index unique autoInc columns foreign func proModule project>
<template>
    <!-- template-2.0.0 -->
	<div class="qdp-list-layout">
		<!-- 搜索 -->
		<!-- 搜索-END -->
		<!-- 操作栏 -->
		<#if table.actions?has_content>
		<el-row class="qdp-list-content qdp-el-action" type="flex" justify="end" align="top">
			<@formatCode align=['@click='] start='			' end='' split='\n' leftSpace=' ' rightSpace=''>
			<#list table.actions?keys as key><#local action=table.actions[key]/>
			<el-button size="small" type="text" title="${action.actionName}"    @click="onPost('action-bar','${action.eventName}')"><i class="${action.actionIcon}"></i></el-button>
			</#list>
			</@formatCode>
		</el-row>
		</#if>
		<!-- 操作栏-END- -->
		<!-- 列表 -->
		<!-- 列表-END -->
		<!-- 分页 -->
		<!-- 分页-END -->
		<!-- 树 -->
		<el-row class="qdp-list-content qdp-tree-form" type="flex" justify="center" align="top">
			<el-col :span="6">
				<el-tree :highlight-current="true" :show-checkbox="false" node-key="id" :data="tree.data" :default-checked-keys="tree.checked" :default-expanded-keys="tree.expanded" :props="tree.props"
					@node-click="onPost('treeForm', 'node-click', arguments[0], arguments[1])"
					@check-change="onPost('treeForm', 'check-change', arguments[0], arguments[1], arguments[2])"
					:render-content="renderContent" ref="treeForm">
				</el-tree>
			</el-col>
			<el-col :span="18">
				<el-row type="flex" justify="start" align="top" class="qdp-info-form" v-if="infoForm.id">
					<table>
						<tbody>
							<#list table.fields as field><#local label = javaFieldEnName(javaField(field.description!field.field))?capitalize/>
								<#if ['radio','checkbox','switch','select']?seq_index_of(field.type.grid) gt -1>
							<tr><td class="qdp-infoform-title">${label}</td><td class="qdp-infoform-content"><div><el-tag>{{getOption(infoForm, '${javaField(field.field)}')}}</el-tag></div></td></tr>
								<#else>
							<tr><td class="qdp-infoform-title">${label}</td><td class="qdp-infoform-content"><div>{{infoForm.${javaField(field.field)}}}</div></td></tr>
								</#if>
							</#list>
						</tbody>
					</table>
				</el-row>
			</el-col>
		</el-row>
		<!-- 树-END -->
		<!-- 添加 --><#local ableFields=[]/><#list table.fields as field><#if field.addable><#local ableFields=ableFields+[field]/></#if></#list>
		<el-dialog title="添加" :visible.sync="addFormVisible" :close-on-press-escape="false" :close-on-click-modal="false">
			<el-row type="flex" justify="center" align="top">
				<el-form class="qdp-add-form" :model="addForm" :rules="addFormRules" label-width="100px" inline ref="addForm" @keydown.13.native.stop="onPost('addForm', 'submit')">
					<#list ableFields as field>
						<#if ['ID','UPDATE_TIME','CREATE_TIME','PARENT_ID','ROOT_ID']?seq_index_of(field.field) lt 0><@genForm field=field formType='add'/></#if>
					</#list>
				</el-form>
			</el-row>
			<el-row type="flex" justify="center" align="top">
				<el-button type="primary" @click="onPost('addForm','reset') " icon="ion-backspace-outline">重置</el-button>
				<el-button type="primary" @click="onPost('addForm','submit')" icon="ion-checkmark">提交</el-button>
			</el-row>
		</el-dialog>
		<!-- 添加-END -->
		<!-- 添加子节点 --><#local ableFields=[]/><#list table.fields as field><#if field.addable><#local ableFields=ableFields+[field]/></#if></#list>
		<el-dialog title="添加子节点" :visible.sync="addChildFormVisible" :close-on-press-escape="false" :close-on-click-modal="false">
			<el-row type="flex" justify="center" align="top">
				<el-form class="qdp-add-form" :model="addChildForm" :rules="addChildFormRules" label-width="100px" inline ref="addChildForm" @keydown.13.native.stop="onPost('addChildForm', 'submit')">
					<#list ableFields as field>
						<#if ['ID','UPDATE_TIME','CREATE_TIME','TYPE1','TYPE2','PARENT_ID','ROOT_ID','MAX_LEVEL']?seq_index_of(field.field) lt 0><@genForm field=field formType='addChild'/></#if>
					</#list>
				</el-form>
			</el-row>
			<el-row type="flex" justify="center" align="top">
				<el-button type="primary" @click="onPost('addChildForm','reset') " icon="ion-backspace-outline">重置</el-button>
				<el-button type="primary" @click="onPost('addChildForm','submit')" icon="ion-checkmark">提交</el-button>
			</el-row>
		</el-dialog>
		<!-- 添加子节点-END -->
		<!-- 修改 --><#local ableFields=[]/><#list table.fields as field><#if field.editable><#local ableFields=ableFields+[field]/></#if></#list>
		<el-dialog title="修改" :visible.sync="editFormVisible" :close-on-press-escape="false" :close-on-click-modal="false">
			<el-row type="flex" justify="center" align="top">
				<el-form class="qdp-edit-form" :model="editForm" :rules="editFormRules" label-width="100px" inline ref="editForm" @keydown.13.native.stop="onPost('editForm', 'submit')">
					<#list ableFields as field>
						<#if ['ID','UPDATE_TIME','CREATE_TIME']?seq_index_of(field.field) lt 0><@genForm field=field formType='edit'/></#if>
					</#list>
				</el-form>
			</el-row>
			<el-row type="flex" justify="center" align="top">
				<el-button type="primary" @click="onPost('editForm','reset') " icon="ion-backspace-outline">重置</el-button>
				<el-button type="primary" @click="onPost('editForm','submit')" icon="ion-checkmark">提交</el-button>
			</el-row>
		</el-dialog>
		<!-- 修改-END -->
		<!-- 详情 --><#local ableFields=[]/><#list table.fields as field><#if field.infoable><#local ableFields=ableFields+[field]/></#if></#list>
		<el-dialog title="详情" :visible.sync="infoFormVisible">
			<el-row type="flex" justify="center" align="top" class="qdp-info-form">
				<table>
					<tbody>
						<#list ableFields as field><#local label = javaFieldEnName(javaField(field.label!field.description!field.field))?capitalize/>
							<#if ['radio','checkbox','switch','select']?seq_index_of(field.type.grid) gt -1>
						<tr><td class="qdp-infoform-title">${label}</td><td class="qdp-infoform-content"><div><el-tag>{{getOption(infoForm, '${javaField(field.field)}')}}</el-tag></div></td></tr>
							<#elseif field.type.add == 'modal'>
								<#if field.refTable?has_content && field.refField?has_content && field.refLabelField?has_content><#if field.isNotTableColumn>
						<tr><td class="qdp-infoform-title">${label}</td><td class="qdp-infoform-content"><div><el-tag v-for="value in split(infoForm, '${javaField(field.field)}Name')" v-if="value" :key="value">{{value}}</el-tag></div></td></tr>
								<#else>
						<tr><td class="qdp-infoform-title">${label}</td><td class="qdp-infoform-content"><div><el-tag>{{infoForm.${javaField(field.field)}Name||""}}</el-tag></div></td></tr>
								</#if></#if>
							<#else>
						<tr><td class="qdp-infoform-title">${label}</td><td class="qdp-infoform-content"><div>{{infoForm.${javaField(field.field)}}}</div></td></tr>
							</#if>
						</#list>
					</tbody>
				</table>
			</el-row>
		</el-dialog>
		<!-- 详情-END -->
		<!-- 图标选择 -->
		<#list table.fields as field><#if field.type.all == 'icon'><#local javaFieldName=javaField(field.field)/>
		<el-dialog class="qdp-form-dialog" title="IconPanel" :visible.sync="${javaFieldName}Visible" v-if="${javaFieldName}Visible">
			<el-row type="flex" justify="center" align="top">
				<Icon-panel v-if="currentFormName" :value.sync="this[currentFormName]" :multi="false" :field="'${javaFieldName}'" :id-field="'id'"></Icon-panel>
			</el-row>
			<el-row type="flex" justify="center" align="top" class="qdp-dialog-btns">
				<el-button type="primary" @click="resetSelectionLabel(currentFormName, '${javaFieldName}'),${javaFieldName}Visible=false" icon="ion-backspace-outline">重置</el-button>
				<el-button type="primary" @click="getSelectionLabel(currentFormName, '${javaFieldName}', 'name'),${javaFieldName}Visible=false" icon="ion-checkmark">选择</el-button>
			</el-row>
		</el-dialog>
		<#break/></#if></#list>
		<!-- 图标选择-END -->
		<#list func.children as subtable><#if subtable.type=='page' && subtable.fileName!='index' && subtable.formType=='form'>
		<!-- 弹出表单: ${subtable.label!subtable.name} -->
		<el-dialog class="qdp-form-dialog" title="${subtable.label!subtable.name}" :visible.sync="${subtable.fileName}Visible" v-if="${subtable.fileName}Visible">
			<el-row type="flex" justify="center" align="top">
				<el-col :span="24">
				<${subtable.fileName?lower_case?cap_first} v-if="currentFormName" :edit-form.sync="this[currentFormName]" :edit-form-rules="this[currentFormName+'Rules']" :enums="enums" ref="${subtable.fileName}Form"></${subtable.fileName?lower_case?cap_first}>
				</el-col>
			</el-row>
			<el-row type="flex" justify="center" align="top">
				<el-button type="primary" @click="onPost('${subtable.fileName}Form','reset') " icon="ion-backspace-outline">重置</el-button>
				<el-button type="primary" @click="onPost('${subtable.fileName}Form','submit')" icon="ion-checkmark">提交</el-button>
			</el-row>
		</el-dialog>
		<!-- 弹出表单: ${subtable.label!subtable.name}-END -->
		</#if></#list>
		<#list func.children as subtable><#if subtable.type=='page' && subtable.fileName!='index' && subtable.formType!='form' && subtable.formType!='noShow'>
		<#local multi=false/><#list table.fields as field><#if javaField(field.field)==subtable.fileName><#local multi=field.isNotTableColumn/></#if></#list>
		<!-- 弹出选择：${subtable.label!subtable.name} -->
		<el-dialog class="qdp-form-dialog" title="${subtable.label!subtable.name}" :visible.sync="${subtable.fileName}Visible" v-if="${subtable.fileName}Visible">
			<el-row type="flex" justify="center" align="top">
				<el-col :span="24">
					<${subtable.fileName?lower_case?cap_first} v-if="currentFormName" :value.sync="this[currentFormName]" :multi="${multi?string}" :field="'${subtable.fileName}'" :id-field="'id'" :enums="enums"></${subtable.fileName?lower_case?cap_first}>
				</el-col>
			</el-row>
			<el-row type="flex" justify="center" align="top" class="qdp-dialog-btns">
				<el-button type="primary" @click="resetSelectionLabel(currentFormName, '${subtable.fileName}'),${subtable.fileName}Visible=false" icon="ion-backspace-outline">重置</el-button>
				<el-button type="primary" @click="getSelectionLabel(currentFormName, '${subtable.fileName}', 'name'),${subtable.fileName}Visible=false" icon="ion-checkmark">选择</el-button>
			</el-row>
		</el-dialog>
		<!-- 弹出选择：${subtable.label!subtable.name}-END -->
		</#if></#list>
	</div>
</template>
<script type="text/javascript">
	<#local comPath=[''] comName=[''] coms=[] names=[]/> 
	<#list table.fields as field><#if field.type.add=='icon' || field.type.edit=='icon'>
		<#local names=names+[javaField(field.field)]/>
		<#if !coms?has_content><#local comPath=comPath+["'vuel!pages/components/IconPanel.html'"] comName=comName+['IconPanel'] coms=coms+['IconPanel: IconPanel']/></#if>
	</#if></#list>
	<#list func.children as subtable><#if subtable.type=='page' && subtable.fileName!='index' && subtable.formType!='noShow'>
		<#local comPath =comPath+["'vuel!pages/"+packageToPath(module,func.packageName,class?lower_case,subtable.fileName)+".html'"] comName=comName+[javaField(subtable.fileName)?lower_case?cap_first] coms=coms+["${javaField(subtable.fileName)?lower_case?cap_first}: ${javaField(subtable.fileName)?lower_case?cap_first}"] names=names+[subtable.fileName]/>
	</#if></#list>
	define([ 'vue'${comPath?join(', ')}], function(Vue${comName?join(', ')}) {
		return {
			template : template, //
			components : {
				${coms?join(', ')}
			},
			data : function() {
				return {
					<#list names as name>
					${name}Visible : false,
					</#list>
					<#list func.children as subtable><#if subtable.type=='page' && subtable.fileName!='index' && ['selectMultiGrid', 'selectMultiTree']?seq_index_of(subtable.formType) gt -1>
					${subtable.fileName}LoadData : {
						url : '/ws/json/${class?uncap_first}Application/pageFindAssign${javaClass(subtable.tableName,removePrefix)?cap_first}/arg0={{d.vo}}/arg1={{d.page}}', 
						query : qdpObj({}, 'vo', {}, 'page', qdpObj({}, 'pageSize', 1000, 'currentPage', 1, 'orderBy', [])),
						callback : function(data, callback, formName) {
							var checkedAll = data || [], checkedArr = qdpIsArray(checkedAll) ? checkedAll : (checkedAll.rows || []);//要考虑返回是一个Page对象的情况
							var checked = [], ids = [], names = [], form = this[formName];
							qdpMap(checkedArr, function(value) {
								checked.push(value.id), ids.push(value.id), names.push(value.name);
							});
							this.cache('${class?lower_case}${subtable.fileName?cap_first}', checked);
							if (form) {
								form.${subtable.fileName} = ids.join(','), form.${subtable.fileName}Name = names.join(',');
							}
							typeof (callback) == 'function' && callback.call(this);
						}
					},
					</#if></#list>
					//===
					<#local actions=[]/><#list table.actions?keys as key><#local action=table.actions[key]/><#local actions=actions+["'${action.shortCut?lower_case}' : '${action.eventName}'"]/></#list>
					hotKey : {
						controlKey : 0, keyAction : {
							${actions?join(', ')}
						}
					},
					currentFormName : '',//addForm, editForm
					searchForm : {
					},
					addFormUrl : '/ws/json/${class?uncap_first}Application/add${class}',
					addFormVisible : false,
					addForm : {
						${formFields(table, 'addable')}
					},
					addFormRules : ${genFormRule(table, 'addable')},
					editFormUrl : '/ws/json/${class?uncap_first}Application/update${class}',
					editFormVisible : false,
					editForm : {
						${formFields(table, 'editable')}
					},
					editFormRules : ${genFormRule(table, 'editable')},
					infoFormVisible : false,
					infoForm : {
						${formFields(table, 'infoable')}
					},
					deleteFormUrl : '/ws/json/${class?uncap_first}Application/remove${class}ById',
					addChildFormUrl : '/ws/json/${class?uncap_first}Application/add${class}',
					addChildFormVisible : false,
					addChildForm : {
						${formFields(table, 'addable')}
					},
					addChildFormRules : ${genFormRule(table, 'addable')},
					<#list func.children as subtable><#if subtable.type=='page' && subtable.fileName!='index' && subtable.formType=='form'>
					${subtable.fileName}FormUrl : '/ws/json/${class?uncap_first}Application/${subtable.fileName}',
					${subtable.fileName}FormVisible : false,
					${subtable.fileName}Form : {
						${formFields(subtable, 'editable')}
					},
					${subtable.fileName}FormRules : ${genFormRule(subtable, 'editable')},
					</#if></#list>
					enums : ${genEnums(func,project)},
					tree : {
						url : '/ws/json/${class?uncap_first}Application/pageFind${class}Map/arg0={{d.vo}}/arg1={{d.page}}',
						query : qdpObj({}, 'vo', {}, 'page', qdpObj({}, 'pageSize', 1000, 'currentPage', 1, 'orderBy', [])), oneSelection : null,
						multiSelection : [], checked:[], expanded:[], props:{label: 'name', children:'children'}, data : []
					}
				};
			}, //
			created : function() {
				this.loadTree();
			}, //
			mounted : function() {
				//快捷键
				document.addEventListener('keyup', this.onHotKey);
				document.addEventListener('keydown', this.onHotKey);
			},//
			beforeDestroy : function() {
				//快捷键
				document.removeEventListener('keyup', this.onHotKey);
				document.removeEventListener('keydown', this.onHotKey);
			},//
			methods : {
				cache : getters.cache,//
				accountId : getters.accountId,//
				qdpIcon: function(config, value) {
	                return qdpIcon(config, value) || 'el-icon-ion-android-menu';
	            },//
				// 加载Tree
				loadTree : function() {
					var root = this, tree = root.tree;
					var query = tree.query, url = tree.url, params = params || {}, prefix="${class?lower_case}";
					query.page && params.orderBy && (query.page.orderBy = params.orderBy);
					query.page && qdpIsNumber(params.pageSize) && (query.page.pageSize = params.pageSize || 1) && (query.page.currentPage = 1);
					query.page && qdpIsNumber(params.currentPage) && (query.page.currentPage = params.currentPage);
					query = qdpCombine(root.queryContext(), query);
					qdpMap(root.queryContext(), function(value, key){//把当前上下文参数传给VO
						(query.vo||{})[key] = value;
					});
					url = qdpFormatUrl(ctx + url, query);//把参数替换到URL中去
					LOG("FN: tree-load-data.url=" + url);
					var callback = function(arr){
						var treeData = [];
						//如果设置分组，或检测到可以分组的进行自动按分组组装成树
						if (tree.groupFields||(arr && arr.length>0 && !arr[0].parentId && !arr[0].rootId && arr[0].type1 && arr[0].type2)) {
							treeData = qdpGroupByTree(tree.groupFields, arr, tree.labelField || "name", tree.idField || "id", tree.pidField || "parentId",
									tree.isExpand !== false, tree.checked || [], tree.disabled || []);
						} else {
							treeData = qdpTree(arr, tree.labelField || "name", tree.idField || "id", tree.pidField || "parentId", tree.isExpand !== false,
									tree.checked || [], tree.disabled || []);
						}
						Vue.set(tree, 'data', treeData);
					};
					root.$http.get(url).then(function(data) {
						var all = qdpAjaxDataBody(data) || [], arr = qdpIsArray(all) ? all : (all.rows || []);//要考虑返回是一个Page对象的情况
						callback(arr);
					}, function(response) {
						root.$notify({
							title : 'Http=>Error', desc : response.url + '\n<br/>' + qdpToString(response), duration : 0
						});
					});
				},
				// 返回选择项的值
				getOption : function(data, field) {
					var fieldName = field.property || field, enums = ((this.enums || {}).index||{})[fieldName] || {};
					var value = data[fieldName];
					var option = enums[value == null ? '' : value];
					return option == null ? (value == null ? '' : value) : option;
				},
				// 返回选择数据的值
				getSelectionLabel : function(formName, field, labelField) {
					var form = this[formName], names = [], ids = [], datas = form[field + 'Selections'];
					qdpMap(datas, function(value) {
						names.push(value[labelField]);
						ids.push(value.id);
					});
					form[field + 'Name'] = names.join(',');
					form[field] = ids.join(',');
					return;
				},
				// 重置选择数据的值
				resetSelectionLabel : function(formName, field) {
					var ref = this.$refs[formName + '-' + field], ref2 = this.$refs[formName + '-' + field + 'Name'];
					ref && ref.resetField();
					ref2 && ref2.resetField();
					this[formName][field + 'Selections'] = [];
				},
				// 分割字符串，预防空值
				split : function(value, fieldName) {
					return ((value||{})[fieldName]||',').split(',');
				},
				// 删除选择项
				removeSelect : function(form, field, value) {
					var root = this, names = root.split(form, field+'Name'), ids = root.split(form, field);
					var index = names.indexOf(value);
					if(index > -1 && names.length == ids.length){
						names.splice(index,1), ids.splice(index,1);
						form[field+'Name']=names.join(','), form[field]=ids.join(',');
					}
				},
				// 展现树节点
				renderContent: function(h, node_data_store){
					var root=this, node=node_data_store.node, data=node_data_store.data, store=node_data_store.start;
					return h('span', {'class': 'el-tree-node__label'}, [
								h('i', {'class': root.qdpIcon(root.tree, data)}),
								qdpLabel(root.tree, data)
							]);
				},
				// 热键，默认就是Ctrl+其他键
				onHotKey : function(event) {
					var root = this;
					if (event.type == "keyup") {
						root.hotKey.controlKey = null;
						return;
					}
					if (event.type != "keydown") {
						return;
					}
					// 获取键值, Ctrl=17, Alt=18, Shit=16, Command=224
					var keyCode = event.keyCode;
					if (keyCode == 17) {
						root.hotKey.controlKey = 17;
						return;
					}
					var keyValue = String.fromCharCode(event.keyCode).toLowerCase();
					if (root.hotKey.controlKey < 1) {
						return;
					}
					var map = root.hotKey.keyAction, value = map[keyValue];
					if (!value) {
						return;
					}
					LOG("FN: onHotKey.Ctrl+" + keyValue);
					event.stopPropagation(), event.preventDefault(); // 阻止事件冒泡 // 阻止该元素默认的keyup事件
					root.onPost('action-bar', value);
				},
				// 打开弹窗
				openDialog : function(formName, field) {
					var root = this;
					root.currentFormName = formName, root[field + 'Visible'] = true;
				},
				// 查询上下文
				queryContext : function(value) {
					var qc = qdpSGetItem('queryContext', {});
					if (value === null || value != null) {
						qc.${class?lower_case}Id = value;
						qdpSSetItem('queryContext', qc);
					}
					return qc;
				},
				// 清除表单多余项
				clearForm : function(formJson) {
					var keys = [];
					qdpMap(formJson, function(value, key) {
						if (((key + 'Selections') in formJson) && (key + 'Name') in formJson) {
							keys.push(key + 'Selections');
							keys.push(key + 'Name');
						}
					});
					qdpMap(keys, function(value, key) {
						delete formJson[value];
					});
					return formJson;
				},
				//
				onPost : function(eventType, arg0, arg1, arg2, arg3) {
					LOG("EV: onPost." + eventType);
					var root = this;
					if ("searchForm" == eventType) {
						LOG("searchForm:" + arg0);
						var action = arg0;
						if ('reset' == arg0) {
							root.$refs.searchForm.resetFields();
							qdpMap(root.searchForm, function(value, key) {
								root.tree.query.vo[key] = value;
							});
						}
						if ('search' == arg0) {
							qdpMap(root.searchForm, function(value, key) {
								root.tree.query.vo[key] = value;
							});
						}
						root.loadTree();
					}
					if ("action-bar" == eventType) {
						LOG("action-bar:" + arg0);
						var action = arg0;
						if ('back' == action) {
							Router.go(-1);
							return;
						}
						if ('refresh' == action) {
							root.loadTree();
							return;
						}
						if ('add' == action) {
							root.addFormVisible = true;
							return;
						}
						if ('addChild' == action) {
							var one = root.tree.oneSelection;
							if (one == null) {
								root.$message.info("请选择数据!");
								return;
							}
							root.addChildForm = qdpCombine({
								${exFormFields(table, 'addable')}
							}, {});
							qdpMap(['type1','type2','rootId'], function(value, key){
								(one[value] != null) && (root.addChildForm[value] = one[value]);
							});
							(one.id != null) && (root.addChildForm.parentId = one.id);
							//
							qdpMap(root.addChildForm, function(value, field) {
								var loadData = root[field + 'LoadData'];
								if (!loadData) {
									return;
								}
								var url = loadData.url, query = qdpCombine(root.queryContext(), loadData.query), prefix="${class?lower_case}";
								qdpMap(root.queryContext(), function(value, key){//把当前上下文参数传给VO
									if(key.indexOf(prefix) == 0){
										var newKey = key.substring(prefix.length);
										newKey = newKey.substring(0, 1).toLowerCase() + newKey.substring(1);
										(query.vo||{})[newKey] = value;
									}else{
										(query.vo||{})[key] = value;
									}
								});
								var processor = typeof (loadData.callback) == 'function' ? loadData.callback : noop;
								url = qdpFormatUrl(ctx + url, query);
								LOG("FN: edit-load-data.url=" + url);
								root.$http.get(url).then(function(data) {
									processor.call(root, qdpAjaxDataBody(data), null, 'editForm');
								}, function(response) {
									root.$notify({
										title : 'Http=>Error', desc : response.url + '\n<br/>' + qdpToString(response), duration : 0
									});
									processor.call(root, null, null, 'addChildForm');
								});
							});
							root.addChildFormVisible = true;
							root.queryContext.${class?lower_case}Id = root.tree.oneSelection.id;
							return;
						}
						if ('edit' == action) {
							if (root.tree.oneSelection == null) {
								root.$message.info("请选择数据!");
								return;
							}
							root.editForm = qdpCombine({
								${exFormFields(table, 'editable')}
							}, root.tree.oneSelection);
							//
							qdpMap(root.editForm, function(value, field) {
								var loadData = root[field + 'LoadData'];
								if (!loadData) {
									return;
								}
								var url = loadData.url, query = qdpCombine(root.queryContext(), loadData.query), prefix="${class?lower_case}";
								qdpMap(root.queryContext(), function(value, key){//把当前上下文参数传给VO
									if(key.indexOf(prefix) == 0){
										var newKey = key.substring(prefix.length);
										newKey = newKey.substring(0, 1).toLowerCase() + newKey.substring(1);
										(query.vo||{})[newKey] = value;
									}else{
										(query.vo||{})[key] = value;
									}
								});
								var processor = typeof (loadData.callback) == 'function' ? loadData.callback : noop;
								url = qdpFormatUrl(ctx + url, query);
								LOG("FN: edit-load-data.url=" + url);
								root.$http.get(url).then(function(data) {
									processor.call(root, qdpAjaxDataBody(data), null, 'editForm');
								}, function(response) {
									root.$notify({
										title : 'Http=>Error', desc : response.url + '\n<br/>' + qdpToString(response), duration : 0
									});
									processor.call(root, null, null, 'editForm');
								});
							});
							root.editFormVisible = true;
							root.queryContext.${class?lower_case}Id = root.tree.oneSelection.id;
							return;
						}
						if ('delete' == action) {
							root.onPost('deleteForm');
							return;
						}
						if ('info' == action) {
							if (root.tree.oneSelection == null) {
								root.$message.info("请选择数据!");
								return;
							}
							root.infoForm = qdpJsonCopy(root.tree.oneSelection);
							root.infoFormVisible = true;
							return;
						}
						if (root[action + 'Visible'] != null) {
							if (root.tree.oneSelection == null) {
								root.$message.info("请选择数据!");
								return;
							}
							root[action + 'Form'] = qdpCombine(root[action + 'Form'], root.tree.oneSelection);
							root.currentFormName = action + 'Form', root[action + 'Visible'] = true;
							root.queryContext.${class?lower_case}Id = root.tree.oneSelection.id;
							return;
						}
					}
					if ('treeForm' == eventType) {
						LOG("treeForm:" + arg0);
						var action = arg0, tree = root.tree;
						if ('check-change' == action) {
							var cheched = root.$refs.treeForm.getCheckedNodes(true);
							Vue.set(tree, 'multiSelection', cheched);
						}
						if ('node-click' == action) {
							var data=arg1, nodeData=arg2, $node=arg3;
							tree.oneSelection = data;
							data && (root.infoForm = qdpJsonCopy(tree.oneSelection));
							root.queryContext(qdpId(root, tree.oneSelection));
						}
						return;
					}
					if ('addForm' == eventType) {
						LOG("addForm:" + arg0);
						var action = arg0, $addForm = root.$refs.addForm, addForm = root.addForm;
						if ('reset' == action) {
							$addForm.resetFields();
						}
						if ('submit' == action) {
							var contextCondition = {}, formData, url;
							var success = function(data) {
								if (qdpIsSuccess(data)) {
									root.$message.success('成功');
									root.addFormVisible = false;
									root.loadTree();
									$addForm.resetFields();
								} else {
									root.$notify({
										title : 'Form Post=>Error', message : url + '\n<br/>' + qdpToString(qdpAjaxData(data)), duration : 0, type : 'error'
									});
								}
							};
							var error = function(response) {
								root.$notify({
									title : 'Http=>Error', desc : response.url + '\n<br/>' + qdpToString(response), duration : 0
								});
							};
							var valid = function(valid) {
								if (!valid) {
									return;
								}
								root.$http.post(url, formData).then(success, error);
							};
							var then = function() {
								formData = qdpJsonForm({arg0 : root.clearForm(qdpJsonCopy(addForm))});
								//submit form
								url = qdpFormatUrl(ctx + root.addFormUrl, contextCondition);
								LOG("FN: addForm.url=" + url);
								$addForm.validate(valid);
							};
							var confirm = {
								confirmButtonText : '确定', cancelButtonText : '取消', type : 'warning'
							};
							root.$confirm('提交表单', '提示', confirm).then(then)['catch'](noop);
						}
						return;
					}
					if ('addChildForm' == eventType) {
						LOG("addChildForm:" + arg0);
						var action = arg0, $addChildForm = root.$refs.addChildForm, addChildForm = root.addChildForm;
						if ('reset' == action) {
							$addChildForm.resetFields();
						}
						if ('submit' == action) {
							var contextCondition = {}, formData, url;
							var success = function(data) {
								if (qdpIsSuccess(data)) {
									root.$message.success('成功');
									$addChildForm.resetFields();
									root.addChildFormVisible = false;
									root.loadTree();
								} else {
									root.$notify({
										title : 'Form Post=>Error', message : url + '\n<br/>' + qdpToString(qdpAjaxData(data)), duration : 0, type : 'error'
									});
								}
							};
							var error = function(response) {
								root.$notify({
									title : 'Http=>Error', desc : response.url + '\n<br/>' + qdpToString(response), duration : 0
								});
							};
							var valid = function(valid) {
								if (!valid) {
									return;
								}
								root.$http.post(url, formData).then(success, error);
							};
							var then = function() {
								formData = qdpJsonForm({arg0 : root.clearForm(qdpJsonCopy(addChildForm))});
								//submit form
								url = qdpFormatUrl(ctx + root.addChildFormUrl, contextCondition);
								LOG("FN: addChildForm.url=" + url);
								$addChildForm.validate(valid);
							};
							var confirm = {
								confirmButtonText : '确定', cancelButtonText : '取消', type : 'warning'
							};
							root.$confirm('提交表单', '提示', confirm).then(then)['catch'](noop);
						}
						return;
					}//addChildForm-END
					if ('editForm' == eventType) {
						LOG("editForm:" + arg0);
						var action = arg0, $editForm = root.$refs.editForm, editForm = root.editForm;
						if ('reset' == action) {
							$editForm.resetFields();
						}
						if ('submit' == action) {
							var contextCondition = {}, formData, url;
							var success = function(data) {
								if (qdpIsSuccess(data)) {
									root.$message.success('成功');
									$editForm.resetFields();
									root.editFormVisible = false;
									root.loadTree();
								} else {
									root.$notify({
										title : 'Form Post=>Error', message : url + '\n<br/>' + qdpToString(qdpAjaxData(data)), duration : 0, type : 'error'
									});
								}
							};
							var error = function(response) {
								root.$notify({
									title : 'Http=>Error', desc : response.url + '\n<br/>' + qdpToString(response), duration : 0
								});
							};
							var valid = function(valid) {
								if (!valid) {
									return;
								}
								root.$http.post(url, formData).then(success, error);
							};
							var then = function() {
								formData = qdpJsonForm({arg0 : root.clearForm(qdpJsonCopy(editForm))});
								//submit form
								url = qdpFormatUrl(ctx + root.editFormUrl, contextCondition);
								LOG("FN: editForm.url=" + url);
								$editForm.validate(valid);
							};
							var confirm = {
								confirmButtonText : '确定', cancelButtonText : '取消', type : 'warning'
							};
							root.$confirm('提交表单', '提示', confirm).then(then)['catch'](noop);
						}
						return;
					}//editForm-END
					if ('deleteForm' == eventType) {
						var action = arg0, deleteForm = root.tree.oneSelection?[root.tree.oneSelection]:[];
						var contextCondition = {}, formData, url, messages;
						{
							formData = [], messages = [];
							if (deleteForm && deleteForm.length < 1) {
								root.$message.info("请选择数据!");
								return;
							}
							qdpMap(deleteForm, function(value) {
								formData.push(qdpId(root, value));
								messages.push(qdpLabel(root, value));
							});
						}
						var success = function(data) {
							if (qdpIsSuccess(data)) {
								root.$message.success('成功');
								root.loadTree();
							} else {
								root.$notify({
									title : 'Form Post=>Error', message : url + '\n<br/>' + qdpToString(qdpAjaxData(data)), duration : 0, type : 'error'
								});
							}
						};
						var error = function(response) {
							root.$notify({
								title : 'Http=>Error', desc : response.url + '\n<br/>' + qdpToString(response), duration : 0
							});
						};
						var then = function() {
							//submit form
							url = qdpFormatUrl(ctx + root.deleteFormUrl, contextCondition);
							LOG("FN: deleteForm.url=" + url);
							root.$http.post(url, {
								arg0 : formData.join(',')
							}).then(success, error);
						};
						var confirm = {
							confirmButtonText : '确定', cancelButtonText : '取消', type : 'warning'
						};
						root.$confirm('确定删除：' + messages.join(','), '提示', confirm).then(then)['catch'](noop);
						return;
					}//deleteForm-END
					if (eventType.endsWith('Form')) {
						LOG(eventType + ":" + arg0);
						var action = arg0, $form = root.$refs[eventType], form = root[eventType], actionBarName = eventType.substring(0, eventType
								.indexOf('Form'));
						if ('reset' == action) {
							$form.resetFields();
						}
						if ('submit' == action) {
							var contextCondition = {}, formData, url;
							var success = function(data) {
								if (qdpIsSuccess(data)) {
									root.$message.success('成功');
									root[actionBarName + 'Visible'] = false;
									root.loadTree();
									$form.resetFields();
								} else {
									root.$notify({
										title : 'Form Post=>Error', message : url + '\n<br/>' + qdpToString(qdpAjaxData(data)), duration : 0, type : 'error'
									});
								}
							};
							var error = function(response) {
								root.$notify({
									title : 'Http=>Error', desc : response.url + '\n<br/>' + qdpToString(response), duration : 0
								});
							};
							var valid = function(valid) {
								if (!valid) {
									return;
								}
								root.$http.post(url, formData).then(success, error);
							};
							var then = function() {
								formData = qdpJsonForm({arg0 : root.clearForm(qdpJsonCopy(form))});
								//submit form
								url = qdpFormatUrl(ctx + root[eventType + 'Url'], contextCondition);
								LOG("FN: form.url=" + url);
								$form.validate(valid);
							};
							var confirm = {
								confirmButtonText : '确定', cancelButtonText : '取消', type : 'warning'
							};
							root.$confirm('提交表单', '提示', confirm).then(then)['catch'](noop);
						}
						return;
					}//*Form-END
				}
			}
		};
	});
</script>

</#macro>

<#assign project=root.data.data />
<#assign removePrefix=root.rmTablePrefix!'' />
<#assign package=root.groupId />
<#assign projectName=root.artifactId />
<#assign date=.now?date?string['yyyy-MM-dd'] />
<#list project as module>
	<#assign moduleName=module.packageName />
	<#list module.children as func>
		<#assign class=javaClass(func.tableName,removePrefix)?cap_first/>
		<#list func.children as item>
			<#if item.type=='page' && item.name=='index' && item.formType=='tree'>
				<#assign table=item pk=[] notNull=[] index=[] unique=[] autoInc=[] columns=[] foreign=[]/>
				<#list table.fields as field>
					<#if field.isPk><#assign pk=pk+[field]/></#if>
					<#if !field.isNull><#assign notNull=notNull+[field]/></#if>
					<#if field.isIndex><#assign index=index+[field]/></#if>
					<#if field.isUnique><#assign unique=unique+[field]/></#if>
					<#if field.isAutoIncrease><#assign autoInc=autoInc+[field]/></#if>
					<#if !field.isNotTableColumn><#assign columns=columns+[field]/></#if>
					<#if field.refTable?has_content && field.refField?has_content && field.refLabelField?has_content><#assign foreign=foreign+[field]/></#if>
				</#list>
				<@filewriter type="html" basedir=projectdir(root.projectName)+"/src/main/webapp/resource/pages/"+packageToPath(moduleName,func.packageName,class?lower_case)
					name="index.html">
					<@page date=date package=package class=class module=moduleName table=table pk=pk notNull=notNull index=index unique=unique autoInc=autoInc columns=columns foreign=foreign func=func proModule=module project=project/>
				</@filewriter>
			</#if>
		</#list>
	</#list>
</#list>

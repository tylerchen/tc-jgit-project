<#include "/_functions.ftl" />
<#include "/_page_util.ftl" />
<#macro page date package class module table pk notNull index unique autoInc columns foreign func proModule>
<#local table=getTable(table.tableName, project).table!table mainTable={}/>
<#list func.children as item><#if item.type=='page' && item.name=='index'><#local mainTable=item/></#if></#list>
<template>
    <!-- template-2.0.0 -->
	<div class="qdp-list-layout">
		<!-- 搜索 -->
		<!-- 搜索-END -->
		<!-- 操作栏 -->
		<!-- 树 -->
		<el-row class="qdp-list-content qdp-tree" type="flex" justify="center" align="top">
			<el-col :span="24">
				<el-tree :highlight-current="true" :show-checkbox="multi" node-key="id" :data="tree.data" :default-checked-keys="tree.checked" :default-expanded-keys="tree.expanded" :props="tree.props" :default-expand-all="true"
					@node-click="onPost('tree-form', 'node-click', arguments[0], arguments[1])"
					@check-change="onPost('tree-form', 'check-change', arguments[0], arguments[1], arguments[2])"
					:render-content="renderContent" ref="tree-form">
				</el-tree>
			</el-col>
		</el-row>
		<!-- 树-END -->
		<!-- 操作栏-END- -->
		<!-- 列表 -->
		<!-- 列表-END -->
		<!-- 分页 -->
		<!-- 分页-END -->
		<!-- 添加 -->
		<!-- 添加-END -->
		<!-- 修改 -->
		<!-- 修改-END -->
		<!-- 详情 -->
		<!-- 详情-END -->
	</div>
</template>
<script type="text/javascript">
	define([ 'vue' ], function(Vue) {
		return {
			template : template, //
			//设置选择
			props:{
				value:{required:true, type:Object},
				multi:{'default':true, type:Boolean},
				field:{required:true, type:String},
				idField:{required:true, type:String},
				enums:{required:true, type:Object}
			},
			data : function() {
				return {
					searchForm : {
						${formFields(table, 'searchable')}
					},
					tree : {
						url : '/ws/json/${javaClass(table.tableName,removePrefix)?uncap_first}Application/pageFind${javaClass(table.tableName,removePrefix)}/arg0={{d.vo}}/arg1={{d.page}}',
						query : qdpObj({}, 'vo', {}, 'page', qdpObj({}, 'pageSize', 1000, 'currentPage', 1, 'orderBy', [])), oneSelection : null,
						multiSelection : [], checked:[], expanded:[], props:{label: 'name', children:'children'}, data : [], groupFields : [],checkedUrl : {
							url : '/ws/json/${class?uncap_first}Application/pageFindAssign${javaClass(table.tableName,removePrefix)}/arg0={{d.vo}}/arg1={{d.page}}', 
							query : qdpObj({}, 'vo', {}, 'page', qdpObj({}, 'pageSize', 1000, 'currentPage', 1, 'orderBy', []))
						}
					}
				};
			}, //
			created : function() {
				this.loadTree();
			}, //
			mounted : function() {
				//快捷键
			},//
			beforeDestroy : function() {
				//快捷键
			},//
			methods : {
				cache:getters.cache,//
				accountId : getters.accountId, //
				qdpIcon: function(config, value) {
	                return qdpIcon(config, value) || 'el-icon-ion-android-menu';
	            },//
				loadTree : function() {
					var root = this, tree = root.tree;
					var query = tree.query, url = tree.url, params = params || {};
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
						if ((tree.groupFields||[]).length>0||(arr && arr.length>0 && !arr[0].parentId && !arr[0].rootId && arr[0].type1 && arr[0].type2)) {
							tree.groupFields = ['type1', 'type2'];
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
						var checkedFromCache = root.cache('${javaClass(mainTable.tableName,removePrefix)?lower_case}${table.fileName}}');
						checkedFromCache && Vue.set(tree, "checked", checkedFromCache);
						if (checkedFromCache || !(tree.checkedUrl && tree.checkedUrl.url)) {
							callback(arr);
							return;
						}
						var checkedUrl = tree.checkedUrl, query2 = qdpCombine(root.queryContext(), checkedUrl.query), url2 = checkedUrl.url, prefix2="${javaClass(mainTable.tableName,removePrefix)?lower_case}";
						qdpMap(root.queryContext(), function(value, key){//把当前上下文参数传给VO
							if(key.indexOf(prefix2) == 0){
								var newKey = key.substring(prefix2.length);
								newKey = newKey.substring(0, 1).toLowerCase() + newKey.substring(1);
								(query2.vo||{})[newKey] = value;
							}else{
								(query.vo||{})[key] = value;
							}
						});
						url2 = qdpFormatUrl(ctx + url2, query2);
						LOG("FN: tree-checkedUrl.url=" + url2);
						root.$http.get(url2).then(function(data) {
							var checkedAll = qdpAjaxDataBody(data) || [], checkedArr = qdpIsArray(checkedAll) ? checkedAll : (checkedAll.rows || []);//要考虑返回是一个Page对象的情况
							var checked = [];
							qdpMap(checkedArr, function(value){
								checked.push(value.id);
							});
							Vue.set(tree, "checked", checked);
							//process tree
							callback(arr);
						}, function(response) {
							root.$Notice.open({
								title : 'Http=>Error', desc : response.url + '\n<br/>' + qdpToString(response), duration : 0
							});
							//process tree
							callback(arr);
						});
					}, function(response) {
						root.$notify({
							title : 'Http=>Error', desc : response.url + '\n<br/>' + qdpToString(response), duration : 0
						});
					});
				},// 
				getOption : function(data, field) {
					var fieldName = field.property||field,  enums = ((this.enums || {}).${table.fileName}||{})[fieldName] || {};
					var value = data[fieldName];
					var option = enums[value == null ? '' : value];
					return option == null ? (value == null ? '' : value) : option;
				},//
				renderContent: function(h, node_data_store){
					var root=this, node=node_data_store.node, data=node_data_store.data, store=node_data_store.start;
					return h('span', {'class': 'el-tree-node__label'}, [
								h('i', {'class': root.qdpIcon(root.tree, node)}),
								qdpLabel(root.tree, node)
							]);
				},
				//设置选择
				setSelection: function(selection){
					var root=this, sels = qdpIsArray(selection)?selection:(selection?[selection]:[]), ids=[];
					qdpMap(sels, function(value){
						ids.push(selection[root.idField]);
					});
					root.value[root.field] = ids.join(',');
					root.value[root.field+'Selections'] = qdpJsonCopy(sels); 
				},//
				queryContext: function(value){
					var qc = qdpSGetItem('queryContext',{});
					return qc;
				},//
				onPost : function(eventType, arg0, arg1, arg2, arg3) {
					LOG("EV: onPost." + eventType);
					var root = this;
					if ('tree-form' == eventType) {
						LOG("tree-form:" + arg0);
						var action = arg0, tree = root.tree;
						if ('check-change' == action) {
							var cheched = root.$refs['tree-form'].getCheckedNodes(true);
							{//保持选择的顺序
								var arr = tree.multiSelection || [], arrMap = {}, chechedMap = {}, newArr = [], index;
								qdpMap(arr, function(value){
									arrMap[value[root.idField]] = value;
								});
								qdpMap(cheched, function(value){
									chechedMap[value[root.idField]] = value;
								});
								qdpMap(arrMap, function(value, key){
									if(chechedMap[key]){
										newArr.push(value);
										delete chechedMap[key];
									}
								});
								qdpMap(chechedMap, function(value, key){
									newArr.push(value);
								});
								cheched = newArr;
							}
							Vue.set(tree, 'multiSelection', cheched);
							//设置选择
							root.setSelection(tree.multiSelection);
						}
						if ('node-click' == action) {
							var data=arg1, nodeData=arg2, $node=arg3;
							tree.oneSelection = data;
							//设置选择
							root.setSelection(tree.oneSelection);
						}
					}//tree-form-END
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
			<#if item.type=='page' && item.name!='index' && ['selectOneTree', 'selectMultiTree']?seq_index_of(item.formType) gt -1 >
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
					name="${item.fileName}.html">
					<@page date=date package=package class=class module=moduleName table=table pk=pk notNull=notNull index=index unique=unique autoInc=autoInc columns=columns foreign=foreign func=func proModule=module/>
				</@filewriter>
			</#if>
		</#list>
	</#list>
</#list>


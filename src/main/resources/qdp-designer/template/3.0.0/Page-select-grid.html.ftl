<#include "/_functions.ftl" />
<#include "/_page_util.ftl" />
<#macro page date package class module table pk notNull index unique autoInc columns foreign func proModule>
<#local table=getTable(table.tableName, project).table!table/>
<template>
    <!-- template-2.0.0 -->
	<div class="qdp-list-layout">
		<!-- 搜索 --><#local searchs=[]/><#list table.fields as field><#if field.searchable><#local searchs=searchs+[field]/></#if></#list>
		<#if searchs?has_content>
		<el-row class="qdp-list-content" type="flex" justify="start" align="top">
			<el-form class="qdp-search-form" :model="searchForm" label-width="100px" inline ref="searchForm" @keydown.13.native.stop="onPost('search-form', 'search')">
				<#list searchs as field>
					<#if field?index lt 2><@searchForm field=field/></#if>
				</#list>
				<el-form-item>
					<el-button size="small" type="info" @click="onPost('search-form','reset')"><i class="el-icon-ion-backspace-outline"></i></el-button>
					<el-button size="small" type="info" @click="onPost('search-form','search')"><i class="el-icon-ion-search"></i></el-button>
				</el-form-item>
			</el-form>
		</el-row>
		</#if>
		<!-- 搜索-END -->
		<!-- 操作栏 -->
		<!-- 操作栏-END- -->
		<!-- 列表 -->
		<el-row class="qdp-list-content qdp-grid" type="flex" justify="center" align="top">
			<el-table :data="grid.data.rows" style="width: 100%;" border highlight-current-row ref="grid" :height="200" :max-height="200"
				@selection-change="onPost('grid-table', 'selection-change', arguments[0])"
				@row-click="onPost('grid-table', 'row-click', arguments[0], arguments[1], arguments[2])"
				@sort-change="onPost('grid-table', 'sort-change', arguments[0])">
				<el-table-column type="selection" width="35" v-if="multi!=false"></el-table-column>
				<#list table.fields as field>
					<#if field.listable>
						<#if ['radio','checkbox','switch','select']?seq_index_of(field.type.grid) gt -1>
							<el-table-column :fixed="true" prop="${javaField(field.field)}" label="${javaFieldEnName(javaField(field.description!field.field))?capitalize}">
								<template scope="scope"><el-tag>{{getOption(scope.row, scope.column)}}</el-tag></template>
							</el-table-column>
						<#else>
							<el-table-column :fixed="true" prop="${javaField(field.field)}" label="${javaFieldEnName(javaField(field.description!field.field))?capitalize}" :sortable="${field.sortable?then('true','false')}"></el-table-column>
						</#if>
					</#if>
				</#list>
			</el-table>
		</el-row>
		<!-- 列表-END -->
		<!-- 分页 -->
		<el-row class="qdp-list-content qdp-page" type="flex" justify="end" align="top">
			<el-pagination :current-page="grid.data.currentPage" :total="grid.data.totalCount" :page-size="grid.data.pageSize" :page-sizes="[ 5, 10, 20, 50, 100 ]" layout="total, sizes, prev, pager, next, jumper" 
				@size-change="onPost('grid-page', 'size-change', arguments[0])" @current-change="onPost('grid-page', 'current-change', arguments[0])"></el-pagination>
		</el-row>
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
					grid : {
						url : '/ws/json/${javaClass(table.tableName,removePrefix)?uncap_first}Application/pageFind${javaClass(table.tableName,removePrefix)}/arg0={{d.vo}}/arg1={{d.page}}',
						query : qdpObj({}, 'vo', {}, 'page', qdpObj({}, 'pageSize', 10, 'currentPage', 1, 'orderBy', [])), oneSelection : null,
						multiSelection : [], data : {
							currentPage : 1, totalCount : 0, pageSize : 10, rows : []
						}
					}
				};
			}, //
			created : function() {
				this.loadGrid();
			}, //
			mounted : function() {
				//快捷键
			},//
			beforeDestroy : function() {
				//快捷键
			},//
			methods : {
				accountId : getters.accountId, //
				loadGrid : function() {
					var root = this, grid = root.grid;
					var query = grid.query, url = grid.url, params = params || {};
					query.page && params.orderBy && (query.page.orderBy = params.orderBy);
					query.page && qdpIsNumber(params.pageSize) && (query.page.pageSize = params.pageSize || 1) && (query.page.currentPage = 1);
					query.page && qdpIsNumber(params.currentPage) && (query.page.currentPage = params.currentPage);
					url = qdpFormatUrl(ctx + url, query);
					LOG("FN: Grid.grid-load-data.url=" + url);
					root.$http.get(url).then(function(data) {
						Vue.set(grid, 'data', qdpAjaxDataBody(data));
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
				},
				//设置选择
				setSelection: function(selection){
					var root=this, sels = qdpIsArray(selection)?selection:(selection?[selection]:[]), ids=[];
					qdpMap(sels, function(value){
						ids.push(selection[root.idField]);
					});
					root.value[root.field] = ids.join(',');
					root.value[root.field+'Selections'] = qdpJsonCopy(sels); 
				},
				onPost : function(eventType, arg0, arg1, arg2, arg3) {
					LOG("EV: onPost." + eventType);
					var root = this;
					if ("search-form" == eventType) {
						LOG("search-form:" + arg0);
						var action = arg0;
						if ('reset' == arg0) {
							root.$refs.searchForm.resetFields();
							qdpMap(root.searchForm, function(value, key) {
								root.grid.query.vo[key] = value;
							});
						}
						if ('search' == arg0) {
							qdpMap(root.searchForm, function(value, key) {
								root.grid.query.vo[key] = value;
							});
						}
						root.loadGrid();
					}
					if ('grid-page' == eventType) {
						LOG("grid-page:" + arg0);
						var action = arg0;
						if ('size-change' == action) {
							var pageSize = arg1;
							root.grid.query.page.pageSize = pageSize > 0 ? pageSize : 5;
						}
						if ('current-change' == action) {
							var currentPage = arg1;
							root.grid.query.page.currentPage = currentPage > 0 ? currentPage : 1;
						}
						root.loadGrid();
					}
					if ('grid-table' == eventType) {
						LOG("grid-table:" + arg0);
						var action = arg0;
						if ('selection-change' == action) {
							var rows = arg1, grid = root.grid, $grid = root.$refs.grid;
							if (rows.length == 1) {
								grid.oneSelection = row;
								$grid.setCurrentRow(rows[0]);
							}
							qdpMap(rows, function(value){
								var hasValue = false;
								qdpMap(grid.multiSelection, function(value2){
									hasValue = hasValue || (value ==  value2);
								});
								if(!hasValue){
									Vue.set(grid.multiSelection, grid.multiSelection.length, value);
								}
							});
							//设置选择
							root.setSelection(grid.multiSelection);
						}
						if ('row-click' == action) {
							var row = arg1, event = arg2, column = arg3, grid = root.grid, $grid = root.$refs.grid;
							if (grid.oneSelection == row) {
								$grid.setCurrentRow();
								grid.oneSelection = null;
							} else {
								grid.oneSelection = row;
								$grid.setCurrentRow(row);
							}
							//设置选择
							root.setSelection(grid.oneSelection);
						}
						if ('sort-change' == action) {
							var columnPropOrder = arg1, column = arg1.column, prop = arg1.prop, order = arg1.order;//[descending,ascending,null]
							var orderMap = qdpObj({}, 'descending', 'desc', 'ascending', 'asc', '-', '');
							root.grid.query.page.orderBy[0] = qdpObj({}, 'name', prop, 'order', orderMap[order || '-']);
							root.loadGrid();
						}
					}
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
			<#if item.type=='page' && item.name!='index' && ['selectOneGrid', 'selectMultiGrid']?seq_index_of(item.formType) gt -1 >
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


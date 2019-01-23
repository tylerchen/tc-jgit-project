<#include "/_functions.ftl" />
<#include "/_page_util.ftl" />
<#macro page date package class module table pk notNull index unique autoInc columns foreign func proModule>
<template>
	<!-- template-2.0.0 --><!-- 修改密码 -->
	<el-form class="qdp-edit-form" :model="editForm" :rules="editFormRules" label-width="100px" inline ref="editForm" @keydown.13.native.stop="onPost('editForm', 'submit')">
		<#list table.fields as field>
			<@genForm field=field formType='edit'/>
		</#list>
	</el-form>
	<!-- 修改密码-END -->
</template>
<script type="text/javascript">
	define([ 'vue' ], function(Vue) {
		return {
			template : template, //
			components : {
			},
			//设置选择
			props:{
				editForm:{required:true, type:Object},
				editFormRules:{required:true, type:Object},
				enums:{required:true, type:Object}
			},
			data : function() {
				return {
				};
			}, //
			created : function() {
			}, //
			mounted : function() {
				//快捷键
			},//
			beforeDestroy : function() {
				//快捷键
			},//
			methods : {
				cache : getters.cache,//
				accountId : getters.accountId, //
				resetFields: function(){
					this.$refs.editForm.resetFields();
				},
				validate: function(validFunc){
					this.$refs.editForm.validate(validFunc);
				},
				getOption : function(data, field) {
					var fieldName = field.property || field, enums = ((this.enums || {}).${table.fileName}||{})[fieldName] || {};
					var value = data[fieldName];
					var option = enums[value == null ? '' : value];
					return option == null ? (value == null ? '' : value) : option;
				},
				//
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
				resetSelectionLabel : function(formName, field) {
					var ref = this.$refs[formName + '-' + field], ref2 = this.$refs[formName + '-' + field + 'Name'];
					ref && ref.resetField();
					ref2 && ref2.resetField();
					this[formName][field + 'Selections'] = [];
				},
				// 热键，默认就是Ctrl+其他键
				onHotKey : function(event) {
				},//
				onPost : function(eventType, arg0, arg1, arg2, arg3) {
					LOG("EV: onPost." + eventType);
					var root = this;
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
			<#if item.type=='page' && item.name!='index' && item.formType=='form'>
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

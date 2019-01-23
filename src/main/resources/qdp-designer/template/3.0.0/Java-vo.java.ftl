<#include "/_functions.ftl" />
<#include "/_page_util.ftl" />
<#macro model date package class module table pk notNull index unique autoInc columns foreign func proModule project>
/*******************************************************************************
 * Copyright (c) ${date} @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package ${voPackage(package,module,func.packageName)};

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * ${func.name} - ${class}
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since ${date}
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@XmlRootElement(name = "${class?substring(0, class?length-2)}")
@SuppressWarnings("serial")
public class ${class} implements Serializable {

<#if table.formType=='form'><#list func.children as item><#if item.type=='page' && item.name=='index'><#list item.fields as field><#--主模型-->
<#if field.isPk>

	/** ${field.label!field.field} **/
	private ${field.javaType} ${javaField(field.field)};
</#if>
</#list></#if></#list></#if>
<#list columns as field>
	/** ${field.label!field.field} **/
	private ${field.javaType} ${javaField(field.field)};
</#list>
<#list foreign as field><#if !field.isNotTableColumn>
	<#local refTable=getTable(field.refTable, project).table/><#if refTable?has_content><#local refField=getField(field.refLabelField, refTable)/><#if refField?has_content>
	<#local refFieldName = javaField(field.field)+javaField(field.refLabelField)?cap_first />
	/** ${refField.label!refField.field} **/
	private ${refField.javaType} ${refFieldName};
	<#if field.refTable?upper_case=='AUTH_RESOURCE'><#--这是一个特殊的权限专用代码：vo-prop/vo-getset/mapping.-->
	/** ${javaField(field.field)}Code，用于权限中比较特殊的 **/
	private String ${javaField(field.field)}Code;
	</#if>
	</#if></#if>
</#if></#list>
<#list table.fields as field><#if field.isNotTableColumn>
	/** ${field.label!field.field} **/
	private ${field.javaType} ${javaField(field.field)};
	<#if field.refTable?has_content && field.refField?has_content && field.refLabelField?has_content>
	<#local refTable=getTable(field.refTable, project).table/><#if refTable?has_content><#local refField=getField(field.refLabelField, refTable)/><#if refField?has_content>
	<#local refFieldName = javaField(field.field)+javaField(field.refLabelField)?cap_first />
	/** ${refField.label!refField.field} **/
	private ${refField.javaType} ${refFieldName};
	</#if></#if></#if>
</#if></#list>

	public ${class}() {
	}
<#if table.formType=='form'><#list func.children as item><#if item.type=='page' && item.name=='index'><#list item.fields as field><#--主模型-->
<#if field.isPk><#local javaFieldName=javaField(field.field)/>

	public ${field.javaType} get${javaFieldName?cap_first}() {
		return ${javaFieldName};
	}

	public void set${javaFieldName?cap_first}(${field.javaType} ${javaFieldName}) {
		this.${javaFieldName} = ${javaFieldName};
	}

</#if>
</#list></#if></#list></#if>
<#list columns as field>
	<#local javaFieldName=javaField(field.field)/>
	public ${field.javaType} get${javaFieldName?cap_first}() {
		return ${javaFieldName};
	}

	public void set${javaFieldName?cap_first}(${field.javaType} ${javaFieldName}) {
		this.${javaFieldName} = ${javaFieldName};
	}

</#list>
<#list foreign as field><#if !field.isNotTableColumn>
	<#local refTable=getTable(field.refTable, project).table/><#if refTable?has_content><#local refField=getField(field.refLabelField, refTable)/><#if refField?has_content>
	<#local refFieldName = javaField(field.field)+javaField(field.refLabelField)?cap_first />
	public ${refField.javaType} get${refFieldName?cap_first}() {
		return ${refFieldName};
	}

	public void set${refFieldName?cap_first}(${refField.javaType} ${refFieldName}) {
		this.${refFieldName} = ${refFieldName};
	}
	<#if field.refTable?upper_case=='AUTH_RESOURCE'><#--这是一个特殊的权限专用代码：vo-prop/vo-getset/mapping.-->
	
	public String get${javaField(field.field)?cap_first}Code() {
		return ${javaField(field.field)}Code;
	}

	public void set${javaField(field.field)?cap_first}Code(String code) {
		this.${javaField(field.field)}Code = code;
	}
	</#if>
	</#if></#if>
</#if></#list>
<#list table.fields as field><#if field.isNotTableColumn>
	<#local javaFieldName=javaField(field.field)/>
	public ${field.javaType} get${javaFieldName?cap_first}() {
		return ${javaFieldName};
	}

	public void set${javaFieldName?cap_first}(${field.javaType} ${javaFieldName}) {
		this.${javaFieldName} = ${javaFieldName};
	}
	<#if field.refTable?has_content && field.refField?has_content && field.refLabelField?has_content>
	<#local refTable=getTable(field.refTable, project).table/><#if refTable?has_content><#local refField=getField(field.refLabelField, refTable)/><#if refField?has_content>
	<#local refFieldName = javaField(field.field)+javaField(field.refLabelField)?cap_first />
	public ${refField.javaType} get${refFieldName?cap_first}() {
		return ${refFieldName};
	}

	public void set${refFieldName?cap_first}(${refField.javaType} ${refFieldName}) {
		this.${refFieldName} = ${refFieldName};
	}
	</#if></#if></#if>
</#if></#list>

}
</#macro>

<#assign project=root.data.data />
<#assign removePrefix=root.rmTablePrefix!'' />
<#assign package=root.groupId />
<#assign projectName=root.artifactId />
<#assign date=.now?date?string['yyyy-MM-dd'] />
<#list project as module>
	<#assign moduleName=module.packageName />
	<#list module.children as func>
		<#assign class=javaClass(func.tableName,removePrefix)?cap_first+'VO'/>
		<#list func.children as item>
			<#--主模型-->
			<#if item.type=='page' && item.name=='index' && (item.formType=='grid' || item.formType=='tree' || item.formType=='domainOnly')>
				<#assign class=javaClass(func.tableName,removePrefix)?cap_first+'VO'/>
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
				<@filewriter type="java" basedir=projectdir(root.projectName)+"/src/main/java/"
					name="${voPackage(package, moduleName,func.packageName)}.${class}.java">
					<@model date=date package=package class=class module=moduleName table=table pk=pk notNull=notNull index=index unique=unique autoInc=autoInc columns=columns foreign=foreign func=func proModule=module project=project/>
				</@filewriter>
			</#if>
			<#--弹出表单-->
			<#if item.type=='page' && item.name!='index' && item.formType=='form'>
				<#assign class=javaClass(columnField(item.fileName),'')?cap_first+'VO'/>
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
				<@filewriter type="java" basedir=projectdir(root.projectName)+"/src/main/java/"
					name="${voPackage(package, moduleName,func.packageName)}.${class}.java">
					<@model date=date package=package class=class module=moduleName table=table pk=pk notNull=notNull index=index unique=unique autoInc=autoInc columns=columns foreign=foreign func=func proModule=module project=project/>
				</@filewriter>
			</#if>
		</#list>
	</#list>
</#list>


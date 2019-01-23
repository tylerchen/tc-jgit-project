<#include "/_functions.ftl" />
<#macro edas>
<#noparse>
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:tx="http://www.springframework.org/schema/tx" xmlns:security="http://www.springframework.org/schema/security"
	xmlns:p="http://www.springframework.org/schema/p" xmlns:hsf="http://www.taobao.com/hsf"
	xsi:schemaLocation="http://www.springframework.org/schema/security
		http://www.springframework.org/schema/security/spring-security-3.2.xsd
		http://www.springframework.org/schema/beans
		http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
		http://www.springframework.org/schema/tx
		http://www.springframework.org/schema/tx/spring-tx-3.2.xsd
		http://www.springframework.org/schema/context
		http://www.springframework.org/schema/context/spring-context-3.2.xsd
		http://www.taobao.com/hsf
		http://www.taobao.com/hsf/hsf.xsd">
	<description>
	<![CDATA[
		EDAS
	]]>
	</description>
</#noparse>
<#list project as module>
	<#assign moduleName=module.packageName />
	<#list module.children as func>
		<#assign class=javaClass(func.tableName,removePrefix)?cap_first/>
		<#list func.children as item>
			<#if item.type=='page' && item.name=='index' && (item.formType=='grid' || item.formType=='tree')>
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
	<hsf:provider id="${class?uncap_first}ApplicationEDAS"
		   interface="${rsPackage(package,moduleName,func.packageName)}.${class}Application"
		         ref="${class?uncap_first}Application"
		       group="system" />
			</#if>
		</#list>
	</#list>
</#list>

</beans>

</#macro>

<#assign project=root.data.data />
<#assign removePrefix=root.rmTablePrefix!'' />
<#assign package=root.groupId />
<#assign projectName=root.artifactId />
<#assign date=.now?date?string['yyyy-MM-dd'] />
<@filewriter type="xml" basedir=projectdir(root.projectName)+"/src/main/resources/META-INF/spring-edas-provider/"
	name="edas-provider-system.xml">
	<@edas/>
</@filewriter>


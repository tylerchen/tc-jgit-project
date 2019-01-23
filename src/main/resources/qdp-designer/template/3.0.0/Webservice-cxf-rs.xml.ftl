<#include "/_functions.ftl" />
<#macro cxfRs date package class module table pk notNull index unique autoInc columns foreign func proModule>
<#noparse>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:http="http://cxf.apache.org/transports/http/configuration"
	xmlns:util="http://www.springframework.org/schema/util" xmlns:jaxrs="http://cxf.apache.org/jaxrs"
	xmlns:cxf="http://cxf.apache.org/core"
	xsi:schemaLocation="
	http://www.springframework.org/schema/beans 
	http://www.springframework.org/schema/beans/spring-beans.xsd 
	http://cxf.apache.org/transports/http/configuration 
	http://cxf.apache.org/schemas/configuration/http-conf.xsd
	http://www.springframework.org/schema/util 
	http://www.springframework.org/schema/util/spring-util.xsd http://cxf.apache.org/jaxrs
	http://cxf.apache.org/schemas/jaxrs.xsd
	http://cxf.apache.org/core
	http://cxf.apache.org/schemas/core.xsd"
	default-autowire="byName">
</#noparse>
	<jaxrs:server id="${class?uncap_first}ApplicationRs" address="/${class?lower_case}">
		<jaxrs:serviceBeans>
			<ref bean="${class?uncap_first}RsApplication" />
		</jaxrs:serviceBeans>
		<!-- JSON PROVIDER -->
		<jaxrs:providers>
			<ref bean="jsonProvider" />
		</jaxrs:providers>
	</jaxrs:server>
</beans>

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
				<@filewriter type="xml" basedir=projectdir(root.projectName)+"/src/main/resources/META-INF/spring-rs/"
					name="${class}Application-webservice-cxf.xml">
					<@cxfRs date=date package=package class=class module=moduleName table=table pk=pk notNull=notNull index=index unique=unique autoInc=autoInc columns=columns foreign=foreign func=func proModule=module/>
				</@filewriter>
			</#if>
		</#list>
	</#list>
</#list>


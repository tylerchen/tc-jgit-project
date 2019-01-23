<#include "/_functions.ftl" />
<#macro message date package class module table pk notNull index unique autoInc columns foreign func proModule>
${class} = ${func.name}
<#list columns as field>
${class}.${javaField(field.field)} = ${javaFieldEnName(javaField(field.description!field.field))?capitalize}
</#list>
<#list table.fields as field><#if field.isNotTableColumn>
${class}.${javaField(field.field)} = ${javaFieldEnName(javaField(field.description!field.field))?capitalize}
</#if></#list>

${class}.en = ${javaFieldEnName(class)}
<#list columns as field>
${class}.${javaField(field.field)}.en = ${javaFieldEnName(javaField(field.field))?capitalize}
</#list>
<#list table.fields as field><#if field.isNotTableColumn>
${class}.${javaField(field.field)}.en = ${javaFieldEnName(javaField(field.field))?capitalize}
</#if></#list>
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
			<#if item.type=='page' && item.name=='index'>
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
				<@filewriter type="java" basedir=projectdir(root.projectName)+"/src/main/resources/META-INF/i18n/${moduleName}/"
					name="${class}.properties">
					<@message date=date package=package class=class module=moduleName table=table pk=pk notNull=notNull index=index unique=unique autoInc=autoInc columns=columns foreign=foreign func=func proModule=module/>
				</@filewriter>
			</#if>
		</#list>
	</#list>
</#list>


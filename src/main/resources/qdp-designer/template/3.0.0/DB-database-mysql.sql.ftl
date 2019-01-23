<#include "/_functions.ftl" />
<#macro database date package>
set character set utf8;
SET foreign_key_checks = 0;

<#list project as module>
	<#local moduleName=module.packageName />
	<#list module.children as func>
		<#local class=javaClass(func.tableName,removePrefix)?cap_first/>
		<#list func.children as item>
			<#if item.type=='page' && item.name=='index'>
				<#local table=item pk=[] notNull=[] index=[] unique=[] autoInc=[] columns=[] foreign=[]/>
				<#list table.fields as field>
					<#if field.isPk><#local pk=pk+[field]/></#if>
					<#if !field.isNull><#local notNull=notNull+[field]/></#if>
					<#if field.isIndex><#local index=index+[field]/></#if>
					<#if field.isUnique><#local unique=unique+[field]/></#if>
					<#if field.isAutoIncrease><#local autoInc=autoInc+[field]/></#if>
					<#if !field.isNotTableColumn><#local columns=columns+[field]/></#if>
					<#if field.refTable?has_content && field.refField?has_content && field.refLabelField?has_content && !field.isNotTableColumn><#local foreign=foreign+[field]/></#if>
				</#list>
				<#local contents=[] indexs=[]/>
DROP TABLE IF EXISTS `${table.tableName?upper_case}`;
CREATE TABLE `${table.tableName?upper_case}` (
				<#-- 定义主键 -->
				<#list pk as field>
					<#local col=field.field?upper_case />
					<#local colType=field.dbType?upper_case javaType=field.javaType/>
					<#local isStr=(javaType=='String') isScale=(colType == 'DECIMAL' || colType == 'NUMERIC')  len=field.length?has_content?then(field.length, '0')?number />
					<#local hasScale=field.scale?has_content lenable=((isStr||isScale) && (len gt 0))/>
					<#local colLen=(lenable && !hasScale)?then("(${field.length})", "")/> 
					<#local colScale=(lenable && isScale && hasScale)?then("(${field.length}, ${field.scale})", "")/>
					<#local colNull=field.isNull?then("", "NOT NULL")/>
					<#local colAutoPk=field.isAutoIncrease?then("AUTO_INCREMENT", "")/>
					<#local colComment=field.description!field.label!field.field/>
					<#local content>    `${col}` ${colType}${colLen}${colScale} ${colNull} ${colAutoPk} COMMENT '${colComment}'</#local>
					<#local contents = contents+[content]/>
				</#list>
				<#-- 定义普通列 -->
				<#list columns as field><#if !field.isPk>
					<#local col=field.field?upper_case />
					<#local colType=field.dbType?upper_case javaType=field.javaType/>
					<#local isStr=(javaType=='String') isScale=(colType == 'DECIMAL' || colType == 'NUMERIC')  len=field.length?has_content?then(field.length, '0')?number />
					<#local hasScale=field.scale?has_content lenable=((isStr||isScale) && (len gt 0))/>
					<#local colLen=(lenable && !hasScale)?then("(${field.length})", "")/> 
					<#local colScale=(lenable && isScale && hasScale)?then("(${field.length}, ${field.scale})", "")/>
					<#local colNull=field.isNull?then("", "NOT NULL")/>
					<#local defVal=field.defValue!'' hasDef=defVal?has_content colDefault=''>
					<#local colDefault=(hasDef&&defVal=='NULL')?then("DEFAULT NULL", colDefault) colDefault=(hasDef&&defVal!='NULL')?then("DEFAULT ${getDefaultValue(field)}", colDefault)/>
					<#local colAutoPk=field.isAutoIncrease?then("AUTO_INCREMENT", "")/>
					<#local colComment=field.description!field.label!field.field/>
					<#local content>    `${col}` ${colType}${colLen}${colScale} ${colNull} ${colAutoPk} ${colDefault} COMMENT '${colComment}'</#local>
					<#local contents = contents+[content]/>
				</#if></#list>
				<#-- PRIMARY KEY xxx, 设置主键 -->
				<#list pk as field>
					<#local content>    PRIMARY KEY (`${field.field?upper_case}`)</#local>
					<#local contents = contents+[content]/>
				</#list>
				<#-- KEY xxx, 设置索引 -->
				<#list index as field>
					<#local content>    INDEX (`${field.field?upper_case}`)</#local>
					<#local contents = contents+[content]/>
				</#list>
				<#-- UNIQUE KEY xxx, 设置唯一约束 -->
				<#list unique as field>
					<#local content>    UNIQUE KEY (`${field.field?upper_case}`)</#local>
					<#local contents = contents+[content]/>
				</#list>
				<#-- FOREIGN KEY xxx REFERENCES xxx, 设置外键 -->
				<#list foreign as field>
					<#local content>    FOREIGN KEY (`${field.field?upper_case}`) REFERENCES `${field.refTable?upper_case}` (`${field.refField?upper_case}`)</#local>
					<#local contents = contents+[content]/>
				</#list>
${contents?join(", \n")}
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='${func.name!table.remarks!(table.tableName?upper_case)}';


			</#if>
		</#list>
	</#list>
</#list>
SET foreign_key_checks = 1;
</#macro>

<#assign project=root.data.data />
<#assign removePrefix=root.rmTablePrefix!'' />
<#assign package=root.groupId />
<#assign projectName=root.artifactId />
<#assign date=.now?date?string['yyyy-MM-dd'] />
<@filewriter type="sql" basedir=projectdir(root.projectName)+"/src/main/resources/database"
	name="050-${root.projectName?lower_case}-table-mysql.sql">
	<@database date=date package=package/>
</@filewriter>


<#assign basedir=root.projectBaseDir!"/Users/zhaochen/dev/workspace/cocoa/" />
<#assign isService=(root.projectType!"")=="service" />
<#assign isWeb=(root.projectType!"")=="web" />
<#assign isCode=(root.projectType!"")=="code" />
<@mvel var="aa">System.out.println("${basedir}");</@mvel>
<#assign removePrefix="qdp" />
<#function projectdir projectName>
	<#local parentdir=basedir?has_content?then(basedir,'/tmp/') />
	<#local dir=parentdir?ends_with("/")?then(parentdir+projectName,parentdir+"/"+projectName) />
	<#return dir/>
</#function>

<#function columnField javaFiedName>
	<#local name="" strs=helper('org.apache.commons.lang3.StringUtils').splitByCharacterTypeCamelCase(javaFiedName)/>
	<#list strs as str>
		<#local name=name+'_'+str />
	</#list>
	<#return name?capitalize/>
</#function>
<#function javaField columnName>
	<#local name="" />
	<#list columnName?split('_') as part>
		<#local name=name+(part?is_first?then(part?lower_case,part?lower_case?cap_first)) />
	</#list>
	<#return name?uncap_first/>
</#function>
<#function javaFieldEnName javaFieldName>
	<#local names=helper("org.apache.commons.lang3.StringUtils").splitByCharacterTypeCamelCase(javaFieldName)/>
	<#return names?join(" ")?capitalize/>
</#function>
<#function javaClass modelName removePrefix="">
	<#local name=""/>
	<#if removePrefix?has_content>
		<#list removePrefix?split(',') as sp>
			<#local sp=sp?trim />
			<#if sp?has_content && modelName?upper_case?starts_with(sp?upper_case)>
				<#local modelName=modelName?substring(sp?length) />
				<#break/>
			</#if>
		</#list>
	</#if>
	<#list modelName?split('_') as part>
		<#local name=name+(part?is_first?then(part?lower_case,part?lower_case?cap_first)) />
	</#list>
	<#return name?cap_first/>
</#function>

<#function voPackage package module funcPackage="">
	<#return "${package}.infra.vo.${module}${funcPackage?has_content?then('.'+funcPackage?trim,'')}"/>
</#function>
<#function domainPackage package module funcPackage="">
	<#return "${package}.domain.${module}${funcPackage?has_content?then('.'+funcPackage?trim,'')}"/>
</#function>
<#function appPackage package module funcPackage="">
	<#return "${package}.application.${module}${funcPackage?has_content?then('.'+funcPackage?trim,'')}"/>
</#function>
<#function appimplPackage package module funcPackage="">
	<#return "${package}.application.${module}${funcPackage?has_content?then('.'+funcPackage?trim,'')}.impl"/>
</#function>
<#function controllerPackage package module funcPackage="">
	<#return "${package}.web.${module}${funcPackage?has_content?then('.'+funcPackage?trim,'')}"/>
</#function>
<#function wsPackage package module funcPackage="">
	<#return "${package}.application.${module}${funcPackage?has_content?then('.'+funcPackage?trim,'')}.ws"/>
</#function>
<#function wsImplPackage package module funcPackage="">
	<#return "${package}.application.${module}${funcPackage?has_content?then('.'+funcPackage?trim,'')}.ws.impl"/>
</#function>
<#function rsPackage package module funcPackage="">
	<#return "${package}.application.${module}${funcPackage?has_content?then('.'+funcPackage?trim,'')}.rs"/>
</#function>
<#function rsImplPackage package module funcPackage="">
	<#return "${package}.application.${module}${funcPackage?has_content?then('.'+funcPackage?trim,'')}.rs.impl"/>
</#function>

<#function packageToPath pkg1 pkg2='' pkg3='' pkg4='' pkg5=''>
	<#local pkg=pkg1+pkg2?has_content?then('.'+pkg2?trim,'')+pkg3?has_content?then('.'+pkg3?trim,'')+pkg4?has_content?then('.'+pkg4?trim,'')+pkg5?has_content?then('.'+pkg5?trim,'')/>
	<#return pkg?replace(".","/")/>
</#function>

<#function getDefaultValue field>
	<#local numberTypes='|Integer|Long|Double|BigDecimal|Boolean|Float|Short|Byte|byte[]|'/>
	<#local defValue=field.defValue javaType=field.javaType isNumberType=numberTypes?index_of(javaType) gt -1/>
	<#return isNumberType?then(defValue,"'${defValue}'")/>
</#function>

<#macro genSetSelect class id name>
<#noparse><@jstm></#noparse>
	function setSelect${class}(id,name){
		$('#${id}').val(id);
		$('#${name}').val(name);
	}
<#noparse></@jstm></#noparse>
</#macro>

<#function oracleType javaType="">
	<#return javaType?switch(
		 "Boolean"				, "BIT"
		,"Integer"				, "INT"
		,"Long"					, "BIGINT"
		,"Double"				, "DOUBLE"
		,"Float"				, "REAL"
		,"java.math.BigDecimal"	, "NUMERIC"
		,"java.util.Date"		, "DATE"
		,"Short"				, "SMALLINT"
		,"String"				, "VARCHAR2"
		,"VARCHAR2"
	)/>
</#function>
<#function toOracleType jdbcType="">
	<#return jdbcType?switch(
		 "VARCHAR"				, "VARCHAR2"
		,"CHAR"					, "CHAR"
		,"DATETIME"				, "DATE"
		,"TIMESTAMP"			, "TIMESTAMP"
		,"INT"					, "NUMBER"
		,"BOOLEAN"				, "BIT"
		,"SMALLINT"				, "NUMBER"
		,"DOUBLE"				, "DOUBLE"
		,"FLOAT"				, "FLOAT"
		,"DECIMAL"				, "NUMBER"
		,"BLOB"					, "BLOB"
		,"MEDIUMBLOB"			, "BLOB"
		,"LONGBLOB"				, "BLOB"
		,"TEXT"					, "CLOB"
		,"MEDIUMTEXT"			, "CLOB"
		,"LONGTEXT"				, "CLOB"
		,"CLOB"					, "CLOB"
		,"VARCHAR2"
	)/>
</#function>
<#function toMysqlType jdbcType="">
	<#return jdbcType?switch(
		 "VARCHAR"				, "VARCHAR"
		,"CHAR"					, "CHAR"
		,"DATETIME"				, "DATETIME"
		,"TIMESTAMP"			, "TIMESTAMP"
		,"INT"					, "INT"
		,"BOOLEAN"				, "BOOLEAN"
		,"SMALLINT"				, "SMALLINT"
		,"DOUBLE"				, "DOUBLE"
		,"FLOAT"				, "FLOAT"
		,"DECIMAL"				, "DECIMAL"
		,"BLOB"					, "BLOB"
		,"MEDIUMBLOB"			, "MEDIUMBLOB"
		,"LONGBLOB"				, "LONGBLOB"
		,"TEXT"					, "TEXT"
		,"MEDIUMTEXT"			, "MEDIUMTEXT"
		,"LONGTEXT"				, "LONGTEXT"
		,"CLOB"					, "LONGTEXT"
		,"VARCHAR"
	)/>
</#function>
<#function mybatisJdbcType javaType="">
	<#return javaType?switch(
		 "String"				, "VARCHAR"
		,"java.math.BigDecimal"	, "NUMERIC"
		,"boolean"				, "BIT"
		,"Boolean"				, "BIT"
		,"byte"					, "TINYINT"
		,"Byte"					, "TINYINT"
		,"short"				, "SMALLINT"
		,"Short"				, "SMALLINT"
		,"int"					, "INTEGER"
		,"Integer"				, "INTEGER"
		,"long"					, "BIGINT"
		,"Long"					, "BIGINT"
		,"float"				, "REAL"
		,"Float"				, "REAL"
		,"double"				, "FLOAT"
		,"Double"				, "FLOAT"
		,"byte[]"				, "BINARY"
		,"[b"					, "BINARY"
		,"java.util.Date"		, "DATE"
		,"java.sql.Date"		, "DATE"
		,"java.sql.Time"		, "TIME"
		,"java.sql.Timestamp"	, "TIMESTAMP"
		,"java.sql.Clob"		, "CLOB"
		,"java.sql.Blob"		, "BLOB"
		,"java.sql.Array"		, "ARRAY"
		,"java.sql.Struct"		, "STRUCT"
		,"java.sql.Ref"			, "REF"
		,"java.net.URL"			, "DATALINK"
		,"VARCHAR"
	)/>
</#function>

<#function genCondition field alias="">
	<#assign jdbcType=",jdbcType=${mybatisJdbcType(field.javaType)}" />
	<#return r"#{" + "${alias}${(alias?length gt 0)?then('.','')}${javaField(field.field)}${jdbcType}" + "}"/>
</#function>

<#macro formatColumns space="">
	<#local columns><#nested/></#local>
	<#local fieldNames=[] ases=[] aliases=[] maxLen=0/>
	<#list columns?split(',') as column>
		<#local columnSplit=column?trim?split(' ') />
		<#if columnSplit?size == 1 >
			<#local a=columnSplit[0]?trim/>
			<#local fieldNames=fieldNames+[a] ases=ases+['AS'] aliases=aliases+[a] maxLen=(maxLen < a?length)?then(a?length, maxLen)/>
		</#if>
		<#if columnSplit?size == 2 >
			<#local a=columnSplit[0]?trim c=columnSplit[1]?trim/>
			<#local fieldNames=fieldNames+[a] ases=ases+['AS'] aliases=aliases+[c] maxLen=(maxLen < a?length)?then(a?length, maxLen)/>
		</#if>
		<#if columnSplit?size == 3 >
			<#local a=columnSplit[0]?trim b=columnSplit[1]?trim c=columnSplit[2]?trim/>
			<#local fieldNames=fieldNames+[a] ases=ases+[b] aliases=aliases+[c] maxLen=(maxLen < a?length)?then(a?length, maxLen)/>
		</#if>
	</#list>
	<#list fieldNames as column>
${space}${(column?index==0)?then(' ',',')}${column?right_pad(maxLen+4)} ${ases[column?index]} ${aliases[column?index]}
	</#list>
</#macro>


<#macro formatCode align start='' end='' split='\n' leftSpace=' ' rightSpace=' '>
	<#local aligns=align?is_sequence?then(align, [align]) />
	<#local content><#nested/></#local><#local content=content?trim />
	<#list aligns as alignment>
		<#local maxBeforeLen=0 maxAfterLen=0 befores=[] afters=[] result=[]/>
		<#list content?split('\n') as line>
			<#list line?trim?split(split) as item>
				<#local item=item?trim/>
				<#if item?index_of(alignment) gt -1>
					<#local before=item?substring(0, item?index_of(alignment))?trim after=item?substring(item?index_of(alignment)+alignment?length)?trim />
					<#local befores=befores+[before?trim] afters=afters+[after?trim] />
					<#local maxBeforeLen=(maxBeforeLen gt before?length)?then(maxBeforeLen, before?length)/>
					<#local maxAfterLen=(maxAfterLen gt before?length)?then(maxAfterLen, before?length)/>
				<#else>
					<#if item?length gt 0>
						<#local befores=befores+[item] afters=afters+[''] />
					</#if>
				</#if>
			</#list>
		</#list>
		<#list befores as before>
		<#local tmpresult>
${start}${before?right_pad(maxBeforeLen)}${leftSpace}${(afters[before?index]?length gt 0)?then(alignment,'')}${rightSpace}${afters[before?index]?right_pad(maxAfterLen)}${before?is_last?then('',end)}
		</#local>
		<#local result=result+[tmpresult]/>
		</#list>
		<#local content=result?join('')/>
	</#list>
${content}
</#macro>
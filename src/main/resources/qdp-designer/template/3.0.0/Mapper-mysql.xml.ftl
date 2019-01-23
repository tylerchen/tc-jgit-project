<#include "/_functions.ftl" />
<#include "/_page_util.ftl" />
<#macro mapper date package class module table pk notNull index unique autoInc columns foreign func proModule project>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${class}">
	<resultMap id="${class}" type="${domainPackage(package,module,func.packageName)}.${class}">
		<@formatCode align=['property=','column='] start='        ' end='' split='\n' leftSpace=' ' rightSpace=''>
			<#list pk as field><#local javaFieldName=javaField(field.field)/>
				<#if field?index==0>
					<id property="${javaFieldName}" column="${field.field?upper_case}"/>
				<#else>
					<!-- Not Support Multi Primary Key Yet, for column ${field.field}. -->
				</#if>
			</#list>
			<#list columns as field><#if !field.isPk><#local javaFieldName=javaField(field.field)/>
					<result property="${javaFieldName}" column="${field.field?upper_case}"/>
			</#if></#list>
		</@formatCode>
	</resultMap>
	<sql id="orderBy">
		<if test="page !=null and page.orderBy!=null">
			<#local tmpNames=[] namesMap={}/>
			<#list columns as field><#local javaFieldName=javaField(field.field)/>
				<#local namesMap=namesMap+{"'${javaFieldName?upper_case}'":"'${field.field?upper_case}'", "'${field.field?upper_case}'":"'${field.field?upper_case}'"} />
			</#list>
			<#list namesMap?keys as key>
				<#local tmpNames=tmpNames+[key+':'+namesMap[key]]/>
			</#list>
			<#noparse><bind name="fieldColMap" value="#{</#noparse>
				<@formatCode align=[':'] start='           ' end=',' split=',' leftSpace=' ' rightSpace=' '>
				${tmpNames?join(',')}
				</@formatCode>
			}"/>
			<#noparse>
			<bind name="orderMap" value="#{'ASC':'ASC', 'DESC':'DESC'}"/>
			<trim prefix="order by " suffixOverrides=",">
				<foreach item="item" index="index" collection="page.orderBy" separator=",">
					<if test="item != null and item.name !=null and (fieldColMap[item.name.toUpperCase()] != null or orderMap[item.order.toUpperCase()]!=null)">${aliasDot}${fieldColMap[item.name.toUpperCase()]} ${orderMap[item.order.toUpperCase()]}</if>
				</foreach>
			</trim>
			</#noparse>
		</if>
	</sql>
	<select id="get${class}ById" parameterType="${domainPackage(package,module,func.packageName)}.${class}" resultMap="${class}">
		SELECT * 
		FROM ${table.tableName?upper_case}
		<where>
			<#list pk as field >
				AND ${field.field?upper_case} = ${genCondition(field)}
			</#list>
		</where>
	</select>
	<select id="get${class}MapById" parameterType="${domainPackage(package,module,func.packageName)}.${class}" resultType="hashmap">
		<#local aliasNames=[]/>
		<#list columns as field>
			<#local aliasNames=aliasNames+[class+"."+field.field?upper_case+" AS "+javaField(field.field)]/>
		</#list>
		SELECT 
			<@formatCode align=[' AS '] start='           ' end=',' split=',' leftSpace=' ' rightSpace=' '>
				${aliasNames?join(", ")}
				<#list foreign as field><#local javaFieldName=javaField(field.field)/><#if !field.isNotTableColumn>
					, ${javaFieldName}${javaClass(field.refTable,removePrefix)}.${field.refLabelField?upper_case} AS ${javaFieldName}${javaField(field.refLabelField)?cap_first}
				</#if></#list>
			</@formatCode>
		FROM ${table.tableName?upper_case} ${class}
		<@formatCode align=[' ON ', ' = '] start='		' end='' split='\n' leftSpace='' rightSpace=''>
		<#list foreign as field><#local javaFieldName=javaField(field.field)/><#if !field.isNotTableColumn>
		LEFT JOIN ${field.refTable?upper_case} ${javaFieldName}${javaClass(field.refTable,removePrefix)} ON ${class}.${field.field?upper_case} = ${javaFieldName}${javaClass(field.refTable,removePrefix)}.${field.refField}
		</#if></#list>
		</@formatCode>
		<where>
			<#list pk as field>
				AND ${class?uncap_first}.${field.field?upper_case} = ${genCondition(field)}
			</#list>
		</where>
	</select>
	<select id="pageFind${class}" resultMap="${class}">
		SELECT *
		FROM ${table.tableName?upper_case}
		<where>
			<@formatCode align=['">'] start='           ' end='' split='\n' leftSpace=' ' rightSpace=' '>
				<#list columns as field><#local javaFieldName=javaField(field.field)/>
					<#if field.searchable && field.javaType=='String'>
					<if test="vo!=null and vo.${javaFieldName} != null and vo.${javaFieldName} != ''"> AND ${field.field?upper_case} LIKE CONCAT('%',${genCondition(field, "vo")},'%') </if>
					<#else>
					<if test="vo!=null and vo.${javaFieldName} != null and vo.${javaFieldName} != ''"> AND ${field.field?upper_case} = ${genCondition(field, "vo")} </if>
					</#if>
				</#list>
			</@formatCode>
		</where>
		<bind name="aliasDot" value="''"/><include refid="orderBy"/>
	</select>
	<select id="pageFind${class}Map" resultType="hashmap">
		<#local aliasNames=[]/>
		<#list columns as field>
			<#local aliasNames=aliasNames+[class+"."+field.field?upper_case+" AS "+javaField(field.field)]/>
		</#list>
		SELECT 
			<@formatCode align=[' AS '] start='           ' end=',' split=',' leftSpace=' ' rightSpace=' '>
				${aliasNames?join(", ")}
				<#list foreign as field><#local javaFieldName=javaField(field.field)/><#if !field.isNotTableColumn>
					, ${javaFieldName}${javaClass(field.refTable,removePrefix)}.${field.refLabelField?upper_case} AS ${javaFieldName}${javaField(field.refLabelField)?cap_first}
					<#if field.refTable?upper_case=='AUTH_RESOURCE'><#--这是一个特殊的权限专用代码-->
						, ${javaFieldName}${javaClass(field.refTable,removePrefix)}.CODE AS ${javaFieldName}Code
					</#if>
				</#if></#list>
			</@formatCode>
		FROM ${table.tableName?upper_case} ${class}
		<@formatCode align=[' ON ', ' = '] start='		' end='' split='\n' leftSpace='' rightSpace=''>
		<#list foreign as field><#local javaFieldName=javaField(field.field)/><#if !field.isNotTableColumn>
		LEFT JOIN ${field.refTable?upper_case} ${javaFieldName}${javaClass(field.refTable,removePrefix)} ON ${class}.${field.field?upper_case} = ${javaFieldName}${javaClass(field.refTable,removePrefix)}.${field.refField}
		</#if></#list>
		</@formatCode>
		<where>
			<@formatCode align=['">'] start='           ' end='' split='\n' leftSpace=' ' rightSpace=' '>
				<#list columns as field><#local javaFieldName=javaField(field.field)/>
					<#if field.searchable && field.javaType=='String'>
					<if test="vo!=null and vo.${javaFieldName} != null and vo.${javaFieldName} != ''"> AND ${class}.${field.field?upper_case} LIKE CONCAT('%',${genCondition(field, "vo")},'%') </if>
					<#else>
					<if test="vo!=null and vo.${javaFieldName} != null and vo.${javaFieldName} != ''"> AND ${class}.${field.field?upper_case} = ${genCondition(field, "vo")} </if>
					</#if>
				</#list>
			</@formatCode>
		</where>
		<bind name="aliasDot" value="'${class}.'"/><include refid="orderBy"/>
	</select>
	<insert id="insert${class}" parameterType="${domainPackage(package,module,func.packageName)}.${class}" ${pk?has_content?then(' useGeneratedKeys="true" keyProperty="${javaField(pk[0].field)}"','')} >
		<#local names=[] values=[]/>
		<#list columns as field>
			<#local names=names+[field.field?upper_case]/>
			<#local values=values+[genCondition(field)]/>
		</#list>
		INSERT INTO ${table.tableName?upper_case}
			( ${names?join(", ")} )
		VALUES
			(
			<@formatCode align=['#{'] start='           ' end='},' split='},' leftSpace='' rightSpace=''>
			${values?join(", ")}
			</@formatCode>
			)
	</insert>
	<update id="update${class}" parameterType="${domainPackage(package,module,func.packageName)}.${class}">
		UPDATE ${table.tableName?upper_case}
		<set>
			<@formatCode align=['\">'] start='           ' end='' split='\n' leftSpace=' ' rightSpace=' '>
				<#list columns as field><#local javaFieldName=javaField(field.field)/>
					<#if field.isNull >
					${field.field?upper_case} = ${genCondition(field)},
					<#else>
					<if test="${javaFieldName} != null">${field.field?upper_case} = ${genCondition(field)},</if>
					</#if>
				</#list>
			</@formatCode>
		</set>
		<where>
			<#list pk as field>
				AND ${field.field?upper_case} = ${genCondition(field)}
			</#list>
		</where>
	</update>
	<delete id="delete${class}" parameterType="${domainPackage(package,module,func.packageName)}.${class}">
		DELETE FROM ${table.tableName?upper_case}
		<where>
			<#list pk as field>
				AND ${field.field?upper_case} = ${genCondition(field)}
			</#list>
		</where>
	</delete>
	<#--唯一查询-->
	<#list unique as ufield><#local javaFieldName=javaField(ufield.field)/>
	<select id="get${class}By${javaFieldName?cap_first}" parameterType="${domainPackage(package,module,func.packageName)}.${class}" resultMap="${class}">
		SELECT * 
		FROM ${table.tableName?upper_case}
		<where>
			${ufield.field?upper_case} = ${genCondition(ufield)}
		</where>
	</select>
	<select id="get${class}MapBy${javaFieldName?cap_first}" parameterType="${domainPackage(package,module,func.packageName)}.${class}" resultType="hashmap">
		<#local aliasNames=[]/>
		<#list columns as field>
			<#local aliasNames=aliasNames+[class+"."+field.field?upper_case+" AS "+javaField(field.field)]/>
		</#list>
		SELECT 
			<@formatCode align=[' AS '] start='           ' end=',' split=',' leftSpace=' ' rightSpace=' '>
				${aliasNames?join(", ")}
				<#list foreign as field><#local javaFieldName=javaField(field.field)/><#if !field.isNotTableColumn>
					, ${javaFieldName}${javaClass(field.refTable,removePrefix)}.${field.refLabelField?upper_case} AS ${javaFieldName}${javaField(field.refLabelField)?cap_first}
				</#if></#list>
			</@formatCode>
		FROM ${table.tableName?upper_case} ${class}
		<@formatCode align=[' ON ', ' = '] start='		' end='' split='\n' leftSpace='' rightSpace=''>
		<#list foreign as field><#local javaFieldName=javaField(field.field)/><#if !field.isNotTableColumn>
		LEFT JOIN ${field.refTable?upper_case} ${javaFieldName}${javaClass(field.refTable,removePrefix)} ON ${class}.${field.field?upper_case} = ${javaFieldName}${javaClass(field.refTable,removePrefix)}.${field.refField}
		</#if></#list>
		</@formatCode>
		<where>
			${ufield.field?upper_case} = ${genCondition(ufield)}
		</where>
	</select>
	</#list>
	<#--关联查询-->
	<#list foreign as field><#if field.midTable?has_content && field.midMainField?has_content && field.midSecondField?has_content>
	<#local refClass=javaClass(field.refTable,removePrefix) midClass=javaClass(field.midTable,removePrefix) />
	<#local refTable=getTable(field.refTable, project).table midTable=getTable(field.midTable, project).table/><#if !refTable?has_content || !midTable?has_content><break/></#if>
	
	<select id="pageFind${class}By${refClass}" resultMap="${class}">
		SELECT DISTINCT ${class}.* 
		FROM ${table.tableName?upper_case} ${class}
		<@formatCode align=[' ON ','='] start='		' end='' split='\n' leftSpace=' ' rightSpace=' '>
		INNER JOIN ${field.midTable?upper_case} ${midClass} ON    ${class}.ID = ${midClass}.${field.midMainField}
		INNER JOIN ${field.refTable?upper_case} ${refClass} ON ${refClass}.ID = ${midClass}.${field.midSecondField}
		</@formatCode>
		<where>
			<@formatCode align=['">'] start='           ' end='' split='\n' leftSpace=' ' rightSpace=' '>
				<#list columns as field><#local javaFieldName=javaField(field.field)/>
					<#if field.searchable && field.javaType=='String'>
					<if test="vo!=null and vo.${javaFieldName} != null and vo.${javaFieldName} != ''"> AND ${class}.${field.field?upper_case} LIKE CONCAT('%',${genCondition(field, "vo")},'%') </if>
					<#else>
					<if test="vo!=null and vo.${javaFieldName} != null and vo.${javaFieldName} != ''"> AND ${class}.${field.field?upper_case} = ${genCondition(field, "vo")} </if>
					</#if>
				</#list>
				<#list refTable.fields as field><#local javaFieldName=javaField(field.field)/>
					<#if field.searchable && field.javaType=='String'>
					<if test="${refClass}!=null and ${refClass}.${javaFieldName} != null and ${refClass}.${javaFieldName} != ''"> AND ${refClass}.${field.field?upper_case} LIKE CONCAT('%',${genCondition(field, "${refClass}")},'%') </if>
					<#else>
					<if test="${refClass}!=null and ${refClass}.${javaFieldName} != null and ${refClass}.${javaFieldName} != ''"> AND ${refClass}.${field.field?upper_case} = ${genCondition(field, "${refClass}")} </if>
					</#if>
				</#list>
			</@formatCode>
		</where>
		<bind name="aliasDot" value="'${class}.'"/><include refid="orderBy"/>
	</select>
	 
	<select id="pageFind${refClass}By${midClass}Map" resultType="hashmap">
		<#local aliasNames=[]/><#list refTable.fields as field><#if !field.isNotTableColumn>
			<#local aliasNames=aliasNames+[refClass+"."+field.field?upper_case+" AS "+javaField(field.field)]/>
		</#if></#list>
		SELECT 
			<@formatCode align=[' AS '] start='           ' end=',' split=',' leftSpace=' ' rightSpace=' '>
				${aliasNames?join(", ")}
			</@formatCode>
		FROM ${table.tableName?upper_case} ${class}
		<@formatCode align=[' ON ','='] start='		' end='' split='\n' leftSpace=' ' rightSpace=' '>
		INNER JOIN ${field.midTable?upper_case} ${midClass} ON    ${class}.ID = ${midClass}.${field.midMainField}
		INNER JOIN ${field.refTable?upper_case} ${refClass} ON ${refClass}.ID = ${midClass}.${field.midSecondField}
		</@formatCode>
		<where>
			<@formatCode align=['">'] start='           ' end='' split='\n' leftSpace=' ' rightSpace=' '>
				<#list columns as field><#local javaFieldName=javaField(field.field)/>
					<#if field.searchable && field.javaType=='String'>
					<if test="vo!=null and vo.${javaFieldName} != null and vo.${javaFieldName} != ''"> AND ${class}.${field.field?upper_case} LIKE CONCAT('%',${genCondition(field, "vo")},'%') </if>
					<#else>
					<if test="vo!=null and vo.${javaFieldName} != null and vo.${javaFieldName} != ''"> AND ${class}.${field.field?upper_case} = ${genCondition(field, "vo")} </if>
					</#if>
				</#list>
			</@formatCode>
		</where>
	</select>
	
	<select id="pageFind${refClass}By${refClass}Map" resultType="hashmap">
		<#local aliasNames=[]/><#list refTable.fields as field><#if !field.isNotTableColumn>
			<#local aliasNames=aliasNames+[refClass+"."+field.field?upper_case+" AS "+javaField(field.field)]/>
		</#if></#list>
		SELECT 
			<@formatCode align=[' AS '] start='           ' end=',' split=',' leftSpace=' ' rightSpace=' '>
				${aliasNames?join(", ")}
			</@formatCode>
		FROM ${table.tableName?upper_case} ${class}
		<@formatCode align=[' ON ','='] start='		' end='' split='\n' leftSpace=' ' rightSpace=' '>
		INNER JOIN ${field.midTable?upper_case} ${midClass} ON    ${class}.ID = ${midClass}.${field.midMainField}
		INNER JOIN ${field.refTable?upper_case} ${refClass} ON ${refClass}.ID = ${midClass}.${field.midSecondField}
		</@formatCode>
		<where>
			<@formatCode align=['">'] start='           ' end='' split='\n' leftSpace=' ' rightSpace=' '>
				<#list columns as field><#local javaFieldName=javaField(field.field)/>
					<#if field.searchable && field.javaType=='String'>
					<if test="vo!=null and vo.${javaFieldName} != null and vo.${javaFieldName} != ''"> AND ${class}.${field.field?upper_case} LIKE CONCAT('%',${genCondition(field, "vo")},'%') </if>
					<#else>
					<if test="vo!=null and vo.${javaFieldName} != null and vo.${javaFieldName} != ''"> AND ${class}.${field.field?upper_case} = ${genCondition(field, "vo")} </if>
					</#if>
				</#list>
				<#list refTable.fields as field><#local javaFieldName=javaField(field.field)/>
					<#if field.searchable && field.javaType=='String'>
					<if test="${refClass}!=null and ${refClass}.${javaFieldName} != null and ${refClass}.${javaFieldName} != ''"> AND ${refClass}.${field.field?upper_case} LIKE CONCAT('%',${genCondition(field, "${refClass}")},'%') </if>
					<#else>
					<if test="${refClass}!=null and ${refClass}.${javaFieldName} != null and ${refClass}.${javaFieldName} != ''"> AND ${refClass}.${field.field?upper_case} = ${genCondition(field, "${refClass}")} </if>
					</#if>
				</#list>
			</@formatCode>
		</where>
	</select>
	
	<select id="pageFind${midClass}Map" resultType="hashmap">
		<#local aliasNames=[] /><#list midTable.fields as field><#if !field.isNotTableColumn>
			<#local aliasNames=aliasNames+[midClass+"."+field.field?upper_case+" AS "+javaField(field.field)]/>
		</#if></#list>
		SELECT 
			<@formatCode align=[' AS '] start='           ' end=',' split=',' leftSpace=' ' rightSpace=' '>
				${aliasNames?join(", ")}
			</@formatCode>
		FROM ${table.tableName?upper_case} ${class}
		<@formatCode align=[' ON ','='] start='		' end='' split='\n' leftSpace=' ' rightSpace=' '>
		INNER JOIN ${field.midTable?upper_case} ${midClass} ON    ${class}.ID = ${midClass}.${field.midMainField}
		</@formatCode>
		<where>
			<@formatCode align=['">'] start='           ' end='' split='\n' leftSpace=' ' rightSpace=' '>
				<#list columns as field><#local javaFieldName=javaField(field.field)/>
					<#if field.searchable && field.javaType=='String'>
					<if test="vo!=null and vo.${javaFieldName} != null and vo.${javaFieldName} != ''"> AND ${class}.${field.field?upper_case} LIKE CONCAT('%',${genCondition(field, "vo")},'%') </if>
					<#else>
					<if test="vo!=null and vo.${javaFieldName} != null and vo.${javaFieldName} != ''"> AND ${class}.${field.field?upper_case} = ${genCondition(field, "vo")} </if>
					</#if>
				</#list>
			</@formatCode>
		</where>
	</select>
	</#if></#list>
	<#--检查唯一字段-->
	<#list unique as field><#local javaFieldName=javaField(field.field)/>
	<select id="count${class}By${javaFieldName?cap_first}" resultType="java.lang.Long">
		SELECT count(*)
		FROM ${table.tableName?upper_case}
		<where>
			<@formatCode align=['">'] start='           ' end='' split='\n' leftSpace=' ' rightSpace=' '>
				${field.field?upper_case} = ${genCondition(field, "vo")}
				<#list pk as field><#local javaFieldName=javaField(field.field)/>
					<if test="vo!=null and vo.${javaFieldName} != null and vo.${javaFieldName} != ''">AND ${field.field?upper_case} &lt;&gt; ${genCondition(field, "vo")}</if>
				</#list>
			</@formatCode>
		</where>
	</select>
	</#list>
</mapper>
</#macro>


<#assign project=root.data.data />
<#assign removePrefix=root.rmTablePrefix!'' />
<#assign package=root.groupId />
<#assign projectName=root.artifactId />
<#assign date=.now?date?string['yyyy-MM-dd'] />
<#list project as module>
	<#assign moduleName=module.packageName />
	<#list module.children as func>
		<#assign class=javaClass(func.tableName,removePrefix)/>
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
				<@filewriter type="xml" basedir=projectdir(root.projectName)+"/src/main/resources/META-INF/mappings/"+packageToPath(package,'domain',module.packageName,func.packageName)
					name="${class}Mapper-mysql.xml">
					<@mapper date=date package=package class=class module=moduleName table=table pk=pk notNull=notNull index=index unique=unique autoInc=autoInc columns=columns foreign=foreign func=func proModule=module project=project/>
				</@filewriter>
			</#if>
		</#list>
	</#list>
</#list>

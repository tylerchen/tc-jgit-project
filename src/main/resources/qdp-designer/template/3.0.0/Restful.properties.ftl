<#include "/_functions.ftl" />
<#include "/_page_util.ftl" />
<#macro restfull date package class module table pk notNull index unique autoInc columns foreign func proModule project>
<#--搜索--><#local ableFields=[] fieldNames=[]/><#list table.fields as field><#if field.searchable && field.field != 'id' && ableFields?size lt 4><#local ableFields=ableFields+[field] fieldNames=fieldNames+[javaField(field.field)]/></#if></#list>
<#local fieldNamesJoin=fieldNames?join(',') fieldNamesPath=fieldNames?has_content?then("/:${fieldNames?join('/:')}","")/>
###
${class}.restful.tc=true
${class}.interface=${appPackage(package,module,func.packageName)}.${class}Application
${class}.interface.path=/${class}
${class}.interface.beanName=${class?uncap_first}Application
${class}.interface.nullChar=-
#
${class}.method.get${class}=get${class}
${class}.method.get${class}.path=/get/:id
${class}.method.get${class}.arg0=${class}VO
${class}.method.get${class}.arg0.props=id
#
${class}.method.pageFind${class}=pageFind${class}
${class}.method.pageFind${class}.path=/page${fieldNamesPath}/:currentPage/:pageSize,/page${fieldNamesPath}/:currentPage,/page${fieldNamesPath}
${class}.method.pageFind${class}.arg0=${class}VO
${class}.method.pageFind${class}.arg0.props=${fieldNamesJoin}
${class}.method.pageFind${class}.arg1=Page
${class}.method.pageFind${class}.arg1.props=currentPage,pageSize
#
${class}.method.pageFind${class}Map=pageFind${class}Map
${class}.method.pageFind${class}Map.path=/page/map${fieldNamesPath}/:currentPage/:pageSize,/page/map${fieldNamesPath}/:currentPage,/page/map${fieldNamesPath}
${class}.method.pageFind${class}Map.arg0=${class}VO
${class}.method.pageFind${class}Map.arg0.props=${fieldNamesJoin}
${class}.method.pageFind${class}Map.arg1=Page
${class}.method.pageFind${class}Map.arg1.props=currentPage,pageSize
#
${class}.method.add${class}=add${class}
${class}.method.add${class}.method=POST
${class}.method.add${class}.path=/
${class}.method.add${class}.arg0=${class}VO
#
${class}.method.update${class}=update${class}
${class}.method.update${class}.method=PUT
${class}.method.update${class}.path=/
${class}.method.update${class}.arg0=${class}VO
<#list pk as field><#if field_index==0><#local javaFieldName=javaField(field.field)/>
#
${class}.method.remove${class}ByIds=remove${class}ByIds
${class}.method.remove${class}ByIds.method=DELETE
${class}.method.remove${class}ByIds.path=/:ids
${class}.method.remove${class}ByIds.arg0=String[]
${class}.method.remove${class}ByIds.arg0.props=ids
</#if></#list>
<#--唯一方法-->
<#list unique as field><#local javaFieldName=javaField(field.field)/>
#
${class}.method.getBy${javaFieldName?cap_first}=getBy${javaFieldName?cap_first}
${class}.method.getBy${javaFieldName?cap_first}.path=/get/${javaFieldName}/:${javaFieldName}
${class}.method.getBy${javaFieldName?cap_first}.arg0=${field.javaType}
${class}.method.getBy${javaFieldName?cap_first}.arg0.props=${javaFieldName}
</#list>
<#--关联查询-->
<#list foreign as field><#if field.midTable?has_content && field.midMainField?has_content && field.midSecondField?has_content>
<#local refClass=javaClass(field.refTable,removePrefix) midClass=javaClass(field.midTable,removePrefix) refMFT=getTable(field.refTable, project) refMidMFT=getTable(field.midTable, project)/>
<#local refModule=refMFT.module refFunc=refMFT.func refTable=refMFT.table/><#if !refTable?has_content><break/></#if>
<#local mainPk={} secondPk={} midPk={}/>
<#list pk as pkf><#if !mainPk?has_content><#local mainPk=pkf/></#if></#list>
<#list refTable.fields as pkf><#if pkf.isPk && !secondPk?has_content><#local secondPk=pkf/></#if></#list>
<#list refMidMFT.table.fields as pkf><#if pkf.isPk && !midPk?has_content><#local midPk=pkf/></#if></#list>
<#local refModule=refMFT.module refFunc=refMFT.func refTable=refMFT.table/><#if !refTable?has_content><break/></#if>
#
${class}.method.pageFindAssign${refClass}=pageFindAssign${refClass}
${class}.method.pageFindAssign${refClass}.path=/${refClass}/:id${fieldNamesPath}/:currentPage/:pageSize,/${refClass}/:id${fieldNamesPath}/:currentPage,/${refClass}/:id${fieldNamesPath}
${class}.method.pageFindAssign${refClass}.arg0=${class}VO
${class}.method.pageFindAssign${refClass}.arg0.props=id,${fieldNamesJoin}
${class}.method.pageFindAssign${refClass}.arg1=Page
${class}.method.pageFindAssign${refClass}.arg1.props=currentPage,pageSize
#
${class}.method.assign${refClass}=assign${refClass}
${class}.method.assign${refClass}.method=PUT
${class}.method.assign${refClass}.path=/${refClass}/:${javaField(field.field)}
${class}.method.assign${refClass}.arg0=${class}VO
${class}.method.assign${refClass}.arg0.props=${javaField(field.field)}
</#if></#list>
<#--弹出表单-->
<#list func.children as item><#if item.type=='page' && item.name!='index' && item.formType=='form'>
#
${class}.method.${javaField(columnField(item.fileName))}=${javaField(columnField(item.fileName))}
${class}.method.${javaField(columnField(item.fileName))}.method=PUT
${class}.method.${javaField(columnField(item.fileName))}.path=/${javaField(columnField(item.fileName))}
${class}.method.${javaField(columnField(item.fileName))}.arg0=${javaClass(columnField(item.fileName),'')?cap_first}VO
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
				<@filewriter type="properties" basedir=projectdir(root.projectName)+"/src/main/resources/META-INF/restful/"
					name="${class}.properties">
					<@restfull date=date package=package class=class module=moduleName table=table pk=pk notNull=notNull index=index unique=unique autoInc=autoInc columns=columns foreign=foreign func=func proModule=module project=project/>
				</@filewriter>
			</#if>
		</#list>
	</#list>
</#list>


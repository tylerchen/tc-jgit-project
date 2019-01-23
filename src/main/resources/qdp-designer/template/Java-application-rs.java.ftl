<#include "/_functions.ftl" />
<#include "/_page_util.ftl" />
<#macro application date package class module table pk notNull index unique autoInc columns foreign func proModule project>
/*******************************************************************************
 * Copyright (c) ${date} @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package ${rsPackage(package,module,func.packageName)};

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.iff.infra.util.mybatis.plugin.Page;
import ${voPackage(package,module,func.packageName)}.${class}VO;
<#--弹出表单--><#list func.children as item><#if item.type=='page' && item.name!='index' && item.formType=='form'>
import ${voPackage(package,module,func.packageName)}.${javaClass(columnField(item.fileName),'')?cap_first}VO;
</#if></#list>

/**
 * ${class} Application.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since ${date}
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@Path("${class?uncap_first}RsApplication")
@Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.MULTIPART_FORM_DATA })
public interface ${class}RsApplication {

	/**
	 * <pre>
	 * get ${class}VO by id.
	 * </pre>
	 * @param vo
	 * @return ${class}VO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	@POST
	@Path("/get${class}")
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
	${class}VO get${class}(@FormParam("${class}VO") ${class}VO vo);

	<#list pk as field><#if field_index==0><#local javaFieldName=javaField(field.field)/>
	/**
	 * <pre>
	 * get ${class}VO by id.
	 * </pre>
	 * @param vo
	 * @return ${class}VO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	@GET
	@Path("/get${class}ById/{${javaFieldName}}")
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
	${class}VO get${class}ById(@PathParam("${javaFieldName}") ${field.javaType} ${javaFieldName});
	
	</#if></#list>
	/**
	 * <pre>
	 * page find ${class}VO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	@POST
	@Path("/pageFind${class}")
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
	Page pageFind${class}(@FormParam("${class}VO") ${class}VO vo, @FormParam("Page") Page page);
	
	/**
	 * <pre>
	 * page find ${class}VO Map.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	@POST
	@Path("/pageFind${class}Map")
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
	Page pageFind${class}Map(@FormParam("${class}VO") ${class}VO vo, @FormParam("Page") Page page);

	/**
	 * <pre>
	 * add ${class}.
	 * </pre>
	 * @param vo
	 * @return ${class}VO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	@POST
	@Path("/add${class}")
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
	${class}VO add${class}(@FormParam("${class}VO") ${class}VO vo);
	
	/**
	 * <pre>
	 * update ${class}.
	 * </pre>
	 * @param vo
	 * @return ${class}VO
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	@POST
	@Path("/update${class}")
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
	${class}VO update${class}(@FormParam("${class}VO") ${class}VO vo);

	/**
	 * <pre>
	 * remove ${class}.
	 * </pre>
	 * @param vo conditions.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	@DELETE
	@Path("/remove${class}")
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
	void remove${class}(@FormParam("${class}VO") ${class}VO vo);
	
	<#list pk as field><#if field_index==0><#local javaFieldName=javaField(field.field)/>
	/**
	 * <pre>
	 * remove ${class}.
	 * </pre>
	 * @param id.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	@DELETE
	@Path("/remove${class}ById/{${javaFieldName}}")
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
	void remove${class}ById(@PathParam("id") ${field.javaType} id);
	
	/**
	 * <pre>
	 * remove ${class}.
	 * </pre>
	 * @param ids.
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	@DELETE
	@Path("/remove${class}ByIds")
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
	void remove${class}ByIds(@FormParam("ids") ${field.javaType}[] ids);
	
	</#if></#list>
	
	<#--唯一方法-->
	<#list unique as field><#local javaFieldName=javaField(field.field)/>
	/**
	 * <pre>
	 * get ${class} by unique ${javaFieldName}
	 * Usage : ${class}.getBy${javaFieldName?cap_first}(${javaFieldName})
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	@POST
	@Path("/getBy${javaFieldName?cap_first}")
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
	${class}VO getBy${javaFieldName?cap_first}(${field.javaType} ${javaFieldName});
	
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
	/**
	 * <pre>
	 * page find assign ${refClass}VO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	@POST
	@Path("/pageFindAssign${refClass}")
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
	Page pageFindAssign${refClass}(@FormParam("${class}VO") ${class}VO vo, @FormParam("Page") Page page);
	
	/**
	 * <pre>
	 * assign ${refClass} by id
	 * </pre>
	 * @param ids ${refClass} id
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	@POST
	@Path("/assign${refClass}ByIds")
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
	void assign${refClass}(@FormParam("${class}VO") ${class}VO vo);
	</#if></#list>
	
	<#list func.children as item><#--弹出表单-->
		<#if item.type=='page' && item.name!='index' && item.formType=='form'>
	/**
	 * <pre>
	 * ${javaField(columnField(item.fileName))}
	 * </pre>
	 * @param vo ${javaClass(columnField(item.fileName),'')?cap_first+'VO'}
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	@POST
	@Path("/${javaField(columnField(item.fileName))}")
	@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.MULTIPART_FORM_DATA })
	void ${javaField(columnField(item.fileName))}(${javaClass(columnField(item.fileName),'')?cap_first}VO vo);
		</#if>
	</#list>
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
				<@filewriter type="java" basedir=projectdir(root.projectName)+"/src/main/java/"
					name="${rsPackage(package, moduleName,func.packageName)}.${class}RsApplication.java">
					<@application date=date package=package class=class module=moduleName table=table pk=pk notNull=notNull index=index unique=unique autoInc=autoInc columns=columns foreign=foreign func=func proModule=module project=project/>
				</@filewriter>
			</#if>
		</#list>
	</#list>
</#list>


<#include "/_functions.ftl" />
<#include "/_page_util.ftl" />
<#macro applicationimpl date package class module table pk notNull index unique autoInc columns foreign func proModule project>
/*******************************************************************************
 * Copyright (c) ${date} @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package ${rsImplPackage(package,module,func.packageName)};

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

import org.iff.infra.util.BeanHelper;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.mybatis.plugin.Page;
import org.iff.infra.util.mybatis.service.Dao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import ${rsPackage(package,module,func.packageName)}.${class}RsApplication;
import ${appPackage(package,module,func.packageName)}.${class}Application;
import ${voPackage(package,module,func.packageName)}.${class}VO;
<#--弹出表单--><#list func.children as item><#if item.type=='page' && item.name!='index' && item.formType=='form'>
import ${voPackage(package,module,func.packageName)}.${javaClass(columnField(item.fileName),'')?cap_first}VO;
</#if></#list>
<#--关联查询的VO--><#list foreign as field><#if field.midTable?has_content && field.midMainField?has_content && field.midSecondField?has_content>
<#local refClass=javaClass(field.refTable,removePrefix) midClass=javaClass(field.midTable,removePrefix) refMFT=getTable(field.refTable, project)/>
<#local refModule=refMFT.module refFunc=refMFT.func refTable=refMFT.table/><#if !refTable?has_content><break/></#if>
import ${voPackage(package,refModule.packageName,refFunc.packageName)}.${refClass}VO;
</#if></#list>
import ${domainPackage(package,module,func.packageName)}.${class};
<#--关联查询的DO--><#list foreign as field><#if field.midTable?has_content && field.midMainField?has_content && field.midSecondField?has_content>
<#local refClass=javaClass(field.refTable,removePrefix) midClass=javaClass(field.midTable,removePrefix) refMFT=getTable(field.refTable, project)/>
<#local refModule=refMFT.module refFunc=refMFT.func refTable=refMFT.table/><#if !refTable?has_content><break/></#if>
import ${domainPackage(package,refModule.packageName,refFunc.packageName)}.${refClass};
</#if></#list>

/**
 * ${class}
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since ${date}
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@Named("${class?uncap_first}RsApplication")
public class ${class}RsApplicationImpl implements ${class}RsApplication {

	@Inject
	${class}Application ${class?uncap_first}Application;

	/**
	 * <pre>
	 * get ${class}VO by id.
	 * </pre>
	 * @param vo
	 * @return ${class}VO
	 * (non-Javadoc)
	 * @see ${wsPackage(package,module,func.packageName)}.${class}Application#get${class}(${class}VO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	public ${class}VO get${class}(${class}VO vo) {
		return ${class?uncap_first}Application.get${class}(vo);
	}

	<#list pk as field><#if field_index==0><#local javaFieldName=javaField(field.field)/>
	/**
	 * <pre>
	 * get ${class}VO by id.
	 * </pre>
	 * @param vo
	 * @return ${class}VO
	 * (non-Javadoc)
	 * @see ${wsPackage(package,module,func.packageName)}.${class}Application#get${class}ById(${field.javaType})
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	public ${class}VO get${class}ById(${field.javaType} ${javaFieldName}){
		return ${class?uncap_first}Application.get${class}ById(${javaFieldName});
	}
	
	</#if></#list>
	/**
	 * <pre>
	 * page find ${class}VO.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * (non-Javadoc)
	 * @see ${wsPackage(package,module,func.packageName)}.${class}Application#pageFind${class}(${class}VO, Page)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	public Page pageFind${class}(${class}VO vo, Page page) {
		return ${class?uncap_first}Application.pageFind${class}(vo, page);
	}
	
	/**
	 * <pre>
	 * page find ${class}VO Map.
	 * </pre>
	 * @param vo conditions
	 * @param page page setting
	 * @return Page
	 * (non-Javadoc)
	 * @see ${wsPackage(package,module,func.packageName)}.${class}Application#pageFind${class}Map(${class}VO, Page)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	public Page pageFind${class}Map(${class}VO vo, Page page) {
		return ${class?uncap_first}Application.pageFind${class}Map(vo, page);
	}

	/**
	 * <pre>
	 * add ${class}.
	 * </pre>
	 * @param vo
	 * @return ${class}VO
	 * (non-Javadoc)
	 * @see ${wsPackage(package,module,func.packageName)}.${class}Application#add${class}(${class}VO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	public ${class}VO add${class}(${class}VO vo) {
		return ${class?uncap_first}Application.add${class}(vo);
	}

	/**
	 * <pre>
	 * update ${class}.
	 * </pre>
	 * @param vo
	 * @return ${class}VO
	 * (non-Javadoc)
	 * @see ${wsPackage(package,module,func.packageName)}.${class}Application#update${class}(${class}VO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	public ${class}VO update${class}(${class}VO vo) {
		return ${class?uncap_first}Application.update${class}(vo);
	}
	
	/**
	 * <pre>
	 * remove ${class}.
	 * </pre>
	 * @param vo conditions.
	 * (non-Javadoc)
	 * @see ${wsPackage(package,module,func.packageName)}.${class}Application#remove${class}(${class}VO)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	public void remove${class}(${class}VO vo) {
		${class?uncap_first}Application.remove${class}(vo);
	}

	<#list pk as field><#if field_index==0><#local javaFieldName=javaField(field.field)/>
	/**
	 * <pre>
	 * remove ${class}.
	 * </pre>
	 * @param id.
	 * (non-Javadoc)
	 * @see ${wsPackage(package,module,func.packageName)}.${class}Application#remove${class}ById(${field.javaType})
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	public void remove${class}ById(${field.javaType} id) {
		${class?uncap_first}Application.remove${class}ById(id);
	}
	
	/**
	 * <pre>
	 * remove ${class}.
	 * </pre>
	 * @param ids.
	 * (non-Javadoc)
	 * @see ${wsPackage(package,module,func.packageName)}.${class}Application#remove${class}ByIds(${field.javaType}[])
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	public void remove${class}ByIds(${field.javaType}[] ids) {
		${class?uncap_first}Application.remove${class}ByIds(ids);
	}
	
	</#if></#list>
	
	<#--唯一方法-->
	<#list unique as field><#local javaFieldName=javaField(field.field)/>
	/**
	 * <pre>
	 * get ${class} by unique ${javaFieldName}
	 * </pre>
	 * (non-Javadoc)
	 * @see ${appPackage(package,module,func.packageName)}.${class}Application#getBy${javaFieldName?cap_first}(${field.javaType}[])
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	public ${class}VO getBy${javaFieldName?cap_first}(${field.javaType} ${javaFieldName}) {
		return ${class?uncap_first}Application.getBy${javaFieldName?cap_first}(${javaFieldName});
	}
	
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
	 * (non-Javadoc)
	 * @see ${wsPackage(package,module,func.packageName)}.${class}Application#pageFindAssign${refClass}(${class}VO, Page)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	public Page pageFindAssign${refClass}(${class}VO vo, Page page) {
		return ${class?uncap_first}Application.pageFindAssign${refClass}(vo, page);
	}
	
	/**
	 * <pre>
	 * assign ${refClass} by id
	 * </pre>
	 * @param ids ${refClass} id
	 * (non-Javadoc)
	 * @see ${wsPackage(package,module,func.packageName)}.${class}Application#assign${refClass}(${class}VO, Page)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	public void assign${refClass}(${class}VO vo) {
		${class?uncap_first}Application.assign${refClass}(vo);
	}
	</#if></#list>
	
	<#list func.children as item><#--弹出表单-->
		<#if item.type=='page' && item.name!='index' && item.formType=='form'>
	/**
	 * <pre>
	 * ${javaField(columnField(item.fileName))}
	 * </pre>
	 * @param vo ${javaClass(columnField(item.fileName),'')?cap_first+'VO'}
	 * (non-Javadoc)
	 * @see ${wsPackage(package,module,func.packageName)}.${class}Application#${javaField(columnField(item.fileName))}(${javaClass(columnField(item.fileName),'')?cap_first}VO, Page)
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 * auto generate by qdp v3.0.
	 */
	public void ${javaField(columnField(item.fileName))}(${javaClass(columnField(item.fileName),'')?cap_first}VO vo) {
		${class?uncap_first}Application.${javaField(columnField(item.fileName))}(vo);
	}
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
					name="${rsImplPackage(package, moduleName,func.packageName)}.${class}RsApplicationImpl.java">
					<@applicationimpl date=date package=package class=class module=moduleName table=table pk=pk notNull=notNull index=index unique=unique autoInc=autoInc columns=columns foreign=foreign func=func proModule=module project=project/>
				</@filewriter>
			</#if>
		</#list>
	</#list>
</#list>



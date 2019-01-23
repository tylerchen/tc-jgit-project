<#include "/_functions.ftl" />
<#include "/_page_util.ftl" />
<#macro applicationimpl date package class module table pk notNull index unique autoInc columns foreign func proModule project>
<#--搜索字段--><#local ableFields=[] fieldNames=[]/><#list table.fields as field><#if field.searchable && field.field != 'id' && ableFields?size lt 4><#local ableFields=ableFields+[field] fieldNames=fieldNames+[javaField(field.field)]/></#if></#list>
<#local fieldNamesJoin=fieldNames?join(',') fieldNamesPath=fieldNames?has_content?then("/{${fieldNames?join('}/{')}}","")/>
/*******************************************************************************
 * Copyright (c) ${date} @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package ${rsImplPackage(package,module,func.packageName)};

import java.util.*;
import javax.inject.Inject;

import org.iff.infra.util.*;
import org.iff.infra.util.mybatis.plugin.Page;
import org.iff.infra.util.mybatis.service.Dao;
import org.apache.commons.lang3.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import com.foreveross.common.ResultBean;
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

/**
 * ${class}
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since ${date}
 * @version 1.0.0
 * auto generate by qdp v5.0.
 */
@RestController
@RequestMapping("/api/${class}")
@Api("${class} Api")
public class ${class}RSApplicationImpl {

    @Inject
    ${class}Application ${class?uncap_first}Application;

    /**
     * <pre>
     * get ${class}VO by id.
     * USAGE:
     *   GET /api/${class}/get/{id}
     * SUCCESS:
     *   {header:{status:success},
     *   body:{${class}VO}}
     * ERROR:
     *   {header: {status: error}, body:{Exception.getMessage()}}
     * </pre>
     *
     * @param id
     * @return ResultBean
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since ${date}
     * auto generate by qdp v5.0
     */
    @ApiOperation(value = "get ${class} by id", notes = "get ${class} by id")
    @GetMapping("/get/{id}")
    public ResultBean get${class}ById(@PathVariable(value = "id") String id) {
        try {
            return ResultBean.success().setBody(${class?uncap_first}Application.get${class}ById(id));
        } catch (Exception e) {
            return ResultBean.error().setBody(e.getMessage());
        }
    }

    /**
     * <pre>
     * page find ${class}VO.
     * USAGE:
     *   GET /api/${class}/page${fieldNamesPath}/{currentPage}/{pageSize}/{asc}/{desc}
     * SUCCESS:
     *   {header:{status:success},
     *   body:{pageSize,totalCount,currentPage,offset,offsetPage,orderBy:[],rows:[{${class}VO}]}}
     * ERROR:
     *   {header: {status: error}, body:{Exception.getMessage()}}
     * </pre>
     *
     <#list fieldNames as fieldName>
     * @param ${fieldName}
     </#list>
     * @param currentPage
     * @param pageSize
     * @param asc
     * @param desc
     * @return ResultBean
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since ${date}
     * auto generate by qdp v5.0
     */
    @ApiOperation(value = "page find ${class}", notes = "page find ${class}")
    @GetMapping({"/page${fieldNamesPath}/{currentPage}/{pageSize}/{asc}/{desc}",
            "/page${fieldNamesPath}/{currentPage}/{pageSize}",
            "/page${fieldNamesPath}/{currentPage}",
            <#list fieldNames as fieldName>
            <#local subArr=fieldNames?size-fieldName?index-1 />
            "/page/{${fieldNames[0..subArr]?join('}/{')}}",
            </#list>
            "/page"})
    public ResultBean pageFind${class}(
                                       <#list fieldNames as fieldName>
                                       @PathVariable(required = false, value = "${fieldName}") String ${fieldName},
                                       </#list>
                                       @PathVariable(required = false, value = "currentPage") Integer currentPage,
                                       @PathVariable(required = false, value = "pageSize") Integer pageSize,
                                       @PathVariable(required = false, value = "asc") String asc,
                                       @PathVariable(required = false, value = "desc") String desc) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            {
                 <#list fieldNames as fieldName>
                 map.put("${fieldName}", PreCheckHelper.equalsToNull(${fieldName}, "-"));
                 </#list>
            }
            Page page = new Page();
            {
                page.setCurrentPage(NumberHelper.getInt(currentPage, 1));
                page.setPageSize(NumberHelper.getInt(pageSize, 10));
                asc = PreCheckHelper.equalsToNull(asc, "-");
                desc = PreCheckHelper.equalsToNull(desc, "-");
                if (StringUtils.isNotBlank(asc)) {
                    page.addAscOrderBy(asc);
                }
                if (StringUtils.isNotBlank(desc)) {
                    page.addDescOrderBy(desc);
                }
            }
            ${class}VO vo = BeanHelper.copyProperties(${class}VO.class, map);
            return ResultBean.success().setBody(${class?uncap_first}Application.pageFind${class}(vo, page));
        } catch (Exception e) {
            return ResultBean.error().setBody(e.getMessage());
        }
    }

    /**
     * <pre>
     * page find ${class}VO.
     * USAGE:
     *   GET /api/${class}/pageMap${fieldNamesPath}/{currentPage}/{pageSize}/{asc}/{desc}
     * SUCCESS:
     *   {header:{status:success},
     *   body:{pageSize,totalCount,currentPage,offset,offsetPage,orderBy:[],rows:[{${class}VO}]}}
     * ERROR:
     *   {header: {status: error}, body:{Exception.getMessage()}}
     * </pre>
     *
     <#list fieldNames as fieldName>
     * @param ${fieldName}
     </#list>
     * @param currentPage
     * @param pageSize
     * @param asc
     * @param desc
     * @return ResultBean
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since ${date}
     * auto generate by qdp v5.0
     */
    @ApiOperation(value = "page find ${class}", notes = "page find ${class}")
    @GetMapping({"/pageMap${fieldNamesPath}/{currentPage}/{pageSize}/{asc}/{desc}",
            "/pageMap${fieldNamesPath}/{currentPage}/{pageSize}",
            "/pageMap${fieldNamesPath}/{currentPage}",
            <#list fieldNames as fieldName>
            <#local subArr=fieldNames?size-fieldName?index-1 />
            "/pageMap/{${fieldNames[0..subArr]?join('}/{')}}",
            </#list>
            "/pageMap"})
    public ResultBean pageFind${class}Map(
                                       <#list fieldNames as fieldName>
                                       @PathVariable(required = false, value = "${fieldName}") String ${fieldName},
                                       </#list>
                                       @PathVariable(required = false, value = "currentPage") Integer currentPage,
                                       @PathVariable(required = false, value = "pageSize") Integer pageSize,
                                       @PathVariable(required = false, value = "asc") String asc,
                                       @PathVariable(required = false, value = "desc") String desc) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            {
                 <#list fieldNames as fieldName>
                 map.put("${fieldName}", PreCheckHelper.equalsToNull(${fieldName}, "-"));
                 </#list>
            }
            Page page = new Page();
            {
                page.setCurrentPage(NumberHelper.getInt(currentPage, 1));
                page.setPageSize(NumberHelper.getInt(pageSize, 10));
                asc = PreCheckHelper.equalsToNull(asc, "-");
                desc = PreCheckHelper.equalsToNull(desc, "-");
                if (StringUtils.isNotBlank(asc)) {
                    page.addAscOrderBy(asc);
                }
                if (StringUtils.isNotBlank(desc)) {
                    page.addDescOrderBy(desc);
                }
            }
            ${class}VO vo = BeanHelper.copyProperties(${class}VO.class, map);
            return ResultBean.success().setBody(${class?uncap_first}Application.pageFind${class}Map(vo, page));
        } catch (Exception e) {
            return ResultBean.error().setBody(e.getMessage());
        }
    }

    /**
     * <pre>
     * add ${class}.
     * USAGE:
     *   POST /api/${class}/
     *   {${class}VO}
     * SUCCESS:
     *   {header:{status:success},
     *   body:{${class}VO}}
     * ERROR:
     *   {header: {status: error}, body:{Exception.getMessage()}}
     * </pre>
     *
     * @param vo
     * @return ${class}VO
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since ${date}
     * auto generate by qdp v5.0.
     */
    @ApiOperation(value = "add ${class}", notes = "add ${class}")
    @PostMapping("/")
    public ResultBean add${class}(@RequestBody ${class}VO vo) {
        try {
            return ResultBean.success().setBody(${class?uncap_first}Application.add${class}(vo));
        } catch (Exception e) {
            return ResultBean.error().setBody(e.getMessage());
        }
    }

    /**
     * <pre>
     * update ${class}.
     * USAGE:
     *   POST /api/${class}/
     *   {${class}VO}
     * SUCCESS:
     *   {header:{status:success},
     *   body:{${class}VO}}
     * ERROR:
     *   {header: {status: error}, body:{Exception.getMessage()}}
     * </pre>
     *
     * @param vo
     * @return ${class}VO
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since ${date}
     * auto generate by qdp v5.0.
     */
    @ApiOperation(value = "update ${class}", notes = "update ${class}")
    @PutMapping("/")
    public ResultBean update${class}(@RequestBody ${class}VO vo) {
        try {
            return ResultBean.success().setBody(${class?uncap_first}Application.update${class}(vo));
        } catch (Exception e) {
            return ResultBean.error().setBody(e.getMessage());
        }
    }

    /**
     * <pre>
     * remove ${class} multi ids join by comma ','.
     * USAGE:
     *   DELETE /api/${class}/{id}
     * SUCCESS:
     *   {header:{status:success},
     *   body:{}}
     * ERROR:
     *   {header: {status: error}, body:{Exception.getMessage()}}
     * </pre>
     *
     * @param id
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since ${date}
     * auto generate by qdp v5.0.
     */
    @ApiOperation(value = "delete ${class}", notes = "delete ${class}")
    @DeleteMapping("/{id}")
    public ResultBean remove${class}ById(@PathVariable(value = "id") String id) {
        try {
            if (id.indexOf(',') > -1) {
                ${class?uncap_first}Application.remove${class}ByIds(StringUtils.split(id, ','));
            } else {
                ${class?uncap_first}Application.remove${class}ById(id);
            }
            return ResultBean.success();
        } catch (Exception e) {
            return ResultBean.error().setBody(e.getMessage());
        }
    }
	
	<#--唯一方法-->
	<#list unique as field><#local javaFieldName=javaField(field.field)/>
    /**
     * <pre>
     * get ${class} by unique name
     * USAGE:
     *   GET /api/${class}/get/${javaFieldName}/{${javaFieldName}}
     * SUCCESS:
     *   {header:{status:success},
     *   body:{${class}VO}}
     * ERROR:
     *   {header: {status: error}, body:{Exception.getMessage()}}
     * </pre>
     *
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since ${date}
     * auto generate by qdp v5.0.
     */
    @ApiOperation(value = "get ${class} by ${javaFieldName}", notes = "get ${class} by ${javaFieldName}")
    @GetMapping("/get/${javaFieldName}/{${javaFieldName}}")
    public ResultBean getBy${javaFieldName?cap_first}(@PathVariable(value = "${javaFieldName}") ${field.javaType} ${javaFieldName}) {
        try {
            return ResultBean.success().setBody(${class?uncap_first}Application.getBy${javaFieldName?cap_first}(${javaFieldName}));
        } catch (Exception e) {
            return ResultBean.error().setBody(e.getMessage());
        }
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
     * USAGE:
     *   GET /api/${class}/pageAssign${refClass}${fieldNamesPath}/{currentPage}/{pageSize}/{asc}/{desc}
     * SUCCESS:
     *   {header:{status:success},
     *   body:{pageSize,totalCount,currentPage,offset,offsetPage,orderBy:[],rows:[{${refClass}VO}]}}
     * ERROR:
     *   {header: {status: error}, body:{Exception.getMessage()}}
     * </pre>
     *
     <#list fieldNames as fieldName>
     * @param ${fieldName}
     </#list>
     * @param currentPage
     * @param pageSize
     * @param asc
     * @param desc
     * @return ResultBean
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since ${date}
     * auto generate by qdp v5.0
     */
    @ApiOperation(value = "page find assign ${refClass}", notes = "page find assign ${refClass}")
    @GetMapping({"/pageAssign${refClass}${fieldNamesPath}/{currentPage}/{pageSize}/{asc}/{desc}",
            "/pageAssign${refClass}${fieldNamesPath}/{currentPage}/{pageSize}",
            "/pageAssign${refClass}${fieldNamesPath}/{currentPage}",
            <#list fieldNames as fieldName>
            <#local subArr=fieldNames?size-fieldName?index-1 />
            "/pageAssign${refClass}/{${fieldNames[0..subArr]?join('}/{')}}",
            </#list>
            "/pageAssign${refClass}"})
    public ResultBean pageFindAssign${refClass}(
                                       <#list fieldNames as fieldName>
                                       @PathVariable(required = false, value = "${fieldName}") String ${fieldName},
                                       </#list>
                                       @PathVariable(required = false, value = "currentPage") Integer currentPage,
                                       @PathVariable(required = false, value = "pageSize") Integer pageSize,
                                       @PathVariable(required = false, value = "asc") String asc,
                                       @PathVariable(required = false, value = "desc") String desc) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            {
                 <#list fieldNames as fieldName>
                 map.put("${fieldName}", PreCheckHelper.equalsToNull(${fieldName}, "-"));
                 </#list>
            }
            Page page = new Page();
            {
                page.setCurrentPage(NumberHelper.getInt(currentPage, 1));
                page.setPageSize(NumberHelper.getInt(pageSize, 10));
                asc = PreCheckHelper.equalsToNull(asc, "-");
                desc = PreCheckHelper.equalsToNull(desc, "-");
                if (StringUtils.isNotBlank(asc)) {
                    page.addAscOrderBy(asc);
                }
                if (StringUtils.isNotBlank(desc)) {
                    page.addDescOrderBy(desc);
                }
            }
            ${class}VO vo = BeanHelper.copyProperties(${class}VO.class, map);
            return ResultBean.success().setBody(${class?uncap_first}Application.pageFindAssign${refClass}(vo, page));
        } catch (Exception e) {
            return ResultBean.error().setBody(e.getMessage());
        }
    }

    /**
     * <pre>
     * assign ${refClass} by id(s).
     * USAGE:
     *   POST /api/${class}/assign${refClass}
     *   {${class}VO}
     * SUCCESS:
     *   {header:{status:success},
     *   body:{}
     * ERROR:
     *   {header: {status: error}, body:{Exception.getMessage()}}
     * </pre>
     *
     * @param vo
     * @return ResultBean
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since ${date}
     * auto generate by qdp v5.0.
     */
    @ApiOperation(value = "assign ${refClass} by id(s)", notes = "assign ${refClass} by id(s)")
    @PostMapping("/assign${refClass}")
	public ResultBean assign${refClass}(${class}VO vo) {
        try {
            ${class?uncap_first}Application.assign${refClass}(vo);
            return ResultBean.success();
        } catch (Exception e) {
            return ResultBean.error().setBody(e.getMessage());
        }
	}
	</#if></#list>
	
	<#list func.children as item><#--弹出表单-->
		<#if item.type=='page' && item.name!='index' && item.formType=='form'>
    /**
     * <pre>
     * ${javaField(columnField(item.fileName))}.
     * USAGE:
     *   POST /api/${class}/${javaField(columnField(item.fileName))}
     *   {${javaClass(columnField(item.fileName),'')?cap_first}VO}
     * SUCCESS:
     *   {header:{status:success},
     *   body:{}
     * ERROR:
     *   {header: {status: error}, body:{Exception.getMessage()}}
     * </pre>
     *
     * @param vo
     * @return ResultBean
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since ${date}
     * auto generate by qdp v5.0.
     */
    @ApiOperation(value = "operation ${javaField(columnField(item.fileName))}", notes = "operation ${javaField(columnField(item.fileName))}")
    @PostMapping("/${javaField(columnField(item.fileName))}")
	public ResultBean ${javaField(columnField(item.fileName))}(${javaClass(columnField(item.fileName),'')?cap_first}VO vo) {
        try {
            ${class?uncap_first}Application.${javaField(columnField(item.fileName))}(vo);
            return ResultBean.success();
        } catch (Exception e) {
            return ResultBean.error().setBody(e.getMessage());
        }
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
					name="${rsImplPackage(package, moduleName,func.packageName)}.${class}RSApplicationImpl.java">
					<@applicationimpl date=date package=package class=class module=moduleName table=table pk=pk notNull=notNull index=index unique=unique autoInc=autoInc columns=columns foreign=foreign func=func proModule=module project=project/>
				</@filewriter>
			</#if>
		</#list>
	</#list>
</#list>



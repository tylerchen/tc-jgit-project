<#include "/_functions.ftl" />
<#macro controller date package class module table pk notNull index unique autoInc columns foreign func proModule>
/*******************************************************************************
 * Copyright (c) ${date} @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package ${controllerPackage(package,module,func.packageName)};

import java.io.IOException;
import java.util.Map;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.iff.infra.util.MapHelper;
import org.iff.infra.util.ValidateHelper;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.commons.lang3.StringUtils;
import com.foreveross.common.ResultBean;
import com.foreveross.common.ConstantBean;
import com.foreveross.common.web.BaseController;
import ${appPackage(package,module,func.packageName)}.${class}Application;
import ${voPackage(package,module,func.packageName)}.${class}VO;

/**
 * ${class}
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since ${date}
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@Controller
@RequestMapping("/${module}/${class?lower_case}")
public class ${class}Controller extends BaseController {

	@Inject
	${class}Application ${class?uncap_first}Application;

	/**
	 * add ${class}.
	 * @param vo
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return ResultBean
	 * @throws IOException
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	@ResponseBody
	@RequestMapping("/add${class}.do")
	public ResultBean add${class}(${class}VO vo, ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		try{
			ValidateHelper validate = validate(vo, "add");
			if(validate.hasNoErrors()){
				${class?uncap_first}Application.add${class}(vo);
				return success("操作成功！");
			} else {
				return error(validate.joinErrors("\n"));
			}
		} catch (Exception e) {
			return error(e.getMessage());
		}
	}

	/**
	 * edit ${class}.
	 * @param vo
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return ResultBean
	 * @throws IOException
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	@ResponseBody
	@RequestMapping("/edit${class}.do")
	public ResultBean edit${class}(${class}VO vo, ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		try{
			ValidateHelper validate = validate(vo, "edit");
			if(validate.hasNoErrors()){
				${class?uncap_first}Application.update${class}(vo);
				return success("操作成功！");
			} else {
				return error(validate.joinErrors("\n"));
			}
		} catch (Exception e) {
			return error(e);
		}
	}

	/**
	 * delete ${class}.
	 * @param vo
	 * @param modelMap
	 * @param request
	 * @param response
	 * @return ResultBean
	 * @throws IOException
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	@ResponseBody
	@RequestMapping("/delete${class}.do")
	public ResultBean delete${class}(${class}VO vo, ModelMap modelMap,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		try{
			ValidateHelper validate = validate(vo, "delete");
			if(validate.hasNoErrors()){
				<#list pk as field><#if field_index==0><#local javaFieldName=javaField(field.field)/>
				String[] ids = StringUtils.split(vo.get${javaFieldName?cap_first}(), ",");
				if (ids.length > 1) {
					${class?uncap_first}Application.remove${class}ByIds(ids);
				} else {
					${class?uncap_first}Application.remove${class}ById(vo.get${javaFieldName?cap_first}());
				}
				</#if></#list>
				<#if (pk?size>1) >
				// TODO Not Support multiple primary keys, change codes by yourself.
				</#if>
				return success("操作成功！");
			} else {
				return error(validate.joinErrors("\n"));
			}
		} catch (Exception e) {
			return error(e);
		}
	}

	/**
	 * validate ${class} data.
	 * @param vo
	 * @param type add, edit, delete
	 * @return ValidateHelper
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	private ValidateHelper validate(${class}VO vo, String type){
		ValidateHelper validate = ValidateHelper.create();
		/*
			"NO":"不用验证","email":"EMAIL","tel":"电话号码","mobile":"手机号码","zipcode":"邮政编码","url":"网址",
			"date":"日期","number":"数字","digits":"整数","creditcard" :"信用卡号","idcard":"身份证号码",
			"chinese":"中文","ipv4":"IPv4","ipv6":"IPv6"
		*/
		validate.required("${class}", vo, "iff.validate.required","{0} is required!");
		if(vo == null){
			return validate;
		}
		if("add".equals(type) || "edit".equals(type)){
		<#--<#list modelProperties as mp>
			<#local ModelPropertyConfigVO=modelPropertyConfigs[mp.id]!{} javaFieldName=javaField(mp.name)/>
			<#if ModelPropertyConfigVO.validateType?has_content>
				<#switch ModelPropertyConfigVO.validateType>
				<#case "NO">
				<#break>
				<#case "email">
			validate.email("${class}.${javaFieldName}", vo.get${javaFieldName?cap_first}(), "iff.validate.email","{0} is not valid email!");
				<#break>
				<#case "tel">
			validate.tel("${class}.${javaFieldName}", vo.get${javaFieldName?cap_first}(), "iff.validate.tel","{0} is not valid tel No.!");
				<#break>
				<#case "mobile">
			validate.mobile("${class}.${javaFieldName}", vo.get${javaFieldName?cap_first}(), "iff.validate.mobile","{0} is not valid mobile No.!");
				<#break>
				<#case "zipcode">
			validate.zipcode("${class}.${javaFieldName}", vo.get${javaFieldName?cap_first}(), "iff.validate.zipcode","{0} is not valid zipcode!");
				<#break>
				<#case "url">
			validate.url("${class}.${javaFieldName}", vo.get${javaFieldName?cap_first}(), "iff.validate.url","{0} is not valid url!");
				<#break>
				<#case "date">
			validate.date("${class}.${javaFieldName}", vo.get${javaFieldName?cap_first}(), "iff.validate.date","{0} is not valid date!");
				<#break>
				<#case "number">
			validate.number("${class}.${javaFieldName}", vo.get${javaFieldName?cap_first}(), "iff.validate.number","{0} is not valid number!");
				<#break>
				<#case "digits">
			validate.digits("${class}.${javaFieldName}", vo.get${javaFieldName?cap_first}(), "iff.validate.digits","{0} is not valid digits!");
				<#break>
				<#case "creditcard">
			validate.creditcard("${class}.${javaFieldName}", vo.get${javaFieldName?cap_first}(), "iff.validate.creditcard","{0} is not valid creditcard!");
				<#break>
				<#case "idcard">
			validate.idcard("${class}.${javaFieldName}", vo.get${javaFieldName?cap_first}(), "iff.validate.idcard","{0} is not valid idcard!");
				<#break>
				<#case "chinese">
			validate.chinese("${class}.${javaFieldName}", vo.get${javaFieldName?cap_first}(), "iff.validate.chinese","{0} is not valid chinese!");	
				<#break>
				<#case "ipv4">
			/*validate.ipv4("${class}.${javaFieldName}", vo.get${javaFieldName?cap_first}(), "iff.validate.ipv4","{0} is not valid ipv4!");*/
			/*ipv4 validate is not support yet!*/
				<#break>
				<#case "ipv6">
			/*validate.ipv6("${class}.${javaFieldName}", vo.get${javaFieldName?cap_first}(), "iff.validate.ipv6","{0} is not valid ipv6!");*/
			/*ipv6 validate is not support yet!*/
				<#break>
				<#default>
					<#if !('Y'==mp.isPrimaryKey) && !('Y'==mp.isNullable)>
			validate.required("${class}.${javaFieldName}", vo.get${javaFieldName?cap_first}(), "iff.validate.required","{0} is required!");
					</#if>
				</#switch>
			</#if>
		</#list>-->
		}
		if ("edit".equals(type) || "delete".equals(type)){
			<#list pk as field><#if field_index==0><#local javaFieldName=javaField(field.field)/>
			validate.required("${class}.${javaFieldName}", vo.get${javaFieldName?cap_first}(), "iff.validate.required","{0} is required!");
			</#if></#list>
		}
		return validate;
	}
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
					name="${controllerPackage(package, moduleName,func.packageName)}.${class}Controller.java">
					<@controller date=date package=package class=class module=moduleName table=table pk=pk notNull=notNull index=index unique=unique autoInc=autoInc columns=columns foreign=foreign func=func proModule=module/>
				</@filewriter>
			</#if>
		</#list>
	</#list>
</#list>


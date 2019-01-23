<#include "/_functions.ftl" />
<#include "/_page_util.ftl" />
<#macro model date package class module table pk notNull index unique autoInc columns foreign func proModule project>
<#local rootId={} parentId={} menuIndex={} maxLevel={}/>
<#if table.formType=='tree'><#list columns as field>
<#if field.field=='ROOT_ID' && field.refTable==table.tableName><#local rootId=field/></#if>
<#if field.field=='PARENT_ID' && field.refTable==table.tableName><#local parentId=field/></#if>
<#if field.field=='MENU_INDEX'><#local menuIndex=field/></#if>
<#if field.field=='MAX_LEVEL'><#local maxLevel=field/></#if>
</#list></#if>
/*******************************************************************************
 * Copyright (c) ${date} @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package ${domainPackage(package,module,func.packageName)};

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.iff.infra.util.Assert;
import org.iff.infra.util.BeanHelper;
import org.iff.infra.util.Exceptions;
import org.iff.infra.util.MapHelper;
import org.iff.infra.util.ValidateHelper;
import org.iff.infra.util.mybatis.plugin.Page;
import org.iff.infra.util.mybatis.service.Dao;

import org.apache.commons.lang3.StringUtils;

/**
 * ${class}
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since ${date}
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
@SuppressWarnings("serial")
public class ${class} implements Serializable {

<#list columns as field>
	/** ${field.label!field.field} **/
	private ${field.javaType} ${javaField(field.field)};
</#list>

	public ${class}() {
		super();
	}

<#list columns as field>
	<#local javaFieldName=javaField(field.field)/>
	public ${field.javaType} get${javaFieldName?cap_first}() {
		return ${javaFieldName};
	}

	public void set${javaFieldName?cap_first}(${field.javaType} ${javaFieldName}) {
		this.${javaFieldName} = ${javaFieldName};
	}
</#list>

	/**
	 * <pre>
	 * get ${class} by id	 
	 * Usage : ${class}.get(id);
	 * </pre>
	 * @param ${class?uncap_first}
	 * @return ${class}
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	public static ${class} get(${class} ${class?uncap_first}) {
		return Dao.queryOne("${class}.get${class}ById", ${class?uncap_first});
	}
	
	<#list pk as field><#if field_index==0><#local javaFieldName=javaField(field.field)/>
	/**
	 * <pre>
	 * get ${class} by id	 
	 * Usage : ${class}.get(id);
	 * </pre>
	 * @param ${javaFieldName}
	 * @return ${class}
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	public static ${class} get(${field.javaType} ${javaFieldName}) {
		${class} ${class?uncap_first} = new ${class}();
		${class?uncap_first}.set${javaFieldName?cap_first}(${javaFieldName});
		return get(${class?uncap_first});
	}
	
	</#if></#list>
	/**
	 * <pre>
	 * remove ${class} by id
	 * Usage : ${class}.remove(id)
	 * </pre>
	 * @param ${class?uncap_first}
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	public static void remove(${class} ${class?uncap_first}) {
		${class?uncap_first}.remove();
	}
	
	<#list pk as field><#if field_index==0><#local javaFieldName=javaField(field.field)/>
	/**
	 * <pre>
	 * remove ${class} by id
	 * Usage : ${class}.remove(id)
	 * </pre>
	 * @param ${javaFieldName}
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	public static void remove(${field.javaType} ${javaFieldName}) {
		${class} ${class?uncap_first} = new ${class}();
		${class?uncap_first}.set${javaFieldName?cap_first}(${javaFieldName});
		remove(${class?uncap_first});
	}
	
	/**
	 * <pre>
	 * remove ${class} by id
	 * Usage : ${class}.remove(id)
	 * </pre>
	 * @param ${javaFieldName}s
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	public static void remove(${field.javaType}[] ${javaFieldName}s) {
		if (${javaFieldName}s != null) {
			for (${field.javaType} ${javaFieldName} : ${javaFieldName}s) {
				${class} ${class?uncap_first} = new ${class}();
				${class?uncap_first}.set${javaFieldName?cap_first}(${javaFieldName});
				remove(${class?uncap_first});
			}
		}
	}
	
	</#if></#list>
	<#list unique as field><#local javaFieldName=javaField(field.field)/>
	/**
	 * <pre>
	 * has ${javaFieldName?cap_first}
	 * Usage : ${class}.has${javaFieldName?cap_first}(${class})
	 * </pre>
	 * @param ${class?uncap_first}
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	public static boolean has${javaFieldName?cap_first}(${class} ${class?uncap_first}) {
		return Dao.querySize("${class}.has${javaFieldName?cap_first}", ${class?uncap_first}) > 0;
	}
	
	</#list>
	/**
	 * <pre>
	 * add ${class}
	 * Usage : ${class}.add()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	public ${class} add() {
		ValidateHelper validate = validate("add");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("${class}.insert${class}", this);
		<#local mainPk={}/>
		<#if rootId?has_content><#list pk as pkf><#if !mainPk?has_content><#local mainPk=pkf/></#if></#list>
		if (StringUtils.isBlank(get${javaField(rootId.field)?cap_first}())) {// set root id for tree.
			set${javaField(rootId.field)?cap_first}(get${javaField(mainPk.field)?cap_first}());
			update();
		}
		</#if>
		<#if rootId?has_content && parentId?has_content && menuIndex?has_content>
		reOrder( getParentId() );
		</#if>
		return this;
	}

	/**
	 * <pre>
	 * update ${class}
	 * Usage : ${class}.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	public ${class} update() {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Dao.save("${class}.update${class}", this);
		<#if rootId?has_content && parentId?has_content && menuIndex?has_content>
		reOrder( getParentId() );
		</#if>
		return this;
	}
	
	/**
	 * <pre>
	 * add or update ${class}
	 * Usage : ${class}.update()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	public ${class} addOrUpdate() {
		<#local hasPk=0/>
		<#list table.fields as field><#if field.isPk><#local javaFieldName=javaField(field.field)/><#local hasPk=hasPk+1/>
		if (StringUtils.isBlank(get${javaFieldName?cap_first}())) {
			return add();
		} else {
			return update();
		}
		</#if></#list>
		<#if hasPk gt 1>
		// TODO Not Support multiple primary keys, change codes by yourself.
		</#if>
	}
	
	/**
	 * <pre>
	 * remove ${class} by id
	 * Usage : ${class}.remove()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	public void remove() {
		ValidateHelper validate = validate("delete");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		<#--关联中间表操作-->
		<#list foreign as field>
			<#if field.midTable?has_content && field.midMainField?has_content && field.midSecondField?has_content>
				<#local refClass=javaClass(field.refTable,removePrefix) midClass=javaClass(field.midTable,removePrefix) refMFT=getTable(field.refTable, project) refMidMFT=getTable(field.midTable, project)/>
				<#local refModule=refMFT.module refFunc=refMFT.func refTable=refMFT.table/><#if !refTable?has_content><break/></#if>
				<#local mainPk={} secondPk={} midPk={}/>
				<#list pk as pkf><#if !mainPk?has_content><#local mainPk=pkf/></#if></#list>
				<#list refTable.fields as pkf><#if pkf.isPk && !secondPk?has_content><#local secondPk=pkf/></#if></#list>
				<#list refMidMFT.table.fields as pkf><#if pkf.isPk && !midPk?has_content><#local midPk=pkf/></#if></#list>
		// 删除${midClass}
		remove${midClass}();
		</#if></#list>
		// 删除本对象
		Dao.remove("${class}.delete${class}", this);
	}
	
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
	public static ${class} getBy${javaFieldName?cap_first}(${field.javaType} ${javaFieldName}) {
		${class} domain = new ${class}();
		domain.set${javaFieldName?cap_first}(${javaFieldName});
		return Dao.queryOne("${class}.get${class}By${javaFieldName?cap_first}", domain);
	}
	
	</#list>
	
	<#--关联中间表操作-->
	<#list foreign as field>
		<#if field.midTable?has_content && field.midMainField?has_content && field.midSecondField?has_content>
			<#local refClass=javaClass(field.refTable,removePrefix) midClass=javaClass(field.midTable,removePrefix) refMFT=getTable(field.refTable, project) refMidMFT=getTable(field.midTable, project)/>
			<#local refModule=refMFT.module refFunc=refMFT.func refTable=refMFT.table/><#if !refTable?has_content><break/></#if>
			<#local midModule=refMidMFT.module midFunc=refMidMFT.func midTable=refMidMFT.table/><#if !midTable?has_content><break/></#if>
			<#local mainPk={} secondPk={} midPk={}/>
			<#list pk as pkf><#if !mainPk?has_content><#local mainPk=pkf/></#if></#list>
			<#list refTable.fields as pkf><#if pkf.isPk && !secondPk?has_content><#local secondPk=pkf/></#if></#list>
			<#list refMidMFT.table.fields as pkf><#if pkf.isPk && !midPk?has_content><#local midPk=pkf/></#if></#list>
			<#local packageDomain=domainPackage(package,module,func.packageName)/>
			<#local packageRef=domainPackage(package, refModule.packageName,refFunc.packageName)/>
			<#local packageMid=domainPackage(package, midModule.packageName,midFunc.packageName)/>
			<#local packageRef=(packageRef==packageDomain)?then('', packageRef+'.')/>
			<#local packageMid=(packageMid==packageDomain)?then('', packageMid+'.')/>
	/**
	 * <pre>
	 * find ${midClass}.
	 * Usage : ${class}.find${midClass}()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	public List<${packageRef}${refClass}> find${refClass}() {
		${class} condition = new ${class}();
		{
			condition.set${javaField(mainPk.field)?cap_first}(this.get${javaField(mainPk.field)?cap_first}());
		}
		Page page = Dao.queryPage("${class}.pageFind${refClass}By${midClass}Map", 
				MapHelper.toMap("page", Page.offsetPage(0, 100000, null), "vo", condition));
		return page.toPage(${packageRef}${refClass}.class).getRows();
	}
	/**
	 * <pre>
	 * find ${midClass}.
	 * Usage : ${class}.find${midClass}()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	public List<${packageMid}${midClass}> find${midClass}() {
		${class} condition = new ${class}();
		{
			condition.set${javaField(mainPk.field)?cap_first}(this.get${javaField(mainPk.field)?cap_first}());
		}
		Page page = Dao.queryPage("${class}.pageFind${midClass}Map",
				MapHelper.toMap("page", Page.offsetPage(0, 100000, null), "vo", condition));
		return page.toPage(${packageMid}${midClass}.class).getRows();
	}
	/**
	 * <pre>
	 * remove ${midClass}.
	 * Usage : ${class}.remove${midClass}()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	public void remove${midClass}() {
		List<${midClass}> list = find${midClass}();
		for (${midClass} item : list) {
			item.remove();
		}
	}
	
	/**
	 * <pre>
	 * assign ${refClass} by id
	 * Usage : ${class}.assign${refClass}()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	public void assign${refClass}ByIds(${secondPk.javaType}[] ids) {
		ValidateHelper validate = validate("edit");
		if (validate.hasErrors()) {
			Exceptions.runtime(validate.joinErrors("\n"));
		}
		Assert.notNull(${class}.get(this.get${javaField(mainPk.field)?cap_first}()));
		Map<${secondPk.javaType}, ${midClass}> map = new HashMap<${secondPk.javaType}, ${midClass}>();
		if (ids != null) {
			int counter = 0;
			for (${secondPk.javaType} id : ids) {
				${refClass} ref = ${refClass}.get(id);
				Assert.notNull(ref);
				<#compress>
					<#local refRootId={} refParentId={} midSort={}/><#--树、排序的特有操作-->
					<#if refTable.formType=='tree'>
						<#list refTable.fields as field>
							<#if field.field=='ROOT_ID' && field.refTable==refTable.tableName><#local refRootId=field/></#if>
							<#if field.field=='PARENT_ID' && field.refTable==refTable.tableName><#local refParentId=field/></#if>
						</#list>
						<#list midTable.fields as field>
							<#if field.field=='SORT'><#local midSort=field/></#if>
						</#list>
					</#if>
				</#compress>
				<#if refRootId?has_content && refParentId?has_content>
				{// 如果分配的是树型结构，那分配的是根节点
					id = ref.getRootId();
				} 
				</#if>
				${midClass} tmp = new ${midClass}();
				{
					tmp.set${javaField(field.midMainField)?cap_first}(this.get${javaField(mainPk.field)?cap_first}());
					tmp.set${javaField(field.midSecondField)?cap_first}(id);
					<#if midSort?has_content>
					// 如果需要排序
					tmp.setSort(++counter * 10);
					</#if>
				}
				map.put(id, tmp);
			}
		}

		List<${midClass}> list = find${midClass}();
		for (${midClass} item : list) {
			if (map.containsKey(item.get${javaField(field.midSecondField)?cap_first}())) {
				${midPk.javaType} ${javaField(midPk.field)} = item.get${javaField(midPk.field)?cap_first}();
				${midClass} tmp = BeanHelper.copyProperties(item, map.remove(item.get${javaField(field.midSecondField)?cap_first}()));
				tmp.set${javaField(midPk.field)?cap_first}(${javaField(midPk.field)});
				<#if midClass=='AuthAutherRole' || midClass=='AuthAutherMenu'><#--这个是权限的特殊代码-->tmp.setType(getClass().getSimpleName());</#if>
				tmp.addOrUpdate();
			} else {
				item.remove();
			}
		}

		for (${midClass} item : map.values()) {
			<#if midClass=='AuthAutherRole' || midClass=='AuthAutherMenu'><#--这个是权限的特殊代码-->item.setType(getClass().getSimpleName());</#if>
			item.add();
		}
	}
	</#if></#list>
	
	<#--树的特有操作-->
	<#if rootId?has_content && parentId?has_content>
	/**
	 * <pre>
	 * find all menu by rootId.
	 * Usage : ${class}.findByRootId()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	public static List<${class}> findByRootId(${rootId.javaType} rootId) {
		if ( StringUtils.isBlank(String.valueOf(rootId)) ) {
			return new ArrayList<${class}>();
		}
		${class} condition = new ${class}();
		condition.setRootId(rootId);
		Page page = Page.offsetPage(0, 100000, null);
		<#if menuIndex?has_content>page.addAscOrderBy("menuIndex");</#if>
		List<${class}> list = Dao.queryList("${class}.pageFind${class}", MapHelper.toMap("page", page, "vo", condition));
		return list;
	}

	/**
	 * <pre>
	 * find all menu by parentId.
	 * Usage : ${class}.findByParentId()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	public static List<${class}> findByParentId(${parentId.javaType} parentId) {
		if ( StringUtils.isBlank(String.valueOf(parentId)) ) {
			return new ArrayList<${class}>();
		}
		${class} condition = new ${class}();
		condition.setParentId(parentId);
		Page page = Page.offsetPage(0, 100000, null);
		<#if menuIndex?has_content>page.addAscOrderBy("menuIndex");</#if>
		List<${class}> list = Dao.queryList("${class}.pageFind${class}", 
				MapHelper.toMap("page", page, "vo", condition));
		return list;
	}

		<#if maxLevel?has_content>
	/**
	 * <pre>
	 * check menu level.
	 * Usage : ${class}.checkMenuLevel()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	public static boolean checkMenuLevel(${parentId.javaType} parentId) {
		if ( parentId == null || StringUtils.isBlank(String.valueOf(parentId)) ) {
			return true;
		}
		${class} domain = get(parentId);
		int count = 0;
		while ( domain != null ) {
			count = count + 1;
			if ( StringUtils.equals(domain.getId(), domain.getRootId()) || 
					StringUtils.equals(domain.getId(), domain.getParentId()) || count > 100) {// 如果当前节点就是根节点
				break;
			}
			domain = get( domain.getParentId() );
		}
		if ( domain != null && (domain.getMaxLevel() - 1) >= count ) {
			return true;
		}
		return false;
	}
		</#if>
	
		<#if menuIndex?has_content>
	/**
	 * <pre>
	 * Re-order by parentId.
	 * Usage : ${class}.reOrder()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	public static List<${class}> reOrder(${parentId.javaType} parentId) {
		List<${class}> items = findByParentId(parentId);
		int i = 1;
		for( ${class} item : items ) {
			item.setMenuIndex( i++ * 10 );
			item.updateByMenuIdex();
		}
		return items;
	}
	
	/**
	 * <pre>
	 * update ${class} by menu index.
	 * Usage : ${class}.updateByMenuIdex()
	 * </pre>
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	public ${class} updateByMenuIdex() {
		Dao.save("${class}.update${class}", this);
		return this;
	}
		</#if>
	</#if>
	
	/**
	 * <pre>
	 * validate add/update/delete
	 * </pre>
	 * @param type add/update/delete
	 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
	 * @since ${date}
	 */
	private ValidateHelper validate(String type) {
		ValidateHelper validate = ValidateHelper.create();
		if ("add".equals(type)) {
			{//初始化值
		<#list table.fields as field><#local javaFieldName=javaField(field.field)/>
			<#if field.javaType=='Date' && ['updateTime','createTime','updateDate','createDate','updateDateTime','createDateTime']?seq_contains(javaFieldName) >
				set${javaFieldName?cap_first}(new java.util.Date());
			</#if>
			<#if field.type.add=='password' && field.javaType=='String'>
				if( StringUtils.isNotBlank(get${javaFieldName?cap_first}()) ){
					set${javaFieldName?cap_first}(org.iff.infra.util.MD5Helper.secondSalt(org.iff.infra.util.MD5Helper.firstSalt(get${javaFieldName?cap_first}())));
				} else {
					set${javaFieldName?cap_first}( null );
				}
			</#if>
		</#list>
			}
			<#--树的特有操作-->
			<#if rootId?has_content && parentId?has_content && maxLevel?has_content>
			{// 设置默认值
				${class} parent = get(getParentId());
				if( parent != null ) {
					<#if getField('TYPE1', table)?has_content>setType1(parent.getType1());</#if>
					<#if getField('TYPE2', table)?has_content>setType2(parent.getType2());</#if>
					setRootId(parent.getRootId());
					setMaxLevel(parent.getMaxLevel());
				}
			}
			{// 检查最大层级
				validate.isTrue("${class}.maxLevel", checkMenuLevel(getParentId()), "${class}.maxLevel",
						"{0} level is deep than root menu setting.");
			}
			</#if>
			// validte the field
			// "NO":"不用验证","email":"EMAIL","tel":"电话号码","mobile":"手机号码","zipcode":"邮政编码","url":"网址",
			// "date":"日期","number":"数字","digits":"整数","creditcard" :"信用卡号","idcard":"身份证号码",
			// "chinese":"中文","ipv4":"IPv4","ipv6":"IPv6"
			{
		<#list columns as field><#if field.addable><#local javaFieldName=javaField(field.field) ruleType=field.rule.type!'NO'/>
			<#switch ruleType>
				<#case "NO">
				<#break>
				<#case "email">
				validate.email("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.email", "{0} is not valid email!");
				<#break>
				<#case "tel">
				validate.tel("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.tel", "{0} is not valid tel No.!");
				<#break>
				<#case "mobile">
				validate.mobile("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.mobile", "{0} is not valid mobile No.!");
				<#break>
				<#case "zipcode">
				validate.zipcode("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.zipcode", "{0} is not valid zipcode!");
				<#break>
				<#case "url">
				validate.url("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.url", "{0} is not valid url!");
				<#break>
				<#case "date">
				validate.date("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.date", "{0} is not valid date!");
				<#break>
				<#case "number">
				validate.number("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.number", "{0} is not valid number!");
				<#break>
				<#case "digits">
				validate.digits("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.digits", "{0} is not valid digits!");
				<#break>
				<#case "creditcard">
				validate.creditcard("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.creditcard", "{0} is not valid creditcard!");
				<#break>
				<#case "idcard">
				validate.idcard("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.idcard", "{0} is not valid idcard!");
				<#break>
				<#case "chinese">
				validate.chinese("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.chinese", "{0} is not valid chinese!");	
				<#break>
				<#case "ipv4">
				//validate.ipv4("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.ipv4", "{0} is not valid ipv4!");//ipv4 validate is not support yet!
				<#break>
				<#case "ipv6">
				//validate.ipv6("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.ipv6", "{0} is not valid ipv6!");//ipv6 validate is not support yet!
				<#break>
				<#default>
					<#if !field.isPk && !field.isNull && ['createTime']?seq_index_of(javaFieldName) lt 0>
				validate.required("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.required", "{0} is required!");
					</#if>
			</#switch>
		</#if></#list>
			}
			{// validate unique
				<#list unique as field><#local javaFieldName=javaField(field.field)/>
				validate.isTrue("${class}.${javaFieldName}", 
						Dao.querySize("${class}.count${class}By${javaFieldName?cap_first}", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
				</#list>
			}
			{// validate value range
			<#list table.fields as field><#local javaFieldName=javaField(field.field)/>
				<#if (['radio','checkbox','switch','select']?seq_index_of(field.type.add) gt -1)
				     && helper("org.iff.infra.util.validation.ValidationMethods").json(field.jsonData)>
					<#local dataMap=helper("GsonHelper").toJson(field.jsonData)!{} keyArr=dataMap.keySet()/>
					<#local keyString=keyArr?join('", "')/>
					 <#if keyString?has_content>
					 	<#local keyString='String[] keys = new String[]{"'+keyString+'"};'/>
			 	{
				 	${keyString}
				 	validate.inArray("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
					</#if>
				</#if>
			</#list>
			}
			{// validate foreign key
			<#list foreign as field><#local javaFieldName=javaField(field.field)/>
				<#if !field.isNull><#local refModule=getTable(field.refTable,project) refTable=refModule.table/>
				<#if !refTable?has_content || field.isNotTableColumn><#break/></#if>
				<#local refModuleName=refModule.module.packageName refClass=javaClass(refModule.func.tableName,removePrefix) />
				{// check the foreign key.
					${domainPackage(package,refModuleName,refModule.func.packageName)}.${refClass} ${refClass?uncap_first} = ${domainPackage(package,refModuleName,refModule.func.packageName)}.${refClass}.get(get${javaFieldName?cap_first}());
					validate.required("${refClass}", ${refClass?uncap_first}, "iff.validate.required", "{0} is required!");
				}
				<#else>
				{// clean the empty data.
					if ((get${javaFieldName?cap_first}() instanceof String) && get${javaFieldName?cap_first}().length() < 1) {
						set${javaFieldName?cap_first}(null);
					}
				}
				</#if>
			</#list>
			}
		} else if ("edit".equals(type)) {
			{//初始化值
		<#list table.fields as field><#local javaFieldName=javaField(field.field)/>
			<#if field.javaType=='Date' && ['updateTime','updateDate','updateDateTime']?seq_contains(javaFieldName) >
				set${javaFieldName?cap_first}(new java.util.Date());
			</#if>
			<#if field.javaType=='Date' && ['createTime','createDate','createDateTime']?seq_contains(javaFieldName) >
				set${javaFieldName?cap_first}(null);
			</#if>
			<#if field.type.edit=='password' && field.javaType=='String'>
				if( StringUtils.isNotBlank(get${javaFieldName?cap_first}()) ){
					set${javaFieldName?cap_first}(org.iff.infra.util.MD5Helper.secondSalt(org.iff.infra.util.MD5Helper.firstSalt(get${javaFieldName?cap_first}())));
				} else {
					set${javaFieldName?cap_first}( null );
				}
			</#if>
		</#list>
			}
			<#--树的特有操作-->
			<#if rootId?has_content && parentId?has_content && maxLevel?has_content>
			{// 设置默认值
				${class} parent = get(getParentId());
				if( parent != null ) {
					<#if getField('TYPE1', table)?has_content>setType1(parent.getType1());</#if>
					<#if getField('TYPE2', table)?has_content>setType2(parent.getType2());</#if>
					setRootId(parent.getRootId());
					setMaxLevel(parent.getMaxLevel());
				}
			}
			</#if>
			// validte the field
			// "NO":"不用验证","email":"EMAIL","tel":"电话号码","mobile":"手机号码","zipcode":"邮政编码","url":"网址",
			// "date":"日期","number":"数字","digits":"整数","creditcard" :"信用卡号","idcard":"身份证号码",
			// "chinese":"中文","ipv4":"IPv4","ipv6":"IPv6"
			{
		<#list columns as field><#if field.editable><#local javaFieldName=javaField(field.field) ruleType=field.rule.type!'NO'/>
			<#switch ruleType>
				<#case "NO">
				<#break>
				<#case "email">
				validate.email("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.email", "{0} is not valid email!");
				<#break>
				<#case "tel">
				validate.tel("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.tel", "{0} is not valid tel No.!");
				<#break>
				<#case "mobile">
				validate.mobile("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.mobile", "{0} is not valid mobile No.!");
				<#break>
				<#case "zipcode">
				validate.zipcode("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.zipcode", "{0} is not valid zipcode!");
				<#break>
				<#case "url">
				validate.url("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.url", "{0} is not valid url!");
				<#break>
				<#case "date">
				validate.date("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.date", "{0} is not valid date!");
				<#break>
				<#case "number">
				validate.number("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.number", "{0} is not valid number!");
				<#break>
				<#case "digits">
				validate.digits("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.digits", "{0} is not valid digits!");
				<#break>
				<#case "creditcard">
				validate.creditcard("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.creditcard", "{0} is not valid creditcard!");
				<#break>
				<#case "idcard">
				validate.idcard("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.idcard", "{0} is not valid idcard!");
				<#break>
				<#case "chinese">
				validate.chinese("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.chinese", "{0} is not valid chinese!");	
				<#break>
				<#case "ipv4">
				//validate.ipv4("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.ipv4", "{0} is not valid ipv4!");//ipv4 validate is not support yet!
				<#break>
				<#case "ipv6">
				//validate.ipv6("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.ipv6", "{0} is not valid ipv6!");//ipv6 validate is not support yet!
				<#break>
				<#default>
					<#if !field.isPk && !field.isNull && ['createTime']?seq_index_of(javaFieldName) lt 0>
				validate.required("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.required", "{0} is required!");
					</#if>
			</#switch>
		</#if></#list>
			}
			{// validate the primary key 
				<#list pk as field><#local javaFieldName=javaField(field.field)/>
				validate.required("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.required", "{0} is required!");
				</#list>
			}
			{// validate unique
				<#list unique as field><#local javaFieldName=javaField(field.field)/>
				validate.isTrue("${class}.${javaFieldName}", 
						Dao.querySize("${class}.count${class}By${javaFieldName?cap_first}", MapHelper.toMap("vo", this)) < 1, 
						"iff.validate.hasExisted", "{0} is existed!");
				</#list>
			}
			{// validate value range
			<#list table.fields as field><#local javaFieldName=javaField(field.field)/>
				<#if (['radio','checkbox','switch','select']?seq_index_of(field.type.add) gt -1)
				     && helper("org.iff.infra.util.validation.ValidationMethods").json(field.jsonData)>
					<#local dataMap=helper("GsonHelper").toJson(field.jsonData)!{} keyArr=dataMap.keySet()/>
					<#local keyString=keyArr?join('", "')/>
					 <#if keyString?has_content>
					 	<#local keyString='String[] keys = new String[]{"'+keyString+'"};'/>
			 	{
				 	${keyString}
				 	validate.inArray("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.notInArray", "{0} is not in array!", keys);
			 	}
					</#if>
				</#if>
			</#list>
			}
			{// validate foreign key
			<#list foreign as field><#local javaFieldName=javaField(field.field)/>
				<#if !field.isNull><#local refModule=getTable(field.refTable,project) refTable=refModule.table/>
				<#if !refTable?has_content || field.isNotTableColumn><#break/></#if>
				<#local refModuleName=refModule.module.packageName refClass=javaClass(refModule.func.tableName,removePrefix) />
				{// check the foreign key.
					${domainPackage(package,refModuleName,refModule.func.packageName)}.${refClass} ${refClass?uncap_first} = ${domainPackage(package,refModuleName,refModule.func.packageName)}.${refClass}.get(get${javaFieldName?cap_first}());
					validate.required("${refClass}", ${refClass?uncap_first}, "iff.validate.required", "{0} is required!");
				}
				<#else>
				{// clean the empty data.
					if ((get${javaFieldName?cap_first}() instanceof String) && get${javaFieldName?cap_first}().length() < 1) {
						set${javaFieldName?cap_first}(null);
					}
				}
				</#if>
			</#list>
			}
		} else if ("delete".equals(type)) {
			{// validate the primary key 
				<#list pk as field><#local javaFieldName=javaField(field.field)/>
				validate.required("${class}.${javaFieldName}", get${javaFieldName?cap_first}(), "iff.validate.required", "{0} is required!");
				</#list>
			}
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
			<#if item.type=='page' && item.name=='index' && (item.formType=='grid' || item.formType=='tree' || item.formType=='domainOnly')>
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
					name="${domainPackage(package, moduleName,func.packageName)}.${class}.java">
					<@model date=date package=package class=class module=moduleName table=table pk=pk notNull=notNull index=index unique=unique autoInc=autoInc columns=columns foreign=foreign func=func proModule=module project=project/>
				</@filewriter>
			</#if>
		</#list>
	</#list>
</#list>





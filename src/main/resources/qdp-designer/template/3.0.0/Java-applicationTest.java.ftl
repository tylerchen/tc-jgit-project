<#include "/_functions.ftl" />
<#macro applicationtest date package class module table pk notNull index unique autoInc columns foreign func proModule>
/*******************************************************************************
 * Copyright (c) ${date} @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package ${appPackage(package,module,func.packageName)};

import java.util.Date;
import org.iff.infra.domain.InstanceFactory;
import org.iff.infra.test.AbstractIntegratedTestCase;
import org.iff.infra.util.mybatis.plugin.Page;
import org.junit.Test;

import ${appPackage(package,module,func.packageName)}.${class}Application;
import ${voPackage(package,module,func.packageName)}.${class}VO;

/**
 * Test for ${class}.
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> 
 * @since ${date}
 * @version 1.0.0
 * auto generate by qdp v3.0.
 */
public class ${class}ApplicationTest extends AbstractIntegratedTestCase {

	protected String[] getDataSetFilePaths() {
		return new String[] { "dataset/${class}.xml" };
	}

	@Test
	public void test_get${class}() {
		${class}Application application = InstanceFactory.getInstance(${class}Application.class);
	}
	
	<#list pk as field><#if field_index==0><#local javaFieldName=javaField(field.field)/>
	@Test
	public void test_get${class}ById(){
		${class}Application application = InstanceFactory.getInstance(${class}Application.class);
	}
	
	</#if></#list>
	@Test
	public void test_pageFind${class}() {
		${class}Application application = InstanceFactory.getInstance(${class}Application.class);
		Page page = Page.pageable(10, 1, 0, null);
		${class}VO vo = new ${class}VO();
		System.out.println(application.pageFind${class}(vo, page));
	}
	
	@Test
	public void test_add${class}() {
		${class}Application application = InstanceFactory.getInstance(${class}Application.class);
		${class}VO vo = new ${class}VO();
		//application.add${class}(vo);
	}

	@Test
	public void test_update${class}() {
		${class}Application application = InstanceFactory.getInstance(${class}Application.class);
		${class}VO vo = new ${class}VO();
		//application.update${class}(vo);
	}

	@Test
	public void test_remove${class}() {
		${class}Application application = InstanceFactory.getInstance(${class}Application.class);
	}
	
	<#list pk as field><#if field_index==0><#local javaFieldName=javaField(field.field)/>
	@Test
	public void test_remove${class}ById(){
		${class}Application application = InstanceFactory.getInstance(${class}Application.class);
	}
	
	@Test
	public void test_remove${class}ByIds(){
		${class}Application application = InstanceFactory.getInstance(${class}Application.class);
	}
	
	</#if></#list>
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
				<@filewriter type="java" basedir=projectdir(root.projectName)+"/src/test/java/"
					name="${appPackage(package, moduleName,func.packageName)}.${class}ApplicationTest.java">
					<@applicationtest date=date package=package class=class module=moduleName table=table pk=pk notNull=notNull index=index unique=unique autoInc=autoInc columns=columns foreign=foreign func=func proModule=module/>
				</@filewriter>
			</#if>
		</#list>
	</#list>
</#list>


<#include "/_functions.ftl" />
<#macro searchForm field>
	<#local label = javaFieldEnName(javaField(field.description!field.field))?capitalize/>
	<#local jfield = javaField(field.field)/>
	<#local type = field.type.search/><#--text|textarea|password|radio|checkbox|switch|select|date|time|cascader|number|rate|file|modal|label-->
	<#if type == 'text'>
				<el-form-item label="${label}" prop="${jfield}" :show-message="false">
					<el-input v-model="searchForm.${jfield}" placeholder="" size="small"></el-input>
				</el-form-item>
	<#elseif type == 'textarea'>
				<el-form-item label="${label}" prop="${jfield}" :show-message="false">
					<el-input v-model="searchForm.${jfield}" placeholder="" size="small" type="textarea"></el-input>
				</el-form-item>
	<#elseif type == 'password'>
				<el-form-item label="${label}" prop="${jfield}" :show-message="false">
					<el-input v-model="searchForm.${jfield}" placeholder="" size="small" type="password"></el-input>
				</el-form-item>
	<#elseif type == 'radio'>
		<#local json=helper('GsonHelper').toJson(field.jsonData?has_content?string(field.jsonData, '{}'))/>
		<#if (json?is_hash_ex) && (json?has_content)><#-- 必须是MAP -->
				<el-form-item label="${label}" prop="${jfield}" :show-message="false">
					<el-radio-group v-model="searchForm.${jfield}">
				    	<#list json.keySet() as key>
				    	<el-radio label="${key}">${json.get(key)!key}</el-radio>
				    	</#list>
				    </el-radio-group>
				</el-form-item>
		</#if>
	<#elseif type == 'checkbox'>
		<#local json=helper('GsonHelper').toJson(field.jsonData?has_content?string(field.jsonData, '{}'))/>
		<#if (json?is_hash_ex) && (json?has_content)><#-- 必须是MAP -->
				<el-form-item label="${label}" prop="${jfield}" :show-message="false">
					<el-checkbox-group v-model="searchForm.${jfield}">
				    	<#list json.keySet() as key>
				    	<el-checkbox label="${key}">${json.get(key)!key}</el-checkbox>
				    	</#list>
				    </el-checkbox-group>
				</el-form-item>
		</#if>
	<#elseif type == 'switch'>
		<#local json=helper('GsonHelper').toJson(field.jsonData?has_content?string(field.jsonData, '{}'))/>
		<#if (json?is_hash_ex) && (json?has_content)><#-- 必须是MAP -->
				<el-form-item label="${label}" prop="${jfield}" :show-message="false">
					<el-switch v-model="searchForm.${jfield}"
				    	<#list json.keySet() as key>
				    	<#if key?index==0>
				    		on-value="${key}"
				    		on-text="${json.get(key)!key}"
				    	</#if>
				    	<#if key?index==1>
				    		off-value="${key}"
				    		off-text="${json.get(key)!key}"
				    	</#if>
				    	</#list>
					>
				    </el-switch>
				</el-form-item>
		</#if>
	<#elseif type == 'select'>
		<#local json=helper('GsonHelper').toJson(field.jsonData?has_content?string(field.jsonData, '{}'))/>
		<#if (json?is_hash_ex) && (json?has_content)><#-- 必须是MAP -->
				<el-form-item label="${label}" prop="${jfield}" :show-message="false">
					<el-select v-model="searchForm.${jfield}" clearable placeholder="" size="small">
						<#list json.keySet() as key>
				    	<el-option label="${json.get(key)!key}"  value="${key}"></el-option>
				    	</#list>
				   	</el-select>
				</el-form-item>
		</#if>
	<#elseif type == 'date'>
				<el-form-item label="${label}" prop="${jfield}" :show-message="false">
					<el-date-picker v-model="searchForm.${jfield}" type="date" placeholder="" size="small"></el-date-picker>
				</el-form-item>
	<#elseif type == 'datetime'>
				<el-form-item label="${label}" prop="${jfield}" :show-message="false">
					<el-date-picker v-model="searchForm.${jfield}" type="datetime" placeholder="" size="small"></el-date-picker>
				</el-form-item>
	<#elseif type == 'time'>
				<el-form-item label="${label}" prop="${jfield}" :show-message="false">
					<el-time-select v-model="searchForm.${jfield}" placeholder="" size="small"></el-time-select>
				</el-form-item>
	<#elseif type == 'cascader'>
		<#local json=helper('GsonHelper').toJson(field.jsonData?has_content?string(field.jsonData, '{}'))/>
		<#if (json?is_collection_ex) && (json?has_content)><#-- 必须是Array --><#local jsonStr=helper('GsonHelper').toJsonString(json)/>
				<el-form-item label="${label}" prop="${jfield}" :show-message="false">
					<el-cascader v-model="searchForm.${jfield}" :options='${jsonStr}' placeholder="" size="small"></el-cascader>
				</el-form-item>
		</#if>
	<#elseif type == 'number'>
				<el-form-item label="${label}" prop="${jfield}" :show-message="false">
					<el-input-number v-model="searchForm.${jfield}" placeholder="" size="small"></el-input-number>
				</el-form-item>
	<#elseif type == 'rate'>
				<el-form-item label="${label}" prop="${jfield}" :show-message="false">
					<el-rate v-model="searchForm.${jfield}" placeholder="" size="small"></el-rate>
				</el-form-item>
	<#elseif type == 'file'>
				<el-form-item label="${label}" prop="${jfield}" :show-message="false">
					<el-upload ref="upload" action="https://jsonplaceholder.typicode.com/posts/" :auto-upload="false">
		  				<el-button slot="trigger" size="small" type="primary">选取文件</el-button>
		  				<el-button style="margin-left: 10px;" size="small" type="success" @click="$refs.upload.submit()">上传到服务器</el-button>
		  				<div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
					</el-upload>
				</el-form-item>
	<#elseif type == 'label'>
				<el-form-item label="${label}" prop="${jfield}" :show-message="false">
					<label class="el-form-item__label">${label}</label>
				</el-form-item>
	<#elseif type == 'modal'>
		
	</#if>
</#macro>


<#macro genForm field formType>
	<#local label = javaFieldEnName(javaField(field.label!field.description!field.field))?capitalize/>
	<#local jfield = javaField(field.field)/>
	<#local type = field.type[formType]!field.type.all/><#--text|textarea|password|radio|checkbox|switch|select|date|time|cascader|number|rate|file|modal|label-->
	<#if type == 'text'>
					<el-form-item label="${label}" prop="${jfield}">
						<el-input v-model="${formType}Form.${jfield}" placeholder="" size="small"></el-input>
					</el-form-item>
	<#elseif type == 'textarea'>
					<el-form-item label="${label}" prop="${jfield}">
						<el-input v-model="${formType}Form.${jfield}" placeholder="" size="small" type="textarea"></el-input>
					</el-form-item>
	<#elseif type == 'password'>
					<el-form-item label="${label}" prop="${jfield}">
						<el-input v-model="${formType}Form.${jfield}" placeholder="" size="small" type="password"></el-input>
					</el-form-item>
	<#elseif type == 'radio'>
		<#local json=helper('GsonHelper').toJson(field.jsonData?has_content?string(field.jsonData, '{}'))/>
		<#if (json?is_hash_ex) && (json?has_content)><#-- 必须是MAP -->
					<el-form-item label="${label}" prop="${jfield}">
						<el-radio-group v-model="${formType}Form.${jfield}">
					    	<#list json.keySet() as key>
					    	<el-radio label="${key}">${json.get(key)!key}</el-radio>
					    	</#list>
					    </el-radio-group>
					</el-form-item>
		</#if>
	<#elseif type == 'checkbox'>
		<#local json=helper('GsonHelper').toJson(field.jsonData?has_content?string(field.jsonData, '{}'))/>
		<#if (json?is_hash_ex) && (json?has_content)><#-- 必须是MAP -->
					<el-form-item label="${label}" prop="${jfield}">
						<el-checkbox-group v-model="${formType}Form.${jfield}">
					    	<#list json.keySet() as key>
					    	<el-checkbox label="${key}">${json.get(key)!key}</el-checkbox>
					    	</#list>
					    </el-checkbox-group>
					</el-form-item>
		</#if>
	<#elseif type == 'switch'>
		<#local json=helper('GsonHelper').toJson(field.jsonData?has_content?string(field.jsonData, '{}'))/>
		<#if (json?is_hash_ex) && (json?has_content)><#-- 必须是MAP -->
					<el-form-item label="${label}" prop="${jfield}">
						<el-switch v-model="${formType}Form.${jfield}"
					    	<#list json.keySet() as key>
					    	<#if key?index==0>
					    		on-value="${key}"
					    		on-text="${json.get(key)!key}"
					    	</#if>
					    	<#if key?index==1>
					    		off-value="${key}"
					    		off-text="${json.get(key)!key}"
					    	</#if>
					    	</#list>
						>
					    </el-switch>
					</el-form-item>
		</#if>
	<#elseif type == 'select'>
		<#local json=helper('GsonHelper').toJson(field.jsonData?has_content?string(field.jsonData, '{}'))/>
		<#if (json?is_hash_ex) && (json?has_content)><#-- 必须是MAP -->
					<el-form-item label="${label}" prop="${jfield}">
						<el-select v-model="${formType}Form.${jfield}" clearable placeholder="" size="small">
							<#list json.keySet() as key>
					    	<el-option label="${json.get(key)!key}"  value="${key}"></el-option>
					    	</#list>
					   	</el-select>
					</el-form-item>
		</#if>
	<#elseif type == 'date'>
					<el-form-item label="${label}" prop="${jfield}">
						<el-date-picker v-model="${formType}Form.${jfield}" type="date" placeholder="" size="small"></el-date-picker>
					</el-form-item>
	<#elseif type == 'datetime'>
					<el-form-item label="${label}" prop="${jfield}">
						<el-date-picker v-model="${formType}Form.${jfield}" type="datetime" placeholder="" size="small"></el-date-picker>
					</el-form-item>
	<#elseif type == 'time'>
					<el-form-item label="${label}" prop="${jfield}">
						<el-time-select v-model="${formType}Form.${jfield}" placeholder="" size="small"></el-time-select>
					</el-form-item>
	<#elseif type == 'cascader'>
		<#local json=helper('GsonHelper').toJson(field.jsonData?has_content?string(field.jsonData, '{}'))/>
		<#if (json?is_collection_ex) && (json?has_content)><#-- 必须是Array --><#local jsonStr=helper('GsonHelper').toJsonString(json)/>
					<el-form-item label="${label}" prop="${jfield}">
						<el-cascader v-model="${formType}Form.${jfield}" :options='${jsonStr}' placeholder="" size="small"></el-cascader>
					</el-form-item>
		</#if>
	<#elseif type == 'number'>
					<el-form-item label="${label}" prop="${jfield}">
						<el-input-number v-model="${formType}Form.${jfield}" placeholder="" size="small"></el-input-number>
					</el-form-item>
	<#elseif type == 'rate'>
					<el-form-item label="${label}" prop="${jfield}">
						<el-rate v-model="${formType}Form.${jfield}" placeholder="" size="small"></el-rate>
					</el-form-item>
	<#elseif type == 'file'>
					<el-form-item label="${label}" prop="${jfield}">
						<el-upload ref="upload" :action="ctx+'/fileupload'" :auto-upload="false">
			  				<el-button slot="trigger" size="small" type="primary">选取文件</el-button>
			  				<el-button style="margin-left: 10px;" size="small" type="success" @click="$refs.upload.submit()">上传到服务器</el-button>
			  				<div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
						</el-upload>
					</el-form-item>
	<#elseif type == 'label'>
					<el-form-item label="${label}" prop="${jfield}">
						<#if ['radio','checkbox','switch','select']?seq_index_of(field.type.grid)>-1>
							<el-tag>{{getOption(${formType}Form, ${jfield})}}</el-tag>
						<#else>
							<label class="el-form-item__label">{{${formType}Form.${jfield}}}</label>
						</#if>
					</el-form-item>
	<#elseif type == 'icon'>
					<el-form-item label="${label}" prop="${jfield}">
                        <div class="qdp-input-icon">
							<el-button icon="el-icon-search" @click="openDialog('${formType}Form','${jfield}')" size="small" type="text"></el-button>
							<el-tag v-if="${formType}Form.${jfield}">{{${formType}Form.${jfield}}}</el-tag>
							<el-button size="small" type="text" v-if="${formType}Form.${jfield}"><i :class="${formType}Form.${jfield}"></i></el-button>
						</div>
					</el-form-item>
	<#elseif type == 'modal'>
		<#if field.refTable?has_content && field.refField?has_content && field.refLabelField?has_content>
					<el-form-item label="${label}" prop="${jfield}Name" ref="${formType}Form-${jfield}Name">
						<div class="qdp-input-model">
						<el-button icon="el-icon-search" @click="openDialog('${formType}Form','${jfield}')" size="small" type="text"></el-button>
							<div class="el-input el-input--small"><!--列表显示所有的选择-->
								<div class="qdp-modal-list-body" style="border:1px solid #d1dbe5;">
									<div class="qdp-modal-list" style="max-height: 100px; overflow: auto;">
										<table cellspacing="0" cellpadding="0" border="0" class="el-table__body" style="width: 100%;">
											<tr class="el-transfer-panel__item el-table__row" v-for="value in split(${formType}Form, '${jfield}Name')" v-if="value" :key="value">
												<td><el-button size="small" type="text" title="删除"   @click="removeSelect(${formType}Form, '${jfield}', value)"><i class="el-icon-ion-trash-b"></i></el-button></td>
												<td style="padding-left: 10px;"><span>{{value}}</span></td>
											</tr>
										</table>
									</div>
								</div>
							</div>
                        </div>
					</el-form-item>
					<el-form-item label="${label}" prop="${jfield}" v-show="false" ref="${formType}Form-${jfield}">
						<el-input v-model="${formType}Form.${jfield}" placeholder="" size="small"></el-input>
					</el-form-item>
		</#if>
	</#if>
</#macro>

<#function genFormRule formTable formType>
	<#local rules={} />
	<#list formTable.fields as field>
		<#if field[formType]==true>
			<#local frule = field.rule/>
			<#local label = javaFieldEnName(javaField(field.description!field.field))?capitalize/>
			<#local jfield = javaField(field.field)/>
			<#local rule=[] /><#--{type, required, requiredMsg, regex, regexMsg, rangeMin, rangeMax, rangeMsg, length, lengthMsg, enums, enumsMsg, noBlank, noBlankMsg}-->
			<#if frule.type?has_content>
				<#local rule=rule+[{'type': frule.type, 'message': "${label}必须为${frule.type?string}类型"}] />
			</#if>
			<#if frule.required>
				<#local rule=rule+[{'required': true, 'message': frule.requiredMsg?has_content?then(frule.requiredMsg, "${label}不能为空")}] />
			</#if>
			<#if frule.rangeMin!=frule.rangeMax >
				<#local rule=rule+[{'min': frule.rangeMin, 'max': frule.rangeMax, 'message': frule.rangeMsg?has_content?then(frule.rangeMsg, "${label}范围${frule.rangeMin?string}至${frule.rangeMax?string}")}] />
			</#if>
			<#if frule.length gt 0>
				<#local rule=rule+[{'len': frule.length, 'message': frule.lengthMsg?has_content?then(frule.lengthMsg, "${label}长度为${frule.length?string}")}] />
			</#if>
			<#if frule.enums?has_content>
				<#attempt>
					<#local enums = helper('GsonHelper').toJsonList(frule.enums) />
					<#local rule=rule+[{'enum': enums, 'message': frule.enumsMsg?has_content?then(frule.enumsMsg, "${label}为数组")}] />
				<#recover>
				</#attempt>
			</#if>
			<#if frule.noBlank>
				<#local rule=rule+[{'whitespace': true, 'message': frule.noBlankMsg?has_content?then(frule.noBlankMsg, "${label}不能为空")}] />
			</#if>
			<#if rule?has_content>
				<#local rules=rules+{"${jfield}":rule}/>
			</#if>
		</#if>
	</#list>
	<#return helper('GsonHelper').toJsonString(rules)/>
</#function>

<#function formFields formTable formType>
	<#local fields=[] />
	<#list formTable.fields as field>
		<#if field[formType]==true>
			<#local jfield = javaField(field.field)/>
			<#local fields=fields+["${jfield} : null"]/>
			<#if field.type.all=='modal' && formType!='search'>
				<#local fields=fields+["${jfield}Name : ''", "${jfield}Selections : []"]/>
			</#if>
		</#if>
	</#list>
	<#return fields?join(', ')/>
</#function>

<#function exFormFields formTable formType>
	<#local fields=[] />
	<#list formTable.fields as field>
		<#if field[formType]==true>
			<#local jfield = javaField(field.field)/>
			<#if field.type.all=='modal'>
				<#local fields=fields+["${jfield} : ''","${jfield}Name : ''", "${jfield}Selections : []"]/>
			</#if>
		</#if>
	</#list>
	<#return fields?join(', ')/>
</#function>

<#function getTable tableName modules>
	<#list modules![] as module>
		<#list module.children![] as func>
			<#list func.children![] as item>
				<#if item.type=='page' && item.name=='index' && item.tableName?upper_case==tableName?upper_case>
					<#return {'module':module, 'func': func, 'table': item}/>
				</#if>
			</#list>
		</#list>
	</#list>
	<#return {'module':{}, 'func': {}, 'table': {}}/>
</#function>

<#function getField fieldName table>
	<#list table.fields as field>
		<#if field.field?upper_case==fieldName?upper_case>
			<#return field/>
		</#if>
	</#list>
	<#return {}/>
</#function>

<#function genEnums func modules>
	<#local enumData={} fieldType=['radio','checkbox','switch','select']/>
	<#list func.children as item>
		<#if item.type=='page'>
			<#local table=item itemEnum={}/><#if item.fileName!='index' && item.tableName?has_content><#local table=getTable(item.tableName, project).table/></#if>
			<#list table.fields as field>
				<#if (fieldType?seq_index_of(field.type.add) gt -1)||(fieldType?seq_index_of(field.type.edit) gt -1)>
					<#if field.jsonData?has_content>
						<#attempt>
							<#local json = helper('GsonHelper').toJsonMap(field.jsonData) />
							<#local itemEnum = itemEnum+{"${javaField(field.field)}":json} />
						<#recover>
						</#attempt>
					</#if><#--if jsonData END-->
				</#if><#--if fieldType END-->
			</#list>
			<#if itemEnum?has_content>
				<#local enumData=enumData+{"${item.fileName}":itemEnum}/>
			</#if>
		</#if>
	</#list><#--list func END-->
	<#return helper('GsonHelper').toJsonString(enumData)/>
</#function>

<#function getAction table eventName>
	<#list table.actions?keys as key><#local action=table.actions[key]/>
		<#if eventName?upper_case==key?upper_case>
			<#return action/>
		</#if>
	</#list>
	<#return {}/>
</#function>

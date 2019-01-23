/*******************************************************************************
 * Copyright (c) Oct 14, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.jgit.core.service;

import org.iff.netty.server.ProcessContext;

import java.nio.charset.Charset;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Oct 14, 2016
 */
public class HelpRestHandler extends BaseDefaultRestHandler {

    public static final String table_get_id = "{ctx}/default/table_get_id/{datasource}/{table}";
    public static final String table_get_name = "{ctx}/default/table_get_name/{datasource}/{table}";
    public static final String table_find_id = "{ctx}/default/table_find_id/{datasource}";
    public static final String table_find_name = "{ctx}/default/table_find_name/{datasource}";
    public static final String querystatement_get_id = "{ctx}/default/querystatement_get_id/{id}";
    public static final String querystatement_get_name = "{ctx}/default/querystatement_get_name/{id}";
    public static final String querystatement_find = "{ctx}/default/querystatement_find/id=?/name=?/createTime=yyyy-MM-dd HH:mm:ss/updateTime=yyyy-MM-dd HH:mm:ss";
    public static final String querystatement_page = "{ctx}/default/querystatement_page/id=?/name=?/createTime=yyyy-MM-dd HH:mm:ss/updateTime=yyyy-MM-dd HH:mm:ss/currentPage=1/pageSize=10";
    public static final String querystatement_update = ""/**/
            + "{ctx}/default/querystatement_update\n"/**/
            + "POST JSON: {'queryStatement':{'id':'', 'name':'', 'selectBody':'', 'fromBody':'', 'whereBody':'', 'description':'', 'updateTime':'', 'createTime':''}}";
    public static final String querystatement_del_id = "{ctx}/default/querystatement_del_id/{id}";
    public static final String querystatement_del_name = "{ctx}/default/querystatement_del_id/{name}";
    public static final String datasource_get_id = "{ctx}/default/datasource_get_id/{id}";
    public static final String datasource_get_name = "{ctx}/default/datasource_get_name/{name}";
    public static final String datasource_find = "{ctx}/default/datasource_find/id=?/name=?/createTime=yyyy-MM-dd HH:mm:ss/updateTime=yyyy-MM-dd HH:mm:ss";
    public static final String datasource_page = "{ctx}/default/datasource_page/id=?/name=?/createTime=yyyy-MM-dd HH:mm:ss/updateTime=yyyy-MM-dd HH:mm:ss/currentPage=1/pageSize=10";
    public static final String datasource_update = ""/**/
            + "{ctx}/default/datasource_update\n"/**/
            + "POST JSON: {'datasource':{'id':'', 'name':'', 'user':'', 'password':'', 'url':'','driver':'','validationQuery':'', 'initConnection':3, 'maxConnection':10, 'description':'', 'updateTime':'', 'createTime':''}}";
    public static final String datasource_del_id = "{ctx}/default/datasource_del_id/{id}";
    public static final String datasource_del_name = "{ctx}/default/datasource_del_name/{name}";
    public static final String datasource_test_id = "{ctx}/default/datasource_test_id/{id}";
    public static final String datasource_test_name = "{ctx}/default/datasource_test_id/{datasource_test_name}";
    public static final String query_get = ""/**/
            + "{ctx}/default/query_get/{datasource}/{query}\n"/**/
            + "POST JSON:{'condtions':'condtionJsonString'}";
    public static final String query_find = ""/**/
            + "{ctx}/default/query_find/{datasource}/{query}\n"/**/
            + "POST JSON:{'condtions':'condtionJsonString'}";
    public static final String query_page = ""/**/
            + "{ctx}/default/query_page/{datasource}/{query}\n"/**/
            + "POST JSON:{'condtions':'condtionJsonString'}";
    public static final String query_count = ""/**/
            + "{ctx}/default/query_count/{datasource}/{query}\n"/**/
            + "POST JSON:{'condtions':'condtionJsonString'}";

    public boolean execute(ProcessContext ctx) {
        String[] helps = new String[]{table_get_id, /**/
                table_get_name, /**/
                table_find_id, /**/
                table_find_name, /**/
                querystatement_get_id, /**/
                querystatement_get_name, /**/
                querystatement_find, /**/
                querystatement_page, /**/
                querystatement_update, /**/
                querystatement_del_id, /**/
                querystatement_del_name, /**/
                datasource_get_id, /**/
                datasource_get_name, /**/
                datasource_find, /**/
                datasource_page, /**/
                datasource_update, /**/
                datasource_del_id, /**/
                datasource_del_name, /**/
                datasource_test_id, /**/
                datasource_test_name, /**/
                query_get, /**/
                query_find, /**/
                query_page, /**/
                query_count/**/
        };
        Charset utf8 = Charset.forName("UTF-8");
        for (String help : helps) {
            ctx.getOutputBuffer().writeCharSequence(help, utf8);
            ctx.getOutputBuffer().writeCharSequence("\n", utf8);
        }
        ctx.outputText();
        return true;
    }
}

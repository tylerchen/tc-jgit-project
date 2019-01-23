/*******************************************************************************
 * Copyright (c) Sep 30, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.jgit.core.service;

import org.iff.jgit.core.DefaultRestHandler;
import org.iff.jgit.core.MyBatisSqlSessionFactory;
import org.iff.infra.util.Assert;
import org.iff.infra.util.GsonHelper;
import org.iff.infra.util.NumberHelper;
import org.iff.netty.server.ProcessContext;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 1. find
 * 2. pageFind
 * 3. get
 * 4. count
 * 5. update
 * 6. insert
 * 7. delete
 * </pre>
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 30, 2016
 */
public class QueryControllerFactory {

    public static final QueryControllerFactory me = new QueryControllerFactory();

    QueryService queryService = new QueryService();

    public static QueryControllerFactory me() {
        return me;
    }

    public static QueryControllerFactory addAllService() {
        DefaultRestHandler.addService("/query_get", me().new QueryGet());
        DefaultRestHandler.addService("/query_find", me().new QueryFind());
        DefaultRestHandler.addService("/query_page", me().new QueryPage());
        DefaultRestHandler.addService("/query_count", me().new QueryCount());
        DefaultRestHandler.addService("/test_mybatis", me().new TestMybatis());
        return me();
    }

    public class TestMybatis extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            MyBatisSqlSessionFactory.test();
            return false;
        }
    }

    public class QueryGet extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:query_get,2:datasource,3:query
            String datasource = "";
            String queryId = "";
            List<String> list = ctx.getPostData().getFirst().get("conditions");
            String conditionJson = list != null && list.size() > 0 ? list.get(0) : null;
            if (uriSplit.length > 2) {
                datasource = uriSplit[2];
            }
            if (uriSplit.length > 3) {
                queryId = uriSplit[3];
            }
            Assert.notBlank(datasource,
                    "datasource name is required! uri: {ctx}/default/query_get/{datasource}/{query}");
            Assert.notBlank(queryId,
                    "query statement id is required! uri: {ctx}/default/query_get/{datasource}/{query}");
            Assert.notBlank(conditionJson, "conditions is required! uri: {ctx}/default/query_get/{datasource}/{query}");
            Map<String, Object> result = queryService.get(datasource, queryId, GsonHelper.toJsonMap(conditionJson));
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(result), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }

    public class QueryFind extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:query_find,2:datasource,3:query
            String datasource = "";
            String queryId = "";
            List<String> list = ctx.getPostData().getFirst().get("conditions");
            String conditionJson = list != null && list.size() > 0 ? list.get(0) : null;
            if (uriSplit.length > 2) {
                datasource = uriSplit[2];
            }
            if (uriSplit.length > 3) {
                queryId = uriSplit[3];
            }
            Assert.notBlank(datasource,
                    "datasource name is required! uri: {ctx}/default/query_find/{datasource}/{query}");
            Assert.notBlank(queryId,
                    "query statement id is required! uri: {ctx}/default/query_find/{datasource}/{query}");
            Assert.notBlank(conditionJson,
                    "conditions is required! uri: {ctx}/default/query_find/{datasource}/{query}");
            List<Map<String, Object>> result = queryService.find(datasource, queryId,
                    GsonHelper.toJsonMap(conditionJson));
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(result), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }

    public class QueryPage extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:query_page,2:datasource,3:query
            String datasource = "";
            String queryId = "";
            List<String> list = ctx.getPostData().getFirst().get("conditions");
            String conditionJson = list != null && list.size() > 0 ? list.get(0) : null;
            if (uriSplit.length > 2) {
                datasource = uriSplit[2];
            }
            if (uriSplit.length > 3) {
                queryId = uriSplit[3];
            }
            Assert.notBlank(datasource,
                    "datasource name is required! uri: {ctx}/default/query_page/{datasource}/{query}");
            Assert.notBlank(queryId,
                    "query statement id is required! uri: {ctx}/default/query_page/{datasource}/{query}");
            Assert.notBlank(conditionJson,
                    "conditions is required! uri: {ctx}/default/query_page/{datasource}/{query}");
            Map<String, Object> map = GsonHelper.toJsonMap(conditionJson);
            Map<String, Object> result = queryService.page(datasource, queryId, map,
                    NumberHelper.getInt(map.get("currentPage"), 0), NumberHelper.getInt(map.get("pageSize"), 10));
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(result), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }

    public class QueryCount extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:query_count,2:datasource,3:query
            String datasource = "";
            String queryId = "";
            List<String> list = ctx.getPostData().getFirst().get("conditions");
            String conditionJson = list != null && list.size() > 0 ? list.get(0) : null;
            if (uriSplit.length > 2) {
                datasource = uriSplit[2];
            }
            if (uriSplit.length > 3) {
                queryId = uriSplit[3];
            }
            Assert.notBlank(datasource,
                    "datasource name is required! uri: {ctx}/default/query_count/{datasource}/{query}");
            Assert.notBlank(queryId,
                    "query statement id is required! uri: {ctx}/default/query_count/{datasource}/{query}");
            Assert.notBlank(conditionJson,
                    "conditions is required! uri: {ctx}/default/query_count/{datasource}/{query}");
            long result = queryService.count(datasource, queryId, GsonHelper.toJsonMap(conditionJson));
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(result), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }
}

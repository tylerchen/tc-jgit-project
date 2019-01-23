/*******************************************************************************
 * Copyright (c) Sep 30, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.jgit.core.service;

import org.iff.jgit.core.DefaultRestHandler;
import org.iff.jgit.core.model.DataSourceModel;
import org.iff.jgit.core.model.DescTableModel;
import org.iff.jgit.core.model.QueryStatementModel;
import org.iff.infra.util.*;
import org.iff.netty.server.ProcessContext;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 1. get table description.
 * 2. getQuery
 * 3. findQuery
 * 4. pageFindQuery
 * 5. updateQuery
 * 6. deleteQuery
 * 7. getDataSource
 * 8. findDataSource
 * 9. pageFindDataSource
 * 10. updateDataSource
 * 11. deleteDataSource
 * 12. testDataSource
 * </pre>
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Sep 30, 2016
 */
public class ManagementControllerFactory {

    public static final ManagementControllerFactory me = new ManagementControllerFactory();

    ManagementService managementService = new ManagementService();

    public static ManagementControllerFactory me() {
        return me;
    }

    public static ManagementControllerFactory addAllService() {
        DefaultRestHandler.addService("/help", new HelpRestHandler());
        DefaultRestHandler.addService("/table_get_id", me().new GetTableDescByDataSourceId());
        DefaultRestHandler.addService("/table_get_name", me().new GetTableDescByDataSourceName());
        DefaultRestHandler.addService("/table_find_id", me().new FindAllTableDescByDataSourceId());
        DefaultRestHandler.addService("/table_find_name", me().new FindAllTableDescByDataSourceName());
        DefaultRestHandler.addService("/querystatement_get_id", me().new GetQueryStatementModelById());
        DefaultRestHandler.addService("/querystatement_get_name", me().new GetQueryStatementModelByName());
        DefaultRestHandler.addService("/querystatement_find", me().new FindQueryStatementModel());
        DefaultRestHandler.addService("/querystatement_page", me().new PageFindQueryStatementModel());
        DefaultRestHandler.addService("/querystatement_update", me().new AddOrUpdateQueryStatementModel());
        DefaultRestHandler.addService("/querystatement_del_id", me().new DeleteQueryStatementModelById());
        DefaultRestHandler.addService("/querystatement_del_name", me().new DeleteQueryStatementModelByName());
        DefaultRestHandler.addService("/datasource_get_id", me().new GetDataSourceModelById());
        DefaultRestHandler.addService("/datasource_get_name", me().new GetDataSourceModelByName());
        DefaultRestHandler.addService("/datasource_find", me().new FindDataSourceModel());
        DefaultRestHandler.addService("/datasource_page", me().new PageFindDataSourceModel());
        DefaultRestHandler.addService("/datasource_update", me().new AddOrUpdateDataSourceModel());
        DefaultRestHandler.addService("/datasource_del_id", me().new DeleteDataSourceModelById());
        DefaultRestHandler.addService("/datasource_del_name", me().new DeleteDataSourceModelByName());
        DefaultRestHandler.addService("/datasource_test_id", me().new TestDataSourceModelById());
        DefaultRestHandler.addService("/datasource_test_name", me().new TestDataSourceModelByName());
        return me();
    }

    public class GetTableDescByDataSourceId extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:table_get,2:datasource,3:table
            String datasource = "";
            String table = "";
            if (uriSplit.length > 2) {
                datasource = uriSplit[2];
            }
            if (uriSplit.length > 3) {
                table = uriSplit[3];
            }
            Assert.notBlank(datasource,
                    "datasource name is required! uri: {ctx}/default/table_get_id/{datasource}/{table}");
            Assert.notBlank(table, "datasource name is required! uri: {ctx}/default/table_get_id/{datasource}/{table}");
            DescTableModel tableDesc = managementService.getTableDescByDataSourceId(datasource, table);
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(tableDesc), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }

    public class GetTableDescByDataSourceName extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:table_get,2:datasource,3:table
            String datasource = "";
            String table = "";
            if (uriSplit.length > 2) {
                datasource = uriSplit[2];
            }
            if (uriSplit.length > 3) {
                table = uriSplit[3];
            }
            Assert.notBlank(datasource,
                    "datasource name is required! uri: {ctx}/default/table_get_name/{datasource}/{table}");
            Assert.notBlank(table,
                    "datasource name is required! uri: {ctx}/default/table_get_name/{datasource}/{table}");
            DescTableModel tableDesc = managementService.getTableDescByDataSourceName(datasource, table);
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(tableDesc), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }

    public class FindAllTableDescByDataSourceId extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:table_find,2:datasource
            String datasource = "";
            if (uriSplit.length > 2) {
                datasource = uriSplit[2];
            }
            Assert.notBlank(datasource, "datasource name is required! uri: {ctx}/default/table_find_id/{datasource}");
            Map<String, DescTableModel> map = managementService.findAllTableDescByDataSourceId(datasource);
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(map), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }

    public class FindAllTableDescByDataSourceName extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:table_find,2:datasource
            String datasource = "";
            if (uriSplit.length > 2) {
                datasource = uriSplit[2];
            }
            Assert.notBlank(datasource, "datasource name is required! uri: {ctx}/default/table_find_name/{datasource}");
            Map<String, DescTableModel> map = managementService.findAllTableDescByDataSourceName(datasource);
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(map), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }

    public class GetQueryStatementModelById extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:querystatement_get_id
            String id = "";
            if (uriSplit.length > 2) {
                id = uriSplit[2];
            }
            Assert.notBlank(id, "QueryStatementModel id is required! uri: {ctx}/default/querystatement_get_id/{id}");
            QueryStatementModel result = managementService.getQueryStatementModelById(id);
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(result), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }

    public class GetQueryStatementModelByName extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:querystatement_get_name
            String name = "";
            if (uriSplit.length > 2) {
                name = uriSplit[2];
            }
            Assert.notBlank(name,
                    "QueryStatementModel name is required! uri: {ctx}/default/querystatement_get_name/{name}");
            QueryStatementModel result = managementService.getQueryStatementModelByName(name);
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(result), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }

    public class FindQueryStatementModel extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:querystatement_find
            Map<String, String> pathParams = pathParams(uriSplit, 2, uriSplit.length);
            Assert.notEmpty(pathParams,
                    "QueryStatementModel is required! uri: {ctx}/default/querystatement_find/{prop=value}");
            QueryStatementModel model = BeanHelper.copyProperties(QueryStatementModel.class, pathParams);
            List<QueryStatementModel> list = managementService.findQueryStatementModel(model);
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(list), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }

    public class PageFindQueryStatementModel extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:querystatement_page
            Map<String, String> pathParams = pathParams(uriSplit, 2, uriSplit.length);
            Assert.notEmpty(pathParams,
                    "QueryStatementModel is required! uri: {ctx}/default/querystatement_page/{prop=value}");
            QueryStatementModel model = BeanHelper.copyProperties(QueryStatementModel.class, pathParams);
            Map<String, Object> page = managementService.pageFindQueryStatementModel(model,
                    NumberHelper.getInt(pathParams.get("currentPage"), 0),
                    NumberHelper.getInt(pathParams.get("pageSize"), 10));
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(page), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }

    public class AddOrUpdateQueryStatementModel extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            //String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:querystatement_update
            List<String> list = ctx.getPostData().getFirst().get("queryStatement");
            Assert.notEmpty(list,
                    "QueryStatementModel is required! uri: {ctx}/default/querystatement_update/{prop=value}");
            QueryStatementModel model = JsonHelper.toObject(QueryStatementModel.class, list.get(0));
            managementService.addOrUpdateQueryStatementModel(model);
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(model), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }

    public class DeleteQueryStatementModelById extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:querystatement_del_id
            String id = "";
            if (uriSplit.length > 2) {
                id = uriSplit[2];
            }
            Assert.notBlank(id, "QueryStatementModel id is required! uri: {ctx}/default/querystatement_del_id/{id}");
            long count = managementService.deleteQueryStatementModelById(id);
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(count), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }

    public class DeleteQueryStatementModelByName extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:querystatement_del_name
            String name = "";
            if (uriSplit.length > 2) {
                name = uriSplit[2];
            }
            Assert.notBlank(name,
                    "QueryStatementModel name is required! uri: {ctx}/default/querystatement_del_name/{name}");
            long count = managementService.deleteQueryStatementModelByName(name);
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(count), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }

    public class GetDataSourceModelById extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:datasource_get_id
            String id = "";
            if (uriSplit.length > 2) {
                id = uriSplit[2];
            }
            Assert.notBlank(id, "DataSourceModel id is required! uri: {ctx}/default/datasource_get_id/{id}");
            DataSourceModel model = managementService.getDataSourceModelById(id);
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(model), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }

    public class GetDataSourceModelByName extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:datasource_get_id
            String name = "";
            if (uriSplit.length > 2) {
                name = uriSplit[2];
            }
            Assert.notBlank(name, "DataSourceModel name is required! uri: {ctx}/default/datasource_get_name/{name}");
            DataSourceModel model = managementService.getDataSourceModelByName(name);
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(model), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }

    public class FindDataSourceModel extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:datasource_find
            Map<String, String> pathParams = pathParams(uriSplit, 2, uriSplit.length);
            Assert.notEmpty(pathParams, "DataSourceModel is required! uri: {ctx}/default/datasource_find/{prop=value}");
            DataSourceModel model = BeanHelper.copyProperties(DataSourceModel.class, pathParams);
            List<DataSourceModel> list = managementService.findDataSourceModel(model);
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(list), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }

    public class PageFindDataSourceModel extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:datasource_page
            Map<String, String> pathParams = pathParams(uriSplit, 2, uriSplit.length);
            Assert.notEmpty(pathParams, "DataSourceModel is required! uri: {ctx}/default/datasource_page/{prop=value}");
            DataSourceModel model = BeanHelper.copyProperties(DataSourceModel.class, pathParams);
            Map<String, Object> page = managementService.pageFindDataSourceModel(model,
                    NumberHelper.getInt(pathParams.get("currentPage"), 0),
                    NumberHelper.getInt(pathParams.get("pageSize"), 10));
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(page), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }

    public class AddOrUpdateDataSourceModel extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            //String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:datasource_update
            System.out.println(ctx.getPostData().getFirst());
            List<String> list = ctx.getPostData().getFirst().get("datasource");
            Assert.notEmpty(list, "DataSourceModel is required! uri: {ctx}/default/datasource_update/{prop=value}");
            DataSourceModel model = JsonHelper.toObject(DataSourceModel.class, list.get(0));
            managementService.addOrUpdateDataSourceModel(model);
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(model), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }

    public class DeleteDataSourceModelById extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:datasource_del_id
            String id = "";
            if (uriSplit.length > 2) {
                id = uriSplit[2];
            }
            Assert.notBlank(id, "DataSourceModel id is required! uri: {ctx}/default/datasource_del_id/{id}");
            long count = managementService.deleteDataSourceModelById(id);
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(count), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }

    public class DeleteDataSourceModelByName extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:datasource_del_name
            String name = "";
            if (uriSplit.length > 2) {
                name = uriSplit[2];
            }
            Assert.notBlank(name, "DataSourceModel name is required! uri: {ctx}/default/datasource_del_name/{name}");
            long count = managementService.deleteDataSourceModelByName(name);
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(count), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }

    public class TestDataSourceModelById extends BaseDefaultRestHandler {
        public boolean execute(ProcessContext ctx) {
            String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:datasource_test_id
            String id = "";
            if (uriSplit.length > 2) {
                id = uriSplit[2];
            }
            Assert.notBlank(id, "DataSourceModel id is required! uri: {ctx}/default/datasource_test_id/{id}");
            boolean test = managementService.testDataSourceModelById(id);
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(test), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }

    public class TestDataSourceModelByName extends BaseDefaultRestHandler {

        public boolean execute(ProcessContext ctx) {
            String[] uriSplit = (String[]) ctx.getAttributes().get(DefaultRestHandler.URI_SPLIT);
            //uriSplit:0:default,1:datasource_test_name
            String name = "";
            if (uriSplit.length > 2) {
                name = uriSplit[2];
            }
            Assert.notBlank(name, "DataSourceModel id is required! uri: {ctx}/default/datasource_test_name/{name}");
            boolean test = managementService.testDataSourceModelByName(name);
            ctx.getOutputBuffer().writeCharSequence(GsonHelper.toJsonString(test), Charset.forName("UTF-8"));
            ctx.outputJson();
            return false;
        }
    }
}

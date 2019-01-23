/*******************************************************************************
 * Copyright (c) Nov 22, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package tempjava;

import org.iff.infra.util.*;
import org.iff.infra.util.HttpBuilderHelper.Get;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Nov 22, 2016
 */
public class DataRestClient {
    private String url;

    public static void main(String[] args) {
        Map<?, ?> map = DataRestClient.create("http://localhost:8989/").getTableByName("test", "auth_account");
        System.out.println(map);
    }

    public static DataRestClient create(String url) {
        Assert.notBlank(url, "data rest url is required!");
        DataRestClient client = new DataRestClient();
        client.url = url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
        return client;
    }

    public Map<?, ?> getTableById(String databaseName, String tableId) {
        final String[] jsonArr = new String[1];
        String pathConcat = url + StringHelper.pathConcat("/default/table_get_id", databaseName, tableId);
        HttpBuilderHelper.get().s02_toUrl(pathConcat).s03_connect().s04_doOutput().s05_doInput().s06_redirect()
                .s07_useCaches().s08_connectTimeout().s09_readTimeout().s11_accept().s12_charset().s13_userAgent()
                .s14_contentType().s15_request()
                .s16_json(new HttpBuilderHelper.Process<HttpBuilderHelper.Get, String>() {
                    public Object run(Get get, String json) {
                        jsonArr[0] = json;
                        return null;
                    }
                });
        return GsonHelper.toJsonMap(jsonArr[0]);
    }

    public Map<?, ?> getTableByName(String databaseName, String tableName) {
        final String[] jsonArr = new String[1];
        String pathConcat = url + StringHelper.pathConcat("/default/table_get_name", databaseName, tableName);
        HttpBuilderHelper.get().s02_toUrl(pathConcat).s03_connect().s04_doOutput().s05_doInput().s06_redirect()
                .s07_useCaches().s08_connectTimeout().s09_readTimeout().s11_accept().s12_charset().s13_userAgent()
                .s14_contentType().s15_request()
                .s16_json(new HttpBuilderHelper.Process<HttpBuilderHelper.Get, String>() {
                    public Object run(Get get, String json) {
                        jsonArr[0] = json;
                        return null;
                    }
                });
        return GsonHelper.toJsonMap(jsonArr[0]);
    }

    public Map<?, ?> findTableById(String databaseId) {
        final String[] jsonArr = new String[1];
        String pathConcat = url + StringHelper.pathConcat("/default/table_find_id", databaseId);
        HttpBuilderHelper.get().s02_toUrl(pathConcat).s03_connect().s04_doOutput().s05_doInput().s06_redirect()
                .s07_useCaches().s08_connectTimeout().s09_readTimeout().s11_accept().s12_charset().s13_userAgent()
                .s14_contentType().s15_request()
                .s16_json(new HttpBuilderHelper.Process<HttpBuilderHelper.Get, String>() {
                    public Object run(Get get, String json) {
                        jsonArr[0] = json;
                        return null;
                    }
                });
        return GsonHelper.toJsonMap(jsonArr[0]);
    }

    public Map<?, ?> findTableByName(String databaseName) {
        final String[] jsonArr = new String[1];
        String pathConcat = url + StringHelper.pathConcat("/default/table_find_name", databaseName);
        HttpBuilderHelper.get().s02_toUrl(pathConcat).s03_connect().s04_doOutput().s05_doInput().s06_redirect()
                .s07_useCaches().s08_connectTimeout().s09_readTimeout().s11_accept().s12_charset().s13_userAgent()
                .s14_contentType().s15_request()
                .s16_json(new HttpBuilderHelper.Process<HttpBuilderHelper.Get, String>() {
                    public Object run(Get get, String json) {
                        jsonArr[0] = json;
                        return null;
                    }
                });
        return GsonHelper.toJsonMap(jsonArr[0]);
    }

    public Map<?, ?> getQueryStatementById(String queryStatementId) {
        final String[] jsonArr = new String[1];
        String pathConcat = url + StringHelper.pathConcat("/default/querystatement_get_id", queryStatementId);
        HttpBuilderHelper.get().s02_toUrl(pathConcat).s03_connect().s04_doOutput().s05_doInput().s06_redirect()
                .s07_useCaches().s08_connectTimeout().s09_readTimeout().s11_accept().s12_charset().s13_userAgent()
                .s14_contentType().s15_request()
                .s16_json(new HttpBuilderHelper.Process<HttpBuilderHelper.Get, String>() {
                    public Object run(Get get, String json) {
                        jsonArr[0] = json;
                        return null;
                    }
                });
        return GsonHelper.toJsonMap(jsonArr[0]);
    }

    public Map<?, ?> getQueryStatementByName(String queryStatementName) {
        final String[] jsonArr = new String[1];
        String pathConcat = url + StringHelper.pathConcat("/default/querystatement_get_name", queryStatementName);
        HttpBuilderHelper.get().s02_toUrl(pathConcat).s03_connect().s04_doOutput().s05_doInput().s06_redirect()
                .s07_useCaches().s08_connectTimeout().s09_readTimeout().s11_accept().s12_charset().s13_userAgent()
                .s14_contentType().s15_request()
                .s16_json(new HttpBuilderHelper.Process<HttpBuilderHelper.Get, String>() {
                    public Object run(Get get, String json) {
                        jsonArr[0] = json;
                        return null;
                    }
                });
        return GsonHelper.toJsonMap(jsonArr[0]);
    }

    public List<Map<?, ?>> findQueryStatement(String id, String name, Date createTime, Date updateTime) {
        final String[] jsonArr = new String[1];
        String paramStr = pathParam("id", id, "name", name, "createTime", createTime, "updateTime", updateTime);
        String pathConcat = url + StringHelper.pathConcat("/default/querystatement_find", paramStr);
        HttpBuilderHelper.get().s02_toUrl(pathConcat).s03_connect().s04_doOutput().s05_doInput().s06_redirect()
                .s07_useCaches().s08_connectTimeout().s09_readTimeout().s11_accept().s12_charset().s13_userAgent()
                .s14_contentType().s15_request()
                .s16_json(new HttpBuilderHelper.Process<HttpBuilderHelper.Get, String>() {
                    public Object run(Get get, String json) {
                        jsonArr[0] = json;
                        return null;
                    }
                });
        return GsonHelper.toJsonList(jsonArr[0]);
    }

    public Map<?, ?> pageQueryStatement(int currentPage, int pageSize, String id, String name, Date createTime,
                                        Date updateTime) {
        final String[] jsonArr = new String[1];
        String paramStr = pathParam("currentPage", currentPage, "pageSize", pageSize, "id", id, "name", name,
                "createTime", createTime, "updateTime", updateTime);
        String pathConcat = url + StringHelper.pathConcat("/default/querystatement_page", paramStr);
        HttpBuilderHelper.get().s02_toUrl(pathConcat).s03_connect().s04_doOutput().s05_doInput().s06_redirect()
                .s07_useCaches().s08_connectTimeout().s09_readTimeout().s11_accept().s12_charset().s13_userAgent()
                .s14_contentType().s15_request()
                .s16_json(new HttpBuilderHelper.Process<HttpBuilderHelper.Get, String>() {
                    public Object run(Get get, String json) {
                        jsonArr[0] = json;
                        return null;
                    }
                });
        return GsonHelper.toJsonMap(jsonArr[0]);
    }

    public Map<?, ?> addOrUpdateQueryStatement(String id, String name, String selectBody, String fromBody,
                                               String whereBody, String description) {
        final String[] jsonArr = new String[1];

        String pathConcat = url + "/default/querystatement_update";
        String queryStatement = GsonHelper.toJsonString(/**/
                MapHelper.toMap("id", id, "name", name, "selectBody", selectBody, "fromBody", fromBody, "whereBody",
                        whereBody, "description", description));
        HttpBuilderHelper.post().s02_toUrl(pathConcat).s03_connect().s04_doOutput().s05_doInput().s06_redirect()
                .s07_useCaches().s08_connectTimeout().s09_readTimeout().s11_accept().s12_charset().s13_userAgent()
                .s14_contentType().s15_data("queryStatement", queryStatement).s16_request()
                .s17_json(new HttpBuilderHelper.Process<HttpBuilderHelper.Post, String>() {
                    public Object run(HttpBuilderHelper.Post get, String json) {
                        jsonArr[0] = json;
                        return null;
                    }
                });
        return GsonHelper.toJsonMap(jsonArr[0]);
    }

    public Map<?, ?> deleteQueryStatementById(String id) {
        final String[] jsonArr = new String[1];

        String paramStr = pathParam("id", id);
        String pathConcat = url + StringHelper.pathConcat("/default/querystatement_del_id", paramStr);
        HttpBuilderHelper.get().s02_toUrl(pathConcat).s03_connect().s04_doOutput().s05_doInput().s06_redirect()
                .s07_useCaches().s08_connectTimeout().s09_readTimeout().s11_accept().s12_charset().s13_userAgent()
                .s14_contentType().s15_request()
                .s16_json(new HttpBuilderHelper.Process<HttpBuilderHelper.Get, String>() {
                    public Object run(HttpBuilderHelper.Get get, String json) {
                        jsonArr[0] = json;
                        return null;
                    }
                });
        return GsonHelper.toJsonMap(jsonArr[0]);
    }

    public Map<?, ?> deleteQueryStatementByName(String name) {
        final String[] jsonArr = new String[1];

        String paramStr = pathParam("name", name);
        String pathConcat = url + StringHelper.pathConcat("/default/querystatement_del_name", paramStr);
        HttpBuilderHelper.get().s02_toUrl(pathConcat).s03_connect().s04_doOutput().s05_doInput().s06_redirect()
                .s07_useCaches().s08_connectTimeout().s09_readTimeout().s11_accept().s12_charset().s13_userAgent()
                .s14_contentType().s15_request()
                .s16_json(new HttpBuilderHelper.Process<HttpBuilderHelper.Get, String>() {
                    public Object run(HttpBuilderHelper.Get get, String json) {
                        jsonArr[0] = json;
                        return null;
                    }
                });
        return GsonHelper.toJsonMap(jsonArr[0]);
    }

    public Map<?, ?> getDataSourceById(String id) {
        final String[] jsonArr = new String[1];
        String pathConcat = url + StringHelper.pathConcat("/default/datasource_get_id", id);
        HttpBuilderHelper.get().s02_toUrl(pathConcat).s03_connect().s04_doOutput().s05_doInput().s06_redirect()
                .s07_useCaches().s08_connectTimeout().s09_readTimeout().s11_accept().s12_charset().s13_userAgent()
                .s14_contentType().s15_request()
                .s16_json(new HttpBuilderHelper.Process<HttpBuilderHelper.Get, String>() {
                    public Object run(Get get, String json) {
                        jsonArr[0] = json;
                        return null;
                    }
                });
        return GsonHelper.toJsonMap(jsonArr[0]);
    }

    public Map<?, ?> getDataSourceByName(String name) {
        final String[] jsonArr = new String[1];
        String pathConcat = url + StringHelper.pathConcat("/default/datasource_get_name", name);
        HttpBuilderHelper.get().s02_toUrl(pathConcat).s03_connect().s04_doOutput().s05_doInput().s06_redirect()
                .s07_useCaches().s08_connectTimeout().s09_readTimeout().s11_accept().s12_charset().s13_userAgent()
                .s14_contentType().s15_request()
                .s16_json(new HttpBuilderHelper.Process<HttpBuilderHelper.Get, String>() {
                    public Object run(Get get, String json) {
                        jsonArr[0] = json;
                        return null;
                    }
                });
        return GsonHelper.toJsonMap(jsonArr[0]);
    }

    public List<Map<?, ?>> findDataSource(String id, String name, Date createTime, Date updateTime) {
        final String[] jsonArr = new String[1];
        String paramStr = pathParam("id", id, "name", name, "createTime", createTime, "updateTime", updateTime);
        String pathConcat = url + StringHelper.pathConcat("/default/datasource_find", paramStr);
        HttpBuilderHelper.get().s02_toUrl(pathConcat).s03_connect().s04_doOutput().s05_doInput().s06_redirect()
                .s07_useCaches().s08_connectTimeout().s09_readTimeout().s11_accept().s12_charset().s13_userAgent()
                .s14_contentType().s15_request()
                .s16_json(new HttpBuilderHelper.Process<HttpBuilderHelper.Get, String>() {
                    public Object run(Get get, String json) {
                        jsonArr[0] = json;
                        return null;
                    }
                });
        return GsonHelper.toJsonList(jsonArr[0]);
    }

    public Map<?, ?> pageDataSource(int currentPage, int pageSize, String id, String name, Date createTime,
                                    Date updateTime) {
        final String[] jsonArr = new String[1];
        String paramStr = pathParam("currentPage", currentPage, "pageSize", pageSize, "id", id, "name", name,
                "createTime", createTime, "updateTime", updateTime);
        String pathConcat = url + StringHelper.pathConcat("/default/datasource_page", paramStr);
        HttpBuilderHelper.get().s02_toUrl(pathConcat).s03_connect().s04_doOutput().s05_doInput().s06_redirect()
                .s07_useCaches().s08_connectTimeout().s09_readTimeout().s11_accept().s12_charset().s13_userAgent()
                .s14_contentType().s15_request()
                .s16_json(new HttpBuilderHelper.Process<HttpBuilderHelper.Get, String>() {
                    public Object run(Get get, String json) {
                        jsonArr[0] = json;
                        return null;
                    }
                });
        return GsonHelper.toJsonMap(jsonArr[0]);
    }

    public Map<?, ?> addOrUpdateDataSource(String id, String name, String user, String password, String url,
                                           String driver, String validationQuery, int initConnection, int maxConnection, String description) {
        final String[] jsonArr = new String[1];
        String pathConcat = url + "/default/datasource_update";
        String datasource = GsonHelper.toJsonString(/**/
                MapHelper.toMap("id", id, "name", name, "user", user, "password", password, "url", url, "driver",
                        driver, "validationQuery", validationQuery, "initConnection", initConnection, "maxConnection",
                        maxConnection, "description", description));
        HttpBuilderHelper.post().s02_toUrl(pathConcat).s03_connect().s04_doOutput().s05_doInput().s06_redirect()
                .s07_useCaches().s08_connectTimeout().s09_readTimeout().s11_accept().s12_charset().s13_userAgent()
                .s14_contentType().s15_data("datasource", datasource).s16_request()
                .s17_json(new HttpBuilderHelper.Process<HttpBuilderHelper.Post, String>() {
                    public Object run(HttpBuilderHelper.Post get, String json) {
                        jsonArr[0] = json;
                        return null;
                    }
                });
        return GsonHelper.toJsonMap(jsonArr[0]);
    }

    public Map<?, ?> deleteDataSourceById(String id) {
        final String[] jsonArr = new String[1];

        String paramStr = pathParam("id", id);
        String pathConcat = url + StringHelper.pathConcat("/default/datasource_del_id", paramStr);
        HttpBuilderHelper.get().s02_toUrl(pathConcat).s03_connect().s04_doOutput().s05_doInput().s06_redirect()
                .s07_useCaches().s08_connectTimeout().s09_readTimeout().s11_accept().s12_charset().s13_userAgent()
                .s14_contentType().s15_request()
                .s16_json(new HttpBuilderHelper.Process<HttpBuilderHelper.Get, String>() {
                    public Object run(HttpBuilderHelper.Get get, String json) {
                        jsonArr[0] = json;
                        return null;
                    }
                });
        return GsonHelper.toJsonMap(jsonArr[0]);
    }

    public Map<?, ?> deleteDataSourceByName(String name) {
        final String[] jsonArr = new String[1];

        String paramStr = pathParam("name", name);
        String pathConcat = url + StringHelper.pathConcat("/default/datasource_del_name", paramStr);
        HttpBuilderHelper.get().s02_toUrl(pathConcat).s03_connect().s04_doOutput().s05_doInput().s06_redirect()
                .s07_useCaches().s08_connectTimeout().s09_readTimeout().s11_accept().s12_charset().s13_userAgent()
                .s14_contentType().s15_request()
                .s16_json(new HttpBuilderHelper.Process<HttpBuilderHelper.Get, String>() {
                    public Object run(HttpBuilderHelper.Get get, String json) {
                        jsonArr[0] = json;
                        return null;
                    }
                });
        return GsonHelper.toJsonMap(jsonArr[0]);
    }

    public Map<?, ?> testDataSourceById(String id) {
        final String[] jsonArr = new String[1];
        String paramStr = pathParam("id", id);
        String pathConcat = url + StringHelper.pathConcat("/default/datasource_test_id", paramStr);
        HttpBuilderHelper.get().s02_toUrl(pathConcat).s03_connect().s04_doOutput().s05_doInput().s06_redirect()
                .s07_useCaches().s08_connectTimeout().s09_readTimeout().s11_accept().s12_charset().s13_userAgent()
                .s14_contentType().s15_request()
                .s16_json(new HttpBuilderHelper.Process<HttpBuilderHelper.Get, String>() {
                    public Object run(HttpBuilderHelper.Get get, String json) {
                        jsonArr[0] = json;
                        return null;
                    }
                });
        return GsonHelper.toJsonMap(jsonArr[0]);
    }

    public Map<?, ?> testDataSourceByName(String name) {
        final String[] jsonArr = new String[1];
        String paramStr = pathParam("name", name);
        String pathConcat = url + StringHelper.pathConcat("/default/datasource_test_name", paramStr);
        HttpBuilderHelper.get().s02_toUrl(pathConcat).s03_connect().s04_doOutput().s05_doInput().s06_redirect()
                .s07_useCaches().s08_connectTimeout().s09_readTimeout().s11_accept().s12_charset().s13_userAgent()
                .s14_contentType().s15_request()
                .s16_json(new HttpBuilderHelper.Process<HttpBuilderHelper.Get, String>() {
                    public Object run(HttpBuilderHelper.Get get, String json) {
                        jsonArr[0] = json;
                        return null;
                    }
                });
        return GsonHelper.toJsonMap(jsonArr[0]);
    }

    private static String pathParam(Object... args) {
        if (args == null || args.length < 1) {
            return "";
        }
        Assert.isTrue(args.length % 2 == 0, "Arguments length not match!");
        StringBuilder sb = new StringBuilder(128);
        for (int i = 0; i < args.length; i++) {
            Object key = args[i++];
            Object value = args[i];
            if (key == null || value == null) {
                continue;
            }
            if (value instanceof Date) {
                value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) value);
            }
            sb.append('/').append(key).append('=').append(urlEncode(value.toString()));
        }
        return sb.toString();
    }

    /**
     * encode url.
     *
     * @param url
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Nov 21, 2016
     */
    private static String urlEncode(String url) {
        if (url != null && url.length() > 0) {
            try {
                return URLEncoder.encode(url, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * decode url.
     *
     * @param url
     * @return
     * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
     * @since Nov 21, 2016
     */
    private static String urlDecode(String url) {
        if (url != null && url.length() > 0) {
            try {
                return URLDecoder.decode(url, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}

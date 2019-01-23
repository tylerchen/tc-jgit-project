/*******************************************************************************
 * Copyright (c) 2018-10-12 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core;

import org.apache.commons.collections.MapUtils;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.iff.infra.util.*;
import org.iff.infra.util.jdbc.dialet.Dialect;
import org.iff.infra.util.mybatis.plugin.Page;
import org.iff.infra.util.mybatis.service.RepositoryService;

import javax.sql.DataSource;
import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MyBatisSqlSessionFactory
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-10-12
 * auto generate by qdp.
 */
public class MyBatisSqlSessionFactory {

    public static Map<String/*datasourceName*/, SqlSessionFactory> map = new HashMap<String/*datasourceName*/, SqlSessionFactory>();

    public static SqlSessionFactory create(String dataSourceName) {
        SqlSessionFactory sqlSessionFactory = map.get(PreRequiredHelper.requireNotBlank(dataSourceName, "DataSource name is required!"));
        if (sqlSessionFactory == null) {
            DataSource dataSource = getDataSource(dataSourceName);
            Environment environment = new Environment(dataSourceName, new JdbcTransactionFactory(), dataSource);
            Configuration configuration = new Configuration(environment);
            PagePlugin pagePlugin = new PagePlugin();
            {
                Properties p = new Properties();
                p.setProperty("dialect", "org.iff.infra.util.jdbc.dialet.MySQLDialect");
                pagePlugin.setProperties(p);
            }
            configuration.addInterceptor(pagePlugin);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
            map.put(dataSourceName, sqlSessionFactory);
        }
        return sqlSessionFactory;
    }

    public static DataSource getDataSource(String dataSourceName) {
        return PreRequiredHelper.requireNotNull(DataSourceFactory.getOrCreate(dataSourceName), "DataSourceFactory can't find the datasource for name: " + dataSourceName);
    }

    public static RepositoryService service(String dataSourceName) {
        MybatisSqlServiceImpl service = new MybatisSqlServiceImpl();
        service.setSqlSession(create(dataSourceName).openSession());
        return service;
    }

    public static void close(RepositoryService service) {
        ((MybatisSqlServiceImpl) service).close();
    }


    public static void commit(RepositoryService service) {
        ((MybatisSqlServiceImpl) service).commit();
    }

    public static void rollback(RepositoryService service) {
        ((MybatisSqlServiceImpl) service).rollback();
    }

    public static void done(boolean isRollback) {
        Map<String, RepositoryService> map = ThreadLocalHelper.get(RepositoryService.class.getName());
        Map<String, Closeable> close = ThreadLocalHelper.get(Closeable.class.getName());
        if (isRollback) {
            if (MapUtils.isNotEmpty(map)) {
                for (Map.Entry<String, RepositoryService> entry : map.entrySet()) {
                    MyBatisSqlSessionFactory.rollback(entry.getValue());
                }
            }
        } else {
            if (MapUtils.isNotEmpty(map)) {
                for (Map.Entry<String, RepositoryService> entry : map.entrySet()) {
                    MyBatisSqlSessionFactory.commit(entry.getValue());
                }
            }
        }
        if (MapUtils.isNotEmpty(close)) {
            for (Map.Entry<String, Closeable> entry : close.entrySet()) {
                SocketHelper.closeWithoutError(entry.getValue());
            }
        }
        ThreadLocalHelper.remove();
    }

    public static void main(String[] args) {
        test();
    }

    public static void test() {
        try {
            String xml = "" +
                    "<script>\n" +
                    "  insert into DS_QUERY_STMT( ID, NAME, SELECT_BODY, FROM_BODY, WHERE_BODY, DESCRIPTION, UPDATE_TIME, CREATE_TIME ) " +
                    "  values (#{vo.id,jdbcType=VARCHAR}, #{vo.name,jdbcType=VARCHAR}, #{vo.selectBody,jdbcType=VARCHAR}, #{vo.fromBody,jdbcType=VARCHAR}, #{vo.whereBody,jdbcType=VARCHAR}, #{vo.description,jdbcType=VARCHAR}, #{vo.updateTime,jdbcType=TIMESTAMP}, #{vo.createTime,jdbcType=TIMESTAMP}) \n" +
                    "</script>";
            RepositoryService service = service("IFF_REST_SYSTEM");
            service.save(xml,
                    MapHelper.toMap("vo",
                            MapHelper.toMap("id", "1111", "name", "testaaaa", "selectBody", "select *", "fromBody", "from test", "whereBody", "where 1=2", "description", "test", "updateTime", new Date(), "createTime", new Date())));
            commit(service);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void test2() {
        try {
            String xml = "<script>\n" +
                    "  SELECT * FROM DS_QUERY_STMT \n" +
                    "  <where>\n" +
                    "    <if test=\"vo !=null and vo.id != null and vo.id != ''\">\n" +
                    "      AND ID = #{vo.id,jdbcType=VARCHAR}\n" +
                    "    </if>\n" +
                    "  </where>\n" +
                    "</script>";
            Page page = service("IFF_REST_SYSTEM").queryPage(xml, MapHelper.toMap("vo", MapHelper.toMap("id", "eUq1nlgpvbBlv7tnaDp")));
            System.out.println(page.getRows().get(0).getClass().getName());
            System.out.println(JsonHelper.toJson(page));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
    public static class PagePlugin implements Interceptor {

        private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
        private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
        private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();
        private static Dialect dialectObject = null; // 数据库方言
        private static Pattern p = Pattern.compile("order *by.*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

        /**
         * 去除hql的select 子句，未考虑union的情况,用于pagedQuery.
         */
        private static String removeSelect(String hql) {
            Assert.hasText(hql);
            int beginPos = hql.toLowerCase().indexOf("from");
            Assert.isTrue(beginPos != -1, " hql : " + hql + " must has a keyword 'from'");
            return hql.substring(beginPos);
        }

        /**
         * 去除hql的orderby 子句，用于pagedQuery.
         */
        private static String removeOrders(String hql) {
            Assert.hasText(hql);
            Matcher m = p.matcher(hql);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                m.appendReplacement(sb, "");
            }
            m.appendTail(sb);
            return sb.toString();
        }

        public Object intercept(Invocation ivk) throws Throwable {
            if (!(ivk.getTarget() instanceof RoutingStatementHandler)) {
                return ivk.proceed();
            }
            RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk.getTarget();
            MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY,
                    DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
            //BaseStatementHandler delegate = (BaseStatementHandler) metaStatementHandler.getValue("delegate");
            MappedStatement mappedStatement = (MappedStatement) metaStatementHandler
                    .getValue("delegate.mappedStatement");
            {
                //Logger.changeLevel(mappedStatement.getId(), "debug");
            }
            if (mappedStatement.getSqlCommandType() != SqlCommandType.SELECT) {
                return ivk.proceed();
            }
            /*拦截需要分页的SQL*/
            BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
            /*分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空*/
            Object parameterObject = boundSql.getParameterObject();
            if (parameterObject == null) {
                return ivk.proceed();
            }
            Page page = null;
            {
                if (parameterObject instanceof Page) { /*参数就是Pages实体*/
                    page = (Page) parameterObject;
                } else if (parameterObject instanceof Map) {
                    for (Map.Entry entry : (Set<Map.Entry>) ((Map) parameterObject).entrySet()) {
                        if (entry.getValue() instanceof Page) {
                            page = (Page) entry.getValue();
                            break;
                        }
                    }
                } else { /*参数为某个实体，该实体拥有Pages属性*/
                    //page = ReflectHelper.getValueByFieldType(parameterObject, Page.class);
                }
                if (page == null) {
                    return ivk.proceed();
                }
            }
            {/*采用物理分页后，就不需要mybatis的内存分页了，所以重置下面的两个参数*/
                metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
                metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
            }
            String sql = boundSql.getSql();
            PreparedStatement countStmt = null;
            ResultSet rs = null;
            if (!page.isOffsetPage()) {
                try {
                    Connection connection = (Connection) ivk.getArgs()[0];
                    String countSql = "select count(*) from (" + removeOrders(sql) + ") tmp_count"; // 记录统计
                    countStmt = connection.prepareStatement(countSql);
                    metaStatementHandler.setValue("delegate.boundSql.sql", countSql);
                    DefaultParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
                    parameterHandler.setParameters(countStmt);
                    rs = countStmt.executeQuery();
                    int count = 0;
                    if (rs.next()) {
                        count = ((Number) rs.getObject(1)).intValue();
                    }
                    page.setTotalCount(count);
                } finally {
                    SocketHelper.closeWithoutError(rs);
                    SocketHelper.closeWithoutError(countStmt);
                }
            }
            String pageSql = generatePagesSql(sql, page);
            /*将分页sql语句反射回BoundSql*/
            metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
            return ivk.proceed();
        }

        /**
         * 根据数据库方言，生成特定的分页sql
         *
         * @param sql
         * @param page
         * @return
         */
        private String generatePagesSql(String sql, Page page) {
            if (page != null && dialectObject != null) {
                if (page.isOffsetPage()) {
                    return dialectObject.getLimitString(sql, page.getOffset(), page.getPageSize());
                } else {
                    return dialectObject.getLimitString(sql, (page.getCurrentPage() - 1) * page.getPageSize(),
                            page.getPageSize());
                }
            }
            return sql;
        }

        public Object plugin(Object plugin) {
            return Plugin.wrap(plugin, this);
        }

        public void setProperties(Properties p) {
            String dialect = ""; // 数据库方言
            dialect = p.getProperty("dialect");
            if (org.apache.commons.lang.StringUtils.isEmpty(dialect)) {
                Exceptions.runtime("PagePlugin dialect property is not found!");
            } else {
                try {
                    dialectObject = (Dialect) Class.forName(dialect).getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(dialect + ", init fail!\n" + e);
                }
            }
        }
    }
}

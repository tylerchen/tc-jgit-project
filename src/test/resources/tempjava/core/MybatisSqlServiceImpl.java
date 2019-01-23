/*******************************************************************************
 * Copyright (c) 2014-2-26 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.jgit.core;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.iff.infra.domain.InstanceFactory;
import org.iff.infra.util.mybatis.plugin.Page;
import org.iff.infra.util.mybatis.service.RepositoryService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2014-2-26
 */
public class MybatisSqlServiceImpl implements RepositoryService {

    SqlSession sqlSession;

    SqlMapper sqlMapper;

    public SqlSession getSqlSession() {
        if (sqlSession == null) {
            sqlSession = InstanceFactory.getInstance(SqlSession.class);
            sqlMapper = new SqlMapper(sqlSession);
        }
        return sqlSession;
    }

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
        this.sqlMapper = new SqlMapper(sqlSession);
    }

    public SqlMapper getSqlMapper() {
        if (sqlMapper == null && getSqlSession() != null) {
            sqlMapper = new SqlMapper(sqlSession);
        }
        return sqlMapper;
    }

    public int remove(String queryDsl, Object params) {
        return getSqlMapper().delete(queryDsl, params);
    }

    public int save(String queryDsl, Object params) {
        return getSqlMapper().insert(queryDsl, params);
    }

    public int update(String queryDsl, Object params) {
        return getSqlMapper().update(queryDsl, params);
    }

    public List<Map<String, Object>> queryList(String queryDsl, Object params) {
        List<Map<String, Object>> list = null;
        if (params == null) {
            list = getSqlMapper().selectList(queryDsl);
        } else {
            Page page = getObjectByType(Page.class, params);
            if (page != null) {
                list = getSqlMapper().selectList(queryDsl, params, new RowBounds(page.getOffset(), page.getLimit()));
            } else {
                list = getSqlMapper().selectList(queryDsl, params);
            }
        }
        return list == null ? new ArrayList<Map<String, Object>>() : list;
    }

    public <T> T queryOne(String queryDsl, Object params) {
        return (T) getSqlMapper().selectOne(queryDsl, params);
    }

    public <T> Page queryPage(String queryDsl, Object params) {
        List<Map<String, Object>> list = null;
        Page page = null;
        if (params == null) {
            list = getSqlMapper().selectList(queryDsl);
            page = Page.offsetPage(0, list.size(), list);
        } else {
            page = getObjectByType(Page.class, params);
            if (page != null) {
                list = getSqlMapper().selectList(queryDsl, params);
                page.setRows(list);
            } else {
                list = getSqlMapper().selectList(queryDsl, params);
                page = Page.offsetPage(0, list.size(), list);
            }
        }
        list = list == null ? new ArrayList<Map<String, Object>>() : list;
        page = page == null ? Page.offsetPage(0, list.size(), list) : page;
        return page;
    }

    public long querySize(String queryDsl, Object params) {
        Number size = getSqlMapper().selectOne(queryDsl, params, Number.class);
        return size == null ? 0 : size.longValue();
    }

    @SuppressWarnings({"unchecked"})
    private <T> T getObjectByType(Class<T> type, Object params) {
        if (params == null) {
            return null;
        } else if (type.isInstance(params)) {
            return (T) params;
        } else if (params.getClass().isArray()) {
            for (Object obj : (Object[]) params) {
                if (type.isInstance(obj)) {
                    return (T) obj;
                }
            }
        } else if (params instanceof Collection<?>) {
            for (Object obj : (Collection<?>) params) {
                if (type.isInstance(obj)) {
                    return (T) obj;
                }
            }
        } else if (params instanceof Map<?, ?>) {
            for (Entry<?, ?> entry : ((Map<?, ?>) params).entrySet()) {
                if (type.isInstance(entry.getValue())) {
                    return (T) entry.getValue();
                }
            }
        }
        return null;
    }

    public void close() {
        sqlSession.close();
    }

    public void commit() {
        try {
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }
    }

    public void rollback() {
        try {
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }
    }

}

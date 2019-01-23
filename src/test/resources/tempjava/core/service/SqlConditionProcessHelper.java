/*******************************************************************************
 * Copyright (c) Oct 11, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.jgit.core.service;

import org.iff.infra.util.TypeConvertHelper;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Oct 11, 2016
 */
public class SqlConditionProcessHelper {

    public static SqlAndParams processCondition(String sql, Map<String, Object> params) {
        int len = sql.length();
        StringBuilder newSql = new StringBuilder(len);
        List<Object> paramList = new ArrayList<Object>();
        StringBuilder block = new StringBuilder(128);
        StringBuilder conditionName = new StringBuilder(128);
        List<Object> tempParamList = new ArrayList<Object>();
        boolean hasEnterBlock = false;
        boolean hasIn = false;
        boolean hasEnterName = false;
        for (int sqlIndex = 0; sqlIndex < len; sqlIndex++) {
            char c = sql.charAt(sqlIndex);
            char lowCase = Character.toLowerCase(c);
            if (c == '[') {
                hasEnterBlock = true;
                continue;
            }
            if (hasEnterBlock && lowCase == 'i' && sqlIndex + 2 < len
                    && Character.toLowerCase(sql.charAt(sqlIndex + 1)) == 'n' && sql.charAt(sqlIndex + 2) == ' ') {
                hasIn = true;
            }
            if (hasEnterBlock && c == ':') {
                hasEnterName = true;
                continue;
            }
            if (hasEnterName) {
                if (c == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
                    conditionName.append(c);
                    continue;
                } else {
                    hasEnterName = false;
                    String cName = conditionName.toString();
                    Object conditionParam = params.get(cName);
                    boolean validIn = conditionParam != null && ((/*is list*/
                            conditionParam instanceof List && ((List<?>) conditionParam).size() > 0) || (/*is array*/
                            conditionParam.getClass().isArray() && Array.getLength(conditionParam) > 0));
                    if ((conditionParam == null && !params.containsKey(cName)) || (hasIn && !validIn)) {
                        /*remove this condition block*/
                        if (c == ']') {
                            /*remove this condition block*/
                            block.setLength(0);
                            conditionName.setLength(0);
                            /*remove this condition param*/
                            tempParamList.clear();
                            /*reset mark*/
                            hasEnterBlock = false;
                            hasIn = false;
                            hasEnterName = false;
                        } else {
                            for (; ++sqlIndex < len; ) {
                                if (sql.charAt(sqlIndex) == ']') {
                                    /*remove this condition block*/
                                    block.setLength(0);
                                    conditionName.setLength(0);
                                    /*remove this condition param*/
                                    tempParamList.clear();
                                    /*reset mark*/
                                    hasEnterBlock = false;
                                    hasIn = false;
                                    hasEnterName = false;
                                    break;
                                }
                            }
                        }
                        continue;
                    } /*END-if (!params.containsKey(cName) || !validIn)*/
                    if (hasIn) {
                        hasIn = false;
                        block.append('(');
                        if (conditionParam instanceof List) {
                            for (Object obj : ((List<?>) conditionParam)) {
                                block.append('?').append(',');
                                tempParamList.add(convertToType(obj, cName));
                            }
                        } else if (conditionParam.getClass().isArray()) {
                            int arrLen = Array.getLength(conditionParam);
                            for (int arrIndex = 0; arrIndex < arrLen; arrIndex++) {
                                block.append('?').append(',');
                                tempParamList.add(convertToType(Array.get(conditionParam, arrIndex), cName));
                            }
                        }
                        block.setLength(block.length() - 1);
                        block.append(')');
                    } else {
                        block.append('?');
                        tempParamList.add(convertToType(conditionParam, cName));
                    }
                    if (c == ']') {
                        if (block.length() > 0) {
                            newSql.append(' ').append(block);
                            paramList.addAll(tempParamList);
                        }
                        {
                            /*remove this condition block*/
                            block.setLength(0);
                            conditionName.setLength(0);
                            /*remove this condition param*/
                            tempParamList.clear();
                            /*reset mark*/
                            hasEnterBlock = false;
                            hasIn = false;
                            hasEnterName = false;
                        }
                        continue;
                    }
                }
                continue;
            } /*END-if (hasEnterName)*/
            if (hasEnterBlock && c == ']') {
                if (block.length() > 0) {
                    newSql.append(' ').append(block);
                    paramList.addAll(tempParamList);
                }
                {
                    /*remove this condition block*/
                    block.setLength(0);
                    conditionName.setLength(0);
                    /*remove this condition param*/
                    tempParamList.clear();
                    /*reset mark*/
                    hasEnterBlock = false;
                    hasIn = false;
                    hasEnterName = false;
                }
                continue;
            }
            if (hasEnterBlock) {
                block.append(c);
            } else {
                newSql.append(c);
            }
        }
        return SqlAndParams.create(newSql.toString(), paramList);
    }

    @SuppressWarnings("serial")
    public static class SqlAndParams implements Serializable {
        private String sql;
        private List<Object> params = new ArrayList<Object>();

        public static SqlAndParams create(String sql, List<Object> params) {
            SqlAndParams sp = new SqlAndParams();
            sp.sql = sql;
            sp.params = params;
            return sp;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public List<Object> getParams() {
            return params;
        }

        public void setParams(List<Object> params) {
            this.params = params;
        }

    }

    public static Object convertToType(Object conditionParam, String conditionName) {
        if (conditionName.indexOf('_') < 0 || conditionName.startsWith("string_")) {
            return TypeConvertHelper.me().get(String.class.getName()).convert(String.class.getName(), conditionParam,
                    conditionParam.getClass(), null);
        }
        if (conditionName.startsWith("date_")) {
            return TypeConvertHelper.me().get(java.util.Date.class.getName()).convert(java.util.Date.class.getName(),
                    conditionParam, conditionParam.getClass(), null);
        }
        if (conditionName.startsWith("int_")) {
            return TypeConvertHelper.me().get(Integer.class.getName()).convert(Integer.class.getName(), conditionParam,
                    conditionParam.getClass(), null);
        }
        if (conditionName.startsWith("long_")) {
            return TypeConvertHelper.me().get(Long.class.getName()).convert(Long.class.getName(), conditionParam,
                    conditionParam.getClass(), null);
        }
        if (conditionName.startsWith("float_")) {
            return TypeConvertHelper.me().get(Float.class.getName()).convert(Float.class.getName(), conditionParam,
                    conditionParam.getClass(), null);
        }
        if (conditionName.startsWith("double_")) {
            return TypeConvertHelper.me().get(Double.class.getName()).convert(Double.class.getName(), conditionParam,
                    conditionParam.getClass(), null);
        }
        if (conditionName.startsWith("numberic_")) {
            return TypeConvertHelper.me().get(BigDecimal.class.getName()).convert(BigDecimal.class.getName(),
                    conditionParam, conditionParam.getClass(), null);
        }
        if (conditionName.startsWith("clob_")) {
            return TypeConvertHelper.me().get(Clob.class.getName()).convert(Clob.class.getName(), conditionParam,
                    conditionParam.getClass(), null);
        }
        if (conditionName.startsWith("blob_")) {
            return TypeConvertHelper.me().get(Blob.class.getName()).convert(Blob.class.getName(), conditionParam,
                    conditionParam.getClass(), null);
        }
        if (conditionName.startsWith("short_")) {
            return TypeConvertHelper.me().get(Short.class.getName()).convert(Short.class.getName(), conditionParam,
                    conditionParam.getClass(), null);
        }
        if (conditionName.startsWith("short_")) {
            return TypeConvertHelper.me().get(Short.class.getName()).convert(Short.class.getName(), conditionParam,
                    conditionParam.getClass(), null);
        }
        if (conditionName.startsWith("byte_")) {
            return TypeConvertHelper.me().get(Byte.class.getName()).convert(Byte.class.getName(), conditionParam,
                    conditionParam.getClass(), null);
        }
        if (conditionName.startsWith("bool_")) {
            return TypeConvertHelper.me().get(Boolean.class.getName()).convert(Boolean.class.getName(), conditionParam,
                    conditionParam.getClass(), null);
        }
        return conditionParam;
    }
}

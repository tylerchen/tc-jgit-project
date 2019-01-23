/*******************************************************************************
 * Copyright (c) 2018-10-26 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core.model;

import org.apache.commons.lang3.StringUtils;
import org.iff.jgit.core.DDLEventService;
import org.iff.jgit.util.FieldNameHelper;
import org.iff.infra.util.Assert;
import org.iff.infra.util.EventBusHelper;
import org.iff.infra.util.TypeConvertHelper;
import org.iff.infra.util.mybatis.plugin.Page;

import javax.validation.constraints.NotNull;
import java.beans.Transient;
import java.io.Serializable;
import java.util.*;

/**
 * DomainModel
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-10-26
 * auto generate by qdp.
 */
public abstract class BaseModel<T extends BaseModel> extends LinkedHashMap<String, Object> implements Serializable, Map<String, Object>, Cloneable {
    transient String namespace;
    transient String modelName;
    transient DescTableModel table;

    public BaseModel() {
    }

    @Transient
    public abstract T init(@NotNull String namespace, @NotNull String modelName);

    @Transient
    public abstract T newInstance();

    @Transient
    public T setValues(Object... keyValue) {
        if (keyValue == null) {
            return (T) this;
        }
        Assert.isTrue(keyValue.length % 2 == 0);
        for (int i = 0; i < keyValue.length; ++i) {
            super.put((String) keyValue[i++], keyValue[i]);
        }
        return (T) this;
    }

    @Transient
    public String getStr(Object key) {
        return (String) get(key);
    }

    @Transient
    public Boolean getBool(Object key) {
        return (Boolean) get(key);
    }

    @Transient
    public Number getNumber(Object key) {
        return (Number) get(key);
    }

    @Transient
    public Date getDate(Object key) {
        Object value = get(key);
        if (value == null) {
            return null;
        }
        Date date = (Date) TypeConvertHelper.me().get(Date.class.getName()).convert(Date.class.getName(), value, value.getClass(), null);
        if (date != value) {
            put((String) key, date);
        }
        return date;
    }

    @Override
    public Object get(Object key) {
        if (super.containsKey(key)) {
            return super.get(key);
        }
        if (!(key instanceof String)) {
            return null;
        }
        String strKey = (String) key;
        {
            String newKey = FieldNameHelper.columnToField(strKey);
            if (super.containsKey(newKey)) {
                return super.get(newKey);
            }
        }
        {
            String newKey = FieldNameHelper.fieldToColumn(strKey);
            if (super.containsKey(newKey)) {
                return super.get(newKey);
            }
        }
        for (Entry<String, Object> entry : super.entrySet()) {
            if (StringUtils.equalsIgnoreCase(strKey, (String) entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public Object getOrDefault(Object key, Object defaultValue) {
        Object value = get(key);
        return value == null ? defaultValue : value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (super.containsKey(key)) {
            return true;
        }
        if (!(key instanceof String)) {
            return false;
        }
        String strKey = (String) key;
        {
            String newKey = FieldNameHelper.columnToField(strKey);
            if (super.containsKey(newKey)) {
                return true;
            }
        }
        {
            String newKey = FieldNameHelper.fieldToColumn(strKey);
            if (super.containsKey(newKey)) {
                return true;
            }
        }
        for (Entry<String, Object> entry : super.entrySet()) {
            if (StringUtils.equalsIgnoreCase(strKey, (String) entry.getKey())) {
                return true;
            }
        }
        return false;
    }

    @Transient
    public String namespace() {
        return namespace;
    }

    @Transient
    public void namespace(String namespace) {
        this.namespace = namespace;
    }

    @Transient
    public String modelName() {
        return modelName;
    }

    @Transient
    public void modelName(String modelName) {
        this.modelName = modelName;
    }

    @Transient
    public DescTableModel table() {
        if (table == null) {
            DDLEventService.Tuple<DescTableModel> tuple = DDLEventService.Tuple.getTableDesc(namespace, "", "", modelName);
            EventBusHelper.me().syncEvent(DDLEventService.EVENT_NAME, tuple);
            table = tuple.result();
        }
        return table;
    }

    @Transient
    public void table(DescTableModel table) {
        this.table = table;
    }

    @Transient
    public T shadowCopy() {
        T dm = newInstance();
        dm.namespace(namespace);
        dm.modelName(modelName);
        dm.table(table);
        return (T) dm;
    }

    @Transient
    public T convertTo(BaseModel<T> dm) {
        dm.rePutAll(this);
        return (T) dm;
    }

    @Transient
    public List<T> fromList(@NotNull List<? extends Map<String, Object>> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        List<T> result = new ArrayList<>();
        list = list == null ? Collections.EMPTY_LIST : list;
        if (!list.isEmpty() && list.get(0).getClass() == getClass()) {
            for (T map : (List<T>) list) {
                map.init(namespace, modelName);
            }
            return (List<T>) list;
        }
        for (Map<String, Object> map : list) {
            if (map.getClass() == getClass()) {
                result.add((T) ((T) map).init(namespace, modelName));
            } else {
                T dm = (T) shadowCopy().rePutAll(map);
                result.add(dm);
            }
        }
        return result;
    }

    @Transient
    public Page<T> fromPage(@NotNull Page<? extends Map<String, Object>> page) {
        if (page == null) {
            return new Page<>();
        }
        List<T> result = new ArrayList<>();
        List<? extends Map<String, Object>> list = page.getRows();
        list = list == null ? Collections.EMPTY_LIST : list;
        if (!list.isEmpty() && list.get(0).getClass() == getClass()) {
            for (T map : (List<T>) list) {
                map.init(namespace, modelName);
            }
            return (Page<T>) page;
        }
        for (Map<String, Object> map : list) {
            if (map.getClass() == getClass()) {
                result.add((T) ((T) map).init(namespace, modelName));
            } else {
                T dm = (T) shadowCopy().rePutAll(map);
                result.add(dm);
            }
        }
        if (page.isOffsetPage()) {
            return Page.offsetPage(page.getOffset(), page.getPageSize(), result);
        }
        return Page.pageable(page.getPageSize(), page.getCurrentPage(), page.getTotalCount(), list);
    }

    @Transient
    public T fromMap(@NotNull Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        if (map != null && map.getClass() == getClass()) {
            return (T) ((T) map).init(namespace, modelName);
        }
        return (T) shadowCopy().rePutAll(map);
    }

    @Transient
    public T toColumnDomainModel() {
        T map = shadowCopy();
        map.clear();
        for (Entry<String, Object> entry : entrySet()) {
            map.put(FieldNameHelper.fieldToColumn(entry.getKey()), entry.getValue());
        }
        return map;
    }

    @Transient
    public T toJavaFieldDomainModel() {
        T map = shadowCopy();
        map.clear();
        for (Entry<String, Object> entry : entrySet()) {
            map.put(FieldNameHelper.columnToField(entry.getKey()), entry.getValue());
        }
        return map;
    }

    @Transient
    public T fixToColumn() {
        return (T) rePutAll(toColumnDomainModel());
    }

    @Transient
    public T fixToJavaField() {
        return (T) rePutAll(toJavaFieldDomainModel());
    }

    @Transient
    public T rePutAll(@NotNull Map<? extends String, ?> m) {
        clear();
        putAll(m);
        return (T) this;
    }

    @Transient
    public T rePut(@NotNull String key, Object value) {
        put(key, value);
        return (T) this;
    }
}

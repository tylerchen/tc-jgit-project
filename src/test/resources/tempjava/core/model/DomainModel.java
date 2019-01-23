/*******************************************************************************
 * Copyright (c) 2018-10-26 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core.model;

import org.iff.jgit.core.DomainEventService;
import org.iff.jgit.util.FieldNameHelper;
import org.iff.infra.util.*;

import javax.validation.constraints.NotNull;
import java.beans.Transient;
import java.util.Map;

/**
 * DomainModel
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-10-26
 * auto generate by qdp.
 */
public class DomainModel<T extends DomainModel> extends BaseModel<T> {

    @Transient
    public T newInstance() {
        if (getClass() == DomainModel.class) {
            return (T) new DomainModel<DomainModel>();
        }
        try {
            return (T) getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            Exceptions.runtime("DomainModel can't create new instance from base class!", e);
        }
        return null;
    }

    @Transient
    public T init(@NotNull String namespace, @NotNull String modelName) {
        this.namespace = PreRequiredHelper.requireNotBlank(namespace);
        this.modelName = PreRequiredHelper.requireNotBlank(modelName);
        return (T) this;
    }

    @Transient
    public T load(Object id) {
        DomainEventService.Tuple params = DomainEventService.Tuple.queryOne(namespace, modelName, "getById", id);
        EventBusHelper.me().syncEvent(DomainEventService.EVENT_NAME, params);
        if (params.hasError()) {
            Exceptions.runtime("DomainModel getById error!", (Throwable) params.result());
        }
        return rePutAll((Map<String, Object>) params.result());
    }

    @Transient
    public T loadByUniqueField(String fieldName, Object fieldValue) {
        String queryName = "getBy" + FieldNameHelper.tableToClass(fieldName);
        DomainEventService.Tuple params = DomainEventService.Tuple.queryOne(namespace, modelName, queryName, fieldValue);
        EventBusHelper.me().syncEvent(DomainEventService.EVENT_NAME, params);
        if (params.hasError()) {
            Exceptions.runtime("DomainModel " + queryName + " error!", (Throwable) params.result());
        }
        return rePutAll((Map<String, Object>) params.result());
    }

    @Transient
    public T save() {
        if (canUseUuidPk()) {
            pk(IdHelper.uuid());
        }
        DomainEventService.Tuple<Integer> tuple = DomainEventService.Tuple.saveDomain(namespace, modelName, "save", this);
        EventBusHelper.me().syncEvent(DomainEventService.EVENT_NAME, tuple);
        if (tuple.hasError()) {
            Exceptions.runtime("DomainModel save error: " + modelName, tuple.error());
        }
        return (T) this;
    }

    @Transient
    public T update() {
        DomainEventService.Tuple params = DomainEventService.Tuple.updateDomain(namespace, modelName, "update", this);
        EventBusHelper.me().syncEvent(DomainEventService.EVENT_NAME, params);
        if (params.hasError()) {
            Exceptions.runtime("DomainModel update error!", (Throwable) params.result());
        }
        return (T) this;
    }

    @Transient
    public int removeById(Object id) {
        DomainEventService.Tuple<Integer> params = DomainEventService.Tuple.removeDomain(namespace, modelName, "remove", id);
        EventBusHelper.me().syncEvent(DomainEventService.EVENT_NAME, params);
        if (params.hasError()) {
            Exceptions.runtime("DomainModel remove error!", (Throwable) params.error());
        }
        return params.result();
    }

    @Transient
    public int removeByIds(@NotNull Object[] ids) {
        int count = 0;
        for (Object id : ids) {
            count += removeById(id);
        }
        return count;
    }

    @Transient
    public boolean canUseUuidPk() {
        DescTableModel table = table();
        if (table != null && table.getPks().size() == 1) {
            DescColumnModel pk = table.getPks().values().iterator().next();
            return "String".equals(pk.getType());
        }
        return false;
    }

    @Transient
    public Object pk() {
        DescTableModel table = table();
        if (table != null && table.getPks().size() == 1) {
            DescColumnModel pk = table.getPks().values().iterator().next();
            return get(pk.getName());
        }
        return null;
    }

    @Transient
    public void pk(Object value) {
        if (pk() != null) {
            DescTableModel table = table();
            DescColumnModel pk = table.getPks().values().iterator().next();
            if (value == null) {
                put(pk.getName(), value);
                return;
            }
            String valueType = value.getClass().getName();
            if (valueType.equals(pk.getJavaType()) || valueType.equals("java.lang." + pk.getJavaType())) {
                put(pk.getName(), value);
                return;
            } else if ("byte[]".equals(pk.getJavaType())) {
                value = TypeConvertHelper.me().get(byte[].class.getName()).convert(byte[].class.getName(), value, value.getClass(), null);
            } else {
                value = TypeConvertHelper.me().get(pk.getJavaType()).convert(pk.getJavaType(), value, value.getClass(), null);
            }
            put(pk.getName(), value);
        }
    }
}

/*******************************************************************************
 * Copyright (c) 2018-12-31 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core.model;

import org.iff.infra.util.TypeConvertHelper;

import java.util.Objects;

/**
 * FieldDefine
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-12-31
 * auto generate by qdp.
 */
public abstract class FieldDefine<T> implements java.io.Serializable {
    protected String name;

    protected FieldDefine() {
    }

    public FieldDefine(String name) {
        this.name = name;
    }

    public T from(Object o) {
        if (o == null) {
            return (T) o;
        }
        String typeName = getTypeName();
        if (o.getClass().getName().equals(typeName)) {
            return (T) o;
        }
        return (T) TypeConvertHelper.me()
                .get(typeName)
                .convert(typeName, o, o.getClass(), null);
    }

    public abstract String getTypeName();

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FieldDefine<?> that = (FieldDefine<?>) o;
        return Objects.equals(name, that.name);
    }

    public int hashCode() {
        return Objects.hash(name);
    }

    public static class StringDefine extends FieldDefine<String> {
        protected int minLength = 0;
        protected int maxLength = 0;

        protected StringDefine() {
        }

        public StringDefine(String name) {
            super(name);
        }

        @Override
        public String getTypeName() {
            return String.class.getName();
        }

        public boolean isRequired() {
            return minLength > 0;
        }

        public StringDefine minLength(int len) {
            minLength = len;
            return this;
        }

        public StringDefine maxLength(int len) {
            maxLength = len;
            return this;
        }
    }
}

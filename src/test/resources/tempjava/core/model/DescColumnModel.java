package org.iff.jgit.core.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DescColumnModel implements Serializable {
    private String name;
    private String type;
    private String javaType;
    private Integer size;
    private Integer digit;
    private String defaultValue;
    private String remarks;
    private String isAutoIncrement;
    private String isNullable;
    private String isPrimaryKey;
    private String isIndex;
    private String isCascadeDelete;
    private String fkCatalog;
    private String fkSchema;
    private String fkTable;
    private String fkColumn;

    public DescColumnModel() {
        super();
    }

    public static DescColumnModel create(String name, String type, String javaType, Integer size, Integer digit, String defaultValue, String remarks, String isAutoIncrement, String isNullable) {
        DescColumnModel dcm = new DescColumnModel();
        dcm.name = name;
        dcm.type = type;
        dcm.javaType = javaType;
        dcm.size = size;
        dcm.digit = digit;
        dcm.defaultValue = defaultValue;
        dcm.remarks = remarks;
        dcm.isAutoIncrement = isAutoIncrement;
        dcm.isNullable = isNullable;
        return dcm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getDigit() {
        return digit;
    }

    public void setDigit(Integer digit) {
        this.digit = digit;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getIsAutoIncrement() {
        return isAutoIncrement;
    }

    public void setIsAutoIncrement(String isAutoIncrement) {
        this.isAutoIncrement = isAutoIncrement;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getIsPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(String isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    public String getIsIndex() {
        return isIndex;
    }

    public void setIsIndex(String isIndex) {
        this.isIndex = isIndex;
    }

    public String getIsCascadeDelete() {
        return isCascadeDelete;
    }

    public void setIsCascadeDelete(String isCascadeDelete) {
        this.isCascadeDelete = isCascadeDelete;
    }

    public String getFkCatalog() {
        return fkCatalog;
    }

    public void setFkCatalog(String fkCatalog) {
        this.fkCatalog = fkCatalog;
    }

    public String getFkSchema() {
        return fkSchema;
    }

    public void setFkSchema(String fkSchema) {
        this.fkSchema = fkSchema;
    }

    public String getFkTable() {
        return fkTable;
    }

    public void setFkTable(String fkTable) {
        this.fkTable = fkTable;
    }

    public String getFkColumn() {
        return fkColumn;
    }

    public void setFkColumn(String fkColumn) {
        this.fkColumn = fkColumn;
    }

    public String toString() {
        return "DescColumnModel{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", javaType='" + javaType + '\'' +
                ", size=" + size +
                ", digit=" + digit +
                ", defaultValue='" + defaultValue + '\'' +
                ", remarks='" + remarks + '\'' +
                ", isAutoIncrement='" + isAutoIncrement + '\'' +
                ", isNullable='" + isNullable + '\'' +
                ", isPrimaryKey='" + isPrimaryKey + '\'' +
                ", isIndex='" + isIndex + '\'' +
                ", isCascadeDelete='" + isCascadeDelete + '\'' +
                ", fkCatalog='" + fkCatalog + '\'' +
                ", fkSchema='" + fkSchema + '\'' +
                ", fkTable='" + fkTable + '\'' +
                ", fkColumn='" + fkColumn + '\'' +
                '}';
    }
}
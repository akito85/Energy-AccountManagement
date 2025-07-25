package com.dbs.common.library.entities;

public class CriteriaTransformed {
    String columnName;
    String value;

    public CriteriaTransformed(String columnName, String value) {
        this.columnName = columnName;
        this.value = value;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getValue() {
        return value;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CriteriaTransformed{" + "columnName=" + columnName + ", value=" + value + '}';
    }
}

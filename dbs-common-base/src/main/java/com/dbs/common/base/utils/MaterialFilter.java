package com.dbs.common.base.utils;

public class MaterialFilter {

    private MaterialFilterColumn column;
    private String operator;
    private String value;

    public MaterialFilter() {
        super();
    }

    public MaterialFilterColumn getColumn() {
        return column;
    }

    public void setColumn(MaterialFilterColumn column) {
        this.column = column;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

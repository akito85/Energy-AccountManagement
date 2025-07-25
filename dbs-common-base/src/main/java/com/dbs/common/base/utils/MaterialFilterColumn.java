package com.dbs.common.base.utils;

import java.util.Map;

public class MaterialFilterColumn {
    private String title;
    private String field;
    private Map<String,Object> tableData;

    public MaterialFilterColumn() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Map<String, Object> getTableData() {
        return tableData;
    }

    public void setTableData(Map<String, Object> tableData) {
        this.tableData = tableData;
    }
}

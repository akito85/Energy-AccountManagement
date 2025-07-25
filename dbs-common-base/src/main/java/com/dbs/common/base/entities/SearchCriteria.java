package com.dbs.common.base.entities;


import com.dbs.common.base.utils.SearchOperation;
import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class SearchCriteria {
    private String key;
    
    private Object value;
    
    private SearchOperation operation;

    public SearchCriteria(String key, Object value, SearchOperation operation) {
        this.key = key;
        this.value = value;
        this.operation = operation;
    }
}

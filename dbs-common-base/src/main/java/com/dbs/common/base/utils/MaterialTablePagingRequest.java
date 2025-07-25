package com.dbs.common.base.utils;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@SuppressWarnings("java:S1068")
public class MaterialTablePagingRequest {
    private Integer page = Constant.DEFAULT_PAGE_NUMBER;
    private Integer size = Constant.DEFAULT_PAGE_SIZE;
    private List<String> sort;

    private List<String> search;
    
    private Map<String,Object> searchMap;
    
    private String searchs;

    public MaterialTablePagingRequest() {
        super();
        this.sort = new ArrayList<>();
        this.sort.add(Constant.DEFAULT_SORT_BY);
        this.search = new ArrayList<>();
        this.searchMap = new HashMap<>();
    }

}

package com.dbs.common.base.utils;

import lombok.Data;


@Data
@SuppressWarnings("java:S1068")
public class PagingInfo {

    private Long number;
    private Long size;
    private Long totalPages;
    private Long totalElements;

    public PagingInfo(Long number, Long size, Long totalPages, Long totalElements) {
        this.number = number;
        this.size = size;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }
}

package com.dbs.common.library.ctrl;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@SuppressWarnings("java:S1068")
public class PagingDTO {
    private Object result;
    private Object page;
    private Object links;

    public PagingDTO() {
    }

    public PagingDTO(Object result, Object page, Object links) {
        this.result = result;
        this.page = page;
        this.links = links;
    }
}

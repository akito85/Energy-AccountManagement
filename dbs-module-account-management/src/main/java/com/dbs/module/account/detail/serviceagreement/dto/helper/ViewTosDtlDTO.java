package com.dbs.module.account.detail.serviceagreement.dto.helper;

import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class ViewTosDtlDTO implements Serializable {
    private Integer id;
    private Integer saTosId;
    private String attribute;
    private Integer attributeId;
    private Integer unit;
    private Integer value;
    private Integer fromItem;
}

package com.dbs.module.account.detail.serviceagreement.dto.helper;

import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class ChooseProductDTO implements Serializable {
    private Integer id;
    private String productName;
    private String productDescription;
    private Integer productTypeId;
    private String productType;
    private Integer productClassId;
    private String productClass;
    private String serviceType;
    private String status;
}

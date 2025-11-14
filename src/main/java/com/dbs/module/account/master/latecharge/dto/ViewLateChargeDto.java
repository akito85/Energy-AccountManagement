package com.dbs.module.account.master.latecharge.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class ViewLateChargeDto {
    private Integer lateChargeId;
    private String name;
    private String description;
    private String currency;
    private String formula;
    private String maxAmount;
    private Double maxAmountReal;
    private String status;
    private String criteria;
}

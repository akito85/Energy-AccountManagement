package com.dbs.module.account.master.latecharge.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class ViewLateChargeRuleDto {
    private Integer id;
    private Integer latechargeId;
    private String currency;
    private String documentNumber;

    private String maxAmount;
    private Double maxAmountReal;
    private String startDate;
    private String endDate;
    private String description;
    private String status;
    private String approvalStatus;


}

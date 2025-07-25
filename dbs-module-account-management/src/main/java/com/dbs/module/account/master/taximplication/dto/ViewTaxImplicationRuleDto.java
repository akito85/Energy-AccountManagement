package com.dbs.module.account.master.taximplication.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class ViewTaxImplicationRuleDto {
    private Integer id;
    private String startDate;
    private String endDate;
    private String description;
    private String status;
    private Integer transCode;
    private String transCodeName;
    private String transCodeString;
    private String isGunggung;
    private String isVatInv;
    private String documentNumber;
    private String implicationType;
    private String approvalStatus;
}

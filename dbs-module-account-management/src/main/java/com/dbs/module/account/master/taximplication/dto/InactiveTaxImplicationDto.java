package com.dbs.module.account.master.taximplication.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class InactiveTaxImplicationDto {
    private Integer taxImplicationId;
    private Integer appHierId;
    private String remark;
    private Integer taxImplicationRuleId;
    private String endDate;
}

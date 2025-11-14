package com.dbs.module.account.master.taximplication.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class ApprovalTaxImplicationRuleDto {
    private Integer taxImplicationId;
    private Integer taxImplicationRuleId;
    private String description;
    private Integer approvalId;
    private String action;
}

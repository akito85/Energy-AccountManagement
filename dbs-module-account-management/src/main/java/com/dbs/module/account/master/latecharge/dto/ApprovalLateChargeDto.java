package com.dbs.module.account.master.latecharge.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class ApprovalLateChargeDto {
    private Integer lateChargeId;
    private Integer lateChargeRuleId;
    private String description;
    private Integer approvalId;
    private String action;
}

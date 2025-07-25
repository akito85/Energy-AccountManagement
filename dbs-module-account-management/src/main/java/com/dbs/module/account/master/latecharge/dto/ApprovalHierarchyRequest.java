package com.dbs.module.account.master.latecharge.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class ApprovalHierarchyRequest {
    private Boolean isDraft;
    private Integer apphierId;
    private Integer lateChargeRuleId;
}

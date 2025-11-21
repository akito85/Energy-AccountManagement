package com.dbs.module.account.master.latecharge.dto;

import lombok.Data;


@Data
@SuppressWarnings("java:S1068")
public class InactiveLateChargeDto {
    private Integer lateChargeId;
    private Integer appHierId;
    private String remark;
    private Integer lateChargeRuleId;

    private String endDate;
}

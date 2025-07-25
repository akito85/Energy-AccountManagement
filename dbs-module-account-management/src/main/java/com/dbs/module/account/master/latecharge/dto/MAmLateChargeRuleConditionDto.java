package com.dbs.module.account.master.latecharge.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class MAmLateChargeRuleConditionDto {
    private Integer lateChargeRuleConditionId;
    private Integer name;
    private Integer operator;
    private Integer dataType;
    private Float value;
}

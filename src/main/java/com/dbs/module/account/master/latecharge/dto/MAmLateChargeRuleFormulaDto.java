package com.dbs.module.account.master.latecharge.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class MAmLateChargeRuleFormulaDto {
    private Integer lateChargeRuleFormulaId;
    private Integer operation;
    private String type;
    private String variableName;
    private String constantName;
    private Float value;
}

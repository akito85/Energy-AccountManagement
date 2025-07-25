package com.dbs.module.account.master.latecharge.dto;

import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class MLateChargeRuleUpdateDto {
    private Integer lateChargeRuleId;
    private String description;
    private Integer appHierId;
    private Boolean isSubmit;
    private String documentNumber;

    private Double maxAmount;
    private String startDate;
    private List<MAmLateChargeRuleFormulaDto> listRuleFormula;
    private List<MAmLateChargeRuleConditionDto> listRuleCondition;
}

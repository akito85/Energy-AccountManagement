package com.dbs.module.account.master.latecharge.dto;

import lombok.Data;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class MAmLateChargeRuleRequestDto {

    private Double maxAmount;
    private Integer lateChargeId;
    private String startDate;
    private String description;
    private String documentNumber;
    private Integer appHierId;
    private Boolean isSubmit;
    private List<MAmLateChargeRuleFormulaDto> listRuleFormula;
    private List<MAmLateChargeRuleConditionDto> listRuleCondition;
}

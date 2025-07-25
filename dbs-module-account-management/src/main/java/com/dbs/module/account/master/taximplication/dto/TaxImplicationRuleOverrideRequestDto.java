package com.dbs.module.account.master.taximplication.dto;

import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class TaxImplicationRuleOverrideRequestDto {
        private Integer id;
        private Integer implicationType;
        private Integer transCode;
        private String description;
        private List<TaxImplicationRuleOverrideConditionReqDto> listRuleOverrideCondition;
}

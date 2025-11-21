package com.dbs.module.account.master.taximplication.dto;

import lombok.Data;


@Data
@SuppressWarnings("java:S1068")
public class TaxImplicationRuleOverrideConditionReqDto {
    private Integer id;
    private Integer name;
    private Integer operator;
    private String value;

    private Integer dataType;
}

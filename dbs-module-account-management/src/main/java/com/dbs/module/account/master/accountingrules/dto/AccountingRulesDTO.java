package com.dbs.module.account.master.accountingrules.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@SuppressWarnings("java:S1068")
public class AccountingRulesDTO {

    private Integer masterAccountingRuleId;

    @NotEmpty(message = "Classification type name not be empty")
    private String classificationTypeName;

    @NotEmpty(message = "Code not be null")
    private String code;

    @NotEmpty(message = "Receivable account not be empty")
    private String receivableAccount;

    @NotEmpty(message = "Revenue account not be empty")
    private String revenueAccount;

    private String description;

    private String remark;
}

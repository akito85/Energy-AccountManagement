package com.dbs.module.account.master.taximplication.dto;

import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class UpdateTaxImplicationRuleDto {
    private Integer taxImplicationRuleId;
    private String description;
    private Integer appHierId;
    private Boolean isSubmit;

    private String documentNumber;
    private Integer implicationType;
    private Boolean isVatInv;
    private Boolean isGunggung;
    private Integer transCode;
    private String transCodeString;
    private String startDate;
    private List<TaxImplicationRuleOverrideRequestDto> listRuleOverride;
}

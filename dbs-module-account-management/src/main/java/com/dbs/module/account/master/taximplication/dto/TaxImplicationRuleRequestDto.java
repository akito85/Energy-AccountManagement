package com.dbs.module.account.master.taximplication.dto;

import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class TaxImplicationRuleRequestDto {
    private Integer appHierId;
    private Integer taxImplicationId;
    private String documentNumber;
    private Integer implicationType;
    private Boolean isVatInv;
    private Boolean isGunggung;
    private Integer transCode;
    private String transCodeString;
    private String startDate;
    private String description;
    private List<TaxImplicationRuleOverrideRequestDto> listRuleOverride;
    private Boolean isSubmit;
}

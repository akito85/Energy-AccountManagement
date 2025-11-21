package com.dbs.module.account.master.latecharge.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class LateChargeCriteriaDto {
    private Integer id;
    private Integer region;
    private String currency;
    private Integer premiseCountry;
    private Integer premiseProvince;
    private Integer premiseCity;
    private Integer premiseDistrict;
    private Integer premiseSubdistrict;
    private Integer sor;
    private Integer costCenter;
    private Integer accountCategory;
    private Integer classificationType;
    private String accountNumber;
    private Integer accountSegment;
    private Integer accountGroupType;
    private Integer accountType;
    private Integer saType;
    private Boolean corporateFlag;
    private Boolean wapuFlag;
    private Boolean isAll;
}

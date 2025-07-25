package com.dbs.module.account.master.taximplication.dto;

import lombok.Data;


@Data
@SuppressWarnings("java:S1068")
public class TaxImplicationCriteriaDto {
    private Integer id;
    private Integer accountCategory;
    private Integer accountGroupType;
    private Integer accountNumber;
    private Integer accountSegment;
    private Integer accountType;
    private Integer classificationType;
    private Boolean corporateFlag;
    private Integer costCenter;
    private Boolean isAll;
    private Integer premiseCity;
    private Integer premiseCountry;
    private Integer premiseDistrict;
    private Integer premiseProvince;
    private Integer premiseSubdistrict;
    private Integer saType;
    private Integer sor;
    private Integer mAmTaximplicationId;
    private Boolean wapuFlag;
    private String startDate;
    private String endDate;
    private String description;
}

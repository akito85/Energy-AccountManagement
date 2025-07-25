package com.dbs.module.account.master.latecharge.dto;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class MAmLateChargeCriteriaDto extends BaseEntities implements Serializable {
    private Integer premiseCountry;
    private Integer premiseProvince;
    private Integer premiseCity;
    private Integer premiseDistrict;
    private Integer premiseSubdistrict;
    private Integer sor;
    private Integer costCenter;
    private Integer accountCategory;
    private Integer classificationType;
    private Integer accountSegment;
    private Integer accountGroupType;
    private Integer accountType;
    private Boolean corporateFlag;
    private Boolean wapuFlag;
    private Integer accountNumber;
    private Boolean allCriteria;
    private Integer saType;
    private String startDate;
    private String endDate;
    private String description;

    private Integer id;
    private Integer latechargeId;
}
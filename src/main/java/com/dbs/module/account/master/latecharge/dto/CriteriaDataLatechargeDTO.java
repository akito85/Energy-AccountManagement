package com.dbs.module.account.master.latecharge.dto;

import lombok.Data;

import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class CriteriaDataLatechargeDTO {
    private Integer id;
    private Integer lateChargeId;
    private GlobalTypeDTO accountGroupType;
    private GlobalTypeDTO accountCategory;
    private GlobalTypeDTO accountType;
    private GlobalTypeDTO saType;
    private GlobalTypeDTO costCenter;
    private GlobalTypeDTO accountNumber;
    private GlobalTypeDTO accountSegment;
    private GlobalTypeDTO classificationType;
    private BooleanDTO corporateFlag;
    private String allCriteria;
    private GlobalTypeDTO sor;
    private BooleanDTO wapuFlag;
    private GlobalTypeDTO premiseCity;
    private GlobalTypeDTO premiseSubdistrict;
    private GlobalTypeDTO premiseProvince;
    private GlobalTypeDTO premiseCountry;
    private GlobalTypeDTO premiseDistrict;
    private String status;
    private String startDate;
    private String endDate;
    private String description;
    private Date createdDate;
    private Date updatedDate;
    private String createdBy;
    private String updatedBy;
}

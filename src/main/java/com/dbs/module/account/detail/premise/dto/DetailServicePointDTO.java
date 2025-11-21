package com.dbs.module.account.detail.premise.dto;

import lombok.Data;

import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class DetailServicePointDTO {
    private Integer servicePointId;
    private Integer premiseAddressId;
    private String premiseAddress;
    private Integer servicePointNameId;
    private String servicePointName;
    private String description;
    private Date createdDate;
    private String createdBy;
    private Date updateDate;
    private String updatedBy;
    private Integer recordId;
}

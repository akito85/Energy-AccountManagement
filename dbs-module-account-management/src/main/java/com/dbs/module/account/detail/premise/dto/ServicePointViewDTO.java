package com.dbs.module.account.detail.premise.dto;

import lombok.Data;

import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class ServicePointViewDTO {
    private Integer servicePointId;
    private String servicePointName;
    private String description;
    private String assetSerialNumber;
    private String assetName;
    private Date installedDate;
    private String status;
}

package com.dbs.module.account.master.location.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class MasterLocationResponseDto {
    private Integer locationId;
    private String locationCode;
    private String locationName;
    private String locationType;
    private String locationParentType;
    private String locationParent;
    private String locationReference;
    private String status;
}

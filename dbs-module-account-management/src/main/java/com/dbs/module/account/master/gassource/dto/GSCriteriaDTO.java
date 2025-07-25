package com.dbs.module.account.master.gassource.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class GSCriteriaDTO {
    private Integer gasSourceCriteriaId;
    private Integer costCenterId;
    private String startDate;
    private String endDate;
    private String description;
}

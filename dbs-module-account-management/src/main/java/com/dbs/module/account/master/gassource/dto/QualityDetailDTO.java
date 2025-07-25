package com.dbs.module.account.master.gassource.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@SuppressWarnings("java:S1068")
public class QualityDetailDTO {

    private Integer gasSourceId;

    private Integer gasSourceDetailId;

    @NotEmpty(message = "Document number not be empty")
    private String documentNumber;

    @NotEmpty(message = "Start date not be empty")
    private String startDate;

    private String endDate;

    private Double m3;

    private Double btu;
    private Double sg;
    private Double n2;
    private Double co2;
    private String description;
}

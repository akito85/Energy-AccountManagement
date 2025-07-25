package com.dbs.module.account.master.meterreadingcodes.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@SuppressWarnings("java:S1068")
public class CreateRequestDTO {
    @NotEmpty(message = "Meter reading code cannot be empty!")
    private String code;

    @NotNull(message = "Cost center cannot be null!")
    private Integer costCenterId;

    private String description;
    private Integer entityId;
}

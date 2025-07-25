package com.dbs.module.account.master.gassource.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@SuppressWarnings("java:S1068")
public class AssignGSDTO {
    @NotNull(message="Account Id cannot be null!")
    private Integer accountId;

    @NotNull(message="Calorie Type cannot be null!")
    private Integer calorieType;

    @NotNull(message="Calorie Code cannot be null!")
    private Integer gasSourceCodeId;

    @NotEmpty(message="Start Date cannot be empty!")
    private String startDate;

    private String remark;
    private Boolean needValidation;
}

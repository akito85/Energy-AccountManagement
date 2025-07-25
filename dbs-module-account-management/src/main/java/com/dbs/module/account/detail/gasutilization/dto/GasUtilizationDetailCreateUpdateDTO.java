package com.dbs.module.account.detail.gasutilization.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class GasUtilizationDetailCreateUpdateDTO {

    private Integer id;

    private Integer gasUtilsId;

    @NotNull(message = "Utilization name cannot be null!")
    private Integer name;

    @NotNull(message = "Percentage cannot be null!")
    private Double percentage;

}

package com.dbs.module.account.detail.gasutilization.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class GasUtilizationCreateUpdateDTO {

    private Integer id;

    @NotNull(message = "Account id cannot be null!")
    private Integer accountId;

    @NotEmpty(message = "Effective date cannot be empty!")
    private String effectiveDate;

    private String description;

    @NotEmpty(message = "Gas utilization detail cannot be empty!")
    List<GasUtilizationDetailCreateUpdateDTO> gasUtilsDtl;

}

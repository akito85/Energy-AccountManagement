package com.dbs.module.account.detail.gasutilization.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class GasUtilizationDetailDTO {

    private Integer id;
    private Integer AccountId;
    private String effectiveDate;
    private String description;
    List<GasUtilizationDetailCreateUpdateDTO> gasUtilsDtl;

}

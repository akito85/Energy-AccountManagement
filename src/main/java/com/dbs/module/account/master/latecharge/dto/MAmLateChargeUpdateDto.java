package com.dbs.module.account.master.latecharge.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class MAmLateChargeUpdateDto {
    private Integer lateChargeId;

    @NotEmpty(message = "Latecharge Name cannot be empty!")
    private String lateChargeName;

    @NotNull(message = "Currency cannot be null!")
    private Integer currency;

    @NotEmpty(message = "Criteria Name cannot be empty!")
    private List<CriteriaListDTO> criteria;

    private String description;
    private List<MAmLateChargeCriteriaDto> lateChargeCriteriaDatas;

}

package com.dbs.module.account.master.latecharge.dto;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class MAmLateChargeDto {
    private Integer lateChargeId;
    @NotEmpty(message = "lateChargeName cannot be empty!")
    private String lateChargeName;

    @NotNull(message = "Currency cannot be null!")
    private Integer currency;

    private String description;
    private String startDate;
    private String endDate;
    private List<MAmLateChargeCriteriaDto> lateChargeCriteriaDatas;

    @NotEmpty(message = "Criteria cannot be empty!")
    private List<CriteriaListDTO> criteria;
}

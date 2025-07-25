package com.dbs.module.account.master.taximplication.dto;

import com.dbs.module.account.master.latecharge.dto.CriteriaListDTO;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class UpdateTaxImplicationDto {
    private Integer taxImplicationId;

    @NotEmpty(message = "Tax Implication name cannot be empty!")
    private String taxImplicationName;
    private String description;
    private List<TaxImplicationCriteriaDto> taxImplicationCriterias;

    @NotNull(message = "Category cannot be null!")
    private Integer category;

    @NotNull(message = "Service Type cannot be null!")
    private Integer serviceType;

    @NotEmpty(message = "Tax Implication name cannot be empty!")
    private List<CriteriaListDTO> criteria;
}

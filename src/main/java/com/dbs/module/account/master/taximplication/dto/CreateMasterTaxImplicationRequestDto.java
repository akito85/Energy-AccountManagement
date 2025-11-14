package com.dbs.module.account.master.taximplication.dto;

import com.dbs.module.account.master.latecharge.dto.CriteriaListDTO;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class CreateMasterTaxImplicationRequestDto {
    private Integer taxImplicationId;
//    implication name
    @NotEmpty(message = "Tax Implication Name cannot be empty!")
    private String taxImplicationName;
//    category
    @NotNull(message = "Category cannot be null!")
    private Integer category;
//    service type
    @NotNull(message = "Service Type cannot be null!")
    private Integer serviceType;
//    criteria
@NotEmpty(message = "Criteria cannot be empty!")
    private List<CriteriaListDTO> criteria;
//    description
    private String description;
//    List taxImplicationCriteria
    private List<TaxImplicationCriteriaDto> taxImplicationCriterias;
}

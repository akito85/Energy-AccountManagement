package com.dbs.module.account.detail.sourcedistribution.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class SourceDistributionCreateUpdateDTO {

    private Integer id;

    @NotEmpty(message = "Type distribution cannot be empty")
    private String typeDist;

    @NotNull(message = "Account id cannot be null")
    private Integer accountId;

    @NotEmpty(message = "Effective date cannot be empty")
    private String effectiveDate;

    @NotNull(message = "Local value cannot be null")
    private Double value1;

    @NotNull(message = "Export value cannot be null")
    private Double value2;

    private String description;

    @NotEmpty(message = "Source distribution detail cannot be empty")
    private List<SourceDistributionDetailCreateUpdateDTO> srcDistDtl;
}

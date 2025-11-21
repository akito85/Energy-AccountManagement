package com.dbs.module.account.detail.sourcedistribution.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SourceDistributionDetailCreateUpdateDTO {

    private Integer id;

    private Integer srcDistId;

    @NotNull(message = "Country cannot be null")
    private Integer country;


    @NotNull(message = "Percentage cannot be null")
    private Double percentage;
}

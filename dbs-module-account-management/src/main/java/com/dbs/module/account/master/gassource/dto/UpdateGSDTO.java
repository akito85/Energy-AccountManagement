package com.dbs.module.account.master.gassource.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@SuppressWarnings("java:S1068") 
public class UpdateGSDTO {
    private Integer gasSourceId;

    @NotEmpty(message = "Calorie code not be empty")
    private String calorieCode;

    @NotEmpty(message = "Name not be empty")
    private String name;

    private String description;

    @NotNull(message = "UOM not be null")
    private Integer uom;

    List<GSCriteriaDTO> criteria;
}

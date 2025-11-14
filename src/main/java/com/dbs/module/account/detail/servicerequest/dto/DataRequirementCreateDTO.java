package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * DTO for creating a new data requirement
 */
@Data
public class DataRequirementCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "Requirement name cannot be empty")
    private String requirementName;

    @NotNull(message = "Requirement type cannot be null")
    private Integer requirementType;

    @NotEmpty(message = "Data type cannot be empty")
    private String dataType; // String, Number, Date, File, etc.

    @NotNull(message = "Is mandatory flag cannot be null")
    private Boolean isMandatory;

    private String validationRule;

    private String defaultValue;

    private String helpText;
}

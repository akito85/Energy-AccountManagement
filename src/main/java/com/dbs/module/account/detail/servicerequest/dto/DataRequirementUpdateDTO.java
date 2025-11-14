package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * DTO for updating a data requirement
 */
@Data
public class DataRequirementUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Data requirement ID cannot be null")
    private Integer dataRequirementId;

    private String requirementName;

    private String dataType;

    private Boolean isMandatory;

    private String validationRule;

    private String defaultValue;

    private String helpText;
}

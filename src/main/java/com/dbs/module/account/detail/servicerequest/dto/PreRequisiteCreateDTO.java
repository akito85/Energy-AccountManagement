package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * DTO for creating a new prerequisite
 */
@Data
public class PreRequisiteCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Prerequisite type cannot be null")
    private Integer prerequisiteType;

    @NotEmpty(message = "Prerequisite name cannot be empty")
    private String prerequisiteName;

    private String description;

    @NotNull(message = "Is mandatory flag cannot be null")
    private Boolean isMandatory;

    private Integer sequenceOrder;
}

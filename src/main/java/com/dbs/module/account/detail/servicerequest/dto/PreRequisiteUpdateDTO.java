package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * DTO for updating a prerequisite
 */
@Data
public class PreRequisiteUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Prerequisite ID cannot be null")
    private Integer prerequisiteId;

    private String prerequisiteName;

    private String description;

    private Boolean isMandatory;

    private Integer sequenceOrder;
}

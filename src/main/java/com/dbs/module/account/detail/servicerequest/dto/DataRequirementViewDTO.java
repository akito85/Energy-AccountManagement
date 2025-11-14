package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for viewing Data Requirement details
 */
@Data
public class DataRequirementViewDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer dataRequirementId;
    private String requirementName;
    private Integer requirementType;
    private String requirementTypeName;
    private String dataType;
    private Boolean isMandatory;
    private String validationRule;
    private String defaultValue;
    private String helpText;
}

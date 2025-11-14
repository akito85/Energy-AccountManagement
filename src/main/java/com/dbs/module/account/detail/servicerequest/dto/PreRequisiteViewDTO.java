package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for viewing Prerequisite details
 */
@Data
public class PreRequisiteViewDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer prerequisiteId;
    private Integer prerequisiteType;
    private String prerequisiteTypeName;
    private String prerequisiteName;
    private String description;
    private Boolean isMandatory;
    private Integer sequenceOrder;
}

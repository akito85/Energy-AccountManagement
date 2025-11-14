package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for prerequisite submission in service request
 */
@Data
public class PrerequisiteSubmissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer prerequisiteId;
    private Boolean completed = false;
    private String completionNotes;
    private Integer sequenceOrder;
}

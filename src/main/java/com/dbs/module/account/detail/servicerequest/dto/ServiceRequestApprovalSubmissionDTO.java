package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * DTO for submitting service request for approval
 * Separated from composite flow to allow independent approval submission
 */
@Data
public class ServiceRequestApprovalSubmissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Service request ID is required")
    private Integer serviceRequestId;

    @NotNull(message = "Approval hierarchy ID is required")
    private Integer approvalHierarchyId;

    private String approvalNotes;

    private String triggerJson; // Optional JSON data for approval context
}

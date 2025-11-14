package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * DTO for complete service request submission
 * Handles the entire flow: SR -> Contact -> Prerequisites/POS -> Attachments -> Approval
 *
 * This composite DTO enables single-transaction submission for optimal performance
 * and data consistency
 */
@Data
public class ServiceRequestCompleteSubmissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // Step 1: Service Request
    @Valid
    @NotNull(message = "Service request data cannot be null")
    private ServiceRequestCreateDTO serviceRequest;

    // Step 2: Contacts (existing or new)
    @Valid
    private List<ContactSubmissionDTO> contacts;

    // Step 3: Prerequisites and POS
    @Valid
    private List<PrerequisiteSubmissionDTO> prerequisites;

    @Valid
    private PointOfSaleSubmissionDTO pointOfSale;

    // Step 4: Attachments
    @Valid
    private List<AttachmentSubmissionDTO> attachments;

    // Step 5: Approval
    private Boolean submitForApproval = false;
    private Integer approvalHierarchyId;  // Required if submitForApproval = true
    private String approvalNotes;

    // Additional workflow control flags
    private Boolean saveDraft = false;      // Save as draft without strict validation
    private Boolean validateOnly = false;   // Only validate, don't submit
    private Boolean autoAssign = true;      // Auto-assign to available user

    // Metadata
    private String submissionSource;  // WEB, MOBILE, API, etc.
    private String clientIpAddress;
    private String userAgent;
}

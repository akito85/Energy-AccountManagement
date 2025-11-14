package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DTO for complete service request submission response
 * Contains all generated IDs and status information
 */
@Data
public class ServiceRequestCompleteResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // Service Request Info
    private Integer serviceRequestId;
    private String requestNumber;
    private String status;
    private Date createdDate;

    // Created Contacts Info
    private List<ContactCreationResult> createdContacts;

    // POS Info
    private PointOfSaleResult posDetails;

    // Attachments Info
    private List<AttachmentResult> attachments;

    // Approval Info
    private ApprovalResult approval;

    // Summary
    private String message;
    private Boolean success;

    @Data
    public static class ContactCreationResult implements Serializable {
        private Integer contactId;
        private Integer accountContactId;
        private String contactName;
        private Boolean isNew;
        private Boolean isPrimary;
    }

    @Data
    public static class PointOfSaleResult implements Serializable {
        private Integer posId;
        private String posNumber;
        private Integer installmentId;
        private Integer productsCount;
        private Integer promosApplied;
    }

    @Data
    public static class AttachmentResult implements Serializable {
        private Integer attachmentId;
        private String fileName;
        private Long fileSize;
        private String filePath;
    }

    @Data
    public static class ApprovalResult implements Serializable {
        private Integer approvalWorkflowId;
        private String approvalStatus;
        private String estimatedApprovalTime;
        private String approvalNotes;
        private Integer assignedApproverId;
    }
}

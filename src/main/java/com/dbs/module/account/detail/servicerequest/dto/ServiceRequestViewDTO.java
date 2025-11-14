package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * DTO for viewing service request details
 */
@Data
public class ServiceRequestViewDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer serviceRequestId;
    private Integer accountId;
    private String accountNumber;
    private String accountName;
    private String requestNumber;

    // Using names from GLOBAL_TYPE_VALUE
    private Integer requestType;
    private String requestTypeName;

    private Integer requestCategory;
    private String requestCategoryName;

    private Integer requestStatus;
    private String requestStatusName;

    private Integer priority;
    private String priorityName;

    private String subject;
    private String description;
    private Date requestedDate;
    private Date dueDate;
    private Date completionDate;

    private Integer assignedTo;
    private String assignedToName;

    private String status;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;

    // Related entities
    private List<PreRequisiteViewDTO> prerequisites;
    private List<DataRequirementViewDTO> dataRequirements;
    private List<WorkOrderViewDTO> workOrders;
    private InstallmentViewDTO installment;
    private List<AttachmentViewDTO> attachments;
}

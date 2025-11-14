package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * DTO for creating a new service request
 */
@Data
public class ServiceRequestCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Account ID cannot be null")
    private Integer accountId;

    @NotEmpty(message = "Request number cannot be empty")
    private String requestNumber;

    @NotNull(message = "Request type cannot be null")
    private Integer requestType;

    @NotNull(message = "Request category cannot be null")
    private Integer requestCategory;

    @NotNull(message = "Priority cannot be null")
    private Integer priority;

    @NotEmpty(message = "Subject cannot be empty")
    private String subject;

    private String description;

    private Date requestedDate;

    private Date dueDate;

    private Integer assignedTo;

    // Related entities (optional)
    private List<Integer> prerequisiteIds;
    private List<Integer> dataRequirementIds;
    private WorkOrderDTO workOrder;
    private InstallmentDTO installment;
    private List<Integer> attachmentIds;
}

package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * DTO for updating a service request
 */
@Data
public class ServiceRequestUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Service request ID cannot be null")
    private Integer serviceRequestId;

    private String subject;

    private String description;

    private Date dueDate;

    private Date completionDate;

    private Integer assignedTo;

    private Integer requestStatus;

    private Integer priority;
}

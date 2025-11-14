package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO for viewing Work Order details
 */
@Data
public class WorkOrderViewDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer workOrderId;
    private String workOrderNumber;
    private Integer workOrderType;
    private String workOrderTypeName;
    private Integer workOrderStatus;
    private String workOrderStatusName;
    private Date scheduledDate;
    private Date startDate;
    private Date completionDate;
    private Integer assignedTeam;
    private String assignedTeamName;
    private Integer assignedTechnician;
    private String assignedTechnicianName;
    private BigDecimal estimatedHours;
    private BigDecimal actualHours;
    private String notes;
    private String status;
    private String createdBy;
    private Date createdDate;
}

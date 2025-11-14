package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO for Work Order
 */
@Data
public class WorkOrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer workOrderId;
    private String workOrderNumber;
    private Integer workOrderType;
    private Integer workOrderStatus;
    private Date scheduledDate;
    private Date startDate;
    private Date completionDate;
    private Integer assignedTeam;
    private Integer assignedTechnician;
    private BigDecimal estimatedHours;
    private BigDecimal actualHours;
    private String notes;
}

package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO for Installment Schedule
 */
@Data
public class InstallmentScheduleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer scheduleId;
    private Integer installmentId;
    private Integer installmentNumber;
    private Date dueDate;
    private BigDecimal amount;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
    private BigDecimal paidAmount;
    private Date paymentDate;
    private Integer paymentStatus;
    private String paymentStatusName;
    private BigDecimal lateFee;
    private String notes;
}

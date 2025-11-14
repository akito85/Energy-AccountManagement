package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO for creating a new installment schedule
 */
@Data
public class ScheduleCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Installment ID cannot be null")
    private Integer installmentId;

    @NotNull(message = "Installment number cannot be null")
    private Integer installmentNumber;

    @NotNull(message = "Due date cannot be null")
    private Date dueDate;

    @NotNull(message = "Amount cannot be null")
    private BigDecimal amount;

    private BigDecimal principalAmount;

    private BigDecimal interestAmount;

    private BigDecimal paidAmount;

    private Date paymentDate;

    private Integer paymentStatus;

    private BigDecimal lateFee;

    private String notes;
}

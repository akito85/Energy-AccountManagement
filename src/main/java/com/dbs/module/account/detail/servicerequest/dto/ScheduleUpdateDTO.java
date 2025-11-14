package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO for updating an installment schedule
 */
@Data
public class ScheduleUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Schedule ID cannot be null")
    private Integer scheduleId;

    private Date dueDate;

    private BigDecimal amount;

    private BigDecimal principalAmount;

    private BigDecimal interestAmount;

    private BigDecimal paidAmount;

    private Date paymentDate;

    private Integer paymentStatus;

    private BigDecimal lateFee;

    private String notes;
}

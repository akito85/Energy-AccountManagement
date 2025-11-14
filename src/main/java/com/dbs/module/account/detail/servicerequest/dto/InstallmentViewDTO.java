package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * DTO for viewing Installment details
 */
@Data
public class InstallmentViewDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer installmentId;
    private String installmentNumber;
    private BigDecimal totalAmount;
    private Integer numberOfInstallments;
    private Date startDate;
    private Date endDate;
    private BigDecimal interestRate;
    private Integer paymentFrequency;
    private String paymentFrequencyName;
    private Integer installmentStatus;
    private String installmentStatusName;
    private BigDecimal downPayment;
    private String status;
    private String createdBy;
    private Date createdDate;

    // Related entities
    private List<InstallmentScheduleDTO> schedules;
    private List<InstallmentBillingItemDTO> billingItems;
    private InstallmentPaymentPlanDTO paymentPlan;
}

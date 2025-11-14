package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO for Installment
 */
@Data
public class InstallmentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer installmentId;
    private String installmentNumber;
    private BigDecimal totalAmount;
    private Integer numberOfInstallments;
    private Date startDate;
    private Date endDate;
    private BigDecimal interestRate;
    private Integer paymentFrequency;
    private Integer installmentStatus;
    private BigDecimal downPayment;
}

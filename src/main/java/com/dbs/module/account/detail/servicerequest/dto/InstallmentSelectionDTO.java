package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for installment selection from billing
 */
@Data
public class InstallmentSelectionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // Selected from existing billing installment
    private Integer installmentId;

    // Installment details
    private String installmentNumber;
    private Integer numberOfInstallments;
    private BigDecimal totalAmount;
    private BigDecimal downPayment;
    private BigDecimal interestRate;
    private Integer paymentFrequency;
}

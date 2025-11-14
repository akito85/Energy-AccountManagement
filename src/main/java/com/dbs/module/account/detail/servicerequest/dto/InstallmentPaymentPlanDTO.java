package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for Installment Payment Plan
 */
@Data
public class InstallmentPaymentPlanDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer paymentPlanId;
    private Integer installmentId;
    private String planName;
    private Integer planType;
    private String planTypeName;
    private String description;
    private String termsAndConditions;
    private Boolean autoDebit;
    private Integer paymentMethod;
    private String paymentMethodName;
}

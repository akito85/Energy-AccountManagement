package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * DTO for creating a new payment plan
 */
@Data
public class PaymentPlanCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Installment ID cannot be null")
    private Integer installmentId;

    @NotEmpty(message = "Plan name cannot be empty")
    private String planName;

    @NotNull(message = "Plan type cannot be null")
    private Integer planType;

    private String description;

    private String termsAndConditions;

    private Boolean autoDebit;

    private Integer paymentMethod;
}

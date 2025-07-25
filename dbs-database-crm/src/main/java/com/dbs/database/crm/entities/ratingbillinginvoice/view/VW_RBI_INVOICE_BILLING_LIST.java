package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_RBI_INVOICE_BILLING_LIST")
@SuppressWarnings("java:S101")
public class VW_RBI_INVOICE_BILLING_LIST {
    @Id
    @Column(name = "INVOICE_NUMBER", nullable = false)
    private String invoiceNumber;

    @Column(name = "TEMPLATE_NAME")
    private String templateName;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "BILLING_CYCLE")
    private String billingCycle;

    @Column(name = "BILLING_PERIOD")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date billingPeriod;

    @Column(name = "BILLING_PERIOD_NAME")
    private String billingPeriodName;

    @Column(name = "BILLING_CODE")
    private String billingCode;

    @Column(name = "CUSTOMER_NUMBER")
    private String customerNumber;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "ACCOUNT_NAME")
    private String accountName;

    @Column(name = "ACCOUNT_GROUP_TYPE")
    private String accountGroupType;

    @Column(name = "SERVICE_TYPE")
    private String serviceType;

    @Column(name = "SOR")
    private String sor;

    @Column(name = "COST_CENTER")
    private String costCenter;

    @Column(name = "ACCOUNT_SEGMENT")
    private String accountSegment;

    @Column(name = "METER_READING_CODE")
    private String meterReadingCode;

    @Column(name = "TOTAL_AMOUNT_IDR")
    private String totalAmountIdr;

    @Column(name = "TOTAL_AMOUNT_IDRREAL")
    private Double totalAmountIdrReal;

    @Column(name = "TOTAL_AMOUNT_USD")
    private String totalAmountUsd;

    @Column(name = "TOTAL_AMOUNT_USDREAL")
    private Double totalAmountUsdReal;

    @Column(name = "TAX_BASIC_IDR")
    private String taxBasicIdr;

    @Column(name = "TAX_BASIC_IDRREAL")
    private Double taxBasicIdrReal;

    @Column(name = "TAX_BASIC_USD")
    private String taxBasicUsd;

    @Column(name = "TAX_BASIC_USDREAL")
    private Double taxBasicUsdReal;

    @Column(name = "TAX_BASIC_EQV_IDR")
    private String taxBasicEqvIdr;

    @Column(name = "TAX_BASIC_EQV_IDRREAL")
    private Double taxBasicEqvIdrReal;

    @Column(name = "VAT_IDR")
    private String vatIdr;

    @Column(name = "VAT_IDRREAL")
    private Double vatIdrReal;

    @Column(name = "VAT_USD")
    private String vatUsd;

    @Column(name = "VAT_USDREAL")
    private Double vatUsdReal;

    @Column(name = "VAT_EQV_IDR")
    private String vatEqvIdr;

    @Column(name = "VAT_EQV_IDRREAL")
    private Double vatEqvIdrReal;

    @Column(name = "WITHHOLDING_TAX")
    private String withHoldingTax;

    @Column(name = "WITHHOLDING_TAXREAL")
    private Double withHoldingTaxReal;

    @Column(name = "TAX_RATE_TYPE")
    private String taxRateType;

    @Column(name = "TAX_RATE")
    private String taxRate;

    @Column(name = "TAX_RATEREAL")
    private Double taxRateReal;

    @Column(name = "TAX_RATE_DATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date taxRateDate;

    @Column(name = "DISCOUNT_AMOUNT_IDR")
    private String discountAmountIdr;

    @Column(name = "DISCOUNT_AMOUNT_IDRREAL")
    private Double discountAmountIdrReal;

    @Column(name = "DISCOUNT_AMOUNT_USD")
    private String discountAmountUsd;

    @Column(name = "DISCOUNT_AMOUNT_USDREAL")
    private Double discountAmountUsdReal;

    @Column(name = "TERM_OF_PAYMENT")
    private String termOfPayment;

    @Column(name = "TRANSACTION_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date transactionDate;

    @Column(name = "INVOICE_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date invoiceDate;

    @Column(name = "DUE_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date dueDate;

    @Column(name = "RATE_TYPE")
    private String rateType;

    @Column(name = "RATE")
    private String rate;

    @Column(name = "RATEREAL")
    private Double rateReal;

    @Column(name = "RATE_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date rateDate;

    @Column(name = "TOTAL_AMOUNT_EQV_IDR")
    private String totalAmountEqvIdr;

    @Column(name = "TOTAL_AMOUNT_EQV_IDRREAL")
    private Double totalAmountEqvIdrReal;

    @Column(name = "TOTAL_AMOUNT_EQV_USD")
    private String totalAmountEqvUsd;

    @Column(name = "TOTAL_AMOUNT_EQV_USDREAL")
    private Double totalAmountEqvUsdReal;

    @Column(name = "CREATED_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    @JsonFormat(pattern = Constant.FORMAT_DATETIME_VIEW, timezone = Constant.TIMEZONE)
    private Date createdDate;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column (name = "COST_CENTER_ID")
    private Integer ccId;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "AMOUNT_IDR")
    private String amountIdr;

    @Column(name = "AMOUNT_IDRREAL")
    private Double amountIdrReal;

    @Column(name = "AMOUNT_USD")
    private String amountUsd;

    @Column(name = "AMOUNT_USDREAL")
    private Double amountUsdReal;
}

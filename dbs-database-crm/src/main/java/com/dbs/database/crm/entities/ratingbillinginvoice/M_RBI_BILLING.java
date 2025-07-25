package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "M_RBI_BILLING")
public class M_RBI_BILLING implements Serializable {
    @Id
    @Column(name = "BILLING_CODE", nullable = false)
    private String billingCode;

    @Column(name = "RATING_CODE")
    private String ratingCode;

    @Column(name = "CALCULATION_CODE")
    private String calculationCode;

    @Column(name = "LINE_NUMBER")
    private Integer lineNumber;

    @Column(name = "BILLING_CYCLE")
    private String billingCycle;

    @Column(name = "BILLING_PERIOD")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date billingPeriod;

    @Column(name = "SA_NUMBER")
    private String saNumber;

    @Column(name = "SA_ID")
    private Integer saId;

    @Column(name = "PRODUCT")
    private String product;

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

    @Column(name = "COST_CENTER_CODE")
    private String costCenterCode;

    @Column(name = "COST_CENTER_ID")
    private Integer costCenterId;

    @Column(name = "ACCOUNT_SEGMENT")
    private String accountSegment;

    @Column(name = "METER_READING_CODE")
    private String meterReadingCode;

    @Column(name = "UOM")
    private String uom;

    @Column(name = "MIN_CONTRACT")
    private Double minContract;

    @Column(name = "MAX_CONTRACT")
    private Double maxContract;

    @Column(name = "TIME_UNIT_CONTRACT")
    private String timeUnitContract;

    @Column(name = "TOTAL_USAGE")
    private Double totalUsage;

    @Column(name = "TOTAL_USAGE_CONV_M3")
    private Double totalUsageConvM3;

    @Column(name = "TOTAL_USAGE_CONV_MMBTU")
    private Double totalUsageConvMmbtu;

    @Column(name = "BASIC_BILLING_IDR")
    private Double basicBillingIdr;

    @Column(name = "BASIC_BILLING_USD")
    private Double basicBillingUsd;

    @Column(name = "TOTAL_BASIC_BILL_EQV_IDR")
    private Double totalBasicBillEqvIdr;

    @Column(name = "TOTAL_BASIC_BILL_EQV_USD")
    private Double totalBasicBillEqvUsd;

    @Column(name = "OTHER_BILL_IDR")
    private Double otherBillIdr;

    @Column(name = "OTHER_BILL_USD")
    private Double otherBillUsd;

    @Column(name = "TOTAL_OTHER_BILL_EQV_IDR")
    private Double totalOtherBillEqvIdr;

    @Column(name = "TOTAL_OTHER_BILL_EQV_USD")
    private Double totalOtherBillEqvUsd;

    @Column(name = "DISCOUNT_AMOUNT_IDR")
    private Double discountAmountIdr;

    @Column(name = "DISCOUNT_AMOUNT_USD")
    private Double discountAmountUsd;

    @Column(name = "TAX_BASIC_IDR")
    private Double taxBasicIdr;

    @Column(name = "TAX_BASIC_USD")
    private Double taxBasicUsd;

    @Column(name = "TAX_BASIC_EQV_IDR")
    private Double taxBasicEqvIdr;

    @Column(name = "VAT_IDR")
    private Double vatIdr;

    @Column(name = "VAT_USD")
    private Double vatUsd;

    @Column(name = "VAT_EQV_IDR")
    private Double vatEqvIdr;

    @Column(name = "WITHHOLDING_TAX")
    private Double withHoldingTax;

    @Column(name = "PREV_WITHHOLDING_TAX")
    private Double prevWithHoldingTax;

    @Column(name = "TAX_RATE_TYPE")
    private String taxRateType;

    @Column(name = "TAX_RATE")
    private Double taxRate;

    @Column(name = "TAX_RATE_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date taxRateDate;

    @Column(name = "TOTAL_AMOUNT_IDR")
    private Double totalAmountIdr;

    @Column(name = "TOTAL_AMOUNT_USD")
    private Double totalAmountUsd;

    @Column(name = "TOTAL_AMOUNT_EQV_IDR")
    private Double totalAmountEqvIdr;

    @Column(name = "TOTAL_AMOUNT_EQV_USD")
    private Double totalAmountEqvUsd;

    @Column(name = "RATE_TYPE")
    private String rateType;

    @Column(name = "RATE")
    private Double rate;

    @Column(name = "RATE_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date rateDate;

    @Column(name = "TRANSACTION_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date transactionDate;

    @Column(name = "ACCOUNTING_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date accountDate;

    @Column(name = "INVOICE_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date invoiceDate;

    @Column(name = "DUE_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dueDate;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "PAYMENT_STATUS")
    private String paymentStatus;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "CREATED_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdDate;

    @Column(name = "UPDATED_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date updatedDate;

    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;

    @Column(name = "TERM_OF_PAYMENT")
    private String termOfPayment;

    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;

    @Column(name = "AMOUNT_IDR")
    private Double amountIdr;

    @Column(name = "AMOUNT_USD")
    private Double amountUsd;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "IS_GENERATE")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isGenerate;

}

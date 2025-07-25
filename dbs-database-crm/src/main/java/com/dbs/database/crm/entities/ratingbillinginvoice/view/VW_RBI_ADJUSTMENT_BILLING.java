package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_RBI_ADJUSTMENT_BILLING")
public class VW_RBI_ADJUSTMENT_BILLING {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ADJUSTMENT_NUMBER")
    private String adjustmentNumber;

    @Column(name = "ADJUSTMENT_TYPE")
    private Integer adjustmentType;

    @Column(name = "ADJUSTMENT_TYPE_NAME")
    private String adjustmentTypeName;

    @Column(name = "BILLING_CYCLE")
    private Integer billingCycle;

    @Column(name = "BILLING_CYCLE_NAME")
    private String billingCycleName;

    @Column(name = "BILLING_PERIOD")
    private Integer billingPeriod;

    @Column(name = "BILLING_PERIOD_NAME")
    private String billingPeriodName;

    @Column(name = "CUSTOMER_NUMBER")
    private String customerNumber;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name="ACCOUNT_ID")
    private Integer accountId;

    @Column(name = "ACCOUNT_NAME")
    private String accountName;

    @Column(name = "ACCOUNT_GROUP_TYPE")
    private String accountGroupType;

    @Column(name = "SOR")
    private String sor;

    @Column(name = "SERVICE_AGREEMENT_CLASS")
    private String serviceAgreementClass;

    @Column(name = "COST_CENTER")
    private String costCenter;

    @Column(name = "ACCOUNT_SEGMENT")
    private String accountSegment;

    @Column(name = "METER_READING_CODE")
    private String meterReadingCode;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "TOTAL_ADJUSTMENT_AMOUNT")
    private String totalAdjustmentAmount;


    @Column(name = "TOTAL_ADJUSTMENT_AMOUNT_REAL")
    private Double totalAdjustmentAmountReal;

    @Column(name = "TOTAL_ADJUSTMENT_AMOUNT_IDR")
    private String totalAdjustmentAmountIdr;

    @Column(name = "TOTAL_ADJUSTMENT_AMOUNT_IDR_REAL")
    private Double totalAdjustmentAmountIdrReal;

    @Column(name = "TOTAL_ADJUSTMENT_AMOUNT_USD")
    private String totalAdjustmentAmountUsd;

    @Column(name = "TOTAL_ADJUSTMENT_AMOUNT_USD_REAL")
    private Double totalAdjustmentAmountUsdReal;

    @Column(name = "TOTAL_ADJUSTMENT_AMOUNT_EQV_IDR")
    private String totalAmountEqvIdr;

    @Column(name = "TOTAL_ADJUSTMENT_AMOUNT_EQV_IDR_REAL")
    private Double totalAmountEqvIdrReal;

    @Column(name = "TOTAL_ADJUSTMENT_AMOUNT_EQV_USD")
    private String totalAmountEqvUsd;

    @Column(name = "TOTAL_ADJUSTMENT_AMOUNT_EQV_USD_REAL")
    private Double totalAmountEqvUsdReal;

    @Column(name = "REFERENCE_INVOICE_NUMBER")
    private String referenceInvoiceNumber;

    @Column(name = "TERMS_OF_PAYMENT")
    private String termsOfPayment;

    @Column(name = "TRANSACTION_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date transactionDate;

    @Column(name = "ACCOUNTING_DATE")
    private String accountingDate;

    @Column(name = "DOCUMENT_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date documentDate;

    @Column(name = "ADJUSTMENT_REASON")
    private Integer adjustmentReason;

    @Column(name = "ADJUSTMENT_REASON_NAME")
    private String adjustmentReasonName;

    @Column(name = "REMARK")
    private String remark;
    @Column(name = "APPHIER_ID")
    private Integer appHierId;

    @Column(name = "RATE_TYPE")
    private String rateType;

    @Column(name = "RATE")
    private String rate;

    @Column(name = "RATE_REAL")
    private Double rateReal;

    //    @JsonSerialize(using = CustomddMMMyyyyhhmmssSer.class)
//    @JsonDeserialize(using = CustomddMMMyyyyhhmmssDes.class)
    @Column(name = "RATE_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date rateDate;

    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "IS_DELETED")
    @Convert(converter = BooleanToYNStringConverter.class)
    private Boolean isDeleted;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "CCID")
    private Integer ccId;

//    @JsonProperty(value="tAdjustmentBillingDetail")
//    private List<T_ADJUSTMENT_BILLING_DETAIL> tAdjustmentBillingDetail;
}

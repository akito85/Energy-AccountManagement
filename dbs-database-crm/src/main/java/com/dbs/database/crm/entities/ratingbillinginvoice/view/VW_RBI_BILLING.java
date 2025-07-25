package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "VW_RBI_BILLING")
public class VW_RBI_BILLING {
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

    @Column(name = "BILLING_CYCLE_ID")
    private Integer billingCycleId;

    @Column(name = "BILLING_PERIOD")
    private String billingPeriod;

    @Column(name = "BILLING_PERIOD_REAL")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date billingPeriodReal;

    @Column(name = "BILLING_PERIOD_MINUS")
//    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private String billingPeriodMinus;

    @Column(name = "BILLING_PERIOD_ID")
    private Integer billingPeriodId;

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
    private Integer ccId;

    @Column(name = "ACCOUNT_SEGMENT")
    private String accountSegment;

    @Column(name = "METER_READING_CODE")
    private String meterReadingCode;

    @Column(name = "UOM")
    private String uom;

    @Column(name = "MIN_CONTRACT")
    private String minContract;
    
    @Column(name = "MIN_CONTRACT_REAL")
    private Double minContractReal;

    @Column(name = "MAX_CONTRACT")
    private String maxContract;
    
    @Column(name = "MAX_CONTRACT_REAL")
    private Double maxContractReal;

    @Column(name = "TIME_UNIT_CONTRACT")
    private String timeUnitContract;

    @Column(name = "TOTAL_USAGE")
    private String totalUsage;
    
     @Column(name = "TOTAL_USAGE_REAL")
    private Double totalUsageReal;

    @Column(name = "TOTAL_USAGE_CONV_M3")
    private String totalUsageConvM3;
    
    @Column(name = "TOTAL_USAGE_CONV_M3_REAL")
    private Double totalUsageConvM3Real;

    @Column(name = "TOTAL_USAGE_CONV_MMBTU")
    private String totalUsageConvMmbtu;
    
    @Column(name = "TOTAL_USAGE_CONV_MMBTU_REAL")
    private Double totalUsageConvMmbtuReal;

    @Column(name = "BASIC_BILLING_IDR")
    private String basicBillingIdr;
    
    @Column(name = "BASIC_BILLING_IDR_REAL")
    private Double basicBillingIdrReal;

    @Column(name = "BASIC_BILLING_USD")
    private String basicBillingUsd;
    @Column(name = "BASIC_BILLING_USD_REAL")
    private Double basicBillingUsdReal;

    @Column(name = "TOTAL_BASIC_BILL_EQV_IDR")
    private String totalBasicBillEqvIdr;
    @Column(name = "TOTAL_BASIC_BILL_EQV_IDR_REAL")
    private Double totalBasicBillEqvIdrReal;

    @Column(name = "TOTAL_BASIC_BILL_EQV_USD")
    private String totalBasicBillEqvUsd;
    @Column(name = "TOTAL_BASIC_BILL_EQV_USD_REAL")
    private Double totalBasicBillEqvUsdReal;

    @Column(name = "OTHER_BILL_IDR")
    private String otherBillIdr;
    @Column(name = "OTHER_BILL_IDR_REAL")
    private Double otherBillIdrReal;

    @Column(name = "OTHER_BILL_USD")
    private String otherBillUsd;
    @Column(name = "OTHER_BILL_USD_REAL")
    private Double otherBillUsdReal;

    @Column(name = "TOTAL_OTHER_BILL_EQV_IDR")
    private String totalOtherBillEqvIdr;
    @Column(name = "TOTAL_OTHER_BILL_EQV_IDR_REAL")
    private Double totalOtherBillEqvIdrReal;

    @Column(name = "TOTAL_OTHER_BILL_EQV_USD")
    private String totalOtherBillEqvUsd;
    @Column(name = "TOTAL_OTHER_BILL_EQV_USD_REAL")
    private Double totalOtherBillEqvUsdReal;

    @Column(name = "DISCOUNT_AMOUNT_IDR")
    private String discountAmountIdr;
    @Column(name = "DISCOUNT_AMOUNT_IDR_REAL")
    private Double discountAmountIdrReal;

    @Column(name = "DISCOUNT_AMOUNT_USD")
    private String discountAmountUsd;
    @Column(name = "DISCOUNT_AMOUNT_USD_REAL")
    private Double discountAmountUsdReal;

    @Column(name = "TAX_BASIC_IDR")
    private String taxBasicIdr;
    @Column(name = "TAX_BASIC_IDR_REAL")
    private Double taxBasicIdrReal;

    @Column(name = "TAX_BASIC_USD")
    private String taxBasicUsd;
    @Column(name = "TAX_BASIC_USD_REAL")
    private Double taxBasicUsdReal;

    @Column(name = "TAX_BASIC_EQV_IDR")
    private String taxBasicEqvIdr;
    @Column(name = "TAX_BASIC_EQV_IDR_REAL")
    private Double taxBasicEqvIdrReal;

    @Column(name = "VAT_IDR")
    private String vatIdr;
    @Column(name = "VAT_IDR_REAL")
    private Double vatIdrReal;

    @Column(name = "VAT_USD")
    private String vatUsd;
    @Column(name = "VAT_USD_REAL")
    private Double vatUsdReal;

    @Column(name = "VAT_EQV_IDR")
    private String vatEqvIdr;
    @Column(name = "VAT_EQV_IDR_REAL")
    private Double vatEqvIdrReal;

    @Column(name = "WITHHOLDING_TAX")
    private String withHoldingTax;
    
    @Column(name = "WITHHOLDING_TAX_REAL")
    private Double withHoldingTaxReal;

    @Column(name = "PREV_WITHHOLDING_TAX")
    private String prevWithHoldingTax;
    
     @Column(name = "PREV_WITHHOLDING_TAX_REAL")
    private Integer prevWithHoldingTaxReal;

    @Column(name = "TAX_RATE_TYPE")
    private String taxRateType;

    @Column(name = "TAX_RATE")
    private String taxRate;
    
    @Column(name = "TAX_RATE_REAL")
    private Double taxRateReal;

    @Column(name = "TAX_RATE_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date taxRateDate;

    @Column(name = "TOTAL_AMOUNT_IDR")
    private String totalAmountIdr;
    @Column(name = "TOTAL_AMOUNT_IDR_REAL")
    private Double totalAmountIdrReal;

    @Column(name = "TOTAL_AMOUNT_USD")
    private String totalAmountUsd;
    @Column(name = "TOTAL_AMOUNT_USD_REAL")
    private Double totalAmountUsdReal;

    @Column(name = "TOTAL_AMOUNT_EQV_IDR")
    private String totalAmountEqvIdr;
    @Column(name = "TOTAL_AMOUNT_EQV_IDR_REAL")
    private Double totalAmountEqvIdrReal;

    @Column(name = "TOTAL_AMOUNT_EQV_USD")
    private String totalAmountEqvUsd;
    @Column(name = "TOTAL_AMOUNT_EQV_USD_REAL")
    private String totalAmountEqvUsdReal;

    @Column(name = "RATE_TYPE")
    private String rateType;

    @Column(name = "RATE")
    private String rate;
    
    @Column(name = "RATE_REAL")
    private Double rateReal;

    @Column(name = "RATE_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date rateDate;

    @Column(name = "TRANSACTION_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date transactionDate;

    @Column(name = "ACCOUNTING_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date accountDate;

    @Column(name = "INVOICE_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date invoiceDate;

    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;

    @Column(name = "DUE_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
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
    private Date createdDate;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;

    @Column(name = "IS_GENERATE")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isGenerate;
}

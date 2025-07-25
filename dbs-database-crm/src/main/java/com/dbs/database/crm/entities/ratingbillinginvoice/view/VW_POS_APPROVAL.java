package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "VW_POS_APPROVAL")
public class VW_POS_APPROVAL extends DefaultBaseEntities implements Serializable {
    
    @Id
    @Column(name="ID", nullable = false, updatable = false)
    private Integer id;
    @Column(name="T_APP_ID")
    private Integer tAppId;
    @Column(name="POS_NUMBER")
    private String posNumber;
    @Column(name="BILLING_CYCLE", length = 50)
    private String billingCycle;
    @Column(name="BILLING_CYCLE_NAME")
    private String billingCycleName;
    @Column(name="BILLING_PERIOD", length = 50)
    private String billingPeriod;
    @Column(name="BILLING_PERIOD_NAME")
    private String billingPeriodName;
    @Column(name="CUSTOMER_NUMBER", length = 15)
    private String customerNumber;
    @Column(name="CUSTOMER_NAME", length = 50)
    private String customerName;
    @Column(name="ACCOUNT_NUMBER")
    private String accountNumber;
    @Column(name="ACCOUNT_NAME")
    private String accountName;
    @Column(name="ACCOUNT_GROUP_TYPE")
    private String accountGroupType;
    @Column(name="SERVICE_TYPE")
    private String serviceType;
    @Column(name="SOR")
    private String sor;
    @Column(name="COST_CENTER")
    private String costcenter;
    @Column(name="ACCOUNT_SEGMENT")
    private String accountSegment;
    @Column(name="METER_READING_CODE")
    private String meterReadingCode;
    @Column(name="CURRENCY")
    private String currency;
    @Column(name="CURRENCY_NAME")
    private String currencyName;
    @Column(name="AMOUNT")
    private Double amount;
    @Column(name="AMOUNT_IDR")
    private Double amountIdr;
    @Column(name="AMOUNT_USD")
    private Double amountUsd;
    @Column(name="TAX_BASIS_IDR")
    private Double taxBasisIdr;
    @Column(name="TAX_BASIS_USD")
    private Double taxBasisUsd;
    @Column(name="TAX_BASIS_EQV_IDR")
    private Double taxBasisEqvIdr;
    @Column(name="TAX_BASIS_EQV_USD")
    private Double taxBasisEqvUsd;
    @Column(name="VAT_IDR")
    private Double vatIdr;
    @Column(name="VAT_USD")
    private Double vatUsd;
    @Column(name="VAT_EQV_IDR")
    private Double vatEqvIdr;
    @Column(name="WITHHOLDING_TAX")
    private Double withholdingTax;
    @Column(name="TAX_RATE")
    private Double taxRate;
    @Column(name="TAX_RATE_DATE")
    private String taxRateDate;
    @Column(name="DISCOUNT_AMOUNT")
    private Double discountAmount;
    @Column(name="DISCOUNT_AMOUNT_IDR")
    private Double discountAmountIdr;
    @Column(name="DISCOUNT_AMOUNT_USD")
    private Double discountAmountUsd;
    @Column(name="TOTAL_AMOUNT")
    private Double totalAmount;
    @Column(name="TOTAL_AMOUNT_IDR")
    private Double totalAmountIdr;
    @Column(name="TOTAL_AMOUNT_USD")
    private Double totalAmountUsd;
    @Column(name="TERMS_OF_PAYMENT")
    private String termsOfPayment;
    @Column(name="TRANSACTION_DATE")
    private String transactionDate;
    @Column(name="PREFIX_CODE")
    private String prefixCode;
    @Column(name="REMARK")
    private String remark;
    @Column(name = "APPHIER_ID")
    private Integer appHierId;
    @Convert(converter=BooleanToYNStringConverter.class)
    @Column(name="IS_DELETED")
    private Boolean isDeleted;
    
    @Column(name = "STATUS_PAYMENT", length = 15)
    private String statusPayment;
    @Column(name = "STATUS", length = 20)
    private String status;
    @Column(name = "STATUS_APPROVAL", length = 20)
    private String statusApproval;
}

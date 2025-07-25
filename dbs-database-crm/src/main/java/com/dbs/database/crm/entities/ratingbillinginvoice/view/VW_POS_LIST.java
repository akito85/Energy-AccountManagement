package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.common.base.utils.CustomddMMMyyyyDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_POS_LIST")
public class VW_POS_LIST extends DefaultBaseEntities implements Serializable {
    
    @Id
    @Column(name="ID", nullable = false, updatable = false)
    private Integer id;
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
    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;
    @Column(name = "PROFORMA_INVOICE_NUMBER")
    private String proformaInvoice;
    @Column(name = "PROFORMA_INVOICE_DATE")
    private Date proformaInvoiceDate;
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
    private String amount;
    @Column(name="AMOUNT_REAL")
    private Double amountReal;
    
    @Column(name="AMOUNT_IDR")
    private String amountIdr;
    @Column(name="AMOUNT_IDR_REAL")
    private String amountIdrReal;
    
    @Column(name="AMOUNT_USD")
    private String amountUsd;
    @Column(name="AMOUNT_USD_REAL")
    private Double amountUsdReal;
    
    @Column(name="TAX_BASIS_IDR")
    private String taxBasisIdr;
    @Column(name="TAX_BASIS_IDR_REAL")
    private Double taxBasisIdrReal;
    
    @Column(name="TAX_BASIS_USD")
    private String taxBasisUsd;
    @Column(name="TAX_BASIS_USD_REAL")
    private Double taxBasisUsdReal;
    
    @Column(name="TAX_BASIS_EQV_IDR")
    private String taxBasisEqvIdr;
    @Column(name="TAX_BASIS_EQV_IDR_REAL")
    private Double taxBasisEqvIdrReal;
    
    @Column(name="TAX_BASIS_EQV_USD")
    private String taxBasisEqvUsd;
    @Column(name="TAX_BASIS_EQV_USD_REAL")
    private Double taxBasisEqvUsdReal;
    
    @Column(name="VAT")
    private String vat;
    @Column(name="VAT_REAL")
    private Double vatReal;
    
    @Column(name="VAT_IDR")
    private String vatIdr;
    @Column(name="VAT_IDR_REAL")
    private Double vatIdrReal;
    
    @Column(name="VAT_USD")
    private String vatUsd;
    @Column(name="VAT_USD_REAL")
    private Double vatUsdReal;
    
    @Column(name="VAT_EQV_IDR")
    private String vatEqvIdr;
    @Column(name="VAT_EQV_IDR_REAL")
    private Double vatEqvIdrReal;
    
    @Column(name="WITHHOLDING_TAX")
    private String withholdingTax;
    @Column(name="WITHHOLDING_TAX_REAL")
    private Double withholdingTaxReal;
    
    @Column(name="TAX_RATE")
    private String taxRate;
    @Column(name="TAX_RATE_REAL")
    private Double taxRateReal;
    
    @Column(name="TAX_RATE_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date taxRateDate;
    
    @Column(name="DISCOUNT_AMOUNT")
    private String discountAmount;
    @Column(name="DISCOUNT_AMOUNT_REAL")
    private Double discountAmountReal;
    
    @Column(name="DISCOUNT_AMOUNT_IDR")
    private String discountAmountIdr;
    @Column(name="DISCOUNT_AMOUNT_IDR_REAL")
    private Double discountAmountIdrReal;
    
    @Column(name="DISCOUNT_AMOUNT_USD")
    private String discountAmountUsd;
    @Column(name="DISCOUNT_AMOUNT_USD_REAL")
    private Double discountAmountUsdReal;
    
    @Column(name="TOTAL_AMOUNT")
    private String totalAmount;
    @Column(name="TOTAL_AMOUNT_REAL")
    private Double totalAmountReal;
    
    @Column(name="TOTAL_AMOUNT_IDR")
    private String totalAmountIdr;
    @Column(name="TOTAL_AMOUNT_IDR_REAL")
    private Double totalAmountIdrReal;
    
    @Column(name="TOTAL_AMOUNT_USD")
    private String totalAmountUsd;
    @Column(name="TOTAL_AMOUNT_USD_REAL")
    private Double totalAmountUsdReal;
    
    @Column(name="TERMS_OF_PAYMENT")
    private String termsOfPayment;
    @Column(name="TRANSACTION_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date transactionDate;
    @JsonDeserialize(using = CustomddMMMyyyyDeserializer.class)
    @Column(name="INVOICE_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date invoiceDate;
    @JsonDeserialize(using = CustomddMMMyyyyDeserializer.class)
    @Column(name="DUE_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dueDate;
    @Column(name="PREFIX_CODE")
    private String prefixCode;
    @Column(name="REMARK")
    private String remark;
    @Column(name = "APPHIER_ID")
    private Integer appHierId;
    @Convert(converter=BooleanToYNStringConverter.class)
    @Column(name="IS_DELETED")
    private Boolean isDeleted;
    
    @Column(name="TOTAL_AMOUNT_EQV_IDR")
    private String totalAmountEqvIdr;
    @Column(name="TOTAL_AMOUNT_EQV_IDR_REAL")
    private Double totalAmountEqvIdrReal;
    
    @Column(name="TOTAL_AMOUNT_EQV_USD")
    private String totalAmountEqvUsd;
    @Column(name="TOTAL_AMOUNT_EQV_USD_REAL")
    private Double totalAmountEqvUsdReal;
    
    @Column(name = "RATE_TYPE")
    private String rateType;
    
    @Column(name = "RATE")
    private String rate;
    @Column(name = "RATE_REAL")
    private Double rateReal;
    
    @Column(name="RATE_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date rateDate;
    @Column(name = "TAX_RATE_TYPE")
    private String taxRateType;
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @Column(name = "CCID")
    private Integer ccId;
    
    @Column(name = "STATUS_PAYMENT", length = 15)
    private String statusPayment;
    @Column(name = "STATUS", length = 20)
    private String status;
    @Column(name = "STATUS_APPROVAL", length = 20)
    private String statusApproval;
    
    @Column(name="ACCOUNTING_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date accountingDate;
}

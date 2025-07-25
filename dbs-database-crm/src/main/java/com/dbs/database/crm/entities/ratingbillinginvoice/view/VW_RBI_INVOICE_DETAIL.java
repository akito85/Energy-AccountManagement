package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import lombok.Data;

import javax.persistence.*;


import com.dbs.database.crm.entities.ratingbillinginvoice.T_RBI_BILLING_ITEM;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "VW_RBI_INVOICE_DETAIL")
public class VW_RBI_INVOICE_DETAIL implements Serializable {
    @Id
    @Column(name="INVOICE_NUMBER")
    private String invoiceNumber;
    
    @Column(name="BILLING_CODE")
    private String billingCode;
    
    @Column(name="INVOICE_DATE")
    private Date invoiceDate;
    
    @Column(name="TERM_OF_PAYMENT")
    private String termsOfPayment;
    
    @Column(name="BILLING_CYCLE")
    private String billingCycle;
    
    @Column(name="BILLING_PERIOD")
    private String billingPeriod;
    
    @Column(name="TOTAL_AMOUNT_IDR")
    private BigDecimal totalAmountIdr;
    
    @Column(name="TOTAL_AMOUNT_USD")
    private BigDecimal totalAmountUsd;
    
    @Column(name="TOTAL_AMOUNT_EQV_IDR")
    private BigDecimal totalAmountEqvIdr;
    
    @Column(name="TOTAL_AMOUNT_EQV_USD")
    private BigDecimal totalAmountEqvUsd;
    
    @Column(name="CURRENCY")
    private String currency;
    
    @Column(name="WITHHOLDING_TAX")
    private BigDecimal withholdingTax;
    
    @Column(name="TAX_BASIC_IDR")
    private BigDecimal taxBasicIdr;
    
    @Column(name="TAX_BASIC_USD")
    private BigDecimal taxBasicUsd;
    
    @Column(name="VAT_IDR")
    private BigDecimal vatIdr;
    
    @Column(name="VAT_USD")
    private BigDecimal vatUsd;
    
    @Column(name="RATE")
    private BigDecimal rate;
    
    @Column(name="RATE_TYPE")
    private String rateType;
    
    @Column(name="RATE_DATE")
    private Date rateDate;
    
    @Column(name="STATUS")
    private String status;
//
//    @OneToMany(mappedBy = "vwRbiInvoiceDetail",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JsonProperty(value="tInvoiceDetail")
//   	private List<T_RBI_BILLING_ITEM> tInvoiceDetail;
   	
}

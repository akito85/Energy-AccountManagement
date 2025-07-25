package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.common.base.utils.CustomddMMMyyyyDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "M_RBI_POS")
public class M_RBI_POS extends DefaultBaseEntities implements Serializable {
    
    @Id
    @Column(name="ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_RBI_POS_SEQ")
    @SequenceGenerator(sequenceName = "M_RBI_POS_SEQ", allocationSize = 1, name = "M_RBI_POS_SEQ")
    private Integer id;
    @Column(name="POS_NUMBER", nullable = false, updatable = false, length = 15)
    private String posNumber;
    @Column(name="BILLING_CYCLE", length = 50)
    private String billingCycle;
    @Column(name="BILLING_PERIOD", length = 50)
    private String billingPeriod;
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
    @Column(name="VAT")
    private Double vat;
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
    @JsonDeserialize(using = CustomddMMMyyyyDeserializer.class)
    @Column(name="TAX_RATE_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date taxRateDate;
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
    @Column(name="TOTAL_AMOUNT_EQV_IDR")
    private Double totalAmountEqvIdr;
    @Column(name="TOTAL_AMOUNT_EQV_USD")
    private Double totalAmountEqvUsd;
    @Column(name = "RATE_TYPE")
    private String rateType;
    @Column(name = "RATE")
    private Double rate;
    @JsonDeserialize(using = CustomddMMMyyyyDeserializer.class)
    @Column(name="RATE_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date rateDate;
    @Column(name = "TAX_RATE_TYPE")
    private String taxRateType;
    @Column(name="TERMS_OF_PAYMENT")
    private String termsOfPayment;
    @JsonDeserialize(using = CustomddMMMyyyyDeserializer.class)
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
    
    @Column(name = "STATUS_PAYMENT", length = 15)
    private String statusPayment;
    @Column(name = "STATUS", length = 20)
    private String status;
    @Column(name = "STATUS_APPROVAL", length = 20)
    private String statusApproval;
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @Column(name = "CCID")
    private Integer ccId;
    
    @JsonProperty("mrbiPosDetails")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "POS_NUMBER",referencedColumnName = "POS_NUMBER")
    private List<R_RBI_POS_DETAIL> mrbiPosDetails;
}

package com.dbs.database.crm.entities.ratingbillinginvoice;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Entity
@Data
@Table(name = "T_ADJUSTMENT_BILLING")
public class T_ADJUSTMENT_BILLING implements Serializable{
	@Id
	@SequenceGenerator(name="T_ADJUSTMENT_BILLING_SEQ", sequenceName="T_ADJUSTMENT_BILLING_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "T_ADJUSTMENT_BILLING_SEQ" )
	@Column(name="ID")
	private Integer id;
	
	@Column(name="ADJUSTMENT_NUMBER")
	private String adjustmentNumber; 
	
	@Column(name="ADJUSTMENT_TYPE")
	private Integer adjustmentType;
	
	@Column(name="BILLING_CYCLE")
	private Integer billingCycle;
	
	@Column(name="BILLING_PERIOD")
	private Integer billingPeriod;
	
	@Column(name="CUSTOMER_NUMBER")
	private String customerNumber; 
	
	@Column(name="CUSTOMER_NAME")
	private String customerName; 
	
	@Column(name="ACCOUNT_NUMBER")
	private String accountNumber;

	@Column(name="ACCOUNT_ID")
	private Integer accountId;
	
	@Column(name="ACCOUNT_NAME")
	private String accountName; 
	
	@Column(name="ACCOUNT_GROUP_TYPE")
	private String accountGroupType;
	
	@Column(name="SOR")
	private String sor; 
	
	@Column(name="COST_CENTER")
	private String costCenter; 
	
	@Column(name="ACCOUNT_SEGMENT")
	private String accountSegment; 
	
	@Column(name="METER_READING_CODE")
	private String meterReadingCode; 
	
	@Column(name="CURRENCY")
	private String currency; 
	
	@Column(name="TOTAL_ADJUSTMENT_AMOUNT")
	private Double totalAdjustmentAmount;
	
	@Column(name="TOTAL_ADJUSTMENT_AMOUNT_IDR")
	private Double totalAdjustmentAmountIdr;
	
	@Column(name="TOTAL_ADJUSTMENT_AMOUNT_USD")
	private Double totalAdjustmentAmountUsd;
	
	@Column(name="REFERENCE_INVOICE_NUMBER")
	private String referenceInvoiceNumber;
	
	@Column(name="TERMS_OF_PAYMENT")
	private String termsOfPayment;

	@Column(name="SERVICE_AGREEMENT_CLASS")
	private String serviceAgreementClass;
	
	@Column(name="TRANSACTION_DATE")
	private Date transactionDate;
	
	@Column(name="ACCOUNTING_DATE")
	private Date accountingDate;
	
	@Column(name="DOCUMENT_DATE")
	private Date documentDate;
	
	@Column(name="ADJUSTMENT_REASON")
	private Integer adjustmentReason;
	
	@Column(name="REMARK")
	private String remark; 
	
	@Column(name="RATE_TYPE")
	private String rateType;
	
	@Column(name="RATE")
	private Double rate;
	
	@Column(name="RATE_DATE")
	private Date rateDate; 
	
	@Column(name="STATUS")
	private String status; 
	
	@Column(name="STATUS_APPROVAL")
	private String statusApproval;   
	
	@Column(name="CREATED_BY")
	private String createdBy;

//	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Column(name="UPDATED_DATE")
	private Date updatedDate;
	
    @Column(name = "IS_DELETED")
	@Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isDeleted;

	@Column(name = "PREFIX_CODE")
	private String prefixCode;

	@Column(name = "ENTITY_ID")
	private Integer entityId;
	@Column(name = "APPHIER_ID")
	private Integer appHierId;

	@Column(name = "CCID")
	private Integer ccId;
    
	@JsonProperty(value="adjustmentBillingDetails")
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "ADJUSTMENT_ID", referencedColumnName = "ID")
	private List<T_ADJUSTMENT_BILLING_DETAIL> adjustmentBillingDetails;


}

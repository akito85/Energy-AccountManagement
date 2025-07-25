package com.dbs.database.crm.entities.ratingbillinginvoice;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "T_RBI_ADJUSTMENT_BILLING")
public class T_RBI_ADJUSTMENT_BILLING implements Serializable{
	@Id
	@Column(name="ID")
	private Integer id;
	@Column(name="ADJUSTMENT_NUMBER")
	private String adjustmentNumber; 
	@Column(name="ADJUSTMENT_TYPE")
	private String adjustmentType; 
	@Column(name="BILLING_CYCLE")
	private String billingCycle; 
	@Column(name="BILLING_PERIOD")
	private String billingPeriod; 
	@Column(name="CUSTOMER_NUMBER")
	private String customerNumber; 
	@Column(name="CUSTOMER_NAME")
	private String customerName; 
	@Column(name="ACCOUNT_NUMBER")
	private String accountNumber;
	@Column(name="ACCOUNT_NAME")
	private String accountName; 
	@Column(name="ACCOUNT_GROUP")
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
	private Integer totalAdjustmentAmount;
	@Column(name="TOTAL_ADJUSTMENT_AMOUNT_IDR")
	private Integer totalAdjustmentAmountIdr;
	@Column(name="TOTAL_ADJUSTMENT_AMOUNT_USD")
	private Integer totalAdjustmentAmountUsd;
	@Column(name="REFERENCE_INVOICE_NUMBER")
	private String referenceInvoiceNumber;
	@Column(name="TERMS_OF_PAYMENT")
	private String termsOfPayment;
	@Column(name="TRANSACTION_DATE")
	private Date transactionDate;
	@Column(name="ACCOUNTING_DATE")
	private Date accountingDate;
	@Column(name="DOCUMENT_DATE")
	private Date documentDate;
	@Column(name="ADJUSTMENT_REASON")
	private String adjustmentReason;
	@Column(name="REMARK")
	private String remark; 
	@Column(name="RATE_TYPE")
	private String rateType;
	@Column(name="RATE")
	private Integer rate; 
	@Column(name="RATE_DATE")
	private Date rateDate; 
	@Column(name="STATUS")
	private String status; 
	@Column(name="STATUS_APPROVAL")
	private String statusApproval;
	@Column(name="PREFIX_CODE")
	private String prefixCode;
}

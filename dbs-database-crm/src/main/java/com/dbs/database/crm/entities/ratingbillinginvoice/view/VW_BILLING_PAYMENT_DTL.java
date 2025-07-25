package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_BILLING_PAYMENT_DTL")
public class VW_BILLING_PAYMENT_DTL extends DefaultBaseEntities implements Serializable {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "BILLING_CODE")
    private String billingCode;
    @Column(name = "RECEIPT_CODE")
    private String receiptCode;
    @Column(name = "RECEIPT_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATETIME_VIEW, timezone = Constant.TIMEZONE)
    private Date receiptDate;
    @Column(name = "CURRENCY")
    private String currency;
    @Column(name = "AMOUNT")
    private String amount;
    @Column(name = "PAYMENT_TYPE")
    private String paymentType;
    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;
    @Column(name = "RECEIPT_NUMBER")
    private String receiptNumber;
    @Column(name = "RECEIPT_CHANNEL")
    private String receiptChannel;
    @Column(name = "BANK")
    private String bank;
    @Column(name = "COLLECTING_AGENT")
    private String collectingAgent;
    @Column(name = "DELIVERY_CHANNEL")
    private String deliveryChannel;
    @Column(name = "RATE_TYPE")
    private String rateType;
    @Column(name = "RATE_DATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date rateDate;
    @Column(name = "RATE_AMOUNT")
    private String rateAmount;
    @Column(name = "REFERENCE_NUMBER")
    private String referenceNumber;
    @Column(name = "BANK_STATEMENT_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATETIME_VIEW, timezone = Constant.TIMEZONE)
    private Date bankStatementDate;
    @Column(name = "CONVERTED_CURRENCY")
    private String convertedCurrency;
    @Column(name = "EQUIVALENT_AMOUNT")
    private String equivalentAmount;
    @Column(name = "APPLIED_AMOUNT")
    private String appliedAmount;
    @Column(name = "EQUIVALENT_APPLIED_AMOUNT")
    private String equivalentAppliedAmount;
    @Column(name = "UN_APPLIED_AMOUNT")
    private String unAppliedAmount;
    @Column(name = "EQUIVALENT_UN_APPLIED_AMOUNT")
    private String equivalentUnAppliedAmount;
    @Column(name = "REFUND_AMOUNT")
    private String refundAmount;
    @Column(name = "TRANSFER_AMOUNT")
    private String transferAmount;
    @Column(name = "AMOUNT_REAL")
    private BigDecimal amountReal;
    @Column(name = "EQUIVALENT_AMOUNT_REAL")
    private BigDecimal equivalentAmountReal;
}

package com.dbs.database.crm.entities.payment.view;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Table(name = "VW_PAY_RECEIPT")
public class VW_PAY_RECEIPT extends BaseEntities implements Serializable {

    private static final long serialVersionUID = -4859393615012416218L;

    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "RECEIPT_CODE")
    private String receiptCode;

    @Column(name = "COST_CENTER")
    private String costCenter;

    @Column(name = "CUSTOMER")
    private String customer;

    @Column(name = "ACCOUNT")
    private String account;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "AREA")
    private String area;

    @Column(name = "RECEIPT_NUMBER")
    private String receiptNumber;

    @Column(name = "RECEIPT_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATETIME_VIEW, timezone = Constant.TIMEZONE)
    private Date receiptDate;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "AMOUNT")
    private String amount;

    @Column(name = "AMOUNT_REAL")
    private BigDecimal amountReal;

    @Column(name = "PAYMENT_TYPE")
    private String paymentType;

    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;

    @Column(name = "RECEIPT_CHANNEL")
    private String receiptChannel;

    @Column(name = "IS_RECONCILED")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isReconciled;

    @Column(name = "BANK")
    private String bank;

    @Column(name = "COLLECTING_AGENT")
    private String collectingAgent;

    @Column(name = "DELIVERY_CHANNEL")
    private String deliveryChannel;

    @Column(name = "PAYMENT_CYCLE")
    private String paymentCycle;

    @Column(name = "PAYMENT_PERIOD")
    private String paymentPeriod;

    @Column(name = "RATE_TYPE")
    private String rateType;

    @Column(name = "RATE_DATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date rateDate;

    @Column(name = "RATE_AMOUNT")
    private String rateAmount;

    @Column(name = "RATE_AMOUNT_REAL")
    private BigDecimal rateAmountReal;

    @Column(name = "CONVERTED_CURRENCY")
    private String convertedCurrency;

    @Column(name = "EQUIVALENT_AMOUNT")
    private String equivalentAmount;

    @Column(name = "EQUIVALENT_AMOUNT_REAL")
    private BigDecimal equivalentAmountReal;

    @Column(name = "REF_NUMBER")
    private String refNumber;

    @Column(name = "BANK_STATEMENT_NAME")
    private String bankStatementName;

    @Column(name = "SOURCE")
    private String source;

    @Column(name = "BANK_STATEMENT_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATETIME_VIEW, timezone = Constant.TIMEZONE)
    private Date bankStatementDate;

    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;

    @Column(name = "APPLIED_AMOUNT")
    private String appliedAmount;

    @Column(name = "APPLIED_AMOUNT_REAL")
    private BigDecimal appliedAmountReal;

    @Column(name = "EQUIVALENT_APPLIED_AMOUNT")
    private String equivalentAppliedAmount;

    @Column(name = "EQUIVALENT_APPLIED_AMOUNT_REAL")
    private BigDecimal equivalentAppliedAmountReal;

    @Column(name = "UN_APPLIED_AMOUNT")
    private String unAppliedAmount;

    @Column(name = "UN_APPLIED_AMOUNT_REAL")
    private BigDecimal unAppliedAmountReal;

    @Column(name = "EQUIVALENT_UN_APPLIED_AMOUNT")
    private String equivalentUnAppliedAmount;

    @Column(name = "EQUIVALENT_UN_APPLIED_AMOUNT_REAL")
    private BigDecimal equivalentUnAppliedAmountReal;

    @Column(name = "REFUND_AMOUNT")
    private String refundAmount;

    @Column(name = "REFUND_AMOUNT_REAL")
    private BigDecimal refundAmountReal;

    @Column(name = "TRANSFER_AMOUNT")
    private String transferAmount;

    @Column(name = "TRANSFER_AMOUNT_REAL")
    private BigDecimal transferAmountReal;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SOR")
    private String sor;

    @Column(name = "ACCOUNT_TYPE")
    private String accountType;

    @Column(name = "ACCOUNT_GROUP")
    private String accountGroup;

    @Column(name = "ACCOUNT_SEGMENT")
    private String accountSegment;

    @Column(name = "PAYMENT_GATEWAY")
    private String paymentGateway;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_MISC")
    private Boolean isMisc;

    @Column(name = "APP_HIER_ID")
    private Integer appHierId;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @Column(name = "RECEIPT_TYPE")
    private String receiptType;
}

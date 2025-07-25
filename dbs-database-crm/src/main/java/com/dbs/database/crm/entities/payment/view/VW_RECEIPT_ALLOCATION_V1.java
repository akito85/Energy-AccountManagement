package com.dbs.database.crm.entities.payment.view;

import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Table(name = "VW_RECEIPT_ALLOCATION_V1")
public class VW_RECEIPT_ALLOCATION_V1 extends DefaultBaseEntities implements Serializable {

    private static final long serialVersionUID = -2753073885992956949L;

    @Id
    @Column(name = "ALLOCATION_ID", nullable = false)
    private Long id;

    @Column(name = "RECEIPT_ID")
    private Long receiptId;

    @Column(name = "ALLOCATION_NUMBER")
    private String allocationNumber;

    @Column(name = "BILLING_ITEM")
    private String billingItem;

    @Column(name = "ALLOCATION_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date allocationDate;

    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;

    @Column(name = "INVOICE_CURRENCY")
    private String invoiceCurrency;

    @Column(name = "BILLING_CYCLE")
    private String billingCycle;

    @Column(name = "BILLING_PERIOD")
    private String billingPeriod;

    @Column(name = "BILLING_ITEM_AMOUNT")
    private String billingItemAmount;

    @Column(name = "ALLOCATION_TYPE")
    private String allocationType;

    @Column(name = "ALLOCATION_AMOUNT")
    private String allocationAmount;

    @Column(name = "BILLING_ITEM_BALANCE")
    private String billingItemBalance;

    @Column(name = "ALLOCATION_STATUS")
    private String allocationStatus;

    @Column(name = "CONVERTED_CURRENCY")
    private String convertedCurrency;

    @Column(name = "EQUIVALENT_AMOUNT")
    private String equivalentAmount;

    @Column(name = "RATE_DATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date rateDate;

    @Column(name = "RATE_AMOUNT")
    private String rateAmount;

    @Column(name = "RATE_AMOUNT_REAL")
    private BigDecimal rateAmountReal;

    @Column(name = "RATE_TYPE")
    private String rateType;
}

package com.dbs.database.crm.entities.payment.view;

import com.dbs.common.base.entities.BaseEntities;
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
@Table(name = "VW_T_LATE_CHARGE")
public class VW_T_LATE_CHARGE extends BaseEntities implements Serializable {

    private static final long serialVersionUID = 406015742186150694L;

    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;

    @Column(name = "ACCOUNT")
    private String account;

    @Column(name = "AREA_CODE")
    private String areaCode;

    @Column(name = "AREA_NAME")
    private String areaName;

    @Column(name = "CUSTOMER_NUMBER")
    private String customerNumber;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "ACCOUNT_SEGMENT")
    private String accountSegment;

    @Column(name = "ACCOUNT_GROUP")
    private String accountGroup;

    @Column(name = "METER_READING_ROUTE")
    private String meterReadingRoute;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "TOTAL_AMOUNT")
    private String totalAmount;

    @Column(name = "TOTAL_AMOUNT_REAL")
    private BigDecimal totalAmountReal;

    @Column(name = "TOTAL_PERIOD_BILL")
    private Integer totalPeriodBill;

    @Column(name = "GAP_PAYMENT_WARRANTY")
    private Integer gapPaymentWarranty;

    @Column(name = "EXPIRED_DATE")
    private Date expiredDate;

    @Column(name = "BILLING_CODE")
    private String billingCode;

    @Column(name = "BILLING_PERIOD")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date billingPeriod;

    @Column(name = "CUSTOMER_ID")
    private Integer customerId;

    @Column(name = "PAYMENT_AMOUNT")
    private String paymentAmount;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "INVOICE_NUMBER")
    private String invoiceNo;

    @Column(name = "TOTAL_LATE")
    private Integer totalLate;

    @Column(name = "DUE_DATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date dueDate;

    @Column(name = "LATE_CHARGE_RATE")
    private BigDecimal lateChargeRate;

    @Column(name = "PAYMENT_DATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date paymentDate;

    @Column(name = "TIME_UNIT")
    private String timeUnit;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "TOTAL_AMOUNT_IDR")
    private String totalAmountIdr;

    @Column(name = "TOTAL_AMOUNT_USD")
    private String totalAmountUsd;
}

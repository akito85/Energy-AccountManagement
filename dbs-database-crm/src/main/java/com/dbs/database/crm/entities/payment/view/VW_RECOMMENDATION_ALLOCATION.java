package com.dbs.database.crm.entities.payment.view;

import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_RECOMMENDATION_ALLOCATION")
public class VW_RECOMMENDATION_ALLOCATION implements Serializable {

    private static final long serialVersionUID = -8213870954250189110L;

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "BILLING_CODE")
    private String billingCode;

    @Column(name = "BILLING_ITEM")
    private String billingItem;

    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;

    @Column(name = "INVOICE_CURRENCY")
    private String invoiceCurrency;

    @Column(name = "BILLING_CYCLE")
    private String billingCycle;

    @Column(name = "BILLING_PERIOD")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date billingPeriod;

    @Column(name = "BILLING_ITEM_AMOUNT")
    private Double billingItemAmount;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "ALLOCATION_AMOUNT")
    private Double allocationAmount;

    @Column(name = "BILLING_ITEM_BALANCE")
    private Double billingItemBalance;

    @Column(name = "ALLOCATION_STATUS")
    private String allocationStatus;

    @Column(name = "CONVERTED_CURRENCY")
    private String convertedCurrency;

    @Column(name = "EQUIVALENT_AMOUNT")
    private Double equivalentAmount;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;
}

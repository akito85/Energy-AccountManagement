package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "VW_ADJUSTMENT_BILLING_ITEM")
public class VW_ADJUSTMENT_BILLING_ITEM {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "BILLING_ITEM")
    private String billingItem;

    @Column(name = "ITEM_ADJUSTMENT")
    private String itemAdjustment;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "PRICE")
    private Integer price;

    @Column(name = "UOM")
    private String uom;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "AMOUNT")
    private Double amount;

    @Column(name = "TOTAL_AMOUNT")
    private Double totalAmount;

    @Column(name = "TOTAL_AMOUNT_EQV_IDR")
    private Double totalAmountEqvIdr;

    @Column(name = "TOTAL_AMOUNT_EQV_USD")
    private Double totalAmountEqvUsd;

    @Column(name = "BILLING_CODE")
    private String billingCode;

    @Column (name = "INVOICE_NUMBER")
    private String invoiceNumber;

    @Column (name = "BILLING_BUCKET_CODE")
    private String billingBucketCode;

    @Column (name = "ACCOUNT_NUMBER")
    private String accNumb;
}

package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "VW_T_BILLING_ITEM")
public class VW_T_BILLING_ITEM {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "BILLING_CODE")
    private String billingCode;

    @Column(name = "ITEM")
    private String item;

    @Column(name = "QUANTITY")
    private String quantity;

    @Column(name = "LINE_NUMBER")
    private Integer lineNumber;

    @Column(name = "UOM")
    private String uom;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "PRICE_CODE")
    private String priceCode;

    @Column(name = "PRICE")
    private String price;

    @Column(name = "PRICE_REAL")
    private Double priceReal;

    @Column(name = "AMOUNT")
    private String amount;

    @Column(name = "AMOUNT_REAL")
    private Double amountReal;

    @Column(name = "DISCOUNT_AMOUNT")
    private String discountAmount;

    @Column(name = "DISCOUNT_AMOUNT_REAL")
    private Double discountAmountReal;

    @Column(name = "TOTAL_AMOUNT")
    private String totalAmount;

    @Column(name = "TOTAL_AMOUNT_REAL")
    private Double totalAmountReal;

    @Column(name = "TOTAL_AMOUNT_EQV_IDR")
    private String totalAmountEqvIdr;

    @Column(name = "TOTAL_AMOUNT_EQV_IDR_REAL")
    private Double totalAmountEqvIdrReal;

    @Column(name = "TOTAL_AMOUNT_EQV_USD")
    private String totalAmountEqvUsd;

    @Column(name = "TOTAL_AMOUNT_EQV_USD_REAL")
    private Double totalAmountEqvUsdReal;

    @Column(name = "RATE")
    private String rate;

    @Column(name = "RATE_REAL")
    private Double rateReal;

    @Column(name = "RATE_TYPE")
    private String rateType;

    @Column(name = "RATE_DATE")
    private Date rateDate;

    @Column(name = "REFERENCE")
    private Integer reference;

    @Column(name = "TYPE_BASIS")
    private String typeBasis;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne
    @JoinColumn(name = "BILLING_CODE", referencedColumnName = "BILLING_CODE", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private VW_RBI_INVOICE_DETAIL vwRbiInvoiceDetail;
}

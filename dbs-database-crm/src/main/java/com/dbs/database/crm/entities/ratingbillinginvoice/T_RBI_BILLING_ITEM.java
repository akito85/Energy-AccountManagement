package com.dbs.database.crm.entities.ratingbillinginvoice;

import lombok.Data;

import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_RBI_INVOICE_DETAIL;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "T_RBI_BILLING_ITEM")
public class T_RBI_BILLING_ITEM implements Serializable {

    private static final long serialVersionUID = 8296892482042368748L;

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "T_RBI_BILLING_ITEM_SEQ")
    @SequenceGenerator(sequenceName = "T_RBI_BILLING_ITEM_SEQ", allocationSize = 1, name = "T_RBI_BILLING_ITEM_SEQ")
    private Integer id;

    @Column(name = "BILLING_CODE")
    private String billingCode;

    @Column(name = "ITEM")
    private String item;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "LINE_NUMBER")
    private Integer lineNumber;

    @Column(name = "UOM")
    private String uom;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "PRICE_CODE")
    private String priceCode;

    @Column(name = "PRICE")
    private Double price;

    @Column(name = "AMOUNT")
    private Double amount;

    @Column(name = "DISCOUNT_AMOUNT")
    private Double discountAmount;

    @Column(name = "TOTAL_AMOUNT")
    private Double totalAmount;

    @Column(name = "TOTAL_AMOUNT_EQV_IDR")
    private Double totalAmountEqvIdr;

    @Column(name = "TOTAL_AMOUNT_EQV_USD")
    private Double totalAmountEqvUsd;

    @Column(name = "RATE")
    private Double rate;

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

    @Column(name = "BILLING_ITEM_CODE")
    private String billingItemCode;
    
	@ManyToOne
	@JoinColumn(name = "BILLING_CODE", referencedColumnName = "BILLING_CODE", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private VW_RBI_INVOICE_DETAIL vwRbiInvoiceDetail;
}

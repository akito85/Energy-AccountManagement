package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import com.dbs.database.crm.entities.ratingbillinginvoice.R_RBI_POS_DETAIL;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "VW_POS_DETAIL")
@Entity
public class VW_POS_DETAIL implements Serializable {
    @Id
    @Column(name="POS_DETAIL_ID")
    private Integer posDetailId;
    @Column(name="POS_NUMBER")
    private String posNumber;
    @Column(name="TYPE_ID")
    private Integer typeId;
    @Column(name="TYPE")
    private String type;
    @Column(name="ITEM")
    private String item;
    @Column(name="ITEM_ID")
    private String itemId;
    @Column(name="PRICE")
    private String price;
    @Column(name="REFERENCE")
    private Integer reference;
    @Column(name="REFERENCE_NAME")
    private String referenceName;
    @Column(name="QUANTITY")
    private Integer quantity;
    @Column(name="UOM", length = 15)
    private String uom;
    @Column(name="CURRENCY")
    private String currency;
    @Column(name="AMOUNT")
    private String amount;
    @Column(name="AMOUNT_EQV_IDR")
    private String amountEqvIdr;
    @Column(name="AMOUNT_EQV_USD")
    private String amountEqvUsd;
    @Column(name="EQV_IDR")
    private String eqvIdr;
    @Column(name="DISCOUNT")
    private String discount;
    @Column(name="TOTAL")
    private String total;
    @Column(name="TOTAL_EQV_USD")
    private String totalEqvUsd;
    @Column(name="TOTAL_EQV_IDR")
    private String totalEqvIdr;
    @Column(name="REMARK")
    private String remark;
    @Column(name="LINE_NUMBER")
    private Integer lineNumber;
}

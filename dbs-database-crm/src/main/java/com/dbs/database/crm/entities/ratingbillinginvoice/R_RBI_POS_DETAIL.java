package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.DefaultBaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "R_RBI_POS_DETAIL")
public class R_RBI_POS_DETAIL extends DefaultBaseEntities implements Serializable {
    @Id
    @Column(name="POS_DETAIL_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_RBI_POS_DETAIL_SEQ")
    @SequenceGenerator(sequenceName = "R_RBI_POS_DETAIL_SEQ", allocationSize = 1, name = "R_RBI_POS_DETAIL_SEQ")
    private Integer posDetailId;
    @Column(name="POS_NUMBER")
    private String posNumber;
    @Column(name="TYPE")
    private Integer type;
    @Column(name="ITEM")
    private String item;
    @Column(name="PRICE")
    private Double price;
    @Column(name="REFERENCE")
    private Integer reference;
    @Column(name="QUANTITY")
    private Integer quantity;
    @Column(name="UOM", length = 15)
    private String uom;
    @Column(name="CURRENCY")
    private String currency;
    @Column(name="AMOUNT")
    private Double amount;
    @Column(name="AMOUNT_EQV_IDR")
    private Double amountEqvIdr;
    @Column(name="AMOUNT_EQV_USD")
    private Double amountEqvUsd;
    @Column(name="EQV_IDR")
    private Double eqvIdr;
    @Column(name="DISCOUNT")
    private Double discount;
    @Column(name="TOTAL")
    private Double total;
    @Column(name="TOTAL_EQV_USD")
    private Double totalEqvUsd;
    @Column(name="TOTAL_EQV_IDR")
    private Double totalEqvIdr;
    @Column(name="REMARK")
    private String remark;
    @Column(name="LINE_NUMBER")
    private Integer lineNumber;
}

package com.dbs.database.crm.entities.accountmanagement;

import java.io.Serializable;

import com.dbs.common.base.entities.BaseEntities;

import lombok.Data;

import javax.persistence.*;
import org.springframework.lang.Nullable;

@Entity
@Data
@Table(name = "M_PRE_REQUISITE")
public class M_PRE_REQUISITE extends BaseEntities implements Serializable {

    @Id
    @Column(name = "ID", nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_PRE_REQUISITE_SEQ")
    @SequenceGenerator(sequenceName = "M_PRE_REQUISITE_SEQ",allocationSize = 1, name = "M_PRE_REQUISITE_SEQ")
    private Integer id;

    @Column(name = "ACCOUNT_ID")
    private Integer accountId;

    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "CATEGORY")
    private Integer category;

    @Nullable
    @Column(name = "PRODUCT_VERSION_ID")
    private Integer productVersionId;

    @Nullable
    @Column(name = "BILLING_ITEM_ID")
    private Integer billingItemId;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "BILL_AMOUNT_TOTAL")
    private Double billAmountTotal;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PAID_BILL_AMOUNT_TOTAL")
    private Double paidBillAmountTotal;

    @Column(name = "UNPAID_BILL_AMOUNT_TOTAL")
    private Double unpaidBillAmountTotal;
}
package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "VW_BILLING_ITEM_TAX")
public class VW_BILLING_ITEM_TAX {
    @Id
    @Column(name = "R_MAPPING_ID", nullable = false, updatable = false)
    private Integer rMappingId;
    @Column(name = "ITEM")
    private String item;
    @Column(name = "R_CATEGORY_ID")
    private Integer rCategoryId;
    @Column(name = "BILLING_ITEM_CODE")
    private String billingItemCode;
    @Column(name = "CATEGORY")
    private Integer category; // LOV 3
}
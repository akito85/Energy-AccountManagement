package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "M_BILLING_ITEM_CATEGORY")
public class M_RBI_BILLING_ITEM_CATEGORY extends BaseEntities implements Serializable {
    @Id
    @Column(name = "R_CATEGORY_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_BILLING_ITEM_CATEGORY_SEQ")
    @SequenceGenerator(sequenceName = "M_BILLING_ITEM_CATEGORY_SEQ", allocationSize = 1, name = "M_BILLING_ITEM_CATEGORY_SEQ")
    private Integer rCategoryId;

    @Column(name = "BILLING_ITEM_CODE")
    private String billingItemCode;

    @Column(name = "CATEGORY")
    private Integer category; // LOV 3

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "DESCRIPTION")
    private String description;
}
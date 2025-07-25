package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "M_BILLING_ITEM_MAPPING")
public class    M_RBI_BILLING_ITEM_MAPPING extends BaseEntities implements Serializable {
    @Id
    @Column(name = "R_MAPPING_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_BILLING_ITEM_MAPPING_SEQ")
    @SequenceGenerator(sequenceName = "M_BILLING_ITEM_MAPPING_SEQ", allocationSize = 1, name = "M_BILLING_ITEM_MAPPING_SEQ")
    private Integer rMappingId;

    @Column(name = "ITEM")
    private String item;

    @Column(name = "R_CATEGORY_ID")
    private Integer rCategoryId;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;
}

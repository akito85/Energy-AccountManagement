package com.dbs.database.crm.entities.accountmanagement;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "M_CRITERIA_ADJUSTMENT_PRICING")
public class M_CRITERIA_ADJUSTMENT_PRICING {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_CRITERIA_ADJUSTMENT_PRICING_SEQ")
    @SequenceGenerator(sequenceName = "M_CRITERIA_ADJUSTMENT_PRICING_SEQ",allocationSize = 1, name = "M_CRITERIA_ADJUSTMENT_PRICING_SEQ")
    private Integer id;

    @Column(name = "CRITERIA")
    private String criteria;

    @Column(name = "M_PRICING_ADJUSTMENT_ID")
    private Integer pricingAdjustmentId;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "ENTITY")
    private Integer entityId;

}

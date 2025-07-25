package com.dbs.database.crm.entities.product;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "M_CRITERIA_DATA_PRODUCT_TARGET")
public class M_CRITERIA_DATA_PRODUCT_TARGET {

    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_CRITERIA_DATA_PRODUCT_SEQ")
    @SequenceGenerator(sequenceName = "M_CRITERIA_DATA_PRODUCT_SEQ",allocationSize = 1, name = "M_CRITERIA_DATA_PRODUCT_SEQ")
    private Integer id;

    @Column(name = "DATA")
    private String data;

    @Column(name = "M_PRODUCT_TARGET_ID")
    private Integer productTargetId;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "UPDATED_BY")
    private String updatedBy;
}

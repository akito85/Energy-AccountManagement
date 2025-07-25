package com.dbs.database.crm.entities.accountmanagement;


import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "M_CRITERIA_DATA_PRICING")
public class M_CRITERIA_DATA_PRICING {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_CRITERIA_DATA_PRICING_SEQ")
    @SequenceGenerator(sequenceName = "M_CRITERIA_DATA_PRICING_SEQ",allocationSize = 1, name = "M_CRITERIA_DATA_PRICING_SEQ")
    private Integer id;

    @Column(name = "DATA")
    private String data;

    @Column(name = "ADJUSTMENTTYPE")
    private String adjustmentType;

    @Column(name = "ADJUSTMENTVALUE")
    private Double adjustmentValue;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "M_PRICING_ADJUSTMENT_ID")
    private Integer pricingAdjustmentId;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

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

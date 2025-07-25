package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "T_AM_SA_PRC_RULE_PRCADJUSTMENT_HEADER")
public class T_AM_SA_PRC_RULE_PRCADJUSTMENT_HEADER extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "T_AM_SA_PRC_RULE_PRCADJUSTMENT_HEADER_SEQ")
    @SequenceGenerator(sequenceName = "T_AM_SA_PRC_RULE_PRCADJUSTMENT_HEADER_SEQ", allocationSize = 1, name = "T_AM_SA_PRC_RULE_PRCADJUSTMENT_HEADER_SEQ")
    private Integer id;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "IS_ACTIVE")
    private String isActive;

    @Column(name = "T_AM_SA_PRC_RULE_DTL_ID")
    private Integer prcRuleDtlId;

    @Column(name = "R_PRICING_ADJUSTMENT_DETAIL_ID")
    private Integer rPricingAdjustmentDetailId;
}

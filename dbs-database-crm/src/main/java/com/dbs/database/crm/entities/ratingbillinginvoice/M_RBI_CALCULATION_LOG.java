package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "M_RBI_CALCULATION_LOG")
public class M_RBI_CALCULATION_LOG extends BaseEntities implements Serializable {
    @Id
    @Column(name ="ID_CALCULATION_LOG")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_RBI_CALCULATIONLOG_SEQ")
    @SequenceGenerator(sequenceName = "M_RBI_CALCULATIONLOG_SEQ",allocationSize = 1, name = "M_RBI_CALCULATIONLOG_SEQ")
    private Integer calLogId;

    @Column(name = "CALCULATION_CODE")
    private String calCode;

    @Column(name = "ACTION")
    private String action;

    @Column(name = "CALCULATION_TYPE")
    private String calType;

    @Column(name = "CALCULATION_DATE")
    private Date calDate;

    @Column(name = "CALCULATION_BY")
    private String calculationBy;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "ENTITY_ID")
    private Integer entityId;
}

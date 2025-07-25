package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "M_RBI_PERIOD")
public class M_RBI_PERIOD extends BaseEntities implements Serializable {

    private static final long serialVersionUID = -5566724441187781083L;

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_RBI_PERIOD_SEQ")
    @SequenceGenerator(sequenceName = "M_RBI_PERIOD_SEQ", allocationSize = 1, name = "M_RBI_PERIOD_SEQ")
    private Integer id;

    @Column(name = "BILLING_CYCLE_ID")
    private Integer billingCycleId;

    @Column(name = "PERIOD")
    @JsonFormat(pattern = Constant.FORMAT_YEAR_MONTH, timezone = Constant.TIMEZONE)
    private Date period;

    @Column(name = "START_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATETIME, timezone = Constant.TIMEZONE)
    private Date startDate;

    @Column(name = "END_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATETIME, timezone = Constant.TIMEZONE)
    private Date endDate;

    @Column(name = "INVOICE_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date invoiceDate;

    @Column(name = "DESCRIPTION")
    private String description;
}

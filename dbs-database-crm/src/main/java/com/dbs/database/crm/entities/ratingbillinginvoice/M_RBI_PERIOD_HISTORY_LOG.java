package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "M_RBI_PERIOD_HISTORY_LOG")
public class M_RBI_PERIOD_HISTORY_LOG implements Serializable {

    private static final long serialVersionUID = 7075787434771229289L;

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_RBI_PERIOD_HISTORY_LOG_SEQ")
    @SequenceGenerator(sequenceName = "M_RBI_PERIOD_HISTORY_LOG_SEQ", allocationSize = 1, name = "M_RBI_PERIOD_HISTORY_LOG_SEQ")
    private Integer id;

    @Column(name = "BILLING_PERIOD_ID")
    private Integer billingPeriodId;

    @Column(name = "ACTION", nullable = false)
    private String action;

    @Column(name = "ACTOR", nullable = false)
    private String actor;

    @Column(name = "ACTION_DATE", nullable = false)
    @JsonFormat(pattern = Constant.FORMAT_DATETIME_VIEW, timezone = Constant.TIMEZONE)
    private Date actionDate;

    @Column(name = "REMARK", nullable = false)
    private String remark;

    @Column(name = "CREATED_DATE", nullable = false)
    private Date createdDate;

    @Column(name = "CREATED_BY", nullable = false)
    private String createdBy;
}

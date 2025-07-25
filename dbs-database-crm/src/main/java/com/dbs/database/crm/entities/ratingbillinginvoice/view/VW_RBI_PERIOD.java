package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_RBI_PERIOD")
public class VW_RBI_PERIOD implements Serializable {
    
    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "BILLING_CYCLE_ID")
    private Integer billingCycleId;

    @Column(name = "PERIOD")
    private String period;

    @Column(name = "START_DATE")
//    @JsonFormat(pattern = Constant.FORMAT_DATETIME, timezone = Constant.TIMEZONE)
    private String startDate;

    @Column(name = "END_DATE")
//    @JsonFormat(pattern = Constant.FORMAT_DATETIME, timezone = Constant.TIMEZONE)
    private String endDate;

    @Column(name = "INVOICE_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date invoiceDate;

    @Column(name = "DESCRIPTION")
    private String description;
    
    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATED_DATE")
    private String createdDate;

    @Column(name = "UPDATED_DATE")
    private String updatedDate;
    
}

package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Temporal;

@Data
@Entity
@Table(name = "VW_BILLING_BUCKET")
public class VW_BILLING_BUCKET extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @Column(name = "CC_ID")
    private Integer ccId;
    @Column(name = "BILLING_BUCKET_CODE")
    private String billingBucketCode;
    @Column(name = "BILLING_BUCKET_NAME")
    private String billingBucketName;
    @Column(name = "PRIORITY_PERIOD")
    private String priorityPeriod;
    @Column(name = "START_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date startDate;
    @Column(name = "END_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date endDate;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;
    @Column(name = "CRITERIA")
    private String criteria;
    @Column(name = "CRITERIA_DOWNLOAD")
    private String criteriaDownload;
}

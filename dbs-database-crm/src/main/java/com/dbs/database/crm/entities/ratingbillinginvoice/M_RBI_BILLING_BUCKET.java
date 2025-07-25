package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.DefaultBaseEntities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "M_RBI_BILLING_BUCKET")
public class M_RBI_BILLING_BUCKET extends DefaultBaseEntities implements Serializable {

    @Column(name = "BILLING_BUCKET_CODE")
    private String billingBucketCode;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_RBI_BILLING_BUCKET_SEQ")
    @SequenceGenerator(sequenceName = "M_RBI_BILLING_BUCKET_SEQ", allocationSize = 1, name = "M_RBI_BILLING_BUCKET_SEQ")
    private Integer id;
    @Column(name = "BILLING_BUCKET_NAME")
    private String bilingBucketName;
    @Column(name = "PRIORITY_PERIOD")
    private Integer priorityPeriod; // LOV 4
    @Column(name = "START_DATE")
    private Date startDate;
    @Column(name = "END_DATE")
    private Date endDate;
    @Column(name = "APPROVAL_HIERARCHY")
    private Integer approvalHierarchy;
    @Column(name = "CC_ID")
    private Integer ccId;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;
    @Column(name = "CREATED_DATE")
    private Date createdDate;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "UPDATED_DATE")
    private Date updatedDate;
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "entity_id")
    private Integer entityId;
    @Column(name = "REMARK")
    private String remark;
    @JsonIgnore
    private String json;
}

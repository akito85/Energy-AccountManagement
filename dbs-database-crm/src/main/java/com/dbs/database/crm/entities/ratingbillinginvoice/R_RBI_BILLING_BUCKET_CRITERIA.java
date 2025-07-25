package com.dbs.database.crm.entities.ratingbillinginvoice;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "R_RBI_BILLING_BUCKET_CRITERIA")
public class R_RBI_BILLING_BUCKET_CRITERIA {
    @Id
    @Column(name = "BILLING_BUCKET_CRITERIA_ID")
    @SequenceGenerator(name = "R_RBI_BILLING_BUCKET_CRITERIA_SEQ", allocationSize = 1, sequenceName = "R_RBI_BILLING_BUCKET_CRITERIA_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_RBI_BILLING_BUCKET_CRITERIA_SEQ")
    private Integer billingBucketCriteriaId;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATED_DATE")
    private Date createdDate;
    @Column(name = "CRITERIA")
    private Integer criteria;
    @Column(name = "BILLING_BUCKET_CODE")
    private String billingBucketCode;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "UPDATED_DATE")
    private Date updatedDate;
}

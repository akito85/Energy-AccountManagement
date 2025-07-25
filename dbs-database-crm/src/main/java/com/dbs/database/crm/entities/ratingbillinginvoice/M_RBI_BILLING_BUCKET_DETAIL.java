package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.DefaultBaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "M_RBI_BILLING_BUCKET_DETAIL")
public class M_RBI_BILLING_BUCKET_DETAIL extends DefaultBaseEntities implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_RBI_BILLING_BUCKET_DETAIL_SEQ")
    @SequenceGenerator(sequenceName = "M_RBI_BILLING_BUCKET_DETAIL_SEQ", allocationSize = 1, name = "M_RBI_BILLING_BUCKET_DETAIL_SEQ")
    private Integer id;
    @Column(name = "BILLING_BUCKET_CODE")
    private String billingBucketCode;
    @Column(name = "BILLING_ITEM_CODE")
    private String billingItemCode;
    @Column(name = "CURRENCY")
    private Integer currency;
    @Column(name = "SEQUENCE")
    private Integer sequence;
    @Column(name = "PRIORITY")
    private Character priority;
    @Column(name = "CATEGORY")
    private Integer category;
    @Column(name = "START_DATE")
    private Date startDate;
    @Column(name = "END_DATE")
    private Date endDate;
    @Column(name = "DESCRIPTION")
    private String description;
}

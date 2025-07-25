package com.dbs.database.crm.entities.ratingbillinginvoice;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "M_RBI_BILLING_ITEM")
public class M_RBI_BILLING_ITEM {
    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_RBI_BILLING_ITEM_SEQ")
    @SequenceGenerator(sequenceName = "M_RBI_BILLING_ITEM_SEQ", allocationSize = 1, name = "M_RBI_BILLING_ITEM_SEQ")
    private Integer id;

    @Column(name = "BILLING_ITEM_CODE")
    private String billingItemCode;

    @Column(name="BILLING_ITEM_CATEGORY")
    private String billingItemCategory;  // LOV 1

    @Column(name="BILLING_ITEM_NAME")
    private String billingItemName;

    @Column(name="BILLING_TYPE")
    private String billingType; // LOV 2

    @Column(name = "APP_HIER_ID")
    private Integer appHierId;

    @Column(name="START_DATE")
    private Date startDate;

    @Column(name="END_DATE")
    private Date endDate;

    @Column(name="LATE_CHARGE")
    private Character lateCharge;

    @Column(name="PAYMENT_WARRANTY")
    private Character paymentWarranty;

    @Column(name="DESCRIPTION")
    private String description;

    @Column(name="STATUS")
    private String status;

    @Column(name="STATUS_APPROVAL")
    private String statusApproval;

    @Column(name="CREATED_DATE")
    private Date createdDate;

    @Column(name="CREATED_BY")
    private String createdBy;

    @Column(name="UPDATED_DATE")
    private Date updatedDate;

    @Column(name="UPDATED_BY")
    private String updatedBy;

    @Column(name="ENTITY_ID")
    private Integer entityId;

    @Column(name = "TRIGGER_JSON")
    private String triggerJson;
    
    @Column(name = "CCID")
    private Integer ccId;
}

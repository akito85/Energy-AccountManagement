package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "VW_RBI_BILLING_ITEM")
public class VW_RBI_BILLING_ITEM {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "BILLING_ITEM_CODE")
    private String billingItemCode;

    @Column(name = "BILLING_ITEM_CATEGORY")
    private String billingItemCategory;

    @Column(name = "BILLING_ITEM_NAME")
    private String billingItemName;

    @Column(name = "BILLING_TYPE")
    private String billingType;

    @Column(name = "APPROVAL_HIERARCHY")
    private Integer approvalHierarchy;

    @Column(name="START_DATE")
    private Date startDate;

    @Column(name="END_DATE")
    private Date endDate;

    @Column(name="LATE_CHARGE")
    private String lateCharge;

    @Column(name="PAYMENT_WARRANTY")
    private String paymentWarranty;

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

    @Column(name="CATEGORY")
    private String category;

    @Column(name="ENTITY_ID")
    private Integer entityId;
    
    @Column(name = "CCID")
    private Integer ccId;
}

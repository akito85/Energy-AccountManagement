package com.dbs.database.crm.entities.accountmanagement;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_ACCOUNT")
public class VW_ACCOUNT implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer accountId;

    @Column(name = "CUSTOMER_ID")
    private Integer customerId;

    @Column(name = "REGISTRATION_NUMBER")
    private String registrationNumber;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "ACCOUNT_NAME")
    private String accountName;

    @Column(name = "ACCOUNT_SEGMENT")
    private String accountSegment;

    @Column(name = "ACCOUNT_GROUP_TYPE")
    private String accountGroupType;

    @Column(name = "ACCOUNT_CATEGORY")
    private String accountCategory;

    @Column(name = "CLASSIFICATION_TYPE")
    private String accountRuleId;

    @Column(name = "ACCOUNT_TYPE")
    private String accountType;

    @Column(name = "PAYMENT_CHANNEL")
    private String paymentChannel;

    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STATUS")
    private String status;

    // LOCATION INFORMATION

    @Column(name = "SOR") //From Cost Center (SOR) || From Cost Center (AREA)
    private String sor;

    @Column(name = "COST_CENTER")
    private String costCenter;

    @Column(name = "COST_CENTER_ID")
    private Integer costCenterId;

    @Column(name = "METER_READING_CODE")
    private String meterReadingCode; // Kode Buku (001, 002, dll)

    // Budget Information

    @Column(name = "BUDGET_YEAR")
    private String budgetYear;

    @Column(name = "BUDGET")
    private String budget;

    @Column(name = "TERITORY")
    private String teritory;

    @Column(name = "INDUSTRIAL_SECTOR")
    private String industrialSector;

    @Column(name = "IS_CORPORATE")
    private String isCorporate;

    @Column(name = "PRIORITY")
    private String priority;

    @Column(name = "IS_EXCEPTION")
    private String isException;

    @Column(name = "ACCOUNT_GROUP")
    private String accountGroup;

    @Column(name = "IS_BAD_DEBT")
    private String isBadDebt;

    @Column(name = "SYNC_FLAG")
    private String syncFlag;

    @Column(name = "ACCOUNT_REFERENCE_ID") // NO SAP
    private String accountReferenceId;

    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @Column(name = "CUSTOMER_MANAGEMENT")
    private String customerManagement;

    @Column(name = "CUSTOMER_MANAGEMENT_ID")
    private Integer customerManagementId;

    @Column(name = "BILLING_BUCKET_ID")
    private String billingBucketId;

    @Column(name = "CREATED_DATE")
    private Date createdDate;
    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

}

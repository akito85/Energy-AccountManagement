/*
 * To change this license header; choose License Headers in Project Properties.
 * To change this template file; choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author RachmatY
 */
@Entity
@Data
@Table(name = "VW_ACCOUNT_INFORMATION")
public class VW_ACCOUNT_INFORMATION implements Serializable {
   
    @Column(name = "CUSTOMER_ID")
    private Integer customerId; 
    @Column(name = "CUSTOMER_NUMBER")
    private String customerNumber;
    @Column(name = "CUSTOMER_TYPE")
    private String customerType;
    @Column(name = "CUSTOMER_TYPE_ID")
    private Integer customerTypeId;
    @Column(name = "CUSTOMER_NAME")
    private String customerName;
    @Column(name = "CUSTOMER_IDENTIFICATION_NUMBER")
    private String customerIdentificationNumber;
    @Column(name = "CUSTOMER_IDENTIFICATION_TYPE")
    private String customerIdentificationType;
    @Column(name = "CUSTOMER_IDENTIFICATION_TYPE_ID")
    private Integer customerIdentificationTypeId;
    @Column(name = "SEX")
    private String sex;
    @Column(name = "SEX_ID")
    private Integer sexId;
    @Column(name = "FOUNDED_BIRTH_DATE")
    private String foundedBirthDate;
    @Column(name = "FOUNDED_BIRTH_PLACE")
    private String foundedBirthPlace;
    @Column(name = "MARITAL_STATUS")
    private String maritalStatus;
    @Column(name = "MARITAL_STATUS_ID")
    private Integer maritalStatusId;
    @Column(name = "CUSTOMER_STATUS")
    private String customerStatus;
    @Column(name = "SEARCH_KEY")
    private String searchKey;
    @Id
    @Column(name = "ACCOUNT_ID")
    private Integer accountId;
    @Column(name = "UNIQUE_ACCOUNT")
    private String uniqueAccount;
    @Column(name = "REGISTRATION_NUMBER")
    private String registrationNumber;
    @Column(name = "ACCOUNT_GROUP")
    private String accountGroup;
    @Column(name = "ACCOUNT_GROUP_ID")
    private Integer accountGroupId;
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;
    @Column(name = "ACCOUNT_NAME")
    private String accountName;
    @Column(name = "ACCOUNT_SEGMENT")
    private String accountSegment;
    @Column(name = "ACCOUNT_SEGMENT_ID")
    private Integer accountSegmentId;
    @Column(name = "ACCOUNT_GROUP_TYPE")
    private String accountGroupType;
    @Column(name = "ACCOUNT_GROUP_TYPE_ID")
    private Integer accountGroupTypeId;
    @Column(name = "ACCOUNT_CATEGORY")
    private String accountCategory;
    @Column(name = "ACCOUNT_CATEGORY_ID")
    private Integer accountCategoryId;
    @Column(name = "CLASSIFICATION_TYPE")
    private String classificationType;
    @Column(name = "CLASSIFICATION_TYPE_ID")
    private Integer classificationTypeId;
    @Column(name = "ACCOUNT_TYPE")
    private String accountType;
    @Column(name = "ACCOUNT_TYPE_ID")
    private Integer accountTypeId;
    @Column(name = "PAYMENT_CHANNEL")
    private String paymentChannel;
    @Column(name = "PAYMENT_CHANNEL_ID")
    private Integer paymentChannelId;
    @Column(name = "ACCOUNT_DESCRIPTION")
    private String accountDescription;
    @Column(name = "SOR")
    private String sor;
    @Column(name = "SOR_ID")
    private Integer sorId;
    @Column(name = "COST_CENTER")
    private String costCenter;
    @Column(name = "COST_CENTER_ID")
    private Integer costCenterId;
    @Column(name = "METER_READING_CODE")
    private String meterReadingCode;
    @Column(name = "METER_READING_CODE_ID")
    private Integer meterReadingCodeId;
    @Column(name = "INDUSTRIAL_SECTOR")
    private String industrialSector;
    @Column(name = "INDUSTRIAL_SECTOR_ID")
    private Integer industrialSectorId;
    @Column(name = "BUDGET_YEAR")
    private String budgetYear;
    @Column(name = "BUDGET_YEAR_ID")
    private Integer budgetYearId;
    @Column(name = "BUDGET")
    private String budget;
    @Column(name = "BUDGET_ID")
    private Integer budgetId;
    @Column(name = "TERITORY")
    private String teritory;
    @Column(name = "TERITORY_ID")
    private Integer teritoryId;
    @Column(name = "TAX_IDENTIFIER_TYPE")
    private String taxIdentifierType;
    @Column(name = "TAX_IDENTIFIER_TYPE_ID")
    private Integer taxIdentifierTypeId;
    @Column(name = "TAX_IDENTIFIER_NUMBER")
    private String taxIdentifierNumber;
    @Column(name = "TAX_IDENTIFIER_NAME")
    private String taxIdentifierName;
    @Column(name = "TAX_IDENTIFIER_ADDRESS")
    private String taxIdentifierAddress;
    @Column(name = "TAX_IDENTIFIER_ADDRESS_ID")
    private Integer taxIdentifierAddressId;
    @Column(name = "TAX_RELATION_IDENTIFIER_TYPE")
    private String taxRelationIdentifierType;
    @Column(name = "TAX_RELATION_IDENTIFIER_TYPE_ID")
    private Integer taxRelationIdentifierTypeId;
    @Column(name = "TAX_RELATION_IDENTIFIER_NUMBER")
    private String taxRelationIdentifierNumber;
    @Column(name = "TAX_RELATION_IDENTIFIER_NAME")
    private String taxRelationIdentifierName;
    @Column(name = "TAX_RELATION_IDENTIFIER_ADDRESS")
    private String taxRelationIdentifierAddress;
    @Column(name = "TAX_RELATION_IDENTIFIER_ADDRESS_ID")
    private Integer taxRelationIdentifierAddressId;
    @Column(name = "PRIORITY")
    private String priority;
    @Column(name = "IS_CORPORATE")
    private String isCorporate;
    @Column(name = "IS_EXCEPTION")
    private String isException;
    @Column(name = "IS_BAD_DEBT")
    private String isBadDebt;
    @Column(name = "IS_SYNC")
    private String isSync;
    @Column(name = "ACCOUNT_REFERENCE_ID")
    private String accountReferenceId;
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @Column(name = "ACCOUNT_STATUS")
    private String accountStatus;
    @Column(name = "CUSTOMER_MANAGEMENT")
    private String customerManagement;
    @Column(name = "CUSTOMER_MANAGEMENT_ID")
    private Integer customerManagementId;
    @Column(name = "CUSTOMER_MANAGEMENT_NAME")
    private String customerManagementName;
    @Column(name = "CUSTOMER_DESCRIPTION")
    private String customerDescription;
    @Column(name = "CREATED_DATE")
    private Date createdDate;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "UPDATED_DATE")
    private Date updatedDate;
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "BILLING_BUCKET_CODE")
    private String billingBucketCode;
}

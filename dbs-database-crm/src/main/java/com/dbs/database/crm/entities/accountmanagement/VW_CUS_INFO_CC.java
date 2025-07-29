package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_CUS_INFO_CC")
public class VW_CUS_INFO_CC extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer customerId;

    @Column(name = "ACCOUNT_ID")
    private Integer accountId;

    @Column(name="UNIQUE_CUSTOMER")
    private String uniqueCustomer;

    @Column(name="PAGING_CUSTOMER_CM")
    private String pagingCustomerCm;

    @Column(name="PAGING_CUSTOMER_HEAD")
    private String pagingCustomerHead;

    @Column(name="CUSTOMER_NAME")
    private String customerName; // Concate First Name, Middle Name, Last Name

    @Column(name="CUSTOMER_NUMBER")
    private String customerNumber;
    
    @Column(name="CUSTOMER_TYPE_ID")
    private Integer customerTypeId;

    @Column(name="CUSTOMER_TYPE")
    private String customerType; // Organization/Person

    @Column(name="FOUNDED_BIRTH_DATE")
    private Date foundedBirthDate;

    @Column(name="FIRST_NAME")
    private String firstName;
    
    @Column(name="IDENTIFICATION_TYPE_ID")
    private Integer identificationTypeId;

    @Column(name="IDENTIFICATION_TYPE")
    private String identificationType; // NPWP/KTP

    @Column(name="LAST_NAME")
    private String lastName;

    @Column(name="MARITAL_STATUS_ID")
    private Integer maritalStatusId;

    @Column(name="MARTIAL_STATUS")
    private String maritalStatus;

    @Column(name="MIDDLE_NAME")
    private String middleName;

    @Column(name="FOUNDED_BIRTH_PLACE")
    private String foundedBirthPlace;

    @Column(name="SEARCH_KEY")
    private String searchKey;

    @Column(name="SEX_ID")
    private Integer sexId;

    @Column(name="SEX")
    private String sex;

    @Column(name="CUSTOMER_IDENTIFICATION_NUMBER")
    private String customerIdentificationNumber;

    @Column(name="CMPOSITIONID")
    private Integer cmPositionId;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @Column(name = "COST_CENTER_ID")
    private Integer ccId;
    @Column(name = "POSITION_ID")
    private Integer positionId;
    
    @Column(name = "ACCOUNT_COST_CENTER_ID")
    private Integer accountCostCenterId;

    @Column(name = "CUSTOMER_MANAGEMENT")
    private String customerManagement;
}

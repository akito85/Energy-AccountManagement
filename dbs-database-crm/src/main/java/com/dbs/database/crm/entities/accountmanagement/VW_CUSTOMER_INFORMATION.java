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
@Table(name = "VW_CUSTOMER_INFORMATION")
public class VW_CUSTOMER_INFORMATION extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer customerId;

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

    @Column(name="MARTIAL_STATUS")
    private String maritalStatus;

    @Column(name="MIDDLE_NAME")
    private String middleName;

    @Column(name="FOUNDED_BIRTH_PLACE")
    private String foundedBirthPlace;

    @Column(name="SEARCH_KEY")
    private String searchKey;

    @Column(name="SEX")
    private String sex;

    @Column(name="CUSTOMER_IDENTIFICATION_NUMBER")
    private String customerIdentificationNumber;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @Column(name = "POSITION_ID")
    private Integer positionId;
}

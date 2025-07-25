package com.dbs.module.account.main.dto.accountinformation;

import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_ACCOUNT_INFORMATION;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class CustomerInformationDto {
    private int number;
    private Integer customerId;
    private String customerName;
    private String customerNumber;
    private String customerType;
    private Date foundedBirthDate;
    private String firstName;
    private String identificationType;
    private String lastName;
    private String maritalStatus;
    private String middleName;
    private String foundedBirthPlace;
    private String searchKey;
    private String sex;
    private String customerIdentificationNumber;
    private Integer cmPositionId;
    private String description;
    private Integer entityId;
    private Integer ccId;
    private Integer positionId;
    private List<VW_ACCOUNT_INFORMATION> allAccount;
    private String createdBy;
    private String updatedBy;
    private String status;
    private Date createdDate;
    private Date updatedDate;
    
    private String customerManagement;
}

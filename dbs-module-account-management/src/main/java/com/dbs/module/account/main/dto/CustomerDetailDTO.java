package com.dbs.module.account.main.dto;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class CustomerDetailDTO extends BaseEntities implements Serializable{
    private String customerNumber;
    private Integer customerType;
    private String firstName;
    private String middleName;
    private String lastName;
    private String customerName;
    private String personalIdentificationNumber;
    private Integer identificationType;
    private Integer position;
    private Integer sex;
    private Date dateOfBirth;
    private String placeOfBirth;
    private Integer maritalStatus;
    private Integer industrialSector;
    private String status;
    private String searchKey;

    public CustomerDetailDTO() {
        super();
    }
    
}

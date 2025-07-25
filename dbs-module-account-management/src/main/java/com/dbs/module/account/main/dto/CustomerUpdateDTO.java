package com.dbs.module.account.main.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

@Data
@SuppressWarnings("java:S1068")
public class CustomerUpdateDTO {
    private String customerNumber;
    private Integer customerType;
    private String firstName;
    private String middleName;
    private String lastName;
    private String personalIdentificationNumber;
    private Integer identificationType;
    private Integer position;
    private Integer sex;
    private String dateOfBirth;
    private String placeOfBirth;
    private Integer maritalStatus;
    private Integer industrialSector;
    private String status;
    private String searchKey;
    
    private String customerName;
    
    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName();
        }
    }
}

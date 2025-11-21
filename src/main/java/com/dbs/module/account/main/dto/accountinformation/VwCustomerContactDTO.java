package com.dbs.module.account.main.dto.accountinformation;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;

@Data
@SuppressWarnings("java:S1068") 
public class VwCustomerContactDTO {
    private Integer customerId;
    private String accountNumber;
    private String accountName;
    private String contactName;
    private Integer contactId;
    private String contactAddress;
    private String firstName;
    private String middleName;
    private String lastName;
    private String job;
    private String jobId;
    private String position;
    private String positionId;
    private String additionalNote;
    private String description;
    private String status;
    private String createdDate;
    private String createdBy;
    private String updatedDate;
    private String updatedBy;
    private List<LinkedHashMap<String, Object>> contactDetail;
}

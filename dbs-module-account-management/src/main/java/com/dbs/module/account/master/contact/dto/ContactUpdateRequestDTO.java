package com.dbs.module.account.master.contact.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class ContactUpdateRequestDTO implements Serializable {
    
    private Integer contactId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String contactName;
    private Integer jobId;
    private Integer positionId;
    private String updatedBy;
    private String updatedDate;
    
    private String remark;
    
    private List<ContactDetailsUpdateRequestDTO> viewDetails;
    
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
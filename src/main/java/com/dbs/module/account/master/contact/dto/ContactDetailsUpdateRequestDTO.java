package com.dbs.module.account.master.contact.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class ContactDetailsUpdateRequestDTO implements Serializable {
    
    private Integer contactDetailId;
    private Integer contactId;
    private Integer type;
    private Integer inputType;
    private Integer prefix1;
    private Integer prefix2;
    private String value;
    private String sufix;
    private String updatedBy;
    private String updatedDate;
    
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

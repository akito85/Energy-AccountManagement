package com.dbs.module.account.detail.contact.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class AccountContactUpdateRequestDTO implements Serializable {

    private Integer accountContactId;
    private Integer accountId;
    private Integer contactId;
    private String description;
    private String descriptionContact;

    private Boolean primaryFlag;
    
    private String status;
    private String remark;

    private Boolean needValidation;
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

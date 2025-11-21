package com.dbs.module.account.master.contact.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class ContactCreateRequestDTO implements Serializable {
    
    private Integer contactId;

    @NotEmpty(message = "First Name cannot be empty")
    private String firstName;

    private String middleName;

    private String lastName;

    @NotEmpty(message = "Contact Name cannot be empty")
    private String contactName;

    private Integer jobId;

    private Integer positionId;

    private String description;

    @NotEmpty(message = "Contact Details cannot be empty")
    private List<ContactDetailsCreateRequestDTO> viewDetails;
    
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

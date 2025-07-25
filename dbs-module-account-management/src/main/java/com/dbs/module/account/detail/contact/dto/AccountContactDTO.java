package com.dbs.module.account.detail.contact.dto;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.module.account.master.contact.dto.ContactCreateRequestDTO;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.Convert;
import java.io.Serializable;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class AccountContactDTO implements Serializable {

    private ContactCreateRequestDTO contact;
    
    private Integer accountId;

    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean primaryFlag;

    private Integer contactAddressId;

    private String additionalNote;

    private Boolean needValidation;

    private String description;
    
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

package com.dbs.module.account.detail.contact.dto;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.module.account.master.contact.dto.ContactDetailsCreateRequestDTO;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.Convert;
import java.io.Serializable;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class AccountContactCreateDTO implements Serializable {
    
    private Integer accountId;
    private Integer contactId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String contactName;
    private Integer jobId;
    private Integer positionId;
    private String description;
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean primaryFlag;
    private Integer contactAddressId;
    private String additionalNote;
    private String contactAddress;
    private List<ContactDetailsCreateRequestDTO> contactDetail;
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

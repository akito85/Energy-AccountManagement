package com.dbs.module.account.detail.relationship.dto;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
// import com.dbs.module.account.master.relationship.dto.ContactDetailsCreateRequestDTO;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.Convert;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class AccountRelationshipCreateDTO implements Serializable {
    
    private Integer id;
    private String directionalFlag;
    private Integer subjectPartyId;
    private Integer objectPartyId;
    private String relationshipCategory;
    private String relationType;
    private Date startDate;
    private Date endDate;
    private String description;
    private String source;
    private String status;
    private String statusApproval;
    
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

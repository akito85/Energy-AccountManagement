package com.dbs.module.account.detail.relationship.dto;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.Convert;
import java.io.Serializable;
import java.util.List;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class AccountRelationshipDTO implements Serializable {

    private Integer id;
    private String directionalFlag;
    private String relationshipType;
    private String relationshipCategory;
    private Integer subjectId;
    private String subjectName;
    private String subjectValue;
    private Integer objectId;
    private String objectName;
    private String objectValue;
    private Date startDate;
    private Date endDate;
    private String description;
    private String status;
    private String statusApproval;
    private Date createdDate;
    private String createdBy;
    private Date updatedDate;
    private String updatedBy;
    
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

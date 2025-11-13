package com.dbs.module.account.detail.relationship.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class AccountRelationshipUpdateRequestDTO implements Serializable {

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

package com.dbs.module.account.detail.relationship.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class ViewAccountRelationshipDTO implements Serializable {
    
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
    private String startDate;
    private String endDate;
    private String description;
    private String status;
    private String statusApproval;

}

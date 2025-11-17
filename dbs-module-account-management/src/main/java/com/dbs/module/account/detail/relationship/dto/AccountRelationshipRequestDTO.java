package com.dbs.module.account.detail.relationship.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class AccountRelationshipRequestDTO {
    private Integer accountId;
    private Integer relationshipId;
    private String directionalFlag;
    private String relationshipType;
    private String relationshipCategory;
    private Integer objectId;
    private String objectName;
    private String objectValue;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDate;
    
    private String description;

    // Getters and Setters
    public Integer getAccountId() { return accountId; }
    public void setAccountId(Integer accountId) { this.accountId = accountId; }
    public Integer getRelationshipId() { return relationshipId; }
    public void setRelationshipId(Integer relationshipId) { this.relationshipId = relationshipId; }
    public String getDirectionalFlag() { return directionalFlag; }
    public void setDirectionalFlag(String directionalFlag) { this.directionalFlag = directionalFlag; }
    public String getRelationshipType() { return relationshipType; }
    public void setRelationshipType(String relationshipType) { this.relationshipType = relationshipType; }
    public String getRelationshipCategory() { return relationshipCategory; }
    public void setRelationshipCategory(String relationshipCategory) { this.relationshipCategory = relationshipCategory; }
    public Integer getObjectId() { return objectId; }
    public void setObjectId(Integer objectId) { this.objectId = objectId; }
    public String getObjectName() { return objectName; }
    public void setObjectName(String objectName) { this.objectName = objectName; }
    public String getObjectValue() { return objectValue; }
    public void setObjectValue(String objectValue) { this.objectValue = objectValue; }
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

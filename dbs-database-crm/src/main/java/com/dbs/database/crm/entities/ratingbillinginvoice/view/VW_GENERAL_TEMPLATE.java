package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_GENERAL_TEMPLATE")
public class VW_GENERAL_TEMPLATE {
    @Id
    @Column(name = "TEMPLATE_ID")
    private Integer templateId;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "PATH")
    private String path;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @Column(name = "CC_ID")
    private Integer ccId;
    @Column(name = "FILE_TYPE")
    private String fileType;
    @Column(name = "TEMPLATE_TYPE")
    private Integer templateTypeId;
    @Column(name = "TEMPLATE_TYPE_NAME")
    private String templateType;
    @Column(name = "FILE_SIZE")
    private Integer fileSize;
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "UPDATED_DATE")
    private Date updatedDate;
    @Column(name = "TEMPLATE_NAME")
    private String templateName;
    @Column(name = "APPROVAL_HIERARCHY")
    private Integer approvalHierarchy;
    @Column(name = "START_DATE")
    private Date startDate;
    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;
    @Column(name = "TRIGGER_JSON")
    private String triggerJson;
    @Column(name = "END_DATE")
    private Date endDate;
    @Column(name = "CREATED_DATE")
    private Date createdDate;
    @Column(name = "FILE_NAME")
    private String fileName;
}

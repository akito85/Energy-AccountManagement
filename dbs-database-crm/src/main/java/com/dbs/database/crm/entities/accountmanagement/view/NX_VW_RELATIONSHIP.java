package com.dbs.database.crm.entities.accountmanagement.view;

import com.dbs.common.base.entities.DefaultBaseEntities;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "NX_VW_RELATIONSHIP")
public class NX_VW_RELATIONSHIP extends DefaultBaseEntities implements Serializable {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "DIRECTIONAL_FLAG")
    private String directionalFlag;

    @Column(name = "RELATIONSHIP_TYPE")
    private String relationshipType;

    @Column(name = "RELATIONSHIP_CATEGORY")
    private String relationshipCategory;

    @Column(name = "SUBJECT_ID")
    private Integer subjectId;

    @Column(name = "SUBJECT_NAME")
    private String subjectName;

    @Column(name = "SUBJECT_VALUE")
    private String subjectValue;

    @Column(name = "OBJECT_ID")
    private Integer objectId;

    @Column(name = "OBJECT_NAME")
    private String objectName;

    @Column(name = "OBJECT_VALUE")
    private String objectValue;

    @Column(name = "START_DATE")
    private String startDate;

    @Column(name = "END_DATE")
    private String endDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;
}

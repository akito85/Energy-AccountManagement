package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "VW_M_APPROVAL_HIERARCHY")
public class VW_M_APPROVAL_HIERARCHY extends BaseEntities {
    @Id
    @Column(name = "APPHIER_ID")
    private Integer appHierId;

    @Column(name = "APPROVAL_TYPE")
    private String approvalType;

//    @Column(name = "APPROVAL_CODE")
//    private String approvalCode;

    @Column(name = "APPROVAL_NAME")
    private String approvalName;

    @Column(name = "DESCRIPTION")
    private String desc;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "IS_DELETED")
    private String isDeleted;
}

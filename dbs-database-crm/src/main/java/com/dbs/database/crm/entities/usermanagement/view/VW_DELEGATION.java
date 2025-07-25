package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_DELEGATION")
public class VW_DELEGATION extends BaseEntities {
    @Id
    @Column(name = "DELEGATION_ID")
    private Integer delegationId;

    @Column(name="REQUESTOR")
    private String requestor;

    @Column(name="REQUESTOR_NAME")
    private String requestorName;

    @Column(name="DELEGATE_TO")
    private String delegateTo;

    @Column(name="DELEGATE_NAME")
    private String delegateName;

    @Column(name="GA_REL_ID")
    private Integer gaRelId;

    @Column(name="GROUP_ACCESS")
    private String groupAccess;

    @Column(name="POSITION")
    private String position;

    @Column(name="POSITION_ID")
    private Integer positionId;

    @Column(name="REASON")
    private String reason;

    @Column(name="REMARK")
    private String remark;

    @Column(name="APPROVAL_DATE")
    private Date approvalDate;

    @Column(name="START_DATE")
    private Date startDate;

    @Column(name="END_DATE")
    private Date endDate;

    @Column(name="ENTITY_ID")
    private Integer entityId;
}

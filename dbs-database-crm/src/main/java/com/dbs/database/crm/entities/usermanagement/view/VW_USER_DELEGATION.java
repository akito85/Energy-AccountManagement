package com.dbs.database.crm.entities.usermanagement.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "VW_USER_DELEGATION")
public class VW_USER_DELEGATION {
    @Id
    @Column(name = "ID")
    private Integer id;
    @Column(name = "POSITION_FROM_DELEGATOR")
    private String positionFromDelegator;
    @Column(name = "POSITION_FROM_DELEGATOR_ID")
    private Integer positionFromDelegatorId;
    @Column(name = "POSITION_DELEGATE_TO")
    private String positionDelegateTo;
    @Column(name = "POSITION_DELEGATE_TO_ID")
    private Integer positionDelegateToId;
    @Column(name = "DELEGATE_FROM")
    private String delegateFrom;
    @Column(name = "DELEGATE_FROM_ID")
    private Integer delegateFromId;
    @Column(name = "DELEGATE_TO")
    private String delegateTo;
    @Column(name = "DELEGATE_TO_ID")
    private Integer delegateToId;
    @Column(name = "START_DATE")
    private Date startDate;
    @Column(name = "END_DATE")
    private Date endDate;
    @Column(name = "REQUEST_REMARK")
    private String requestRemark;
    @Column(name = "APPROVAL_REMARK")
    private String approvalRemark;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CREATED_DATE")
    private Date createdDate;
    @Column(name = "UPDATED_DATE")
    private Date updatedDate;
}

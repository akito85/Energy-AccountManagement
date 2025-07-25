/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author RachmatY
 */
@Entity
@Data
@Table(name = "VW_PENDING_APPROVAL")
public class VW_PENDING_APPROVAL implements Serializable {

    @Id
    @Column(name = "T_APP_ID")
    private Integer tAppId;
    @Column(name = "APPROVAL_TYPE")
    private String approvalType;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "APPROVAL_NAME")
    private String approvalName;
    @Column(name = "APPHIER_DTL_ID")
    private Integer apphierDtlId;
    @Column(name = "APPHIER_ID")
    private Integer apphierId;
    @Column(name = "APPROVAL_LEVEL")
    private String approvalLevel;
    @Column(name = "IS_FINAL")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isFinal;
    @Column(name = "IS_SUBMITTER")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isSubmitter;
    @Column(name = "PENDING_APPROVAL")
    private String pendingApproval;
    @Column(name = "POSITION_ID")
    private Integer positionId;
    @Column(name="POSITION_NAME")
    private String positionName;
    @Column(name="TASK_DATE")
    private String taskDate;
    
    
}

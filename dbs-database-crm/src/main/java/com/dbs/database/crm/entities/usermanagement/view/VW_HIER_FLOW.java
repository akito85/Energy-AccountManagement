package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "VW_HIER_FLOW")
public class VW_HIER_FLOW {
    @Id
    @Column(name = "APPHIER_DTL_ID")
    private Integer apphierDtlId;

    @Column(name = "APPHIER_ID")
    private Integer appHierId;

    @Column(name = "POSITION_ID")
    private Integer positionId;

    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isFinal;

    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isSubmitter;
    
    @Column(name = "APPROVAL_LEVEL")
    private Integer approvalLevel;
    
    @Column(name = "FORWARD_TO")
    private Integer forwardTo;
    
    @Column(name="T_APP_ID")
    private Integer tAppId;
    
    @Column(name = "EMP_FORWARD_TO")
    private String employeeForward;
    
    @Column(name="TASK_FORWARD")
    private Integer taskForward;

}

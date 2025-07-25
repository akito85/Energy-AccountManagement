package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_EMPLOYEE_ASSIGNMENT")
public class VW_EMPLOYEE_ASSIGNMENT extends BaseEntities implements Serializable {
    @Id
    @Column(name = "EMP_ASSIGNMENT_ID")
    private Integer assignId;

    @Column(name="EMPLOYEE_CODE")
    private String employeeCode;

    @Column(name="EMPLOYEE_ID")
    private Integer employeeId;

    @Column(name="EMPLOYEE_NAME")
    private String employeeName;

    @Column(name="JOB_NAME")
    private String jobName;

    @Column(name="POSITION")
    private String position;

    @Column(name="POSITION_ID")
    private Integer positionId;

    @Column(name="COST_CENTER")
    private String costCenter;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name="IS_MAIN")
    private Boolean isMain;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "EMPLOYEE_STATUS")
    private String employeeStatus;

    public VW_EMPLOYEE_ASSIGNMENT() {super();}
}

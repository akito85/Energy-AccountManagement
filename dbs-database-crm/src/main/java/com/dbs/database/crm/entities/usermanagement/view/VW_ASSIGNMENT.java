package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_ASSIGNMENT")
public class VW_ASSIGNMENT extends BaseEntities implements Serializable {
    @Id
    @Column(name = "EMP_ASSIGNMENT_ID")
    private Integer assignId;

    @Column(name = "EMPLOYEE_CODE")
    private String employeeCode;

    @Column(name = "JOB_ID")
    private Integer jobId;

    @Column(name = "JOB_NAME")
    private String jobName;

    @Column(name = "POSITION_ID")
    private Integer positionId;

    @Column(name = "POSITION_NAME")
    private String positionName;

    @Column(name = "START_DATE")
    private String startDate;

    @Column(name = "END_DATE")
    private String endDate;

    @Convert(converter = BooleanToYNStringConverter.class)
    @Column(name = "IS_MAIN")
    private Boolean isMain;

    @Column(name = "EMP_NUMBER")
    private String empNumber;
}

package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.CustomddMMyyyyDeserializer;
import com.dbs.common.base.utils.CustomddMMyyyySerializer;
import com.dbs.common.base.utils.CustomyyyyMMddDeserializer;
import com.dbs.common.base.utils.CustomyyyyMMddSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import lombok.CustomLog;

@Entity
@Data
@Table(name = "VW_EMPLOYEE")
public class VW_EMPLOYEE extends BaseEntities implements Serializable {
    @Id
    @Column(name = "EMPLOYEE_ID")
    private Integer employeeId;

    @Column(name = "EMPLOYEE_CODE")
    private String employeeCode;

    @Column(name = "EMP_NUMBER")
    private String empNumber;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "EMP_TYPE")
    private String empType;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "JOB_NAME")
    private String jobName;

    @Column(name = "POSITION_NAME")
    private String positionName;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ENTITY_ID")
    private String entityId;
}

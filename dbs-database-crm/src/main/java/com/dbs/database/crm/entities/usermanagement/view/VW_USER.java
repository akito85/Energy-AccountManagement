package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_USER")
public class VW_USER extends BaseEntities implements Serializable {
    @Id
    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "USER_CODE")
    private String userCode;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "USER_TYPE")
    private String userType;

    @Column(name = "AUTH_TYPE")
    private String authType;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "START_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;

    @Column(name = "END_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "USER_LEVEL")
    private String userLevel;

    @Column(name = "EMPLOYEE_ID")
    private Integer employeeId;

    @Column(name = "FULL_NAME")
    private String employeeName;

    @Column(name = "GROUP_ACCESS")
    private String groupAccess;
}

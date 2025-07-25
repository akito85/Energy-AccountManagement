package com.dbs.database.crm.entities.usermanagement.view;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "VW_POS_HIER_EMP")
public class VW_POS_HIER_EMP {
    @Id
    @Column(name = "POSITION_ID")
    private Integer positionId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CC_NAME")
    private String ccName;

    @Column(name = "entityId")
    private Integer entityId;
    
    @Column(name = "EMPLOYEE_NAME")
    private String employeeName;

}

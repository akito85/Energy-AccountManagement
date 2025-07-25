package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "VW_R_POS_HIER")
public class VW_R_POS_HIER extends BaseEntities {
    @Id
    @Column(name = "R_HIER_ID")
    private Integer rHierID;

    @Column(name="HIER_ID")
    private Integer hierId;

    @Column(name="POSITION_ID")
    private Integer positionId;

    @Column(name="POSITION")
    private String position;

    @Column(name="PARENT")
    private String parent;

    @Column(name="PARENT_ID")
    private Integer parentId;

    @Column(name="EMPLOYEE")
    private String employee;

    @Column(name="DESCRIPTION")
    private String description;

}

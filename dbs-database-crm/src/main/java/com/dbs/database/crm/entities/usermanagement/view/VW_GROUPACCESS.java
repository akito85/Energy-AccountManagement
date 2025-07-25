package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "VW_GROUPACCESS")
public class VW_GROUPACCESS extends BaseEntities implements Serializable {
    @Id
    @Column(name = "GA_ID")
    private Integer gaId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ENTITY_ID")
    private String entityId;

    @Column(name = "USER_LEVEL")
    private String userLevel;
}

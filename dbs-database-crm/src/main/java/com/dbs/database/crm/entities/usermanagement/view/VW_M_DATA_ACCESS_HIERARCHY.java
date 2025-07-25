package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import java.util.Date;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "VW_M_DATA_ACCESS_HIERARCHY")
public class VW_M_DATA_ACCESS_HIERARCHY extends BaseEntities {
    @Id
    @Column(name = "DAH_ID")
    private Integer dahId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ENTITY_ID")
    private Integer entityId;
}

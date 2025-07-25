package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "VW_R_DATA_ACCESS_HIERARCHY")
public class VW_R_DATA_ACCESS_HIERARCHY extends BaseEntities {
    @Id
    @Column(name = "RDAH_ID")
    private Integer rDahId;

    @Column(name = "DAH_ID")
    private Integer dahId;

    @Column(name = "CC_ID")
    private Integer ccId;

    @Column(name = "PARENT_ID")
    private Integer parentId;

    @Column(name = "DESCRIPTION")
    private String description;
}

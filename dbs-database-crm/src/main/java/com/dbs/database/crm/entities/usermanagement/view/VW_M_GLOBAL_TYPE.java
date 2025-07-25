package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "VW_M_GLOBAL_TYPE")
public class VW_M_GLOBAL_TYPE extends BaseEntities {
    @Id
    @Column(name = "KEY")
    private Integer key;

    @Column(name="GLB_TYPE_ID")
    private Integer glbTypeId;

    @Column(name="GROUPNAME")
    private String groupName;

    @Column(name="DESCRIPTION")
    private String description;

    @Column(name="SORT_BY")
    private String sortBy;

    @Column(name="ENTITY_ID")
    private Integer entityId;

    @Column(name = "STATUS")
    private String status;
}

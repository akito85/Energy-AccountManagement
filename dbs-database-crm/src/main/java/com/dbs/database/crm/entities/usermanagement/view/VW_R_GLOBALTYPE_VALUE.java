package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "VW_R_GLOBALTYPE_VALUE")
public class VW_R_GLOBALTYPE_VALUE extends BaseEntities {
    @Id
    @Column(name = "KEY")
    private Integer key;

    @Column(name = "GLB_TYPE_VAL_ID")
    private Integer glbTypeValId;

    @Column(name="GLB_TYPE_ID")
    private Integer glbTypeId;

    @Column(name="NAME")
    private String name;

    @Column(name="GLB_VALUE")
    private String glbValue;

    @Column(name="GLB_ORDER")
    private Integer glbOrder;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "PARENT_VALUE")
    private Integer parentValue;

    @Column(name = "PARENTNAME")
    private String parentValueName;

    @Column(name = "PARENT_GROUP")
    private Integer parentGroup;

    @Column(name = "PARENTGROUPNAME")
    private String parentGroupName;

    @Column(name = "DESCRIPTION")
    private String description;
}

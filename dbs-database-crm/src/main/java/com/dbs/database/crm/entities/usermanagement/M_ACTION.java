package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "M_ACTION")
public class M_ACTION extends BaseEntities implements Serializable {
    @Id
    @Column(name="ACTION_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_ACTION_SEQ")
    @SequenceGenerator(sequenceName = "M_ACTION_SEQ",allocationSize = 1, name = "M_ACTION_SEQ")
    private Integer actionId;

    @Column(name="NAME")
    private String name;

    @Column(name="DESCRIPTION", length = 250)
    private String description;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @Column(name = "ENTITY_ID")
    private Integer entityId;
}


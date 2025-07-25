package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "VW_POSITION")
public class VW_POSITION extends BaseEntities implements Serializable {
    @Id
    @Column(name = "POSITION_ID")
    private Integer positionId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CC_NAME")
    private String costcenter;

    @Column(name = "ENTITY_ID")
    private String entityId;

    @Convert(converter = BooleanToYNStringConverter.class)
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;
}

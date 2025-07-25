package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "VW_GLB_PROPERTIES")
public class VW_GLB_PROPERTIES extends BaseEntities implements Serializable {
    @Id
    @Column(name = "GP_ID")
    private Integer gpId;

    @Column(name = "GP_TYPE")
    private String gpType;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "GP_TYPE_NAME")
    private String gpTypeName;

    @Column(name = "ENTITY_NAME")
    private String entityName;
}

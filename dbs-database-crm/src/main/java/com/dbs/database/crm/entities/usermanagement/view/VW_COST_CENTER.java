package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "VW_COST_CENTER")
public class VW_COST_CENTER extends BaseEntities {
    @Id
    @Column(name = "CC_ID")
    private Integer ccId;

    @Column(name="CC_NAME")
    private String name;

    @Column(name="CC_CODE")
    private String code;

    @Column(name="ENTITY_ID")
    private Integer entityId;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name="IS_DELETED")
    private Boolean isDeleted;

    @Column(name="CC_TYPE")
    private String ccType;

    @Column(name="VALUE_NAME")
    private String valName;

    @Column(name="VALUE_CODE")
    private String valCode;

    @Column(name = "DESCRIPTION")
    private String description;
}

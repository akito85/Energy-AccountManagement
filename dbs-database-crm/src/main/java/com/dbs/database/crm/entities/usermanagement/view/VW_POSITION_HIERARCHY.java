package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_POSITION_HIERARCHY")
public class VW_POSITION_HIERARCHY extends BaseEntities implements Serializable {
    @Id
    @Column(name="HIER_ID", nullable = false, updatable = false)
    private Integer hierId;

    //    @NotBlank(message = "name cannot be null or empty")
    @Column(name="NAME", length = 250, unique = true)
    private String name;

    @Column(name="DESCRIPTION", length = 250)
    private String description;

    @Column(name="START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name="IS_DELETED")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isDeleted;

    @Column(name="ENTITY_ID", length = 250)
    private Integer entityId;
}

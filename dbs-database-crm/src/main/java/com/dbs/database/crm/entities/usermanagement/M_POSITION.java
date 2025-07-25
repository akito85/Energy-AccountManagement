package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "M_POSITION")
public class M_POSITION extends BaseEntities {
    @Id
    @Column(name="POSITION_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_POSITION_SEQ")
    @SequenceGenerator(sequenceName = "M_POSITION_SEQ", allocationSize = 1, name = "M_POSITION_SEQ")
    private Integer positionId;

    @Column(name="NAME")
    private String name;

    @Column(name="DESCRIPTION", length = 255)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "CC_ID",referencedColumnName = "CC_ID")
    private M_COSTCENTER ccId;

    @Column(name="ENTITY_ID")
    private Integer entityId;

    @Convert(converter=BooleanToYNStringConverter.class)
    @Column(name="IS_DELETED")
    private Boolean isDeleted;

    public M_POSITION() {
        super();
    }
}

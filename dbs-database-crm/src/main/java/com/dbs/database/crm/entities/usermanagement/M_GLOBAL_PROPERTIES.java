package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name="M_GLOBAL_PROPERTIES")
public class M_GLOBAL_PROPERTIES extends BaseEntities implements Serializable {

    @Id
    @Column(name = "GLOBAL_PROPS_ID",nullable = false,updatable = false)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "M_GLOBAL_PROPERTIES_SEQ")
    @SequenceGenerator(sequenceName = "M_GLOBAL_PROPERTIES_SEQ",allocationSize = 1, name = "M_GLOBAL_PROPERTIES_SEQ")
    private Integer gpId;

    @Column(name="GP_TYPE")
    private String gpType;

    @Column(name="NAME")
    private String name;

    @Column(name="DESCRIPTION")
    private String desc;

    @Column(name="ENTITY_ID")
    private Integer entityId;

    @Column(name="IS_DELETED")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isDeleted;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "GLOBAL_PROPS_ID",referencedColumnName = "GLOBAL_PROPS_ID")
    private List<R_GLOBAL_PROPERTIES_DTL> rGlobalPropertiesDtls;
}

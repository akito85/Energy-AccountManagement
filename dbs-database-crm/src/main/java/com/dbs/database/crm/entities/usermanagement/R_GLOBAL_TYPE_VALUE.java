package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "R_GLOBAL_TYPE_VALUE")
public class R_GLOBAL_TYPE_VALUE extends BaseEntities implements Serializable {
    @Id
    @Column(name = "GLB_TYPE_VAL_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_GLOBAL_TYPE_VALUE_SEQ")
    @SequenceGenerator(sequenceName = "R_GLOBAL_TYPE_VALUE_SEQ", allocationSize = 1, name = "R_GLOBAL_TYPE_VALUE_SEQ")
    private Integer glbTypeValId;

    @Column(name = "GLB_TYPE_ID")
    private Integer globalType;

    @Column(name = "NAME", length = 255)
    private String name;

    @Column(name = "GLB_VALUE", length = 255)
    private String glbValue;

    @Column(name = "IS_DELETED")
    @Convert(converter = BooleanToYNStringConverter.class)
    private Boolean isDeleted;

    @Column(name = "GLB_ORDER")
    private Integer glbOrder;

    @Column(name = "PARENT_VALUE")
    private Integer parentValue;

    @Column(name = "PARENT_GROUP")
    private Integer parentGroup;

    @Column(name = "DESCRIPTION", length = 255)
    private String description;

    public R_GLOBAL_TYPE_VALUE(Integer globalType, String name, String glbValue, Boolean isDeleted,
                               Integer glbOrder, Integer parentValue, Integer parentGroup, String description) {
        this.globalType = globalType;
        this.name = name;
        this.glbValue = glbValue;
        this.isDeleted = isDeleted;
        this.glbOrder = glbOrder;
        this.parentValue = parentValue;
        this.parentGroup = parentGroup;
        this.description = description;
    }
    public R_GLOBAL_TYPE_VALUE(){
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + glbTypeValId;
        }
    }

}

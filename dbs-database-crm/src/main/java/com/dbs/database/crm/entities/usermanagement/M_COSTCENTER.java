package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "M_COSTCENTER")
public class M_COSTCENTER extends BaseEntities implements Serializable {
    @Id
    @Column(name="CC_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_COSTCENTER_SEQ")
    @SequenceGenerator(sequenceName = "M_COSTCENTER_SEQ", allocationSize = 1, name = "M_COSTCENTER_SEQ")
    private Integer ccId;

    @Column(name="CC_NAME", length = 100, unique = true)
    private String name;

    @Column(name="CC_CODE", length = 50, unique = true)
    private String code;

    @Column(name = "CC_TYPE")
    private String ccType;

    @Column(name = "VALUE_NAME")
    private String valName;

    @Column(name = "VALUE_CODE")
    private String valCode;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name="ENTITY_ID")
    private Integer entityId;

    @Column(name="IS_DELETED")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isDeleted;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + ccId;
        }
    }
}

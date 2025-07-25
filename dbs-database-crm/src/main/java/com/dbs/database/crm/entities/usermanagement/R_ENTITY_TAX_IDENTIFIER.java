package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "R_ENTITY_TAX_IDENTIFIER",
        uniqueConstraints = {
                @UniqueConstraint(name = "r_entity_tax_identifier_unique_constraint",columnNames = "TAX_NUMBER")
        }
)
@Getter
@Setter
public class R_ENTITY_TAX_IDENTIFIER extends DefaultBaseEntities implements Serializable {
    @Id
    @Column(name = "TAX_ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "R_ENTITY_TAX_IDENTIFIER_SEQ")
    @SequenceGenerator(sequenceName = "R_ENTITY_TAX_IDENTIFIER_SEQ",allocationSize = 1,name = "R_ENTITY_TAX_IDENTIFIER_SEQ")
    private Integer taxId;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "TAX_NUMBER")
    private String taxNumber;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "REMARK")
    private String remark;

    @Convert(converter=BooleanToYNStringConverter.class)
    @Column(name = "IS_MAIN")
    private Boolean isMain;

    @Column(name = "STATUS")
    private String status;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + taxId;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.taxId);
        hash = 29 * hash + Objects.hashCode(this.entityId);
        hash = 29 * hash + Objects.hashCode(this.taxNumber);
        hash = 29 * hash + Objects.hashCode(this.startDate);
        hash = 29 * hash + Objects.hashCode(this.endDate);
        hash = 29 * hash + Objects.hashCode(this.remark);
        hash = 29 * hash + Objects.hashCode(this.isMain);
        hash = 29 * hash + Objects.hashCode(this.status);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final R_ENTITY_TAX_IDENTIFIER other = (R_ENTITY_TAX_IDENTIFIER) obj;
        if (!Objects.equals(this.taxNumber, other.taxNumber)) {
            return false;
        }
        if (!Objects.equals(this.remark, other.remark)) {
            return false;
        }
        if (!Objects.equals(this.taxId, other.taxId)) {
            return false;
        }
        if (!Objects.equals(this.entityId, other.entityId)) {
            return false;
        }
        if (!Objects.equals(this.startDate.getTime(), other.startDate.getTime())) {
            return false;
        }
        long endOld = 0;
        long endNew = 0;
        if (this.endDate != null) {
            endOld = this.endDate.getTime();
        }
        
        if (other.endDate != null) {
            endNew = other.endDate.getTime();
        }
        if (!Objects.equals(endOld, endNew)) {
            return false;
        }
        if (!Objects.equals(this.isMain, other.isMain)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        return true;
    }

}

package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.DefaultBaseEntities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "R_RBI_TAX_CODE_CONDITION")
public class R_RBI_TAX_CODE_CONDITION extends DefaultBaseEntities implements Serializable {
    @Id
    @Column(name="ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_RBI_TAX_CODE_CONDITION_SEQ")
    @SequenceGenerator(sequenceName = "R_RBI_TAX_CODE_CONDITION_SEQ", allocationSize = 1, name = "R_RBI_TAX_CODE_CONDITION_SEQ")
    private Integer id;
    @Column(name="TAX_CODE_ID")
    private Integer taxCodeId;
    @Column(name = "NAME")
    private String name;

    @Column(name = "OPERATOR")
    private String operator;

    @Column(name = "DATA_TYPE")
    private String dataType;
    
    @Column(name = "CONDITION_VALUE")
    private Double conditionValue;
    
    @Column(name = "START_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;

    @Column(name = "END_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;
    
    @Column(name = "STATUS")
    private String status;
    
    @Column(name = "DESCRIPTION")
    private String description;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.taxCodeId);
        hash = 37 * hash + Objects.hashCode(this.name);
        hash = 37 * hash + Objects.hashCode(this.operator);
        hash = 37 * hash + Objects.hashCode(this.dataType);
        hash = 37 * hash + Objects.hashCode(this.conditionValue);
        hash = 37 * hash + Objects.hashCode(this.startDate);
        hash = 37 * hash + Objects.hashCode(this.endDate);
        hash = 37 * hash + Objects.hashCode(this.status);
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
        final R_RBI_TAX_CODE_CONDITION other = (R_RBI_TAX_CODE_CONDITION) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.operator, other.operator)) {
            return false;
        }
        if (!Objects.equals(this.dataType, other.dataType)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.taxCodeId, other.taxCodeId)) {
            return false;
        }
        if (!Objects.equals(this.conditionValue, other.conditionValue)) {
            return false;
        }
        if (!Objects.equals(this.startDate, other.startDate)) {
            return false;
        }
        long lEndDate = 0;
        long lotherEndDate = 0;
        if (this.endDate != null) {
            lEndDate = this.endDate.getTime();
        }
        if (other.endDate != null) {
            lotherEndDate = other.endDate.getTime();
        }
        if (!Objects.equals(lEndDate, lotherEndDate)) {
            return false;
        }
        return true;
    }
    
    
}

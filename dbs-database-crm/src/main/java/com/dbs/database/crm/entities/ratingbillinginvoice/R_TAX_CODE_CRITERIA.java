package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Setter
@Getter
@Entity
@Table(name = "R_TAX_CODE_CRITERIA")
public class R_TAX_CODE_CRITERIA extends DefaultBaseEntities {

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_TAX_CODE_CRITERIA_SEQ")
    @SequenceGenerator(sequenceName = "R_TAX_CODE_CRITERIA_SEQ", allocationSize = 1, name = "R_TAX_CODE_CRITERIA_SEQ")
    private Integer id;
    @Column(name = "TAX_CODE_ID")
    private Integer taxCodeId;
    @Column(name = "CRITERIA")
    private Integer criteria;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CRITERIA", referencedColumnName = "GLB_TYPE_VAL_ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE criteriaValue;

    @Transient
//	@JsonIgnore
    public String getCriteriaName() {
        if (criteriaValue != null) {
            return getCriteriaValue().getName();
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + id;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.id);
        hash = 13 * hash + Objects.hashCode(this.taxCodeId);
        hash = 13 * hash + Objects.hashCode(this.criteria);
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
        final R_TAX_CODE_CRITERIA other = (R_TAX_CODE_CRITERIA) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.taxCodeId, other.taxCodeId)) {
            return false;
        }
        if (!Objects.equals(this.criteria, other.criteria)) {
            return false;
        }
        return true;
    }
    
}

/*
 * id number [PK] id_pricing number criteria varchar2 created_date date
 * created_by varchar2 updated_date date updated_by varchar2
 */

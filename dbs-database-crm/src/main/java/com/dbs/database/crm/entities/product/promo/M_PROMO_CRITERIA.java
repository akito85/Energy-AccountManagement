package com.dbs.database.crm.entities.product.promo;


import com.dbs.common.base.entities.DefaultBaseEntities;
import java.io.Serializable;
import java.util.Objects;

import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Entity
@Data
@Table(name = "M_PROMO_CRITERIA")
public class M_PROMO_CRITERIA extends DefaultBaseEntities implements Serializable{

    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_PROMO_CRITERIA_SEQ")
    @SequenceGenerator(sequenceName = "M_PROMO_CRITERIA_SEQ",allocationSize = 1, name = "M_PROMO_CRITERIA_SEQ")
    private Integer id;

    @Column(name = "ID_CRITERIA")
    private Integer idCriteria;

    @Column(name = "ID_PROMO")
    private Integer idPromo;

    @Column(name = "NAME")
    private String name;

    @Column(name = "STATUS")
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_CRITERIA", referencedColumnName = "GLB_TYPE_VAL_ID", insertable = false, updatable = false)
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
        hash = 13 * hash + Objects.hashCode(this.idCriteria);
        hash = 13 * hash + Objects.hashCode(this.idPromo);
        hash = 13 * hash + Objects.hashCode(this.name);
        hash = 13 * hash + Objects.hashCode(this.status);
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
        final M_PROMO_CRITERIA other = (M_PROMO_CRITERIA) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.idPromo, other.idPromo)) {
            return false;
        }
        if (!Objects.equals(this.idCriteria, other.idCriteria)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        return true;
    }
}

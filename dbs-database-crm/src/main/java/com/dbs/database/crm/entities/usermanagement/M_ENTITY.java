package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(
        name = "M_ENTITY",
        uniqueConstraints = {
                @UniqueConstraint(name = "m_entity_unique_constraint", columnNames = "ENTITY_CODE")
        }
)
public class M_ENTITY extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ENTITY_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_ENTITY_SEQ")
    @SequenceGenerator(sequenceName = "M_ENTITY_SEQ", allocationSize = 1, name = "M_ENTITY_SEQ")
    private Integer entityId;

    @Column(name = "ENTITY_NAME")
    private String entityName;

    @Column(name = "ENTITY_CODE")
    private String entityCode;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "FAX")
    private String fax;

    @Email
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "LOGO")
    private String logo;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "ENTITY_ID", referencedColumnName = "ENTITY_ID")
    private Set<R_ENTITY_TAX_IDENTIFIER> rEntityTaxIdentifier;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + entityId;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.entityId);
        hash = 53 * hash + Objects.hashCode(this.entityName);
        hash = 53 * hash + Objects.hashCode(this.entityCode);
        hash = 53 * hash + Objects.hashCode(this.address);
        hash = 53 * hash + Objects.hashCode(this.description);
        hash = 53 * hash + Objects.hashCode(this.fax);
        hash = 53 * hash + Objects.hashCode(this.email);
        hash = 53 * hash + Objects.hashCode(this.phone);
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
        final M_ENTITY other = (M_ENTITY) obj;
        if (!Objects.equals(this.entityName, other.entityName)) {
            return false;
        }
        if (!Objects.equals(this.entityCode, other.entityCode)) {
            return false;
        }
        if (!Objects.equals(this.address, other.address)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.fax, other.fax)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.phone, other.phone)) {
            return false;
        }
        return true;
    }

}

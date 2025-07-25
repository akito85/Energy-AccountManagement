package com.dbs.database.crm.entities.product.promo;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@Table(name = "M_PROMO")
public class M_PROMO extends BaseEntities implements Serializable {

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_PROMO_SEQ")
    @SequenceGenerator(sequenceName = "M_PROMO_SEQ", allocationSize = 1, name = "M_PROMO_SEQ")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "CATEGORY")
    private Integer category;

    @Column(name = "START_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;

    @Column(name = "END_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;

    @Column(name = "APPHIER_ID")
    private Integer appHierId;

    @Column(name = "TRIGGER_JSON")
    private String triggerJson;

    @Column(name = "CC_ID")
    private Integer ccId;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "STATUS_APPROVAL", length = 20)
    private String statusApproval;
    @Column(name = "STATUS", length = 20)
    private String status;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ID_PROMO", referencedColumnName = "ID")
    private List<M_PROMO_CRITERIA> mPromoCriterias;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ID_PROMO", referencedColumnName = "ID")
    private List<M_PROMO_CONDITION> mPromoConditions;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ID_PROMO", referencedColumnName = "ID")
    private List<M_PROMO_CRITERIA_DATA> mPromoCriteriaDatas;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.name);
        hash = 53 * hash + Objects.hashCode(this.description);
        hash = 53 * hash + Objects.hashCode(this.type);
        hash = 53 * hash + Objects.hashCode(this.category);
        hash = 53 * hash + Objects.hashCode(this.startDate);
        hash = 53 * hash + Objects.hashCode(this.endDate);
        hash = 53 * hash + Objects.hashCode(this.appHierId);
        hash = 53 * hash + Objects.hashCode(this.statusApproval);
        hash = 53 * hash + Objects.hashCode(this.status);
        hash = 53 * hash + Objects.hashCode(this.entityId);
        hash = 53 * hash + Objects.hashCode(this.ccId);
        hash = 53 * hash + Objects.hashCode(this.triggerJson);
        return hash;
    }
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
        final M_PROMO other = (M_PROMO) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.category, other.category)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.statusApproval, other.statusApproval)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        if (!Objects.equals(this.triggerJson, other.triggerJson)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.category, other.category)) {
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
        if (!Objects.equals(this.appHierId, other.appHierId)) {
            return false;
        }
        if (!Objects.equals(this.entityId, other.entityId)) {
            return false;
        }
        if (!Objects.equals(this.ccId, other.ccId)) {
            return false;
        }
        return true;
    }
}

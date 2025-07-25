package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "R_RBI_TOP_CRITERIA")
public class R_RBI_TOP_CRITERIA extends DefaultBaseEntities implements Serializable {
    @Id
    @Column(name = "TERM_OF_PAYMENT_CRITERIA_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_RBI_TERM_OF_PAYMENT_CRITERIA_SEQ")
    @SequenceGenerator(sequenceName = "R_RBI_TERM_OF_PAYMENT_CRITERIA_SEQ", allocationSize = 1, name = "R_RBI_TERM_OF_PAYMENT_CRITERIA_SEQ")
    private Integer termOfPaymentCriteriaId;
    @Column(name = "CRITERIA")
    private Integer criteria;
    @Column(name = "TERM_OF_PAYMENT_ID")
    private Integer termOfPaymentId;

    @ManyToOne
    @JoinColumn(name = "CRITERIA", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE criteriaValue;

    @Transient
    public String getCriteriaName() {
        if (criteriaValue != null)
            return getCriteriaValue().getName();
        else
            return null;
    }
}

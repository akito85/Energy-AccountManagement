package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "R_RBI_INVOICE_TEMPLATE_CRITERIA")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class R_RBI_INVOICE_TEMPLATE_CRITERIA extends DefaultBaseEntities implements Serializable {

    private static final long serialVersionUID = 5727242347220100416L;

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_RBI_INVOICE_TEMPLATE_CRITERIA_SEQ")
    @SequenceGenerator(sequenceName = "R_RBI_INVOICE_TEMPLATE_CRITERIA_SEQ", allocationSize = 1, name = "R_RBI_INVOICE_TEMPLATE_CRITERIA_SEQ")
    private Integer id;

    @Column(name = "INVOICE_TEMPLATE_ID", nullable = false)
    private Integer invoiceTemplateId;

    @Column(name = "CRITERIA", nullable = false)
    private Integer criteria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CRITERIA", referencedColumnName = "GLB_TYPE_VAL_ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE criteriaValue;

    @Transient
    public String getCriteriaName() {
        if (criteriaValue != null) {
            return getCriteriaValue().getName();
        } else {
            return null;
        }
    }
}

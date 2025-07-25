package com.dbs.database.crm.entities.payment;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Entity
@Table(name = "R_PAY_ACCOUNT_BANK_CRITERIA")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class R_PAY_ACCOUNT_BANK_CRITERIA extends BaseEntities implements Serializable {

    private static final long serialVersionUID = -1552650310096550260L;

    @Id
    @Column(name = "ACCOUNT_BANK_CRITERIA_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_PAY_ACCOUNT_BANK_CRITERIA_SEQ")
    @SequenceGenerator(sequenceName = "R_PAY_ACCOUNT_BANK_CRITERIA_SEQ", allocationSize = 1, name = "R_PAY_ACCOUNT_BANK_CRITERIA_SEQ")
    private Long id;

    @Column(name = "ACCOUNT_INFORMATION_ID")
    private Long accountInformationId;

    @Column(name = "CRITERIA")
    private Integer criteria;

    @ManyToOne
    @JoinColumn(name = "CRITERIA", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE criteriaValue;
}

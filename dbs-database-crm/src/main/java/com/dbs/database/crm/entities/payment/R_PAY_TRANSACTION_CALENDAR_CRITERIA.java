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
@Table(name = "R_PAY_TRANSACTION_CALENDAR_CRITERIA")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class R_PAY_TRANSACTION_CALENDAR_CRITERIA extends BaseEntities implements Serializable {
    private static final long serialVersionUID = -4442914509277658796L;

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_PAY_TRANSACTION_CALENDAR_CRITERIA_SEQ")
    @SequenceGenerator(sequenceName = "R_PAY_TRANSACTION_CALENDAR_CRITERIA_SEQ", allocationSize = 1, name = "R_PAY_TRANSACTION_CALENDAR_CRITERIA_SEQ")
    private Long id;

    @Column(name = "TRANSACTION_CALENDAR_ID")
    private Long transactionCalendarId;

    @Column(name = "CRITERIA")
    private Integer criteria;

    @ManyToOne
    @JoinColumn(name = "CRITERIA", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE criteriaValue;
}

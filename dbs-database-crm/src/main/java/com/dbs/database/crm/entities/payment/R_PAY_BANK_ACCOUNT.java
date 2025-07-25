package com.dbs.database.crm.entities.payment;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.database.crm.entities.usermanagement.M_ENTITY;
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
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "R_PAY_BANK_ACCOUNT")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class R_PAY_BANK_ACCOUNT extends BaseEntities implements Serializable {

    private static final long serialVersionUID = 8343097733554326035L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_PAY_BANK_ACCOUNT_SEQ")
    @Column(name = "ID", nullable = false, updatable = false)
    @SequenceGenerator(sequenceName = "R_PAY_BANK_ACCOUNT_SEQ", allocationSize = 1, name = "R_PAY_BANK_ACCOUNT_SEQ")
    private Long id;

    @Column(name = "BANK_ID")
    private Long bankId;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "ACCOUNT_NAME")
    private String accountName;

    @Column(name = "CURRENCY_ID")
    private Integer currencyId;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "TOTAL_DIGIT")
    private Integer totalDigit;

    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;

    @Column(name = "BRANCH_NAME")
    private String branchName;

    @Column(name = "TYPE_ID")
    private Integer typeId;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IS_VA")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isVa;

    @Column(name = "STATIC_CODE")
    private String staticCode;

    @Column(name = "APPHIER_ID")
    private Integer appHierId;

    @Column(name = "LEDGER_ACCOUNT")
    private String ledgerAccount;

    @Column(name = "TRIGGER_JSON")
    private String triggerJson;

    @OneToOne
    @JoinColumn(name = "BANK_ACCOUNT_GL_ID", referencedColumnName = "ID")
    private R_PAY_BANK_ACCOUNT_GL bankAccountGl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CURRENCY_ID", referencedColumnName = "GLB_TYPE_VAL_ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE currency;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE_ID", referencedColumnName = "GLB_TYPE_VAL_ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ENTITY_ID", referencedColumnName = "ENTITY_ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private M_ENTITY entity;
}

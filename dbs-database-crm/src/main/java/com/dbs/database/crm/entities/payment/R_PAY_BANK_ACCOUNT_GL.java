package com.dbs.database.crm.entities.payment;

import com.dbs.common.base.entities.DefaultBaseEntities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "R_PAY_BANK_ACCOUNT_GL")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class R_PAY_BANK_ACCOUNT_GL extends DefaultBaseEntities implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_PAY_BANK_ACCOUNT_GL_SEQ")
    @Column(name = "ID", nullable = false, updatable = false)
    @SequenceGenerator(sequenceName = "R_PAY_BANK_ACCOUNT_GL_SEQ", allocationSize = 1, name = "R_PAY_BANK_ACCOUNT_GL_SEQ")
    private Long id;

    @Column(name = "CASH_ID")
    private Integer cashId;

    @Column(name = "RECEIPT_CONFIRMATION_ID")
    private Integer receiptConfirmationId;

    @Column(name = "REMITTANCE_ID")
    private Integer remittanceId;

    @Column(name = "FACTORING_ID")
    private Integer factoringId;

    @Column(name = "SHORT_TERM_DEBT_ID")
    private Integer shortTermDebtId;

    @Column(name = "BANK_CHARGES_ID")
    private Integer bankChargesId;

    @Column(name = "UNAPPLIED_RECEIPT_ID")
    private Integer unAppliedReceiptId;

    @Column(name = "UNIDENTIFIED_RECEIPT_ID")
    private Integer unIdentifiedReceiptId;

    @Column(name = "ON_ACCOUNT_RECEIPT_ID")
    private Integer onAccountReceiptId;

    @Column(name = "UNEARNED_DISCOUNT_ID")
    private Integer unEarnedDiscountId;

    @Column(name = "EARNED_DISCOUNT_ID")
    private Integer earnedDiscountId;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CASH_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private M_PAY_GL_ACCOUNT cash;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RECEIPT_CONFIRMATION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private M_PAY_GL_ACCOUNT receiptConfirmation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REMITTANCE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private M_PAY_GL_ACCOUNT remittance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FACTORING_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private M_PAY_GL_ACCOUNT factoring;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SHORT_TERM_DEBT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private M_PAY_GL_ACCOUNT shortTermDebt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BANK_CHARGES_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private M_PAY_GL_ACCOUNT bankCharges;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UNAPPLIED_RECEIPT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private M_PAY_GL_ACCOUNT unAppliedReceipt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UNIDENTIFIED_RECEIPT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private M_PAY_GL_ACCOUNT unIdentifiedReceipt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ON_ACCOUNT_RECEIPT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private M_PAY_GL_ACCOUNT onAccountReceipt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UNEARNED_DISCOUNT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private M_PAY_GL_ACCOUNT unEarnedDiscount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EARNED_DISCOUNT_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private M_PAY_GL_ACCOUNT earnedDiscount;
}

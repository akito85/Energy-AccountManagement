package com.dbs.database.crm.entities.payment.view;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "VW_PAY_BANK_ACCOUNT")
@Data
public class VW_PAY_BANK_ACCOUNT extends BaseEntities implements Serializable {

    private static final long serialVersionUID = 7731999172902729520L;

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "BANK_ID")
    private Long bankId;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "ACCOUNT_NAME")
    private String accountName;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "ENTITY_NAME")
    private String entityName;

    @Column(name = "TOTAL_DIGIT")
    private Integer totalDigit;

    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;

    @Column(name = "BRANCH_NAME")
    private String branchName;

    @Column(name = "TYPE")
    private String type;

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

    @Column(name = "LEDGER_ACCOUNT")
    private String ledgerAccount;

    @Column(name = "CASH")
    private String cash;

    @Column(name = "RECEIPT_CONFIRMATION")
    private String receiptConfirmation;

    @Column(name = "REMITTANCE")
    private String remittance;

    @Column(name = "FACTORING")
    private String factoring;

    @Column(name = "SHORT_TERM_DEBT")
    private String shortTermDebt;

    @Column(name = "BANK_CHARGES")
    private String bankCharges;

    @Column(name = "UNAPPLIED_RECEIPT")
    private String unAppliedReceipt;

    @Column(name = "UNIDENTIFIED_RECEIPT")
    private String unIdentifiedReceipt;

    @Column(name = "ON_ACCOUNT_RECEIPT")
    private String onAccountReceipt;

    @Column(name = "UNEARNED_DISCOUNT")
    private String unEarnedDiscount;

    @Column(name = "EARNED_DISCOUNT")
    private String earnedDiscount;

    @Column(name = "DESCRIPTION_GL")
    private String descriptionGl;
}

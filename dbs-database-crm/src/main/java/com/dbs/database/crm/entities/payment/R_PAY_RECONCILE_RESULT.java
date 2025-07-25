package com.dbs.database.crm.entities.payment;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "R_PAY_RECONCILE_RESULT")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class R_PAY_RECONCILE_RESULT extends BaseEntities implements Serializable {

    private static final long serialVersionUID = 5341013624833054117L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_PAY_RECONCILE_RESULT_SEQ")
    @Column(name = "RECONCILE_RESULT_ID", nullable = false)
    @SequenceGenerator(sequenceName = "R_PAY_RECONCILE_RESULT_SEQ", allocationSize = 1, name = "R_PAY_RECONCILE_RESULT_SEQ")
    private Long id;

    @Column(name = "BANK_STATEMENT_ID")
    private Long bankStatementId;

    @Column(name = "RECEIPT_NUMBER")
    private String receiptNumber;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "RECEIPT_DATE")
    private Date receiptDate;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "IS_SOLVED")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isSolved;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;

    @Column(name = "APPHIER_ID")
    private Integer appHierId;

    @Column(name = "TRANSACTION_TYPE")
    private String transactionType;

    @Column(name = "TRANSACTION_CODE")
    private String transactionCode;
}

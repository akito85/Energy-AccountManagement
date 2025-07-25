package com.dbs.database.crm.entities.payment.view;

import com.dbs.common.base.entities.BaseEntities;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "VW_RECONCILE_RESULT")
public class VW_RECONCILE_RESULT extends BaseEntities implements Serializable {

    private static final long serialVersionUID = 8564494692680665948L;

    @Id
    @Column(name = "RECONCILE_RESULT_ID", nullable = false)
    private Long id;

    @Column(name = "BANK_STATEMENT_ID")
    private Long bankStatementId;

    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "ACCOUNT_NAME")
    private String accountName;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "CUSTOMER_NUMBER")
    private String customerNumber;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "AMOUNT")
    private String amount;

    @Column(name = "AMOUNT_REAL")
    private BigDecimal amountReal;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "SOR")
    private String sor;

    @Column(name = "CC_NAME")
    private String costCenter;

    @Column(name = "RECEIPT_NUMBER")
    private String receiptNumber;

    @Column(name = "RECEIPT_DATE")
    private Date receiptDate;

    @Column(name = "RECEIPT_CODE")
    private String receiptCode;

    @Column(name = "APPHIER_ID")
    private Integer appHierId;
}

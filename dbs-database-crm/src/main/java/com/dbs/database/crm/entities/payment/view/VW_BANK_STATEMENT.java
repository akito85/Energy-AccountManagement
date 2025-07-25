package com.dbs.database.crm.entities.payment.view;

import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "VW_BANK_STATEMENT")
@Data
public class VW_BANK_STATEMENT extends DefaultBaseEntities implements Serializable {

    private static final long serialVersionUID = -3371319093397899395L;

    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "STATUS_BANK_STATEMENT")
    private String statusBankStatement;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "BANK_ACCOUNT_NUMBER")
    private String bankAccountNumber;

    @Column(name = "COLLECTING_AGENT")
    private String collectingAgent;

    @Column(name = "PAYMENT_TYPE")
    private String paymentType;

    @Column(name = "BANK_STATEMENT_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATETIME, timezone = Constant.TIMEZONE)
    private Date bankStatementDate;

    @Column(name = "FILENAME")
    private String filename;

    @Column(name = "SOURCE_FILE")
    private String sourceFile;

    @Column(name = "TOTAL_TRANSACTION")
    private Integer totalTransaction;

    @Column(name = "TOTAL_MATCH")
    private Integer totalMatch;

    @Column(name = "TOTAL_FORCE")
    private Integer totalForce;

    @Column(name = "TOTAL_REVERSE")
    private Integer totalReverse;

    @Column(name = "TOTAL_SUNDRY")
    private Integer totalSundry;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "ERROR_MESSAGE")
    private String errorMessage;

    @Column(name = "TOTAL_AMOUNT")
    private String totalAmount;

    @Column(name = "TOTAL_AMOUNT_REAL")
    private BigDecimal totalAmountReal;

    @Column(name = "TOTAL_MATCH_AMOUNT")
    private String totalMatchAmount;

    @Column(name = "TOTAL_MATCH_AMOUNT_REAL")
    private BigDecimal totalMatchAmountReal;

    @Column(name = "UPLOAD_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATETIME_VIEW, timezone = Constant.TIMEZONE)
    private Date uploadDate;
}

package com.dbs.database.crm.entities.payment;

import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "M_BANK_STATEMENT")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class M_BANK_STATEMENT extends DefaultBaseEntities implements Serializable {

    private static final long serialVersionUID = -2655935528401807986L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_BANK_STATEMENT_SEQ")
    @Column(name = "BANK_STATEMENT_ID", nullable = false)
    @SequenceGenerator(sequenceName = "M_BANK_STATEMENT_SEQ", allocationSize = 1, name = "M_BANK_STATEMENT_SEQ")
    private Long id;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "BANK_ACCOUNT_NUMBER")
    private String bankAccountNumber;

    @Column(name = "COLLECTING_AGENT")
    private String collectingAgent;

    @Column(name = "PAYMENT_TYPE")
    private String paymentType;

    @Column(name = "PAYMENT_GATEWAY")
    private String paymentGateway;

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

    @Column(name = "ERROR_MESSAGE")
    private String errorMessage;

    @Column(name = "STATUS_BANK_STATEMENT")
    private String statusBankStatement;

    @Column(name = "PARSING_RESULT")
    private String parsingResult;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @Column(name = "TOTAL_ON_PROCESS")
    private Integer totalOnProcess;

    @Column(name = "TOTAL_MATCH_AMOUNT")
    private BigDecimal totalMatchAmount;

    @Column(name = "ACCOUNT_INFORMATION_ID")
    private Long accountInformationId;

    @Column(name = "RECEIPT_CHANNEL_ID")
    private Integer receiptChannelId;

    @Column(name = "COLLECTING_AGENT_ID")
    private Integer collectingAgentId;

    @Column(name = "UUID")
    private String uuid;

    @Column(name = "BANK_ID")
    private Long bankId;
}

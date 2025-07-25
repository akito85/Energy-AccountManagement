package com.dbs.database.crm.entities.payment;

import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "M_RECEIPT")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class M_RECEIPT extends DefaultBaseEntities implements Serializable {

    private static final long serialVersionUID = 135384555960060563L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_RECEIPT_SEQ")
    @Column(name = "RECEIPT_ID", nullable = false, updatable = false)
    @SequenceGenerator(sequenceName = "M_RECEIPT_SEQ", allocationSize = 1, name = "M_RECEIPT_SEQ")
    private Long id;

    @Column(name = "AREA_CODE")
    private String areaCode;

    @Column(name = "CUSTOMER_NUMBER")
    private String customerNumber;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "RECEIPT_DATE")
    private Date receiptDate;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "PAYMENT_TYPE")
    private String paymentType;

    @Column(name = "METHOD")
    private String method;

    @Column(name = "RECEIPT_NUMBER")
    private String receiptNumber;

    @Column(name = "RECEIPT_CHANNEL")
    private String receiptChannel;

    @Column(name = "BANK")
    private String bank;

    @Column(name = "COLLECTING_AGENT")
    private String collectingAgent;

    @Column(name = "DELIVERY_CHANNEL")
    private String deliveryChannel;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_ONLINE_PAYMENT")
    private boolean isOnlinePayment;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_RECONCILED")
    private boolean isReconciled;

    @Column(name = "RATE_TYPE")
    private String rateType;

    @Column(name = "RATE_DATE")
    private Date rateDate;

    @Column(name = "RATE_AMOUNT")
    private BigDecimal rateAmount;

    @Column(name = "CONVERTED_CURRENCY")
    private String convertedCurrency;

    @Column(name = "EQUIVALENT_AMOUNT")
    private BigDecimal equivalentAmount;

    @Column(name = "REFERENCE_NUMBER")
    private String referenceNumber;

    @Column(name = "SOURCE")
    private String source;

    @Column(name = "BANK_STATEMENT_DATE")
    private Date bankStatementDate;

    @Column(name = "APPROVAL_STATUS")
    private String approvalStatus;

    @Column(name = "STATUS_RECEIPT")
    private String statusReceipt;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "APPLIED_AMOUNT")
    private BigDecimal appliedAmount;

    @Column(name = "UN_APPLIED_AMOUNT")
    private BigDecimal unAppliedAmount;

    @Column(name = "EQUIVALEN_UN_APPLY_AMOUNT")
    private BigDecimal equivalentUnApplyAmount;

    @Column(name = "REFUND_AMOUNT")
    private BigDecimal refundAmount;

    @Column(name = "TRANSFER_AMOUNT")
    private BigDecimal transferAmount;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted;

    @Column(name = "EQUIVALEN_APPLIED_AMOUNT")
    private BigDecimal equivalentAppliedAmount;

    @Column(name = "COST_CENTER")
    private String costCenter;

    @Column(name = "SOR")
    private String sor;

    @Column(name = "SEGMENT")
    private String segment;

    @Column(name = "PAYMENT_GATEWAY")
    private String paymentGateway;

    @Column(name = "ACCOUNT_TYPE")
    private String accountType;

    @Column(name = "ACCOUNT_NAME")
    private String accountName;

    @Column(name = "BANK_STATEMENT_ID")
    private Long bankStatementId;

    @Column(name = "STATUS_RECONCILE")
    private String statusReconcile;

    @Column(name = "RECEIPT_CODE")
    private String receiptCode;

    @Column(name = "RECONCILE_RESULT_ID")
    private Long reconcileResultId;

    @Column(name = "TRANSACTION_PERIOD_ID")
    private Long transactionPeriodId;
}

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

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "M_PAY_RECEIPT")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class M_PAY_RECEIPT extends BaseEntities implements Serializable {

    private static final long serialVersionUID = -7466391772727000248L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_PAY_RECEIPT_SEQ")
    @Column(name = "ID", nullable = false, updatable = false)
    @SequenceGenerator(sequenceName = "M_PAY_RECEIPT_SEQ", allocationSize = 1, name = "M_PAY_RECEIPT_SEQ")
    private Long id;

    @Column(name = "STATUS_APPROVAL", nullable = false)
    private String statusApproval;

    @Column(name = "CUSTOMER_ID")
    private Integer customerId;

    @Column(name = "ACCOUNT_ID")
    private Integer accountId;

    @Column(name = "AREA_ID")
    private Integer areaId;

    @Column(name = "SEGMENT_ID")
    private Integer segmentId;

    @Column(name = "RECEIPT_CHANNEL_ID", nullable = false)
    private Integer receiptChannelId;

    @Column(name = "PAYMENT_GATEWAY_ID", nullable = false)
    private Integer paymentGatewayId;

    @Column(name = "COLLECTING_AGENT_ID", nullable = false)
    private Integer collectingAgentId;

    @Column(name = "DELIVERY_CHANNEL_ID", nullable = false)
    private Integer deliveryChannelId;

    @Column(name = "PAYMENT_METHOD_ID", nullable = false)
    private Integer paymentMethodId;

    @Column(name = "PAYMENT_TYPE_ID", nullable = false)
    private Integer paymentTypeId;

    @Column(name = "BANK_ID", nullable = false)
    private Integer bankId;

    @Column(name = "CURRENCY_ID", nullable = false)
    private Integer currencyId;

    @Column(name = "RATE_TYPE_ID", nullable = false)
    private Integer rateTypeId;

    @Column(name = "RECEIPT_DATE", nullable = false)
    private Date receiptDate;

    @Column(name = "REFERENCE_NUMBER")
    private String referenceNumber;

    @Column(name = "AMOUNT", nullable = false)
    private BigDecimal amount;

    @Column(name = "EQUIVALENT_AMOUNT")
    private BigDecimal equivalentAmount;

    @Column(name = "RATE_DATE", nullable = false)
    private Date rateDate;

    @Column(name = "RATE_AMOUNT", nullable = false)
    private BigDecimal rateAmount;

    @Column(name = "CONVERTED_CURRENCY")
    private String convertedCurrency;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "BANK_STATEMENT_ID")
    private Long bankStatementId;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_RECONCILED")
    private Boolean isReconciled;

    @Column(name = "APPLIED_AMOUNT")
    private BigDecimal appliedAmount;

    @Column(name = "EQUIVALENT_APPLIED_AMOUNT")
    private BigDecimal equivalentAppliedAmount;

    @Column(name = "UN_APPLIED_AMOUNT")
    private BigDecimal unAppliedAmount;

    @Column(name = "EQUIVALENT_UN_APPLIED_AMOUNT")
    private BigDecimal equivalentUnAppliedAmount;

    @Column(name = "REFUND_AMOUNT")
    private BigDecimal refundAmount;

    @Column(name = "TRANSFER_AMOUNT")
    private BigDecimal transferAmount;

    @Column(name = "APP_HIER_ID")
    private Integer appHierId;

    @Column(name = "RECONCILE_RESULT_ID")
    private Long reconcileResultId;

    @Column(name = "TRANSACTION_PERIOD_ID")
    private Long transactionPeriodId;

    @Column(name = "CC_ID")
    private Integer ccId;

    @Column(name = "RECEIPT_CODE")
    private String receiptCode;

    @Column(name = "RECEIPT_NUMBER")
    private String receiptNumber;

    @Column(name = "SOURCE")
    private String source;

    @Column(name = "PAYMENT_CYCLE")
    private String paymentCycle;

    @Column(name = "PAYMENT_PERIOD")
    private String paymentPeriod;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_MISC")
    private Boolean isMisc;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;
}

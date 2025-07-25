package com.dbs.database.crm.entities.payment;

import com.dbs.common.base.entities.BaseEntities;
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
@Table(name = "R_PAY_RECEIPT_ALLOCATION")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class R_PAY_RECEIPT_ALLOCATION extends BaseEntities implements Serializable {

    private static final long serialVersionUID = 8020947627854883612L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_PAY_RECEIPT_ALLOCATION_SEQ")
    @Column(name = "ALLOCATION_ID", nullable = false)
    @SequenceGenerator(sequenceName = "R_PAY_RECEIPT_ALLOCATION_SEQ", allocationSize = 1, name = "R_PAY_RECEIPT_ALLOCATION_SEQ")
    private Long id;

    @Column(name = "ALLOCATION_NUMBER")
    private String allocationNumber;

    @Column(name = "ALLOCATION_TYPE")
    private String allocationType;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "CONVERTED_CURRENCY")
    private String convertedCurrency;

    @Column(name = "AMOUNT_EQUIVALENT")
    private BigDecimal amountEquivalent;

    @Column(name = "BILLING_BUCKET_ID")
    private Integer billingBucketId;

    @Column(name = "RECEIPT_ID")
    private Long receiptId;

    @Column(name = "PAYMENT_METHOD_ID")
    private Long paymentMethodId;

    @Column(name = "ALLOCATION_STATUS")
    private String allocationStatus;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "ALLOCATION_DATE")
    private Date allocationDate;
}

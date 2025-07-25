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

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "T_PAY_LATE_CHARGE")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class T_PAY_LATE_CHARGE extends BaseEntities implements Serializable {

    private static final long serialVersionUID = 8945441771086364039L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "T_PAY_LATE_CHARGE_SEQ")
    @Column(name = "ID", nullable = false)
    @SequenceGenerator(sequenceName = "T_PAY_LATE_CHARGE_SEQ", allocationSize = 1, name = "T_PAY_LATE_CHARGE_SEQ")
    private Long id;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @Column(name = "EXPIRED_DATE")
    private Date expiredDate;

    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;

    @Column(name = "APPHIER_ID")
    private Integer appHierId;

    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;

    @Column(name = "BILL_STATUS")
    private String billStatus;

    @Column(name = "TOTAL_PERIOD_BILL")
    private Integer totalPeriodBill;

    @Column(name = "GAP_PAYMENT_WARRANTY")
    private Integer gapPaymentWarranty;

    @Column(name = "BILLING_CODE")
    private String billingCode;

    @Column(name = "BILLING_PERIOD")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date billingPeriod;

    @Column(name = "BILLING_CYCLE")
    private String billingCycle;
}

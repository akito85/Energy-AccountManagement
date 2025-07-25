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

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "T_PAY_LATE_CHARGE_V2")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class T_PAY_LATE_CHARGE_V2 extends BaseEntities implements Serializable {

    private static final long serialVersionUID = -2275463427805830318L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "T_PAY_LATE_CHARGE_V2_SEQ")
    @Column(name = "ID", nullable = false)
    @SequenceGenerator(sequenceName = "T_PAY_LATE_CHARGE_V2_SEQ", allocationSize = 1, name = "T_PAY_LATE_CHARGE_V2_SEQ")
    private Long id;

    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @Column(name = "BILL_STATUS")
    private String billStatus;

    @Column(name = "TOTAL_PERIOD_BILL")
    private Integer totalPeriodBill;

    @Column(name = "BILLING_CODE")
    private String billingCode;

    @Column(name = "CONSTANT")
    private BigDecimal constant;

    @Column(name = "TIME_UNIT")
    private String timeUnit;
}

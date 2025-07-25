package com.dbs.database.crm.entities.payment;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "R_PAY_TRANSACTION_PERIOD")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class R_PAY_TRANSACTION_PERIOD extends BaseEntities implements Serializable {

    private static final long serialVersionUID = 4156944913824034215L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_PAY_TRANSACTION_PERIOD_SEQ")
    @Column(name = "TRANSACTION_PERIOD_ID", nullable = false)
    @SequenceGenerator(sequenceName = "R_PAY_TRANSACTION_PERIOD_SEQ", allocationSize = 1, name = "R_PAY_TRANSACTION_PERIOD_SEQ")
    private Long id;

    @Column(name = "TRANSACTION_CALENDAR_ID")
    private Long transactionCalendarId;

    @Column(name = "PERIOD", nullable = false)
    private String period;

    @Column(name = "START_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date startDate;

    @Column(name = "END_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date endDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "BILLING_TRANSACTION_ID")
    private Integer billingTransactionId;

    @Column(name = "REMARK")
    private String remark;
}

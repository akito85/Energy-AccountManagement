package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "T_RBI_POS_BUCKET")
public class T_RBI_POS_BUCKET implements Serializable {
    @Id
    @Column(name = "POS_BUCKET_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "T_RBI_POS_BUCKET_SEQ")
    @SequenceGenerator(sequenceName = "T_RBI_POS_BUCKET_SEQ", allocationSize = 1, name = "T_RBI_POS_BUCKET_SEQ")
    private Integer posBucketId;

    @Column(name = "POS_NUMBER")
    private String posNumber;

    @Column(name = "BILLING_ITEM")
    private String billingItem;

    @Column(name = "BILLING_ITEM_CODE")
    private String billingItemCode;

    @Column(name = "BILLING_PERIOD")
    private Date billingPeriod;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "TOTAL_AMOUNT")
    private Double totalAmount;

    @Column(name = "PAID_AMOUNT")
    private Double paidAmount;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "PRIORITY")
    private Boolean priority;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "LATE_CHARGE")
    private Boolean lateCharge;

    @Column(name = "DUE_DATE")
    private Date dueDate;

    @Column(name = "PAYMENT_STATUS")
    private String paymentStatus;

    @Column(name = "SEQUENCE")
    private Integer sequence;

    @Column(name = "PRIORITY_PERIOD")
    private Integer priorityPeriod;

    @Column(name = "BILLING_TYPE")
    private Integer billingType;
}

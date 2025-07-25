package com.dbs.database.crm.entities.payment.view;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_SUPPORT_BUCKET_UN_APP")
public class VW_SUPPORT_BUCKET_UN_APP implements Serializable {

    private static final long serialVersionUID = -8935422649948895535L;

    @Id
    @Column(name = "RATING_CODE")
    private String ratingCode;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "BILLING_PERIOD")
    private Date billingPeriod;

    @Column(name = "PRIORITY")
    @Convert(converter= BooleanToYNStringConverter.class)
    private boolean priority;

    @Column(name = "PRIORITY_PERIOD")
    private Integer priorityPeriod;

    @Column(name = "SEQUENCE")
    private Integer sequence;

    @Column(name = "BILLING_TYPE")
    private Integer billingType;

    @Column(name = "LATE_CHARGE")
    @Convert(converter= BooleanToYNStringConverter.class)
    private boolean lateCharge;
}

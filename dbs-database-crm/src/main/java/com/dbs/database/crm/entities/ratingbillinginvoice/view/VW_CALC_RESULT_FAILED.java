package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_CALC_RESULT_FAILED")
public class VW_CALC_RESULT_FAILED {
    @Id
    @Column(name = "ID_RESULT")
    private Integer resultId;

    @Column(name = "CUSTOMER_NUMBER")
    private String custNumb;

    @Column(name = "CUSTOMER_NAME")
    private String custName;

    @Column(name = "ACCOUNT_NUMBER")
    private String accNumb;

    @Column(name = "ACCOUNT_NAME")
    private String accName;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "IS_TRY")
    private Boolean isTry;

    @Column(name = "CALCULATION_CODE")
    private String calCode;

    @Column(name = "CALCULATION_TYPE")
    private Integer calType;

    @Column(name = "CALCULATION_TYPE_VALUE")
    private String calTypeVal;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "STATUS")
    private String status;
}

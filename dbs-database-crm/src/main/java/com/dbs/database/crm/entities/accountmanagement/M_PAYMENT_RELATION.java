package com.dbs.database.crm.entities.accountmanagement;

import java.io.Serializable;
import java.util.Date;

import com.dbs.common.base.entities.BaseEntities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "M_PAYMENT_RELATION")
public class M_PAYMENT_RELATION extends BaseEntities implements Serializable {

    @Id
    @Column(name = "ID", nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_PAYMENT_RELATION_SEQ")
    @SequenceGenerator(sequenceName = "M_PAYMENT_RELATION_SEQ",allocationSize = 1, name = "M_PAYMENT_RELATION_SEQ")
    private Integer id;

    @Column(name = "ACCOUNT_ID")
    private Integer accountId;
    
    @Column(name = "ACCOUNT_TO_PAY")
    private Integer accountToPay;

    @Column(name = "PRIORITY")
    private Integer priority;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE", nullable = true)
    private Date endDate;
}

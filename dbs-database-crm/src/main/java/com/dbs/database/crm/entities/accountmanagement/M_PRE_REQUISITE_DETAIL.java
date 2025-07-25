package com.dbs.database.crm.entities.accountmanagement;

import java.io.Serializable;
import java.util.Date;

import com.dbs.common.base.entities.BaseEntities;

import lombok.Data;

import javax.persistence.*;
 
@Entity
@Data
@Table(name = "M_PRE_REQUISITE_DETAIL")
public class M_PRE_REQUISITE_DETAIL extends BaseEntities implements Serializable{
    @Id
    @Column(name = "ID", nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_PRE_REQUISITE_DETAIL_SEQ")
    @SequenceGenerator(sequenceName = "M_PRE_REQUISITE_DETAIL_SEQ",allocationSize = 1, name = "M_PRE_REQUISITE_DETAIL_SEQ")
    private Integer id;

    @Column(name = "PRE_REQUISITE_ID")
    private Integer preRequisiteId;

    @Column(name = "PAYMENT_ORDER")
    private Integer paymentOrder;

    @Column(name = "BILL_AMOUNT")
    private Double billAmount;

    @Column(name = "DUE_DATE")
    private Date dueDate;

    @Column(name = "PAID_BILL_AMOUNT")
    private Double paidBillAmount;

    @Column(name = "UNPAID_BILL_AMOUNT")
    private Double unpaidBillAmount;

    @Column(name = "PAYMENT_DATE")
    private Date paymentDate;
}

package com.dbs.database.crm.entities.payment.view;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "VW_PAY_PAYMENT_ITEM")
@Data
public class VW_PAY_PAYMENT_ITEM extends BaseEntities implements Serializable {

    private static final long serialVersionUID = -1725558156655589863L;

    @Id
    @Column(name = "PAYMENT_ITEM_ID")
    private Integer id;

    @Column(name = "PAYMENT_ITEM_CODE")
    private String paymentItemCode;

    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "APPHIER_ID")
    private Integer apphierId;

    @Column(name = "IS_BANK_METHOD")
    private String isBankMethod;
}












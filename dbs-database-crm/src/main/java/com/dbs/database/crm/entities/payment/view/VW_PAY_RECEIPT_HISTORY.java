package com.dbs.database.crm.entities.payment.view;

import com.dbs.common.base.entities.DefaultBaseEntities;
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
@Data
@Table(name = "VW_PAY_RECEIPT_HISTORY")
public class VW_PAY_RECEIPT_HISTORY extends DefaultBaseEntities implements Serializable {

    private static final long serialVersionUID = 6792445863219531998L;

    @Id
    @Column(name = "RECEIPT_ID", nullable = false)
    private Long receiptId;

    @Column(name = "COST_CENTER")
    private String costCenter;

    @Column(name = "CUSTOMER_NUMBER")
    private String customerNumber;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "RECEIPT_DATE")
    private Date receiptDate;
}

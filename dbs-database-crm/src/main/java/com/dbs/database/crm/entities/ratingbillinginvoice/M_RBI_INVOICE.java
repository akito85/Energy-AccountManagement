package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.DefaultBaseEntities;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "M_RBI_INVOICE")
public class M_RBI_INVOICE extends DefaultBaseEntities implements Serializable {
    @Id
    @Column(name = "INVOICE_NUMBER", nullable = false)
    private String invoiceNumber;

    @Column(name = "TEMPLATE")
    private Integer template;

    @Column(name = "BILLING_CODE")
    private String billingCode;

    @Column(name = "BILLING_TYPE")
    private String billingType;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "PREFIX")
    private String prefix;

    @Column (name = "COST_CENTER_ID")
    private Integer ccId;
}

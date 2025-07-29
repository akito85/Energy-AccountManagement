package com.dbs.database.crm.entities.report;

import com.dbs.common.base.entities.DefaultBaseEntities;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_REPORT_AGREEMENT")
public class VW_REPORT_AGREEMENT extends DefaultBaseEntities {
    @Id
    @Column(name = "SEQUENCE")
    private Integer sequence;
    @Column(name = "ACCOUNT_ID")
    private Integer accountId;
    @Column(name = "SOR")
    private String sor;
    @Column(name = "AREA")
    private String area;
    @Column(name = "NO_REF")
    private String noRef;
    @Column(name = "ACCOUNT_NAME")
    private String accountName;
    @Column(name = "SA_NUMBER")
    private String saNumber;
    @Column(name = "SA_NAME")
    private String saName;
    @Column(name = "SA_REFERENCE_NUMBER")
    private String saReferenceNum;
    @Column(name = "SA_TYPE")
    private String saType;
    @Column(name = "PJBG_TYPE")
    private String pjbgType;
    @Column(name = "SA_DATE")
    private Date saDate;
    @Column(name = "START_DATE")
    private Date startDate;
    @Column(name = "END_DATE")
    private Date endDate;
    @Column(name = "UOM")
    private String uom;
    @Column(name = "TEKANAN")
    private String tekanan;
    @Column(name = "TEKANAN_KONTRAK")
    private String tekananKontrak;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "MIN")
    private Integer min;
    @Column(name = "MAX")
    private Integer max;
    @Column(name = "PRODUCT_NAME")
    private String productName;
    @Column(name = "PRICE_CODE")
    private String priceCode;
    @Column(name = "BILLING_CYCLE")
    private String billingCycle;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "SEGMENT")
    private String segment;
    @Column(name = "CUST_TYPE")
    private String custType;
    @Column(name = "COST_CENTER_ID")
    private Integer costCenterId;

}

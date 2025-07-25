package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "VW_RBI_INVOICE_TEMPLATE")
public class VW_RBI_INVOICE_TEMPLATE extends BaseEntities implements Serializable {

    private static final long serialVersionUID = 8156715331590386448L;

    @Id
    @Column(name = "INVOICE_TEMPLATE_ID")
    private Integer id;

    @Column(name = "INVOICE_NAME")
    private String invoiceName;

    @Column(name = "INVOICE_TYPE")
    private String invoiceType;

    @Column(name = "METERAI")
    private String meterai;

    @Column(name = "SIGNATURE")
    private String signature;

    @Column(name = "TEMPLATE_NAME")
    private String templateName;

    @Column(name = "CRITERIAS")
    private String criterias;

    @Column(name = "CRITERIAS_DOWNLOAD")
    private String criteriasDownload;

    @Column(name = "START_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date startDate;

    @Column(name = "END_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date endDate;

    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "CC_ID")
    private Integer ccId;
}

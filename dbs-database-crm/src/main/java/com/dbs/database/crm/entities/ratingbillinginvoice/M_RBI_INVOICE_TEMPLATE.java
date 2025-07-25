package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "M_RBI_INVOICE_TEMPLATE")
public class M_RBI_INVOICE_TEMPLATE extends BaseEntities implements Serializable {

    private static final long serialVersionUID = 8390952141304890219L;

    @Id
    @Column(name = "INVOICE_TEMPLATE_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_RBI_INVOICE_TEMPLATE_SEQ")
    @SequenceGenerator(sequenceName = "M_RBI_INVOICE_TEMPLATE_SEQ", allocationSize = 1, name = "M_RBI_INVOICE_TEMPLATE_SEQ")
    private Integer id;

    @Column(name = "INVOICE_NAME")
    private String invoiceName;

    @Column(name = "INVOICE_TYPE")
    private Integer invoiceType;

    @Column(name = "METERAI")
    private Integer meterai;

    @Column(name = "SIGNATURE")
    private Integer signature;

    @Column(name = "TEMPLATE")
    private Integer template;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "APPROVAL_HIERARCHY")
    private Integer approvalHierarchy;

    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "TRIGGER_JSON")
    private String triggerJson;

    @Column(name = "CC_ID")
    private Integer ccId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "INVOICE_TEMPLATE_ID",referencedColumnName = "INVOICE_TEMPLATE_ID")
    private List<R_RBI_INVOICE_TEMPLATE_CRITERIA> criteriaList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "INVOICE_TEMPLATE_ID",referencedColumnName = "INVOICE_TEMPLATE_ID")
    private List<R_RBI_INVOICE_TEMPLATE_CRITERIA_DATA> criteriaDataList;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "INVOICE_TYPE", referencedColumnName = "GLB_TYPE_VAL_ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE invoiceTypeValue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "METERAI", referencedColumnName = "GLB_TYPE_VAL_ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE meteraiValue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SIGNATURE", referencedColumnName = "GLB_TYPE_VAL_ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE signatureValue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TEMPLATE", referencedColumnName = "TEMPLATE_ID", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private M_GENERAL_TEMPLATE generalTemplate;
}

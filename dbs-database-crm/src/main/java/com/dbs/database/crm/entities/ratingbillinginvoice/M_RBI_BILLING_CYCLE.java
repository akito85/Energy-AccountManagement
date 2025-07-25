package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "M_RBI_BILLING_CYCLE")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class M_RBI_BILLING_CYCLE extends BaseEntities implements Serializable {

    private static final long serialVersionUID = 758586098643061184L;

    @Id
    @Column(name = "BILLING_CYCLE_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_RBI_BILLING_CYCLE_SEQ")
    @SequenceGenerator(sequenceName = "M_RBI_BILLING_CYCLE_SEQ", allocationSize = 1, name = "M_RBI_BILLING_CYCLE_SEQ")
    private Integer billingCycleId;

    @Column(name = "BEGIN_CYCLE")
    private Integer beginCycle;

    @Column(name = "END_CYCLE")
    private Integer endCycle;

    @Column(name = "TIME_UNIT")
    private String timeUnit;  // LOV 5

    @Column(name = "INVOICE_DATE")
    private Integer invoiceDate;

    @Column(name = "START_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date startDate;

    @Column(name = "END_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
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

    @Column(name = "TIME_UNIT_ID")
    private Integer timeUnitId;

    @Column(name = "CC_ID")
    private Integer ccId;
}

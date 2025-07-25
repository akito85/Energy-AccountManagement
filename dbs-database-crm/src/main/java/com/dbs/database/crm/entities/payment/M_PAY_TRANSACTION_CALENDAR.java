package com.dbs.database.crm.entities.payment;

import com.dbs.common.base.entities.BaseEntities;
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
@Table(name = "M_PAY_TRANSACTION_CALENDAR")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class M_PAY_TRANSACTION_CALENDAR extends BaseEntities implements Serializable {
    private static final long serialVersionUID = -7789489272016413261L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_PAY_TRANSACTION_CALENDAR_SEQ")
    @Column(name = "TRANSACTION_CALENDAR_ID", nullable = false)
    @SequenceGenerator(sequenceName = "M_PAY_TRANSACTION_CALENDAR_SEQ", allocationSize = 1, name = "M_PAY_TRANSACTION_CALENDAR_SEQ")
    private Long id;

    @Column(name = "BEGIN_CYCLE")
    private Integer beginCycle;

    @Column(name = "END_CYCLE")
    private Integer endCycle;

    @Column(name = "TIME_UNIT")
    private String timeUnit;
    
    @Column(name = "TIME_UNIT_ID")
    private Integer timeUnitId;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;
    
    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;
    
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "APPHIER_ID")
    private Integer appHierId;

    @Column(name = "TRIGGER_JSON")
    private String triggerJson;

    @Column(name = "NAME")
    private String name;
}

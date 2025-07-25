package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "M_USER_DELEGATION")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class M_USER_DELEGATION extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ID", nullable = false, updatable = false, length = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_USER_DELEGATION_SEQ")
    @SequenceGenerator(sequenceName = "M_USER_DELEGATION_SEQ", allocationSize = 1, name = "M_USER_DELEGATION_SEQ")
    private Integer id;
    @Column(name = "POSITION_FROM_DELEGATOR")
    private Integer posFromDelegator;
    @Column(name = "POSITION_DELEGATE_TO", nullable = false)
    private Integer positionDelegateTo;
    @Column(name = "DELEGATE_FROM")
    private Integer delegateFrom;
    @Column(name = "DELEGATE_TO")
    private Integer delegateTo;
    @Column(name = "START_DATE", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;
    @Column(name = "END_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;
    @Column(name = "REQUEST_REMARK")
    private String requestRemark;
    @Column(name = "APPROVAL_REMARK")
    private String approvalRemark;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "DELEGATE_FROM", referencedColumnName = "EMPLOYEE_ID", insertable = false, updatable = false, nullable = true)
    private M_EMPLOYEE employeeDelegateFrom;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "DELEGATE_TO", referencedColumnName = "EMPLOYEE_ID", insertable = false, updatable = false, nullable = true)
    private M_EMPLOYEE employeeDelegateTo;
}
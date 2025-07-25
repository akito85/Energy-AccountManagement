package com.dbs.database.crm.entities.accountmanagement;

import java.io.Serializable;
import java.util.Date;

import com.dbs.common.base.entities.BaseEntities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "M_ACCOUNT_GAS_SOURCE")
public class M_ACCOUNT_GAS_SOURCE extends BaseEntities implements Serializable {

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_ACCOUNT_GAS_SOURCE_SEQ")
    @SequenceGenerator(sequenceName = "M_ACCOUNT_GAS_SOURCE_SEQ", allocationSize = 1, name = "M_ACCOUNT_GAS_SOURCE_SEQ")
    private Integer id;

    @Column(name = "ACCOUNT_ID")
    private Integer accountId;

    @Column(name = "CALORIE_TYPE")
    private Integer calorieType;

    @Column(name = "GAS_SOURCE_CODE_ID")
    private Integer gasSourceCodeId;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "REMARK")
    private String remark;

    public M_ACCOUNT_GAS_SOURCE() {
    }

    public M_ACCOUNT_GAS_SOURCE(Integer accountId, Integer calorieType,
                                Integer gasSourceCodeId, Date startDate) {
        this.accountId = accountId;
        this.calorieType = calorieType;
        this.gasSourceCodeId = gasSourceCodeId;
        this.startDate = startDate;
    }
}

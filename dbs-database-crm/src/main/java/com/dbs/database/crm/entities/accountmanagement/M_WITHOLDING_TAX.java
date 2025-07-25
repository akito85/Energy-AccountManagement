package com.dbs.database.crm.entities.accountmanagement;

import java.io.Serializable;
import java.util.Date;

import com.dbs.common.base.entities.BaseEntities;

import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "M_WITHOLDING_TAX")
public class M_WITHOLDING_TAX extends BaseEntities implements Serializable {

    @Id
    @Column(name = "ID", nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_WITHOLDING_TAX_SEQ")
    @SequenceGenerator(sequenceName = "M_WITHOLDING_TAX_SEQ",allocationSize = 1, name = "M_WITHOLDING_TAX_SEQ")
    private Integer id;

    @Column(name = "ACCOUNT_ID")
    private Integer accountId;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "START_DATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date startDate;

    @Column(name = "END_DATE", nullable = true)
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date endDate;
}

package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_ACCOUNT_GS")
public class VW_ACCOUNT_GS extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "GAS_SOURCE_ID")
    private Integer gasSourceId;

    @Column(name = "ACCOUNT_ID")
    private Integer accountId;

    @Column(name = "CALORIE_TYPE_ID")
    private Integer calorieTypeId;

    @Column(name = "CALORIE_TYPE")
    private String calorieType;

    @Column(name = "GAS_SOURCE_CODE_ID")
    private Integer gasSourceCodeId;

    @Column(name = "START_DATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date startDate;

    @Column(name = "END_DATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date endDate;

//    @Column(name = "START_DATE_FORMATED")
//    private Date startDateFormated;
//
//    @Column(name = "END_DATE_FORMATED")
//    private Date endDateFormated;

    @Column(name = "CALORIE_CODE")
    private String calorieCode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "REMARK")
    private String remark;
}

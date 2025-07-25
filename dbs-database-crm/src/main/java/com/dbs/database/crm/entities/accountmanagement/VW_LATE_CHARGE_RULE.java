package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_LATE_CHARGE_RULE")
public class VW_LATE_CHARGE_RULE extends BaseEntities implements Serializable {
    @Id
    @Column(name = "LATE_CHARGE_RULE_ID")
    private Integer id;

    @Column(name = "M_AM_LATECHARGE_ID")
    private Integer latechargeId;

    @Column(name = "DOCUMENT_NUMBER")
    private String documentNumber;

    @Column(name = "CURRENCY_ID")
    private Integer currencyId;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "MAX_AMOUNT")
    private String maxAmount;

    @Column(name = "MAX_AMOUNT_REAL")
    private Double maxAmountReal;

    @Column(name = "FORMULA")
    private String formula;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "APPROVAL_STATUS")
    private String approvalStatus;

    @Column(name = "APPHIER_ID")
    private Integer apphierId;

}

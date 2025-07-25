package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_USAGE_LIST_NEW")
public class VW_USAGE_LIST extends BaseEntities implements Serializable {
    @Id
    @Column(name = "RECORD_ID")
    private Integer recordId;
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;
    @Column(name = "BATCH_ID")
    private Integer batchId;
    @Column(name = "RATING_CODE")
    private String ratingCode;
    @Column(name = "ACCOUNT_NAME")
    private String accountName;
    @Column(name="ACCOUNT_SEGMENT")
    private String accountSegment;
    @Column(name="ACCOUNT_ID")
    private Integer accountId;
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @Column(name = "APPROVED_BY")
    private String approvedBy;
    @Column(name = "ACCOUNT_GROUP_TYPE")
    private String accountGroupType;
    @Column(name = "COST_CENTER")
    private String costCenter;
    @Column(name = "COST_CENTER_ID")
    private Integer costCenterId;
    @Column(name = "SOR")
    private String sor;
    @Column(name = "BILLING_PERIOD")
    private String billingPeriod;
    @Column(name = "BILLING_CYCLE")
    private String billingCycle;
    @Column(name = "BILLING_CYCLE_ID")
    private String billingCycleId;
    @Column(name = "ASSET_SERIAL_NUM")
    private String assetSerialNumber;
    @Column(name = "SERVICE_TYPE")
    private String serviceType;
    @Column(name = "ASSET_TYPE")
    private String assetType;

    @Column(name = "MEAS_DATE")
//    @JsonFormat(pattern = Constant.FORMAT_DATETIME_VIEW, timezone = Constant.TIMEZONE)
//    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private String measDate;

    @Column(name = "FDATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fdate;

    @Column(name = "FHOUR")
    private String fhour;

    @Column(name = "STREAM_ID")
    private Integer streamId;

    @Column(name = "TEMPERATURE")
    private String temperature;

    @Column(name = "PRESSURE")
    private String pressure;

    @Column(name = "CORRECTION_FACTOR")
    private String correctionFactor;

    @Column(name = "CALORIE")
    private String calorie;
    @Column(name = "CALORIE_REAL")
    private Double calorieReal;

    @Column(name = "BEGIN_STAND")
    private String beginStand;
    @Column(name = "BEGIN_STAND_REAL")
    private Double beginStandReal;

    @Column(name = "END_STAND")
    private String endStand;
    @Column(name = "END_STAND_REAL")
    private Double endStandReal;

    @Column(name = "VOL_MEASURED_27")
    private String volMeasured27;
    @Column(name = "VOL_MEASURED_27_REAL")
    private Double volMeasured27Real;

    @Column(name = "VOL_MEASURED_60")
    private String volMeasured60;
    @Column(name = "VOL_MEASURED_60_REAL")
    private Double volMeasured60Real;

    @Column(name = "ENG_MEASURED")
    private String engMeasured;
    @Column(name = "ENG_MEASURED_REAL")
    private Double engMeasuredReal;

    @Column(name = "GHV")
    private String ghv;
    
    @Column(name = "GHV_REAL")
    private String ghvReal;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TAXATION_ROW_ID")
    private String taxationRowId;

    @Column(name = "VOL_MSCF")
    private String volMscf;

    @Column(name = "UNCORRECTED_VALUE")
    private String uncorrectedValue;

    @Column(name = "SOURCES")
    private String source;
    @Column(name = "ENERGY")
    private Integer energy;
    @Column(name = "METER_READING_CODE")
    private String meterReadingCode;
    @Column(name = "FILE_SOURCE")
    private String fileSource;
    @Column(name="CUSTOMER_NUMBER")
    private String customerNumber;
    @Column(name="CUSTOMER_NAME")
    private String customerName;
    @Column(name="CUSTOMER_ID")
    private String customerId;
}

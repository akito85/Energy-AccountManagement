package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "M_RBI_USAGE")
public class M_RBI_USAGE extends BaseEntities implements Serializable {
    @Id
    @Column(name="RECORD_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_USAGE_SEQ")
    @SequenceGenerator(sequenceName = "M_USAGE_SEQ", allocationSize = 1, name = "M_USAGE_SEQ")
    private Integer recordId;
    @Column(name = "BATCH_ID")
    private Integer batchId;
    @Column(name = "RATING_CODE")
    private String ratingCode;
    @Column(name = "TEMP_ID")
    private Integer tempId;
    @Column(name="ENTITY_ID")
    private Integer entityId;
    @Column(name="ACCOUNT_GROUP_TYPE")
    private String accountGroupType;
    @Column(name="ACCOUNT_NUMBER")
    private String accountNumber;
    @Column(name="ACCOUNT_NAME")
    private String accountName;
    @Column(name="ASSET_SERIAL_NUM")
    private String assetSerialNumber;
    @Column(name="ACCOUNT_SEGMENT")
    private String accountSegment;
    @Column(name="ACCOUNT_ID")
    private Integer accountId;
    @Column(name="ASSET_TYPE")
    private String assetType;
    @Column(name="MEAS_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Timestamp measDate;
    @Column(name="COST_CENTER")
    private String costCenter;
    @Column(name="COST_CENTER_ID")
    private Integer costCenterId;
    @Column(name="FHOUR")
    private Timestamp fHour;
    @Column(name="FDATE")
    private Timestamp fDate;
    @Column(name="METER_READING_CODE")
    private String meterReadingCode;
    @Column(name="STREAM_ID")
    private Integer streamId;
    @Column(name="TEMPERATURE")
    private Float temperature;
    @Column(name="PRESSURE")
    private Float pressure;
    @Column(name="CORRECTION_FACTOR")
    private Float correctionFactor;
    @Column(name="CALORIE")
    private Float calorie;
    @Column(name="BEGIN_STAND")
    private Double beginStand;
    @Column(name="END_STAND")
    private Double endStand;
    @Column(name="VOL_MEASURED_27")
    private Double volMeasured27;
    @Column(name="VOL_MEASURED_60")
    private Double volMeasured60;
    @Column(name="ENG_MEASURED")
    private Double engMeasured;
    @Column(name="GHV")
    private Float ghv;
    @Column(name="DESCRIPTION")
    private String description;
    @Column(name="ENERGY")
    private Integer energy;
    @Column(name="BILLING_PERIOD")
    private Integer billingPeriod;
    @Column(name="FILE_SOURCE")
    private String fileSource;
    @Column(name="TAXATION_ROW_ID")
    private String taxationRowId;
    @Column(name="APPROVED_BY")
    private String approvedBy;
    @Column(name="VOL_MSCF")
    private Float volMscf;
    @Column(name="UNCORRECTED_VALUE")
    private Double uncorrectedValue;
    @Column(name="BILLING_CYCLE")
    private String billingCycle;
    @Column(name="SOURCES")
    private String source;
    @Column(name="CUSTOMER_NUMBER")
    private String customerNumber;
    @Column(name="CUSTOMER_NAME")
    private String customerName;
    @Column(name="CUSTOMER_ID")
    private String customerId;
    @Column(name = "SERVICE_TYPE")
    private Integer serviceType;
}
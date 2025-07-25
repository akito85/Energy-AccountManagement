package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_APPROVAL_USAGE")
public class VW_APPROVAL_USAGE {
    @Id
    @Column(name = "RECORD_ID")
    private Integer recordId;
    @Column(name = "BATCH_ID")
    private Integer batchId;
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;
    @Column(name = "ACCOUNT_NAME")
    private String accountName;
    @Column(name = "ASSET_SERIAL_NUM")
    private String assetSerialNumber;
    @Column(name = "ACCOUNT_SEGMENT")
    private String accountSegment;
    @Column(name = "SERVICE_TYPE")
    private String serviceType;
    @Column(name = "ACCOUNT_ID")
    private Integer accountId;
    @Column(name = "ASSET_TYPE")
    private String assetType;
    @Column(name = "MEAS_DATE")
    private Date measDate;
    @Column(name = "COST_CENTER")
    private String costCenter;
    @Column(name = "COST_CENTER_ID")
    private Integer costCenterId;
    @Column(name = "METER_READING_CODE")
    private String meterReadingCode;
    @Column(name = "STREAM_ID")
    private Integer streamId;
    @Column(name = "TEMPERATURE")
    private Float temperature;
    @Column(name = "PRESSURE")
    private Float pressure;
    @Column(name = "CORRECTION_FACTOR")
    private Float correctionFactor;
    @Column(name = "CALORIE")
    private Float calorie;
    @Column(name = "BEGIN_STAND")
    private Integer beginStand;
    @Column(name = "END_STAND")
    private Integer endStand;
    @Column(name = "ENG_MEASURED")
    private Float engMeasured;
    @Column(name = "VOL_MEASURED_27")
    private Float volMeasured27;
    @Column(name = "VOL_MEASURED_60")
    private Float volMeasured60;
    @Column(name = "GHV")
    private Float ghv;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "ENERGY")
    private Integer energy;
    @Column(name = "BILLING_PERIOD_VALUE")
    private String billingPeriodVal;
    @Column(name = "BILLING_PERIOD_ID")
    private String billingPeriodId;
    @Column(name = "FILE_SOURCE")
    private String fileSource;
    @Column(name = "TAXATION_ROW_ID")
    private String taxationRowId;
    @Column(name = "APPROVED_BY")
    private String approvedBy;
    @Column(name = "VOL_MSCF")
    private Float volMscf;
    @Column(name = "UNCORRECTED_VALUE")
    private Integer uncorrectedValue;
    @Column(name = "BILLING_CYCLE_VALUE")
    private String billingCycleVal;
    @Column(name = "BILLING_CYCLE_ID")
    private Integer billingCycleId;
    @Column(name = "SOURCES")
    private String sources;
    @Column(name = "CUSTOMER_NUMBER")
    private String customerNumber;
    @Column(name = "CUSTOMER_NAME")
    private String customerName;
    @Column(name = "CUSTOMER_ID")
    private String customerId;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "FDATE")
    private Date fdate;
    @Column(name = "FHOUR")
    private Date fhour;
    @Column(name = "CREATED_DATE")
    private Date createdDate;
    @Column(name = "ACCOUNT_GROUP_TYPE")
    private String accountGroupType;
    @Column(name = "POSITION_ID")
    private Integer positionId;
    @Column(name = "T_APP_ID")
    private Integer tAppId;
    @Column(name = "SOR")
    private String sor;
}

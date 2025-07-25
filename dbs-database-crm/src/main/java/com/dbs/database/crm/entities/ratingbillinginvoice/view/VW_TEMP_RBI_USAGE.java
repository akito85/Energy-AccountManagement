package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "VW_TEMP_RBI_USAGE")
public class VW_TEMP_RBI_USAGE {
    @Id
    @Column(name = "RECORD_ID")
    private Integer recordId;
    @Column(name = "ACCOUNT_ID")
    private Integer accountId;
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;
    @Column(name = "BILLING_PERIOD")
    private String billingPeriod;
    @Column(name = "BILLING_PERIOD_ID")
    private Integer billingPeriodId;
    @Column(name = "ASSET_SERIAL_NUM")
    private String assetSerialNumber;
    @Column(name = "ASSET_TYPE")
    private String assetType;
    @Column(name = "MEAS_DATE")
    private String measDate;
    @Column(name = "STREAM_ID")
    private Integer streamId;
    @Column(name = "TEMPERATURE")
    private String temperature;
    @Column(name = "PRESSURE")
    private String pressure;
    @Column(name = "CORRECTION_FACTOR")
    private Float correctionFactor;
    @Column(name = "CALORIE")
    private String calorie;
    @Column(name = "BEGIN_STAND")
    private String beginStand;
    @Column(name = "END_STAND")
    private String endStand;
    @Column(name = "VOL_MEASURED_27")
    private String volMeasured27;
    @Column(name = "VOL_MEASURED_60")
    private String volMeasured60;
    @Column(name = "ENG_MEASURED")
    private String engMeasured;
    @Column(name = "GHV")
    private String ghv;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "TAXATION_ROW_ID")
    private String taxationRowId;
    @Column(name = "APPROVED_BY")
    private String approvedBy;
    @Column(name = "VOL_MSCF")
    private String volMscf;
    @Column(name = "SOURCES")
    private String source;
    @Column(name = "BATCH_ID")
    private Integer batchId;
    @Column(name = "CUSTOMER_ID")
    private String customerId;
    @Column(name = "CUSTOMER_NUMBER")
    private String customerNumber;
    @Column(name = "CUSTOMER_NAME")
    private String customerName;
    @Column(name = "ACCOUNT_GROUP_TYPE")
    private String accountGroupType;
    @Column(name = "SERVICE_TYPE")
    private Integer serviceType;
    @Column(name = "COST_CENTER")
    private String costCenter;
    @Column(name = "COST_CENTER_ID")
    private Integer costCenterId;
    @Column(name = "ACCOUNT_SEGMENT")
    private String accountSegment;
    @Column(name = "ACCOUNT_NAME")
    private String accountName;
    @Column(name = "FHOUR")
    private String fHour;
    @Column(name = "UNCORRECTED_VALUE")
    private String uncorrectedValue;
    @Column(name = "ENERGY")
    private Integer energy;
    @Column(name = "FILE_SOURCE")
    private String fileSource;
    @Column(name = "SOR")
    private String sor;
    @Column(name = "FDATE")
    private String fDate;
    @Column(name="METER_READING_CODE")
    private String meterReadingCode;
    @Column(name="STATUS")
    private String status;
    @Column(name="BILLING_CYCLE")
    private String billingCycleValue;
    @Column(name="BILLING_CYCLE_ID")
    private Integer billingCycleId;
    @Column(name="APPHIER_ID")
    private Integer apphierId;
    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name="IS_DELETED")
    private Boolean isDeleted;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Column(name = "CREATED_DATE")
    private Date createdDate;
}

package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Entity
@Data
@Table(name = "VW_ASSET_ASSIGNMENT")
public class VW_ASSET_ASSIGNMENT extends BaseEntities implements Serializable {
    
    @Id
    @Column(name = "ASSET_ASSIGNMENT_HISTORY_ID")
    private Integer id;
    
    @Column(name = "SERVICE_POINT_ID")
    private Integer servicePointId;

    @Column(name = "SERVICE_POINT_NAME")
    private String servicePointName;
    
    @Column(name = "ACCOUNT_ADDRESS_ID")
    private Integer accountAddressId;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "ACCOUNT_NAME")
    private String accountName;

    @Column(name = "CUSTOMER_NUMBER")
    private String customerNumber;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "PREMISE")
    private String premise;
    
    @Column(name = "INSTALL_DATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date installDate;
    
    @Column(name = "UNINSTALL_DATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date unInstallDate;
    
    @Column(name = "REMARK")
    private String remark;
    
    @Column(name = "ASSET_ID")
    private Integer assetId;
    
    @Column(name = "ASSET_NAME")
    private String assetName;
    
    @Column(name = "SERIAL_NUMBER")
    private String serialNumber;
    
    @Column(name = "TYPE")
    private String type;
    
    @Column(name = "PRODUCT_NAME")
    private String productName;
    
    @Column(name = "BRAND")
    private String brand;
    
    @Column(name = "YEAR")
    private Integer year;
    
    @Column(name = "CUSTODY_TRANSFER")
    private String custodyTransfer;
    
    @Column(name = "INLET_DIAMETER")
    private Double inletDiameter;
    
    @Column(name = "OUTLET_DIAMETER")
    private Double outletDiameter;
    
    @Column(name = "MAXIMUM_INLET_PRESSURE")
    private Double maximumInletPressure;
    
    @Column(name = "MAXIMUM_OUTLET_PRESSURE")
    private Double maximumOutletPressure;
    
    @Column(name = "MINIMUM_INLET_PRESSURE")
    private Double minimumInletPressure;
    
    @Column(name = "MINIMUM_OUTLET_PRESSURE")
    private Double minimumOutletPressure;
    
    @Column(name = "MAX_FLOW_CAPACITY_PER_STREAM")
    private Double maxFlowCapacityPerStream;
    
    @Column(name = "STREAM_AMOUNT")
    private Double streamAmount;
    
    @Column(name = "SETTING_PRESSURE")
    private Double settingPressure;
    
    @Column(name = "LENGTH")
    private Double length;
    
    @Column(name = "BOLT_HOLE_AMOUNT")
    private Double boltHoleAmount;
    
    @Column(name = "MINIMUM_CAPACITY")
    private Double minimumCapacity;
    
    @Column(name = "MAXIMUM_CAPACITY")
    private Double maximumCapacity;
    
    @Column(name = "ANSI")
    private Integer ansi;
    
    @Column(name = "SOURCE")
    private String source;
    
    @Column(name = "G_SIZE")
    private String gsize;
    
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "LOCATION")
    private String location;
    
}

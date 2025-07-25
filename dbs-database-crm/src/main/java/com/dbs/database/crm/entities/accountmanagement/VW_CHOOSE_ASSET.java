package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_CHOOSE_ASSET")
public class VW_CHOOSE_ASSET extends BaseEntities implements Serializable {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "SERVICE_TYPE_ID")
    private Integer serviceTypeId;

    @Column(name = "SERVICE_TYPE_VALUE")
    private String serviceTypeValue;

    @Column(name = "SERVICE_TYPE")
    private String serviceType;

    @Column(name = "ASSET_NAME_ID")
    private Integer assetNameId;

    @Column(name = "ASSET_NAME_VALUE")
    private String assetNameValue;

    @Column(name = "ASSET_NAME")
    private String assetName;

    @Column(name = "SERIAL_NUMBER")
    private String serialNumber;

    @Column(name = "TYPE_ID")
    private Integer typeId;

    @Column(name = "TYPE_VALUE")
    private String typeValue;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "BRAND_ID")
    private Integer brandId;

    @Column(name = "BRAND_VALUE")
    private String brandValue;

    @Column(name = "BRAND")
    private String brand;

    @Column(name = "YEAR")
    private Integer year;

    @Column(name = "CUSTODY_TRANSFER")
    private String custodyTransfer;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "INLET_DIAMETER")
    private Double inletDiameter;

    @Column(name = "OUTLET_DIAMETER")
    private Double outletDiameter;

    @Column(name = "MAXIMUM_OUTLET_PRESSURE")
    private Double maximumOutletPressure;

    @Column(name = "MAXIMUM_INLET_PRESSURE")
    private Double maximumInletPressure;

    @Column(name = "MINIMUM_OUTLET_PRESSURE")
    private Double minimumOutletPressure;

    @Column(name = "MINIMUM_INLET_PRESSURE")
    private Double minimumInletPressure;

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

    @Column(name = "ANSI_ID")
    private Integer ansiId;

    @Column(name = "ANSI_VALUE")
    private String ansiValue;

    @Column(name = "ANSI")
    private String ansi;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "PRODUCT_NAME_ID")
    private Integer productNameId;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRODUCT_NAME_VALUE")
    private String productNameValue;

    @Column(name = "PRODUCT_START_DATE")
    private Date productStartDate;

    @Column(name = "PRODUCT_END_DATE")
    private Date productEndDate;

    @Column(name = "GSIZE_ID")
    private Integer gSizeId;

    @Column(name = "GSIZE_VALUE")
    private String gSizeValue;

    @Column(name = "GSIZE")
    private String gSize;

    @Column(name = "READY")
    private String ready;

    @Column(name = "LOCATION")
    private String location;

}

package com.dbs.module.account.detail.premise.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class AssignAssetDTO implements Serializable {
    private Integer servicePointId;
    private Integer assetId;
    private Date installDate;
    private String remark;
    private Boolean isDuplicate;
    // ASSETS CREATE    
    private Integer assetName;
    private String serialNumber;
    private Integer type;
    private Integer brand;
    private Integer year;
    private Boolean custodyTransfer;
    private String description;
    private Double inletDiameter;
    private Double outletDiameter;
    private Double maximumInletPressure;
    private Double maximumOutletPressure;
    private Double minimumInletPressure;
    private Double minimumOutletPressure;
    private Double maxFlowCapacityPerStream;
    private Double streamAmount;

    @JsonProperty("gsize")
    private Integer gsize;

    private Double settingPressure;
    private Integer serviceType;
    private Integer productVersion;
    private Double length;
    private Double boltHoleAmount;
    private Double minimumCapacity;
    private Double maximumCapacity;
    private Integer ansi;
    private String source;
    private Integer entityId;
    
    private Integer accountAddressId;
}

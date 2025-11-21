package com.dbs.module.account.master.assets.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@SuppressWarnings("java:S1068")
public class MasterAssetCreateUpdateDto {
    private Integer id;
    private String description;
    private Double inletDiameter;
    private Double outletDiameter;
    private Double maximumInletPressure;
    private Double maximumOutletPressure;
    private Double minimumInletPressure;
    private Double minimumOutletPressure;
    private Double maxFlowCapacityPerStream;
    private Double streamAmount;

    @JsonProperty("gSize")
    private Integer gSize;

    private Double settingPressure;
    private Double length;
    private Double boltHoleAmount;
    private Double maximumCapacity;
    private Double minimumCapacity;
    private Integer ansi;

    private Integer productVersion;

    @NotNull(message = "Service Type cannot be null!")
    private Integer serviceType;

    @NotNull(message = "Asset Name cannot be null!")
    private Integer assetName;

    @NotNull(message = "Asset Type cannot be null!")
    private Integer type;

    @NotEmpty(message = "Serial Number cannot be empty!")
    private String serialNumber;

    @NotNull(message = "Year cannot be null!")
    private Integer year;

    @NotNull(message = "Brand cannot be null!")
    private Integer brand;

    private Boolean custodyTransfer;
}
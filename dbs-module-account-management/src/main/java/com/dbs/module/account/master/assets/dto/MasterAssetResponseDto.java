package com.dbs.module.account.master.assets.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class MasterAssetResponseDto {
    private Integer id;
    private String productName;
    private String serviceType;
    private String assetName;
    private String type;
    private String brand;
    private String gSize;
    private String ansi;
    private String serialNumber;
    private Integer year;
    private String custodyTransfer;
    private String source;
    private String description;
    private Double inletDiameter;
    private Double outletDiameter;
    private Double maximumInletPressure;
    private Double maximumOutletPressure;
    private Double minimumInletPressure;
    private Double minimumOutletPressure;
    private Double maxFlowCapacityPerStream;
    private Double streamAmount;
    private Double settingPressure;
    private Double length;
    private Double boltHoleAmount;
    private Double minimumCapacity;
    private Double maximumCapacity;
    private Integer entityId;
    private String status;
    private String location;
}

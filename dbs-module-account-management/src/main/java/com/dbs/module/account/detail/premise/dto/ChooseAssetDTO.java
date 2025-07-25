package com.dbs.module.account.detail.premise.dto;

import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class ChooseAssetDTO implements Serializable {
    private Integer id;
    private GlobalValueAssetDTO serviceType;
    private GlobalValueAssetDTO assetName;
    private String serialNumber;
    private GlobalValueAssetDTO type;
    private GlobalValueAssetDTO brand;
    private Integer year;
    private String custodyTransfer;
    private String description;
    private Double inletDiameter;
    private Double outletDiameter;
    private Double maximumInletPressure;
    private Double maximumOutletPressure;
    private Double minimumInletPressure;
    private Double minimumOutletPressure;
    private Double maxFlowCapacityPerStream;
    private Double streamAmount;
    private GlobalValueAssetDTO gSize;
    private Double settingPressure;
    private Double length;
    private Double boltHoleAmount;
    private Double minimumCapacity;
    private Double maximumCapacity;
    private GlobalValueAssetDTO ansi;
    private Integer entityId;
    private GlobalValueAssetDTO productName;
    private String status;
}

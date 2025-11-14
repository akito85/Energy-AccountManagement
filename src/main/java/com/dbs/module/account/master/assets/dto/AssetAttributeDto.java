package com.dbs.module.account.master.assets.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class AssetAttributeDto {
    private String description;
    private Double maxOutletPressure;
    private Double streamAmount;
    private Double boltHoleAmount;
    private Double inletDiameter;
    private Double minInletPressure;
    private Integer gSizeId;
    private String gSize;
    private Double minCapacity;
    private Double outletDiameter;
    private Double minimumOutletPressure;
    private Double settingPressure;
    private Double maxCapacity;
    private Double maxInletPressure;
    private Double maxFlowCapacityPerStream;
    private Double length;
    private String ansi;
    private Integer ansiId;
}

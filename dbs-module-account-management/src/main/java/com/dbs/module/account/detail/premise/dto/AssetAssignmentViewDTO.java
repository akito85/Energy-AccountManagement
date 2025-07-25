package com.dbs.module.account.detail.premise.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class AssetAssignmentViewDTO implements Serializable {
    private Date installDate;
    private Date unistallDate;
    private String remark;
    private String status;
    private String assetName;
    private String serialNumber;
    private String type;
    private String productName;
    private String brand;
    private String year;
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
    private String gSize;
    private Double settingPressure;
    private Double length;
    private Integer boltHoleAmount;
    private Double minimumCapacity;
    private Double maximumCapacity;
    private Integer ansi;
    private String source;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName();
        }
    }
}

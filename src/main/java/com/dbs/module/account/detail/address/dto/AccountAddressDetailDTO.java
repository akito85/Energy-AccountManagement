package com.dbs.module.account.detail.address.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class AccountAddressDetailDTO implements Serializable {
    
    private Integer addressId;
    private String primary;
    private String fullAddress;
    private String description;
    private String additionalNote;
    private String type;
    private String houseName;
    private String houseNumber;
    private String streetName;
    private String streetNumber;
    private String rt;
    private String rw;
    private String building;
    private String floor;
    private String postalCode;
    private String subDistrict;
    private String district;
    private String city;
    private String province;
    private String country;
    private String businessPurpose;
    private String premise;
    private String status;
    private String mapSource;
    private String longitude;
    private String latitude;
    private String altitude;

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

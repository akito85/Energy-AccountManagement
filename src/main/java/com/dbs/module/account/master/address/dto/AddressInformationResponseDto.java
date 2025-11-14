package com.dbs.module.account.master.address.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@SuppressWarnings("java:S1068")
public class AddressInformationResponseDto {
    private Integer addressId;

    private String country;
    private Integer countryId;
    private String district;
    private Integer districtId;
    private String building;
    private String streetName;
    private String rt;
    private String additionalInfo;
    private String block;
    private String address;
    private String province;
    private Integer provinceId;
    private String subDistrict;
    private Integer subDistrictId;
    private String floor;
    private String streetNumber;
    private String rw;
    private String city;
    private Integer cityId;
    private String postalCode;
    private Integer postalCodeId;
    private String houseName;
    private String houseNumber;
    private String type;
    private Integer typeId;
    private String description;
    private String fullAddress;

    private Integer buildingPrefix;
    private Integer floorPrefix;
    private Integer brotherhood1Prefix;
    private Integer brotherhood2Prefix;
    private Integer streetNamePrefix;
}

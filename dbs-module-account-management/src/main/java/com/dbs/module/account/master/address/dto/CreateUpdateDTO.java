package com.dbs.module.account.master.address.dto;

import com.dbs.module.account.detail.address.dto.PrefixValueAddressDTO;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@SuppressWarnings("java:S1068")
public class CreateUpdateDTO {
    private Integer addressId;

    @NotNull(message = "Country cannot be null!")
    private Integer countryId;

    @NotNull(message = "Province cannot be null!")
    private Integer provinceId;

    @NotNull(message = "City cannot be null!")
    private Integer cityId;

    @NotNull(message = "District cannot be null!")
    private Integer districtId;

    @NotNull(message = "Sub District cannot be null!")
    private Integer subDistrictId;

    @NotNull(message = "Postal Code cannot be null!")
    private Integer postalCodeId;

//    private PrefixValueAddressDTO building;
    private String building;

//    private PrefixValueAddressDTO floor;
    private String floor;

    private String houseName;

//    private PrefixValueAddressDTO streetName;
    private String streetName;

    private String block;

    private String houseNumber;

//    private PrefixValueAddressDTO neighborhood1;
    private String neighborhood1;
    private String rt;

//    private PrefixValueAddressDTO neighborhood2;
    private String neighborhood2;
    private String rw;

    @NotNull(message = "Type cannot be null!")
    private Integer type;

    private String additionalInfo;

//    private String descAddress;
    private String description;

    @NotEmpty(message = "Source cannot be empty!")
    private String source;

    private String latitude;

    private String longitude;

//    private String altitude;

    private String fullAddress;

}

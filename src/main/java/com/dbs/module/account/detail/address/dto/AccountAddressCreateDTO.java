package com.dbs.module.account.detail.address.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class AccountAddressCreateDTO implements Serializable{

    // ADDRESS
    private Integer countryId;
    private Integer provinceId;
    private Integer cityId;
    private Integer districtId;
    private Integer subDistrictId;
    private Integer postalCodeId;
    private Integer typeId; //type
    private String source;
    private String building;
    private String floor;
    private String houseName;
    private String streetName;
    private String block;
    private String houseNumber;
    private String rt; //neighborhood1
    private String rw; //neighborhood2
    private String additionalInfo;
    private String additionalNote;
    private String longitude;
    private String latitude;
    private String description;
    private Float altitude;
    private String fullAddress;

    // ACCOUNT ADDRESS
    private Integer accountId;
    private Integer addressId;
    private Boolean primaryFlag;
    private Boolean premiseFlag;
    private String descAccountAddress;
    private String descAddress;
    private String tempId;
    private String streetNumber;
    private String desc;
    private String address;
    private Boolean needValidation;

    @NotEmpty(message="Business purpose cannot be empty!")
    private List<Integer> businessPurpose;

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

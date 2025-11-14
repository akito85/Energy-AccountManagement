package com.dbs.module.account.detail.address.dto;

import com.dbs.module.account.main.dto.MLocationDetailDTO;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@SuppressWarnings("java:S1068")
public class AccountAddressDTO {

    private Integer accountAddressId;
    private Integer addressId;
    private BooleanObjectDTO primary;
    private String fullAddress;
    private String description_accountAddress;
    private String description_mAddress;
    private Integer mAddressId;
    private String additionalNote;
    private MLocationDetailDTO type;
    private String houseName;
    private String houseNumber;
    private String block;
    private String streetName;
    private String streetNumber;
    private String rt;
    private String rw;
    private String building;
    private String floor;
    private MLocationDetailDTO postalCode;
    private MLocationDetailDTO subDistrict;
    private MLocationDetailDTO district;
    private MLocationDetailDTO city;
    private MLocationDetailDTO province;
    private MLocationDetailDTO country;
    private List<Map<String, Object>> businessPurpose;
    private BooleanObjectDTO premise;
    private String status;
    private String source;
    private String latitude;
    private String longitude;
    private String altitude;

    private Date createdDate;
    private String createdBy;
    private Date updatedDate;
    private String updatedBy;

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

package com.dbs.module.account.detail.address.dto;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import com.dbs.module.account.main.dto.MLocationDetailDTO;
import lombok.Data;

import javax.persistence.Convert;
import java.util.Date;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class AddressesResponseDto {
    private Integer accountAddressId;
    private Integer addressId;
    private MLocationDetailDTO country;
    private MLocationDetailDTO province;
    private MLocationDetailDTO city;
    private MLocationDetailDTO district;
    private MLocationDetailDTO subDistrict;
    private MLocationDetailDTO postalCode;
    private Integer typeId;
    private String type;
    private String source;
    private String building;
    private String floor;
    private String houseName;
    private String streetName;
    private String block;
    private String houseNumber;
    private String rt;
    private String rw;
    private String additionalNote;
    private String longitude;
    private String latitude;
    private String altitude;

    private String streetNumber;
    
    private String fullAddress;
    
    private String createdBy;
    private String updatedBy;
    private Date createdDate;
    private Date updatedDate;
    private Integer recordId;
    
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean primaryFlag;

    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean premiseFlag;
    
    private String descAccountAddress;
    private String descAddress;
    private String businessPurpose;
    private List<AUDIT_TRAIL> activeInactiveLog;
}

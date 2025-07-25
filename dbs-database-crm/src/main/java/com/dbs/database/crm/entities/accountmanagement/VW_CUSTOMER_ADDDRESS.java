package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "VW_CUSTOMER_ADDDRESS")
public class VW_CUSTOMER_ADDDRESS extends BaseEntities implements Serializable {

    @Id
    @Column(name = "ACCOUNT_ADDRESS_ID")
    private Integer accountAddressId;
    
    @Column(name = "CUSTOMER_ID")
    private Integer customerId;

    @Column(name = "ACCOUNT_ID")
    private Integer accountId;

    @Column(name = "ADDRESS_ID")
    private Integer addressId;
    
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "ACCOUNT_NAME")
    private String accountName;
    
    @Column(name = "CUSTOMER_MANAGEMENT_ID")
    private Integer customerManagementId;
    
    @Column(name = "COST_CENTER_ID")
    private Integer costCenterId;

    @Column(name = "FULL_ADDRESS")
    private String fullAddress;

    @Column(name = "ADDRESS")
    private String address;

//    @Column(name = "DESCRIPTION")
//    private String description;

    @Column(name = "ADDITIONAL_INFO")
    private String additionalInfo;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "TYPE_ID")
    private Integer typeId;

    @Column(name = "HOUSE_NAME")
    private String houseName;

    @Column(name = "STREET_NAME")
    private String streetName;

    @Column(name = "STREET_NUMBER")
    private String streetNumber;

    @Column(name = "HOUSE_NUMBER")
    private String houseNumber;

    @Column(name = "NEIGHBORHOOD_1")
    private String neighborhood1;

    @Column(name = "NEIGHBORHOOD_2")
    private String neighborhood2;

    @Column(name = "BUILDING")
    private String building;

    @Column(name = "FLOOR")
    private String floor;

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @Column(name = "POSTAL_CODE_ID")
    private Integer postalCodeId;

    @Column(name = "SUB_DISTRICT")
    private String subDistrict;

    @Column(name = "SUB_DISTRICT_ID")
    private Integer subDistrictId;

    @Column(name = "DISTRICT")
    private String district;

    @Column(name = "DISTRICT_ID")
    private Integer districtId;

    @Column(name = "CITY")
    private String city;

    @Column(name = "CITY_ID")
    private Integer cityId;

    @Column(name = "PROVINCE")
    private String province;

    @Column(name = "PROVINCE_ID")
    private Integer provinceId;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "COUNTRY_ID")
    private Integer countryId;

    @Column(name = "SOURCE")
    private String source;

    @Column(name = "ALTITUDE")
    private Double altitude;

    @Column(name = "LATITUDE")
    private String latitude;

    @Column(name = "LONGITUDE")
    private String longitude;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "PRIMARY_FLAG")
    private Boolean primaryFlag;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "PREMISE_FLAG")
    private Boolean premiseFlag;

    @Column(name = "BUSINESS_PURPOSE")
    private String businessPurpose;

    @Column(name = "BUSINESS_PURPOSE_ID")
    private Integer businessPurposeId;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "PRIMARY")
    private Boolean primary;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "PREMISE")
    private Boolean premise;

    @Column(name = "DESCRIPTION_ACCOUNT_ADDRESS")
    private String descriptionAccountAddress;

    @Column(name = "DESCRIPTION_M_ADDRESS")
    private String descriptionAddress;

    @Column(name = "ADDITIONAL_NOTE")
    private String additionalNote;

    @Column(name = "RT")
    private String rt;

    @Column(name = "RW")
    private String rw;

}

package com.dbs.database.crm.entities.accountmanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "VW_ADDRESS")
public class VW_ADDRESS extends BaseEntities implements Serializable {

    @Id
    @Column(name = "ADDRESS_ID")
    private Integer addressId;

    @Column(name = "FULL_ADDRESS")
    private String fullAddress;

    @Column(name = "DESCRIPTION")
    private String description;

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

    @Column(name = "BLOCK")
    private String block;

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

    @Column(name = "ACCOUNT_ADDRESS")
    private String accountAddress;
}

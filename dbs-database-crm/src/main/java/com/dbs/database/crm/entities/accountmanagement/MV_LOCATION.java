package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "MV_LOCATION")
public class MV_LOCATION extends BaseEntities implements Serializable {

    @Column(name = "LOCATION_CODE")
    private String locationCode;

    @Id
    @Column(name = " POSTALCODE_ID")
    private Integer postalCodeId;
    @Column(name = "POSTALCODE")
    private String postalCode;

    @Column(name = " SUBDISTRICT_ID")
    private Integer subDistrictId;
    @Column(name = "SUBDISTRICT")
    private String subDistrict;

    @Column(name = " DISTRICT_ID")
    private Integer districtId;
    @Column(name = "DISTRICT")
    private String district;

    @Column(name = " CITY_ID")
    private Integer cityId;
    @Column(name = "CITY")
    private String city;

    @Column(name = " PROVINCE_ID")
    private Integer provinceId;
    @Column(name = "PROVINCE")
    private String province;

    @Column(name = " COUNTRY_ID")
    private Integer countryId;
    @Column(name = "COUNTRY")
    private String country;


}

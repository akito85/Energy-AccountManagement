package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

@Entity
@Data
@Table(name = "M_ADDRESSES")
public class M_ADDRESSES extends BaseEntities {
    
    @Id
    @Column(name = "ID", nullable=false, updatable=false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="M_ADDRESSES_SEQ")
    @SequenceGenerator(sequenceName = "M_ADDRESSES_SEQ", allocationSize=1, name = "M_ADDRESSES_SEQ")
    private Integer addressId;
    
    @Column(name = "COUNTRY_ID")
    private Integer countryId;
    
    @Column(name = "PROVINCE_ID")
    private Integer provinceId;
    
    @Column(name = "CITY_ID")
    private Integer cityId;
    
    @Column(name = "DISTRICT_ID")
    private Integer districtId;
    
    @Column(name = "SUB_DISTRICT_ID")
    private Integer subDistrictId;
    
    @Column(name = "POSTAL_CODE_ID")
    private Integer postalCodeId;
    
    @Column(name = "STREET_NAME")
    private String streetName;
    
    @Column(name = "STREET_NUMBER", length=50)
    private String streetNumber;
    
    @Column(name = "HOUSE_NAME", length=100)
    private String houseName;
    
    @Column(name = "HOUSE_NUMBER", length=50)
    private String houseNumber;
    
    @Column(name = "BLOCK", length=50)
    private String block;
    
    @Column(name = "NEIGHBORHOOD1", length=25) //RT
    private String neighborhood1;
    
    @Column(name = "NEIGHBORHOOD2", length=25) //RW
    private String neighborhood2;
    
    @Column(name = "BUILDING", length=50)
    private String building;
    
    @Column(name = "FLOOR", length=25)
    private String floor;
    
    @Column(name = "ADDITIONAL_INFO", length=100)
    private String additionalInfo;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @Column(name = "FULL_ADDRESS", length=755)
    private String fullAddress;

    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "SOURCE")
    private String source;
    
    @Column(name = "LONGITUDE")
    private String longitude;
    
    @Column(name = "LATITUDE")
    private String latitude;
    
    @Column(name = "ALTITUDE")
    private String altitude;

    @Column(name = "BUILDING_PREFIX")
    private Integer buildingPrefix;

    @Column(name = "FLOOR_PREFIX")
    private Integer floorPrefix;

    @Column(name = "STREET_NAME_PREFIX")
    private Integer streetNamePrefix;

    @Column(name = "NEIGHBORHOOD1_PREFIX")
    private Integer neighborhood1Prefix;

    @Column(name = "NEIGHBORHOOD2_PREFIX")
    private Integer neighborhood2Prefix;
    
    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + addressId;
        }
    }
}

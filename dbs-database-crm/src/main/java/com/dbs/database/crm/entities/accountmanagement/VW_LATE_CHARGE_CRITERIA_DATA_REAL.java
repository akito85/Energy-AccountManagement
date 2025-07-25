package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_LATE_CHARGE_CRITERIA_DATA_REAL")
public class VW_LATE_CHARGE_CRITERIA_DATA_REAL extends BaseEntities {
    @Id
    @Column(name = "ID")
    private Integer id;
    @Column(name = "ACCOUNT_GROUP_TYPE")
    private Integer accountGroupType;
    @Column(name = "ACCOUNT_GROUP_TYPE_NAME")
    private String accountGroupTypeName;
    @Column(name = "LATE_CHARGE_ID")
    private Integer lateChargeId;
    @Column(name = "ACCOUNT_CATEGORY")
    private Integer accountCategory;
    @Column(name = "ACCOUNT_CATEGORY_NAME")
    private String accountCategoryName;
    @Column(name = "ACCOUNT_TYPE")
    private Integer accountType;
    @Column(name = "ACCOUNT_TYPE_NAME")
    private String accountTypeName;
    @Column(name = "COUNTRY")
    private Integer COUNTRY;
    @Column(name = "COUNTRY_NAME")
    private String COUNTRYNAME;
    @Column(name = "CITY")
    private Integer CITY;
    @Column(name = "CITY_NAME")
    private String CITYNAME;
    @Column(name = "DISTRICT")
    private Integer DISTRICT;
    @Column(name = "DISTRICT_NAME")
    private String DISTRICTNAME;
    @Column(name = "PROVINCE")
    private Integer PROVINCE;
    @Column(name = "PROVINCE_NAME")
    private String PROVINCENAME;
    @Column(name = "SUBDISTRICT")
    private Integer SUBDISTRICT;
    @Column(name = "SUBDISTRICT_NAME")
    private String SUBDISTRICTNAME;
    @Column(name = "SA_TYPE")
    private Integer saType;
    @Column(name = "SA_TYPE_NAME")
    private String saTypeName;
    @Column(name = "COST_CENTER")
    private Integer costCenter;
    @Column(name = "COST_CENTER_NAME")
    private String costCenterName;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "ACCOUNT_NUMBER_ID")
    private Integer accountNumberId;

    @Column(name = "ACCOUNT_SEGMENT")
    private Integer accountSegment;

    @Column(name = "ACCOUNT_SEGMENT_NAME")
    private String accountSegmentName;

    @Column(name = "CLASSIFICATION_TYPE")
    private Integer classificationType;

    @Column(name = "CLASSIFICATION_TYPE_NAME")
    private String classificationTypeName;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "CORPORATE_FLAG_BOOLEAN")
    private Boolean corporateFlagBoolean;

    @Column(name = "CORPORATE_FLAG")
    private String corporateFlag;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "ALL_CRITERIA_BOOLEAN")
    private Boolean allCriteriaBoolean;

    @Column(name = "ALL_CRITERIA")
    private String allCriteria;

    @Column(name = "SOR")
    private Integer sor;

    @Column(name = "SOR_NAME")
    private String sorName;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "WAPU_FLAG_BOOLEAN")
    private Boolean wapuFlagBoolean;

    @Column(name = "WAPU_FLAG")
    private String wapuFlag;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "DESCRIPTION")
    private String description;

}

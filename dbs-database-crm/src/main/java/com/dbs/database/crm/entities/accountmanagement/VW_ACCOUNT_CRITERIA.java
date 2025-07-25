package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "VW_ACCOUNT_CRITERIA")
public class VW_ACCOUNT_CRITERIA implements Serializable {

    @Column(name = "CUSTOMER_ID")
    private Integer customerId;
    
    @Column(name = "CUSTOMER_NUMBER")
    private String customerNumber;
    
    @Column(name = "CUSTOMER_TYPE")
    private Integer customerType;

    @Column(name = "CUSTOMER_STATUS")
    private String customerStatus;

    @Id
    @Column(name = "ACCOUNT_ID")
    private Integer accountId;
    
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;
    
    @Column(name = "ACCOUNT_GROUP")
    private String accountGroup;
    
    @Column(name = "ACCOUNT_SEGMENT")
    private Integer accountSegment;

    @Column(name = "ACCOUNT_GROUP_TYPE")
    private Integer accountGroupType;

    @Column(name = "ACCOUNT_BUDGET")
    private Integer accountBudget;

    @Column(name = "ACCOUNT_INDUSTRIAL_SECTOR")
    private Integer accountIndustrialSector;

    @Column(name = "ACCOUNT_CATEGORY")
    private Integer accountCategory;

    @Column(name = "ACCOUNT_ACCOUNT_TYPE")
    private Integer accountAccountType;
    
    @Column(name = "ACCOUNT_CLASSIFICATION_TYPE")
    private Integer accountClassificationType;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "ACCOUNT_CORPORATE_FLAG")
    private Boolean accountCorporateFlag;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "ACCOUNT_WAPU_FLAG")
    private Boolean accountWapuFlag;
    
    @Column(name = "ACCOUNT_PRIORITY")
    private Integer accountPriority;
    
    @Column(name = "ACCOUNT_STATUS")
    private String accountStatus;
    
    @Column(name = "SOR")
    private Integer sor;

    @Column(name = "COST_CENTER")
    private Integer costCenter;

    @Column(name = "METER_READING_CODE")
    private Integer meterReadingCode;
    
    @Column(name = "PREMISE_COUNTRY")
    private Integer premiseCountry;
    
    @Column(name = "PREMISE_PROVINCE")
    private Integer premiseProvince;
    
    @Column(name = "PREMISE_CITY")
    private Integer premiseCity;
    
    @Column(name = "PREMISE_DISTRICT")
    private Integer premiseDistrict;
    
    @Column(name = "PREMISE_SUBDISTRICT")
    private Integer premiseSubdistrict;
    
    @Column(name = "PREMISE_SP_ASSET_G_SIZE")
    private Integer premiseSpAssetGSize;
    
    @Column(name = "SA_SERVICE_TYPE")
    private Integer saServiceType;

    @Column(name = "SA_PRODUCT_VERSION")
    private Integer saProductVersion;
    
    @Column(name = "SA_TYPE")
    private Integer saType;
    
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    
}

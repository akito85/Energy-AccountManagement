package com.dbs.database.crm.entities.ratingbillinginvoice.view;


import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "VW_TERM_OF_PAYMENT_CRITERIA_DATA")
public class VW_TERM_OF_PAYMENT_CRITERIA_DATA extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TERM_OF_PAYMENT_ID")
    private Integer termOfPaymentId;

    @Column(name = "ACCOUNT_CATEGORY_ID")
    private Integer accountCategoryId;

    @Column(name = "ACCOUNT_CATEGORY")
    private String accountCategory;
    @Column(name = "ACCOUNT_GROUP_TYPE_ID")
    private Integer accountGroupTypeId;

    @Column(name = "ACCOUNT_GROUP_TYPE")
    private String accountGroupType;

    @Column(name = "ALL_CRITERIA")
    private String allCriteria;

    @Column(name = "BUDGET")
    private String budget;
    @Column(name = "BUDGET_ID")
    private Integer budgetId;

    @Column(name = "CITY_ID")
    private Integer cityId;

    @Column(name = "CITY")
    private String city;

    @Column(name = "COST_CENTER_ID")
    private Integer costCenterId;

    @Column(name = "COST_CENTER")
    private String costCenter;

    @Column(name = "GSIZES_ID")
    private Integer gsizesId;

    @Column(name = "GSIZES")
    private String gsizes;

    @Column(name = "INDUSTRIAL_SECTOR_ID")
    private Integer industrialSectorId;

    @Column(name = "INDUSTRIAL_SECTOR")
    private String industrialSector;

    @Column(name = "PROVINCE_ID")
    private Integer provinceId;

    @Column(name = "PROVINCE")
    private String province;

    @Column(name = "SUB_DISTRICT_ID")
    private Integer subDistrictId;

    @Column(name = "SUB_DISTRICT")
    private String subDistrict;

    @Column(name = "SOR_ID")
    private Integer sorId;

    @Column(name = "SOR")
    private String sor;

    @Column(name = "START_DATE")
    private String startDate;

    @Column(name = "END_DATE")
    private String endDate;
    @Column(name = "CUSTOMER")
    private String customer;
    @Column(name = "CUSTOMER_ID")
    private Integer customerId;
    @Column(name = "DISTRICT")
    private String district;
    @Column(name = "DISTRICT_ID")
    private Integer districtId;
    @Column(name = "CUSTOMER_SEGMENT_ID")
    private Integer customerSegmentId;
    @Column(name = "CUSTOMER_SEGMENT")
    private String customerSegment;
}

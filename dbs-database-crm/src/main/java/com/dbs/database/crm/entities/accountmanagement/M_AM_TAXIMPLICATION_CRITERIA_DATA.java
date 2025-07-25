package com.dbs.database.crm.entities.accountmanagement;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import org.codehaus.jackson.map.ObjectMapper;

import com.dbs.common.base.entities.BaseEntities;

import lombok.Data;

@Entity
@Data
@Table(name = "M_AM_TAXIMPLICATION_CRITERIA_DATA")
public class M_AM_TAXIMPLICATION_CRITERIA_DATA extends BaseEntities implements Serializable {

    @Id
    @Column(name = "ID",nullable = false, updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_AM_TAXIMPLICATION_CRITERIA_DATA_SEQ")
    @SequenceGenerator(sequenceName = "M_AM_TAXIMPLICATION_CRITERIA_DATA_SEQ", allocationSize = 1, name = "M_AM_TAXIMPLICATION_CRITERIA_DATA_SEQ")
    private Integer id;

    @Column(name = "M_AM_TAXIMPLICATION_ID") // M_AM_TAXIMPLICATION_CRITERIA_HEADER_ID
    private Integer taximplicationId;

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

    @Column(name = "SOR")
    private Integer sor;

    @Column(name = "COST_CENTER")
    private Integer costCenter;

    @Column(name = "ACCOUNT_CATEGORY")
    private Integer accountCategory;

    @Column(name = "CLASSIFICATION_TYPE")
    private Integer classificationType;

    @Column(name = "ACCOUNT_SEGMENT")
    private Integer accountSegment;

    @Column(name = "ACCOUNT_GROUP_TYPE")
    private Integer accountGroupType;

    @Column(name = "ACCOUNT_TYPE")
    private Integer accountType;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "CORPORATE_FLAG")
    private Boolean corporateFlag;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "WAPU_FLAG")
    private Boolean wapuFlag;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_ALL")
    private Boolean isAll;

    @Column(name = "SA_TYPE")
    private Integer saType;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "DESCRIPTION")
    private String description;
    
    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + id;
        }
    }
}

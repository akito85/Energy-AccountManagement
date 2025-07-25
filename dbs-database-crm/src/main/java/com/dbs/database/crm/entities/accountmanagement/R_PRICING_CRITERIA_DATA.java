package com.dbs.database.crm.entities.accountmanagement;


import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.common.base.utils.CustomddMMMyyyyDeserializer;
import com.dbs.common.base.utils.CustomddMMMyyyySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import org.codehaus.jackson.map.ObjectMapper;
import java.util.Date;

@Entity
@Data
@Table(name = "R_PRICING_CRITERIA_DATA")
public class R_PRICING_CRITERIA_DATA extends DefaultBaseEntities{

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "R_PRICING_CRITERIA_DATA_SEQ")
    @SequenceGenerator(sequenceName = "R_PRICING_CRITERIA_DATA_SEQ",allocationSize = 1, name = "R_PRICING_CRITERIA_DATA_SEQ")
    private Integer id;
    
    @Column(name = "ID_PRICING")
    private Integer idPricing;

    @Column(name = "CUSTOMER")
    private Integer customer;

    @Column(name = "BUDGET")
    private Integer budget;

    @Column(name = "SUB_DISTRICT")
    private Integer subDistrict;

    @Column(name = "DISTRICT")
    private Integer district;

    @Column(name = "CITY")
    private Integer city;

    @Column(name = "PROVINCE")
    private Integer province;

    @Column(name = "AREA")
    private Integer area;

    @Column(name = "SOR")
    private Integer sor;

    @Column(name = "INDUSTRIAL_SECTOR")
    private Integer industrialSector;
    
    @Column(name = "PRODUCT")
    private Integer product;
    
    @Column(name = "GSIZES")
    private Integer gsizes;
    
    @Column(name = "CUSTOMER_SEGMENT")
    private Integer customerSegment;
    
    @Column(name = "ACCOUNT_GROUP")
    private Integer accountGroup;
    
    @Column(name = "SERVICE_TYPE")
    private Integer serviceType;
    
    @Column(name = "ACCOUNT_CATEGORY")
    private Integer accountCategory;
    
    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "ALL_CRITERIA")
    private Boolean allCriteria;

    @JsonSerialize(using = CustomddMMMyyyySerializer.class)
    @JsonDeserialize(using = CustomddMMMyyyyDeserializer.class)
    @Column(name="START_DATE")
    private Date startDate;

    @JsonSerialize(using = CustomddMMMyyyySerializer.class)
    @JsonDeserialize(using = CustomddMMMyyyyDeserializer.class)
    @Column(name="END_DATE")
    private Date endDate;
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

package com.dbs.database.crm.entities.accountmanagement;


import com.dbs.common.base.entities.BaseEntities;
import com.dbs.database.crm.entities.mastermanagement.R_PRICING_CRITERIA;
import com.dbs.database.crm.entities.mastermanagement.R_PRICING_DETAIL;

import javax.persistence.*;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Entity
@Data
@Table(name = "M_PRICING")
public class M_PRICING extends BaseEntities implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_PRICING_SEQ")
    @SequenceGenerator(sequenceName = "M_PRICING_SEQ",allocationSize = 1, name = "M_PRICING_SEQ")
    private Integer id;

  
	@Column(name = "PRICE_CODE")
    private String priceCode;

    @Column(name = "PRICE_DESCRIPTION")
    private String priceDescription;

    @Column(name = "STATUS")
    private String status;
    
    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;
    
    @Column(name = "ENTITY")
    private Integer entityId;
    
    @Column(name = "TRIGGER_JSON")
    private String triggerJson;

    @Column(name = "APPHIER_ID")
    private Integer appHierId;

    @Column(name = "REMARK")
    private String remark;
    
    @Column(name = "CC_ID")
    private Integer ccId;

    @Column(name ="MAKER_POSITION")
    private  Integer makerPosition;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ID_PRICING",referencedColumnName = "ID")
    private List<R_PRICING_CRITERIA> rPricingCriterias;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ID_PRICING",referencedColumnName = "ID")
    private List<R_PRICING_CRITERIA_DATA> criteriasValue;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ID_PRICING_NUMBER",referencedColumnName = "ID")
    private List<R_PRICING_DETAIL> mPricingDetails;
    
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

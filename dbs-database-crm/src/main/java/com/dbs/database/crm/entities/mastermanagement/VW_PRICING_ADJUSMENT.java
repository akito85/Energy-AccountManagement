package com.dbs.database.crm.entities.mastermanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "VW_PRICING_ADJUSMENT")
public class VW_PRICING_ADJUSMENT extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;
    @Column(name = "ID_PRICING_ADJUSTMENT")
    private String pricingAdjustmentId;
    @Column(name = "NAME")
    private String name;
    @Column(name = "PRICE_CODE")
    private String priceCode;
    @Column(name = "PRICING")
    private String pricing;
    @Column(name = "CRITERIAS")
    private String criterias;
    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @Column(name = "CC_ID")
    private Integer ccId;
    
    @Column(name = "M_PRICING_DETAIL_ID")
    private Integer pricingDetailId;
    

}

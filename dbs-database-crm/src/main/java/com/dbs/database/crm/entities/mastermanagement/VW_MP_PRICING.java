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
@Table(name = "VW_MP_PRICING")
public class VW_MP_PRICING extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;
    @Column(name = "ORDER_NO")
    private Integer orderNo;
    @Column(name = "PRICE_CODE")
    private String priceCode;
    @Column(name = "PRICING")
    private String pricing;
    @Column(name = "PRODUCT")
    private String product;
    @Column(name = "MAKER_POSITION")
    private String makerPosition;
    @Column(name = "CRITERIAS")
    private String criterias;
    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;
    @Column(name = "PRICE_DESCRIPTION")
    private String priceDescription;
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @Column(name="CC_ID")
    private Integer ccId;

}

package com.dbs.database.crm.entities.product;

import com.dbs.common.base.entities.DefaultBaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the VW_PRICING_RULE database table.
 */
@Data
@Entity
@Table(name = "VW_PRICING_RULE")
public class VW_PRICING_RULE extends DefaultBaseEntities implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PRICING_RULE_ID")
    private Integer pricingRuleId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CRITERIAS")
    private String criterias;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "APPROVAL_STATUS")
    private String approvalStatus;

    @Column(name = "IS_DELETED")
    private String isDeleted;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "CC_ID")
    private Integer ccId;

    @Column(name = "APPHIER_ID")
    private Integer apphierId;

    @Column(name = "DESCRIPTION")
    private String description;

}
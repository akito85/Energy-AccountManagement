package com.dbs.database.crm.entities.product.promo;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_PROMO")
public class VW_PROMO extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "TYPE_NAME")
    private String typeName;

    @Column(name = "CATEGORY")
    private Integer category;

    @Column(name = "CATEGORY_NAME")
    private String categoryName;

    @Column(name = "CRITERIAS")
    private String criterias;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "START_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;

    @Column(name = "END_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;

    @Column(name = "CC_ID")
    private Integer ccId;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "STATUS_APPROVAL", length = 20)
    private String statusApproval;

    @Column(name = "APPHIER_ID")
    private Integer appHierId;
}

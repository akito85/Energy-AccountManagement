package com.dbs.database.crm.entities.product.promo;


import com.dbs.common.base.entities.DefaultBaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "M_PROMO_CONDITION")
public class M_PROMO_CONDITION extends DefaultBaseEntities implements Serializable {

    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_PROMO_CONDITION_SEQ")
    @SequenceGenerator(sequenceName = "M_PROMO_CONDITION_SEQ",allocationSize = 1, name = "M_PROMO_CONDITION_SEQ")
    private Integer id; 

    @Column(name = "NAME")
    private String name;

    @Column(name = "OPERATOR")
    private String operator;

    @Column(name = "DATA_TYPE")
    private String dataType;
    
    @Column(name = "ADJUSTMENT_VALUE")
    private Double adjustmentValue;

    @Column(name = "START_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;

    @Column(name = "END_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;
    
    @Column(name = "ID_PROMO")
    private Integer idPromo;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "DESCRIPTION")
    private String description;
}

package com.dbs.database.crm.entities.product;


import com.dbs.common.base.entities.BaseEntities;
import java.io.Serializable;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "M_LOCATION")
public class M_LOCATION extends BaseEntities implements Serializable{

    @Id
    @Column(name = "LOCATION_ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_LOCATION_SEQ")
    @SequenceGenerator(sequenceName = "M_LOCATION_SEQ",allocationSize = 1, name = "M_LOCATION_SEQ")
    private Integer locationId;

    @Column(name = "LOCATION_CODE")
    private String locationCode;

    @Column(name = "LOCATION_TYPE")
    private String locationType;

    @Column(name = "LOCATION_TYPE_ID")
    private Integer locationTypeId;

    @Column(name = "LOCATION_NAME")
    private String locationName;

    @Column(name = "LOCATION_PARENT")
    private Integer locationParent;

    @Column(name = "LOCATION_PARENT_TYPE")
    private Integer locationParentType;

    @Column(name = "LOCATION_REFERENCE")
    private Integer locationReference;
}

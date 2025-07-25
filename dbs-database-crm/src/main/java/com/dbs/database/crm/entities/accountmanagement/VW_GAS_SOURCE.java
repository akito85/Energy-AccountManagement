package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "VW_GAS_SOURCE")
public class VW_GAS_SOURCE extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer gasSourceId;

    @Column(name = "CALORIE_CODE")
    private String calorieCode;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "UOM")
    private String uom;

    @Column(name = "ENTITY_ID")
    private Integer entityId;
//    @Column(name = "M3")
//    private Float M3;
//    @Column(name = "BTU")
//    private Float BTU;
//    @Column(name = "N2")
//    private Float N2;
//    @Column(name = "CO2")
//    private Float CO2;

}

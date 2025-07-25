package com.dbs.database.crm.entities.accountmanagement;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "VW_SA_CALCRULE")
public class VW_SA_CALCRULE implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "SA_ID")
    private Integer saId;

    @Column(name = "SA_NUMBER")
    private String saNumber;

    @Column(name = "NAME")
    private String name;

    @Column(name = "NAME_ID")
    private Integer nameId;

    @Column(name = "UNIT")
    private String unit;

    @Column(name = "UNIT_ID")
    private String unitId;

    @Column(name = "VALUE")
    private Integer value;

    @Column(name = "DESCRIPTION")
    private String description;
}

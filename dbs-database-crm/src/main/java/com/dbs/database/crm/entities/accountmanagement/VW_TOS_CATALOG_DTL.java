package com.dbs.database.crm.entities.accountmanagement;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "VW_TOS_CATALOG_DTL")
public class VW_TOS_CATALOG_DTL implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "SA_TOS_ID")
    private Integer saTosId;

    @Column(name = "ATTRIBUTE_ID")
    private Integer attributeId;

    @Column(name = "ATTRIBUTE_NAME")
    private String attributeName;

    @Column(name = "FROM_ITEM_ID")
    private Integer fromItemId;

    @Column(name = "FROM_ITEM_NAME")
    private String fromItemName;

    @Column(name = "UNIT_ID")
    private Integer unitId;

    @Column(name = "UNIT_NAME")
    private String unitName;

    @Column(name = "VALUE")
    private Integer value;

}

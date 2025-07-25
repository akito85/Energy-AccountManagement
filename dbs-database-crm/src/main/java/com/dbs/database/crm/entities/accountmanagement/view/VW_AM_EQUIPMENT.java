package com.dbs.database.crm.entities.accountmanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;

@Entity
@Data
@Table(name = "VW_AM_EQUIPMENT")
public class VW_AM_EQUIPMENT extends BaseEntities {
    
    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "ACCOUNT_ID")
    private Integer accountId;

    @Column(name = "NAME_ID")
    private Integer nameId;
    
    @Column(name = "NAME")
    private String name;

    @Column(name = "TYPE_EQUIPMENT_ID")
    private Integer typeEquipmentId;
    
    @Column(name = "TYPE_EQUIPMENT")
    private String typeEquipment;

    @Column(name = "BRAND_ID")
    private Integer brandId;
    
    @Column(name = "BRAND")
    private String brand;
    
    @Column(name = "QTY")
    private Double qty;

    @Column(name = "QTY_UOM_ID")
    private Integer qtyUomId;
    
    @Column(name = "QTY_UOM")
    private String qtyUom;

    @Column(name = "QTY_VALUE")
    private String qtyValue;
    
    @Column(name = "CAP")
    private Double cap;

    @Column(name = "CAP_UOM_ID")
    private Integer capUomId;
    
    @Column(name = "CAP_UOM")
    private String capUom;

    @Column(name = "CAP_VALUE")
    private String capValue;
    
    @Column(name = "CON")
    private Double con;

    @Column(name = "CON_UOM_ID")
    private Integer conUomId;
    
    @Column(name = "CON_UOM")
    private String conUom;

    @Column(name = "CON_VALUE")
    private String conValue;

    @Column(name = "NOH")
    private Integer noh;

    @Column(name = "NOD")
    private Integer nod;

    @Column(name = "GAS_CONV")
    private Double gasConv;

    @Column(name = "GAS_CONV_UOM_ID")
    private Integer gasConvUomId;

    @Column(name = "GAS_CONV_UOM")
    private String gasConvUom;

    @Column(name = "GAS_CONV_VALUE")
    private String gasConvValue;

    @Column(name = "IS_DUAL_FUEL")
    private String isDualFuel;

    @Column(name = "FUEL_TYPE1_ID")
    private Integer fuelType1Id;

    @Column(name = "FUEL_TYPE1")
    private String fuelType1;

    @Column(name = "FUEL_TYPE2_ID")
    private Integer fuelType2Id;
    
    @Column(name = "FUEL_TYPE2")
    private String fuelType2;
    
    @Column(name = "DESCRIPTION")
    private String description;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;
    
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

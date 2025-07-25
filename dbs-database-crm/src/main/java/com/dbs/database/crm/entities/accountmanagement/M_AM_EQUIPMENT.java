package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

@Entity
@Data
@Table(name = "M_AM_EQUIPMENT")
public class M_AM_EQUIPMENT extends BaseEntities {
    
    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_AM_EQUIPMENT_SEQ")
    @SequenceGenerator(sequenceName = "M_AM_EQUIPMENT_SEQ", allocationSize = 1, name = "M_AM_EQUIPMENT_SEQ")
    private Integer id;

    @Column(name = "ACCOUNT_ID")
    private Integer accountId;
    
    @Column(name = "NAME")
    private Integer name;
    
    @Column(name = "TYPE_EQUIPMENT")
    private Integer typeEquipment;
    
    @Column(name = "BRAND")
    private Integer brand;
    
    @Column(name = "QTY")
    private Double qty;
    
    @Column(name = "QTY_UOM")
    private Integer qtyUom;
    
    @Column(name = "CAP")
    private Double cap;
    
    @Column(name = "CAP_UOM")
    private Integer capUom;
    
    @Column(name = "CON")
    private Double con;
    
    @Column(name = "CON_UOM")
    private Integer conUom;

    @Column(name = "NOH")
    private Integer noh;

    @Column(name = "NOD")
    private Integer nod;

    @Column(name = "GAS_CONV")
    private Double gasConv;

    @Column(name = "GAS_CONV_UOM")
    private Integer gasConvUom;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_DUAL_FUEL")
    private Boolean isDualFuel;
    
    @Column(name = "FUEL_TYPE1")
    private Integer fuelType1;
    
    @Column(name = "FUEL_TYPE2")
    private Integer fuelType2;
    
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

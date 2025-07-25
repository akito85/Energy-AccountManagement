package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;


import java.io.Serializable;
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
@Table(name = "M_ASSETS")
public class M_ASSETS extends BaseEntities implements Serializable {
    
    @Id
    @Column(name = "ID", nullable=false, updatable=false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="M_ASSETS_SEQ")
    @SequenceGenerator(sequenceName = "M_ASSETS_SEQ", allocationSize=1, name ="M_ASSETS_SEQ")
    private Integer id;

    @Column(name = "PRODUCT_VERSION")
    private Integer productVersion;
    
    @Column(name = "SERVICE_TYPE")
    private Integer serviceType;
    
    @Column(name = "ASSET_NAME")
    private Integer assetName;
    
    @Column(name = "TYPE")
    private Integer type;
    
    @Column(name = "SERIAL_NUMBER", length=100)
    private String serialNumber;
    
    @Column(name = "YEAR")
    private Integer year;
    
    @Column(name = "BRAND")
    private Integer brand;
            
    @Column(name = "CUSTODY_TRANSFER")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean custodyTransfer;
    
    @Column(name = "SOURCE")
    private String source;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @Column(name = "INLET_DIAMETER")
    private Double inletDiameter;
    
    @Column(name = "OUTLET_DIAMETER")
    private Double outletDiameter;
    
    @Column(name = "MAXIMUM_INLET_PRESSURE")
    private Double maximumInletPressure;
    
    @Column(name = "MAXIMUM_OUTLET_PRESSURE")
    private Double maximumOutletPressure;
    
    @Column(name = "MINIMUM_INLET_PRESSURE")
    private Double minimumInletPressure;
    
    @Column(name = "MINIMUM_OUTLET_PRESSURE")
    private Double minimumOutletPressure;
    
    @Column(name = "MAX_FLOW_CAPACITY_PER_STREAM", length=50)
    private Double maxFlowCapacityPerStream;
    
    @Column(name = "STREAM_AMOUNT")
    private Double streamAmount;
    
    @Column(name = "G_SIZE")
    private Integer gSize;
    
    @Column(name = "SETTING_PRESSURE")
    private Double settingPressure;
    
    @Column(name = "LENGTH")
    private Double length;
    
    @Column(name = "BOLT_HOLE_AMOUNT")
    private Double boltHoleAmount;
    
    @Column(name = "MINIMUM_CAPACITY")
    private Double minimumCapacity;
    
    @Column(name = "MAXIMUM_CAPACITY")
    private Double maximumCapacity;
    
    @Column(name = "ANSI") // class
    private Integer ansi;
    
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    
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

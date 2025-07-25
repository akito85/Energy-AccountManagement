package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

@Entity
@Data
@Table(name = "M_GAS_SOURCE")
public class M_GAS_SOURCE extends BaseEntities implements Serializable {
    
    @Id
    @Column(name = "ID", nullable=false, updatable=false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="M_GAS_SOURCE_SEQ")
    @SequenceGenerator(sequenceName = "M_GAS_SOURCE_SEQ", allocationSize=1, name = "M_GAS_SOURCE_SEQ")
    private Integer gasSourceId;
    
    @Column(name = "CALORIE_CODE", length=25)
    private String calorieCode;
    
    @Column(name = "NAME", length=50)
    private String name;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @Column(name = "UOM")
    private Integer uom;
    
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "GAS_SOURCE_ID", referencedColumnName = "ID")
    private List<R_GAS_SOURCE_DETAIL> rGasSourceDetail;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "GAS_SOURCE_ID", referencedColumnName  = "ID")
    private List<R_GAS_SOURCE_CRITERIA> rGasSourceCriteria;

    public M_GAS_SOURCE() {
    }

    public M_GAS_SOURCE(String calorieCode, String name, String description, Integer uom, Integer entityId) {
        this.calorieCode = calorieCode;
        this.name = name;
        this.description = description;
        this.uom = uom;
        this.entityId = entityId;
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + gasSourceId;
        }
    }
}

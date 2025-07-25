package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
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
@Table(name = "M_ASSETS_ASSIGNMENT_HISTORY")
public class M_ASSETS_ASSIGNMENT_HISTORY extends BaseEntities implements Serializable {
    
    @Id
    @Column(name = "ID", nullable=false, updatable=false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="M_ASSETS_ASSIGNMENT_HISTORY_SEQ")
    @SequenceGenerator(sequenceName = "M_ASSETS_ASSIGNMENT_HISTORY_SEQ", allocationSize=1, name = "M_ASSETS_ASSIGNMENT_HISTORY_SEQ")
    private Integer id;
    
    @Column(name = "SERVICE_POINT_ID")
    private Integer servicePointId;
    
    @Column(name = "ASSET_ID")
    private Integer assetId;
    
    @Column(name = "REMARK")
    private String remark;
    
    @Column(name = "INSTALL_DATE")
    private Date installDate;
    
    @Column(name = "UNINSTALL_DATE")
    private Date uninstallDate;
    
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

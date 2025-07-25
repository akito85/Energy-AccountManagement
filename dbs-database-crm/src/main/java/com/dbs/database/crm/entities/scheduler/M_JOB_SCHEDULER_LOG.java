/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbs.database.crm.entities.scheduler;

import com.dbs.common.base.entities.DefaultBaseEntities;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "M_JOB_SCHEDULER_LOG")
@Data
public class M_JOB_SCHEDULER_LOG extends DefaultBaseEntities implements Serializable {
    
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2",strategy = "uuid2")
    @Column
    private String id;
    
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtime;
    
    @Column
    private String schedulerName;
    
    @Column
    private String schedulerId;
    
    @Column
    private Boolean status;
    
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "mjobSchedulerLog", cascade = CascadeType.ALL)
    private List<R_JOB_SCHEDULER_LOG_DTL> listDtl;

    
    
    public String getFormattedDate() {
        if( this.dtime != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            return sdf.format( this.dtime );
        }
        
        return "";
        
    }
    
}

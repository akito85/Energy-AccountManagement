/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbs.database.crm.entities.scheduler;

import com.dbs.common.base.entities.DefaultBaseEntities;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "R_JOB_SCHEDULER_LOG_DTL")
@Data
public class R_JOB_SCHEDULER_LOG_DTL extends DefaultBaseEntities implements Serializable {
    
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2",strategy = "uuid2")
    @Column
    private String id;
    
    @Column
    private String message;
    
    @Column
    private String parameterJob;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedulerId")
    private M_JOB_SCHEDULER_LOG mjobSchedulerLog;

    public R_JOB_SCHEDULER_LOG_DTL(String message) {
        this.message = message;
    }

    public R_JOB_SCHEDULER_LOG_DTL(String message, String parameterJob) {
        this.message = message;
        this.parameterJob = parameterJob;
    }


    
}
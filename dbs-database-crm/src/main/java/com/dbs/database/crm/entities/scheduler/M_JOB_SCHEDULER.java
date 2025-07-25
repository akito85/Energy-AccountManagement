package com.dbs.database.crm.entities.scheduler;

import com.dbs.common.base.entities.DefaultBaseEntities;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Data;

@Entity
@Table(name = "M_JOB_SCHEDULER")
@Data
public class M_JOB_SCHEDULER extends DefaultBaseEntities implements Serializable {

     @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_JOB_SCHEDULER_SEQ")
    @SequenceGenerator(sequenceName = "M_JOB_SCHEDULER_SEQ",allocationSize = 1, name = "M_JOB_SCHEDULER_SEQ")
    private Long id;

    @Column(name="NAME_SCHEDULER")
    private String nameScheduler;

    @Column(name="CRON_EXP")
    private String cronExp;

    @Column(name="JOB_CLASS")
    private String jobClass;

    @Transient
    private boolean active;
    
    @Transient
    private boolean running;
    
    @Transient
    private String prevFireTime;
    
    @Transient
    private String nextFireTime;

}

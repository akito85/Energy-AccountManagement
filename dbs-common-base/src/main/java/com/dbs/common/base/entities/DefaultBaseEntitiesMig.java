package com.dbs.common.base.entities;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Data
@MappedSuperclass
@SuperBuilder
public class DefaultBaseEntitiesMig {
    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;
    
//    @Column(name = "PGN_INSERT_DATE")
//    private Date pgnInsertDate;
//
//    @Column(name = "ETL_EXECUTION_DATE")
//    private Date etlExecutionDate;
//
//    @Column(name = "MIGRATION_STATUS", length = 1)
//    private Integer migrationStatus;
//    
//    @Column(name = "PGN_VALIDATION", length = 1)
//    private Integer pgnValidation;
    @Column(name = "SOURCE", length = 10)
    private String source;
    
    @Column(name = "SOURCE_ID")
    private Integer sourceId;
    
    @Column(name = "PROCESS_OPERATION", length = 1)
    private String processOperation;
    
    @Column(name = "PROCESS_DATE")
    private Date processDate;
    
    @Column(name = "CREATED_EXECUTION_DATE")
    private Date createdExecutionDate;
    
    @Column(name = "CREATED_EXECUTION_BY")
    private String createdExecutionDateBy;
    
    @Column(name = "PROCESS_STATUS", length = 15)
    private String processStatus;

    public DefaultBaseEntitiesMig() {
        super();
    }

}

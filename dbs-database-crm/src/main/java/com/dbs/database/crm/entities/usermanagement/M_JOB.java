package com.dbs.database.crm.entities.usermanagement;


import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "M_JOB")
public class M_JOB extends BaseEntities implements Serializable {

    @Id
    @Column(name = "JOB_ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_JOB_SEQ")
    @SequenceGenerator(sequenceName = "M_JOB_SEQ",allocationSize = 1, name = "M_JOB_SEQ")
    private Integer jobId;

    @Column(name = "JOBNAME")
    private String jobName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IS_DELETED")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isDeleted;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + jobId;
        }
    }

}

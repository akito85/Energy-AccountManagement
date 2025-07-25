package com.dbs.database.crm.entities.accountmanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.entities.DefaultBaseEntities;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "VW_CONTACT")
public class VW_CONTACT extends DefaultBaseEntities implements Serializable {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "CONTACT_NAME")
    private String contactName;

    @Column(name = "JOB_NAME")
    private String jobName;

    @Column(name = "JOB_ID")
    private Integer jobId;

    @Column(name = "POSITION_NAME")
    private String positionName;

    @Column(name = "POSITION_ID")
    private Integer positionId;

    @Column(name = "STATUS")
    private String status;
}

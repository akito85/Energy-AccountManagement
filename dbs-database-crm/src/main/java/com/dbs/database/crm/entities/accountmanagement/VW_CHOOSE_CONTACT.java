package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "VW_CHOOSE_CONTACT")
public class VW_CHOOSE_CONTACT extends BaseEntities implements Serializable {

    // ONLY FOR CHOOSE CONTACT LIST

    @Column(name = "CONTACT_NAME")
    private String contactName;

    @Id
    @Column(name = "CONTACT_ID")
    private Integer contactId;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "CONTACT_ADDRESS_ID")
    private String contactAddressId;

    @Column(name = "CONTACT_ADDRESS")
    private String contactAddress;

    @Column(name = "JOB_NAME")
    private String jobName;

    @Column(name = "JOB_ID")
    private Integer jobId;

    @Column(name = "POSITION_NAME")
    private String positionName;

    @Column(name = "POSITION_ID")
    private Integer positionId;

    private String source;
}

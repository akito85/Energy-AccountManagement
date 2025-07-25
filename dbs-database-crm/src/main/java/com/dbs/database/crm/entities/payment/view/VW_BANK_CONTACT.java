package com.dbs.database.crm.entities.payment.view;

import com.dbs.common.base.entities.DefaultBaseEntities;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "VW_BANK_CONTACT")
@Data
public class VW_BANK_CONTACT extends DefaultBaseEntities implements Serializable {

    private static final long serialVersionUID = 8396758981126033572L;

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "CONTACT_ADDRESS")
    private String contactAddress;

    @Column(name = "CONTACT_NAME")
    private String contactName;

    @Column(name = "JOB_ID")
    private Integer jobId;

    @Column(name = "JOB_TITLE")
    private String jobTitle;

    @Column(name = "POSITION_ID")
    private Integer positionId;

    @Column(name = "POSITION")
    private String position;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "BANK_CODE")
    private String bankCode;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "BANK_ID")
    private Long bankId;

    @Column(name = "DESCRIPTION")
    private String description;
}

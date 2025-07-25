package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;

@Entity
@Data
@Table(name = "VW_ENTITY")
public class VW_ENTITY extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Column(name = "ENTITY_NAME")
    private String entityName;

    @Column(name = "ENTITY_CODE")
    private String entityCode;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "FAX")
    private String fax;

    @Email
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    public VW_ENTITY() {super();}
}

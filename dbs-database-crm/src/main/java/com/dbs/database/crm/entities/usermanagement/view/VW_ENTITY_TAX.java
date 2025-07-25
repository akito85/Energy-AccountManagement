package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import java.io.Serializable;

@Entity
@Data
@Table(name = "VW_ENTITY_TAX")
public class VW_ENTITY_TAX extends BaseEntities implements Serializable {
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

    @Column(name = "TAX_IDENTIFIER")
    private String taxIdentifier;

    @Column(name = "IS_MAIN")
    private String isMain;
}

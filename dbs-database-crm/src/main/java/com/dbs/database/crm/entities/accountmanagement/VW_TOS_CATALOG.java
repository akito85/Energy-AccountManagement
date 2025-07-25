package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "VW_TOS_CATALOG")
@Entity
@Data
public class VW_TOS_CATALOG extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "SA_ID")
    private Integer saId;

    @Column(name = "SA_TOS_ID")
    private Integer saTosId;

    @Column(name = "SA_TOS_NAME")
    private String saTosName;

    @Column(name = "SA_TOS_DESCRIPTION")
    private String saTosDescription;

}

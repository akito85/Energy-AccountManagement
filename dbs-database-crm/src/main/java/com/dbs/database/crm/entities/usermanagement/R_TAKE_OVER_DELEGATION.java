package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.entities.DefaultBaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "R_TAKE_OVER_DELEGATION")
public class R_TAKE_OVER_DELEGATION extends DefaultBaseEntities implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_TAKE_OVER_DELEGATION_SEQ")
    @SequenceGenerator(sequenceName = "R_TAKE_OVER_DELEGATION_SEQ", allocationSize = 1, name = "R_TAKE_OVER_DELEGATION_SEQ")
    private Integer id;

    @Column(name = "TAKE_OVER_BY")
    private Integer takeOverBy;

    @Column(name = "TAKE_OVER_REMARK")
    private String takeOverRemark;
}

package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "R_MENU_ACTION")
public class R_MENU_ACTION extends BaseEntities implements Serializable {

    @Id
    @Column(name="R_MENU_ACTION_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_MENU_ACTION_ID_SEQ")
    @SequenceGenerator(sequenceName = "R_MENU_ACTION_ID_SEQ", allocationSize = 1, name = "R_MENU_ACTION_ID_SEQ")
    private Integer menuActionId;


    @Column(name = "MENU_ID")
    private Integer menuId;

    @Column(name = "ACTION_ID")
    private Integer actionId;

    @OneToOne
    @JoinColumn(name = "ACTION_ID", referencedColumnName = "ACTION_ID",insertable = false, updatable = false)
    private M_ACTION action;
}

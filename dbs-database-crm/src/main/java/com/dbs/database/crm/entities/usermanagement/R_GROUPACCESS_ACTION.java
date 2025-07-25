package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "R_GROUPACCESS_ACTION")
public class R_GROUPACCESS_ACTION extends BaseEntities implements Serializable {
    @Id
    @Column(name = "GA_ACTION_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_GROUPACCESS_ACTION_SEQ")
    @SequenceGenerator(sequenceName = "R_GROUPACCESS_ACTION_SEQ", allocationSize = 1, name = "R_GROUPACCESS_ACTION_SEQ")
    private Integer gaActionId;

    @Column(name = "GA_MENU_ID")
    private Integer gaMenuId;

    @Column(name = "ACTION_ID")
    private Integer actionId;

    @OneToOne
    @JoinColumn(name = "ACTION_ID", referencedColumnName = "ACTION_ID", insertable = false, updatable = false)
    private M_ACTION action;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + gaActionId;
        }
    }
}


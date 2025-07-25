package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "R_GROUPACCESS_MENU")
public class R_GROUPACCESS_MENU extends BaseEntities implements Serializable {
    @Id
    @Column(name="GA_MENU_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "R_GROUPACCESS_MENU_SEQ")
    @SequenceGenerator(sequenceName = "R_GROUPACCESS_MENU_SEQ",allocationSize = 1, name = "R_GROUPACCESS_MENU_SEQ")
    private Integer gaMenuId;

    @Column(name="GA_ID")
    private Integer gaId;

    @Column(name="MENU_ID")
    private Integer menuId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="GA_MENU_ID", referencedColumnName = "GA_MENU_ID")
    private List<R_GROUPACCESS_ACTION> gaAction;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MENU_ID", referencedColumnName = "MENU_ID",insertable = false, updatable = false)
    private M_MENU menu;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + gaMenuId;
        }
    }
}


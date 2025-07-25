package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "M_MENU")
public class M_MENU extends BaseEntities implements Serializable {
    @Id
    @Column(name="MENU_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_MENU_SEQ")
    @SequenceGenerator(sequenceName = "M_MENU_SEQ", allocationSize = 1, name = "M_MENU_SEQ")
    private Integer menuId;

    @Column
    private Integer parentId;

    @Column(length = 250)
    private String name;

    @Column(length = 500)
    private String path;

    @Column
    private Integer menuOrder;

    @Column(length = 250)
    private String iconTag;

    @Column(length = 250)
    private String description;

    @Convert(converter=BooleanToYNStringConverter.class)
    @Column
    private Boolean isTopParent;

    @Convert(converter=BooleanToYNStringConverter.class)
    @Column
    private Boolean isPage;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "MENU_ID", referencedColumnName = "MENU_ID")
    private List<R_MENU_ACTION> menuActions;

    public M_MENU() {
        super();
    }
}


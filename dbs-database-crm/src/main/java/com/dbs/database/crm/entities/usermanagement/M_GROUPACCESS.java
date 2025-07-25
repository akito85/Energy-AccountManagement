package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(
        name = "M_GROUPACCESS",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "NAME")
        }
)
public class M_GROUPACCESS extends BaseEntities implements Serializable {
    @Id
    @Column(name="GA_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_GROUPACCESS_SEQ")
    @SequenceGenerator(sequenceName = "M_GROUPACCESS_SEQ", allocationSize = 1, name = "M_GROUPACCESS_SEQ")
    private Integer gaId;

    @Column(length = 150)
    private String name;

    @Column(length = 255)
    private String description;

    @Column
    private Integer entityId;

    @Column(name = "USER_LEVEL")
    private String userLevel;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="GA_ID", referencedColumnName = "GA_ID")
    private List<R_GROUPACCESS_MENU> gaMenu;
}


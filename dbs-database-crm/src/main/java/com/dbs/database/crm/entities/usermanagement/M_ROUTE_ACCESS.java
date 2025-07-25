package com.dbs.database.crm.entities.usermanagement;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "M_ROUTE_ACCESS")
public class M_ROUTE_ACCESS implements Serializable {
    @Id
    @Column(name = "ROUTE_ACCESS_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_ROUTE_ACCESS_SEQ")
    @SequenceGenerator(sequenceName = "M_ROUTE_ACCESS_SEQ", allocationSize = 1, name = "M_ROUTE_ACCESS_SEQ")
    private Integer routeAccessId;

    @Column(name = "MENU_ID")
    private Integer menuId;

    @Column(name = "ACTION_ID")
    private Integer actionId;

    @Column(name = "PATH")
    private String path;

    @Column(name = "ELEMENT")
    private String element;

    @Column(name = "AUTHORITY")
    private String authority;

    public M_ROUTE_ACCESS(Integer menuId, Integer actionId, String path, String element, String authority) {
        this.menuId = menuId;
        this.actionId = actionId;
        this.path = path;
        this.element = element;
        this.authority = authority;
    }

    public M_ROUTE_ACCESS() {
    }
}

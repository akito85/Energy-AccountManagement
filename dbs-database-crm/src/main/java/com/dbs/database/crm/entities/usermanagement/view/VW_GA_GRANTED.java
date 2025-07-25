package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "VW_GA_GRANTED")
public class VW_GA_GRANTED implements Serializable {
    @Id
    @Column(name = "IDS")
    private Integer ids;
    
    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "GA_ID")
    private Integer gaId;
    
    @Column(name = "ROLE_NAME")
    private String roleName;

    @Column(name = "GA_MENU_ID")
    private Integer gaMenuId;
    
    @Column(name = "MENU_ID")
    private Integer menuId;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PATH")
    private String path;
}

package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_MENU")
public class VW_MENU implements Serializable {
    @Id
    @Column(name="MENU_ID", nullable = false, updatable = false)
    private Integer menuId;

    @Column(name="PARENT_NAME")
    private String parentName;

    @Column(length = 250)
    private String name;

    @Column(length = 500)
    private String path;

    @Column(name="MENU_ORDER")
    private Integer menuOrder;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column
    private Boolean isTopParent;

    @Column(name="MENU_TYPE")
    private String menuType;

//    @Convert(converter=BooleanToYNStringConverter.class)
    @Column
    private String isPage;

    @Column(length = 250)
    private String description;

    @Column(length = 250)
    private String status;

    @Column(name="CREATED_DATE")
    private Date createdDate;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + menuId;
        }
    }
}

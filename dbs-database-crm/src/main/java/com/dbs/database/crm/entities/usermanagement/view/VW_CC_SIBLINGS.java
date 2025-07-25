package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "VW_CC_SIBLINGS")
public class VW_CC_SIBLINGS extends BaseEntities {
    @Id
    @Column(name = "RCC_ID")
    private Integer rccId;

    @Column(name="PARENT_ID")
    private Integer parentId;

    @Column(name="SIBLING")
    private String sibling;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name="IS_DELETED")
    private Boolean isDeleted;

}

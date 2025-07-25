package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "T_AM_SA_TOS_DTL")
public class T_AM_SA_TOS_DTL extends BaseEntities implements Serializable {

    private static final long serialVersionUID = -2817907711293150467L;

    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "T_AM_SA_TOS_DTL_SEQ")
    @SequenceGenerator(sequenceName = "T_AM_SA_TOS_DTL_SEQ", allocationSize = 1, name = "T_AM_SA_TOS_DTL_SEQ")
    private Integer id;

    @Column(name = "T_AM_SA_TOS_ID")
    private Integer saTosId;

    @Column(name = "ATTRIBUTE")
    private Integer attribute;

    @Column(name = "VALUE")
    private Integer value;

    @Column(name = "UNIT")
    private Integer unit;

    @Column(name = "FROM_ITEM")
    private Integer fromItem;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + id;
        }
    }
}

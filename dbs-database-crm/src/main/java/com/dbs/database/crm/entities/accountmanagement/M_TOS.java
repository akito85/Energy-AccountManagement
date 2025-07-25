package com.dbs.database.crm.entities.accountmanagement;


import com.dbs.common.base.entities.BaseEntities;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "M_TOS")
public class M_TOS extends BaseEntities implements Serializable {

    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_TOS_SEQ")
    @SequenceGenerator(sequenceName = "M_TOS_SEQ",allocationSize = 1, name = "M_TOS_SEQ")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ENTITY")
    private Integer entityId;

    @Column(name = "CCID")
    private Integer ccId;
    
    @OneToMany(mappedBy = "mTos",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<R_TOS_ATTRIBUTE> rTosAttributes;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_TOS",referencedColumnName = "ID")
    private List<R_TOS_CRITERIA> rTosCriterias;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_TOS",referencedColumnName = "ID")
    private List<R_TOS_CRITERIA_DATA> rTosCriteriaDatas;

    public M_TOS(){
        super();
    }

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

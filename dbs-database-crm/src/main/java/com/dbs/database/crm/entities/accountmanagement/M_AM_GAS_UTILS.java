package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

@Entity
@Data
@Table(name = "M_AM_GAS_UTILS")
public class M_AM_GAS_UTILS extends BaseEntities implements Serializable {
    
    @Id
    @Column(name = "ID", nullable=false, updatable=false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="M_AM_GAS_UTILS_SEQ")
    @SequenceGenerator(sequenceName = "M_AM_GAS_UTILS_SEQ", allocationSize = 1, name = "M_AM_GAS_UTILS_SEQ")
    private Integer id;
    
    @Column(name = "ACCOUNT_ID")
    private Integer accountId;
    
    @Column(name = "EFFECTIVE_DATE")
    private Date effectiveDate;
    
    @Column(name = "DESCRIPTION")
    private String description;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="GAS_UTILS_ID", referencedColumnName = "ID")
    private List<R_AM_GAS_UTILS_DTL> gasUtilsDtl;

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

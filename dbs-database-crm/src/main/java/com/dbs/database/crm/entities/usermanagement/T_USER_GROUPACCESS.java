package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "T_USER_GROUPACCESS")
public class T_USER_GROUPACCESS extends BaseEntities implements Serializable {
    @Id
    @Column(name = "USER_GA_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "T_USER_GROUPACCESS_SEQ")
    @SequenceGenerator(sequenceName = "T_USER_GROUPACCESS_SEQ", allocationSize = 1, name = "T_USER_GROUPACCESS_SEQ")
    private Integer userGaId;

    @Column(name = "USER_ID", length = 10)
    private Integer userId;

    @Column(name = "GA_ID")
    private Integer gaId;

    @Column
    private Date startDate;

    @Column
    private Date endDate;

    public T_USER_GROUPACCESS() {
        super();
    }

//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "GA_ID",referencedColumnName = "GA_ID",insertable = false, updatable = false)
//    private List<R_GROUPACCESS_MENU> gaMenu;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + userGaId;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.userGaId);
        hash = 47 * hash + Objects.hashCode(this.userId);
        hash = 47 * hash + Objects.hashCode(this.gaId);
        hash = 47 * hash + Objects.hashCode(this.endDate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final T_USER_GROUPACCESS other = (T_USER_GROUPACCESS) obj;
        if (!Objects.equals(this.userGaId, other.userGaId)) {
            return false;
        }
        if (!Objects.equals(this.userId, other.userId)) {
            return false;
        }
        if (!Objects.equals(this.gaId, other.gaId)) {
            return false;
        }
        return Objects.equals(this.endDate, other.endDate);
    }
    
    
    
}


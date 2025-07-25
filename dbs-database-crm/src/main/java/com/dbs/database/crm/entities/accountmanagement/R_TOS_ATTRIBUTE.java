package com.dbs.database.crm.entities.accountmanagement;


import lombok.Data;

import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "R_TOS_ATTRIBUTE")
public class R_TOS_ATTRIBUTE implements Serializable {

    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "R_TOS_ATTRIBUTE_SEQ")
    @SequenceGenerator(sequenceName = "R_TOS_ATTRIBUTE_SEQ",allocationSize = 1, name = "R_TOS_ATTRIBUTE_SEQ")
    private Integer id;

    @Column(name = "ID_TOS")
    private Integer idTos;

    @Column(name = "ATTRIBUTE_ID")
    private Integer attributeId;

    @Column(name="CREATED_BY")
    private String createdBy;

    @Column(name="UPDATED_BY")
    private String updatedBy;

    @Column(name="CREATED_DATE")
    private Date createdDate;

    @Column(name="UPDATED_DATE")
    private Date updatedDate;
    
    @ManyToOne
    @JoinColumn(name="ID_TOS", referencedColumnName = "ID",updatable = false,insertable = false)
    @JsonIgnore
    private M_TOS mTos;
    
    @ManyToOne
    @JoinColumn(name = "ATTRIBUTE_ID", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private R_GLOBAL_TYPE_VALUE attributeValue;
    
    @Transient
//    @JsonIgnore
    public String getAttributeName() {
        if (attributeValue != null) {
            return getAttributeValue().getName();
        } else {
            return null;
        }
    }

}

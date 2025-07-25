package com.dbs.database.crm.entities.accountmanagement;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.codehaus.jackson.map.ObjectMapper;

import lombok.Data;

@Entity
@Data
@Table(name = "M_BUSINESS_PURPOSE")
public class M_BUSINESS_PURPOSE  implements Serializable {
    @Id
    @Column(name = "ID", nullable=false, updatable=false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="M_BUSINESS_PURPOSE_SEQ")
    @SequenceGenerator(sequenceName = "M_BUSINESS_PURPOSE_SEQ", allocationSize=1, name ="M_BUSINESS_PURPOSE_SEQ")
    private Integer id;
    
    @Column(name = "ACCOUNT_ADDRESS_ID")
    private Integer accountAddressId;

    @Column(name = "BUSINESS_PURPOSE_ID")
    private Integer businessPurposeId;

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

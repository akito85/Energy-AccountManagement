package com.dbs.database.crm.entities.accountmanagement;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import com.dbs.common.base.entities.BaseEntities;

import javax.persistence.*;

@Entity
@Data
@Table(name = "M_PARTY")
public class M_PARTY extends BaseEntities {
    
    @Id
    @Column(name = "ID", nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_PARTY_SEQ")
    @SequenceGenerator(sequenceName = "M_PARTY_SEQ",allocationSize = 1, name = "M_PARTY_SEQ")
    private Integer partyId;

    @Column(name = "PARTY_TYPE")
    private String partyType; // CUSTOMER/ACCOUNT/CONTACT/EMPLOYEE/PARTNER

    @Column(name = "PARTY_OBJECT_ID")
    private String partyObjectId;

    @Column(name = "PARTY_UNIQUE_VALUE")
    private String partyUniqueValue;

    @Column(name = "PARTY_NAME")
    private String partyName;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + partyId;
        }
    }
}

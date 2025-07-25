package com.dbs.database.crm.entities.accountmanagement;

import lombok.Data;
import javax.persistence.*;

import com.dbs.common.base.entities.BaseEntities;
import org.codehaus.jackson.map.ObjectMapper;

@Entity
@Data
@Table(name = "M_CONTACT")
public class M_CONTACT extends BaseEntities {

    @Id
    @Column(name = "ID", nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_CONTACT_SEQ")
    @SequenceGenerator(sequenceName = "M_CONTACT_SEQ",allocationSize = 1, name = "M_CONTACT_SEQ")
    private Integer contactId;

    @Column(name="FIRST_NAME")
    private String firstName;

    @Column(name="MIDDLE_NAME")
    private String middleName;

    @Column(name="LAST_NAME")
    private String lastName;

    @Column(name="CONTACT_NAME")
    private String contactName; // Concate First Name, Middle Name, Last Name

    @Column(name="JOB")
    private Integer jobId;
    
    @Column(name="POSITION")
    private Integer positionId;

    @Column(name="CONTACT_ADDRESS")
    private Integer contactAddressId;

    @Column(name="DESCRIPTION")
    private String description;
    
    @Column(name="PARTY_ID")
    private Integer partyId;
    
    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + contactId;
        }
    }
}

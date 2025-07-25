package com.dbs.database.crm.entities.accountmanagement;

import lombok.Data;

import javax.persistence.*;
import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import org.codehaus.jackson.map.ObjectMapper;

@Entity
@Data
@Table(name = "M_ACCOUNT_CONTACT")
public class M_ACCOUNT_CONTACT extends BaseEntities{
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_ACCOUNT_CONTACT_SEQ")
    @SequenceGenerator(sequenceName = "M_ACCOUNT_CONTACT_SEQ",allocationSize = 1, name = "M_ACCOUNT_CONTACT_SEQ")
    private Integer accountContactId;

    @Column(name = "ACCOUNT_ID")
    private Integer accountId;

    @Column(name = "CONTACT_ID")
    private Integer contactId;
    
    @Column(name = "CONTACT_ADDRESS")
    private Integer contactAddressId;
    
    @Column(name = "ADDITIONAL_NOTE")
    private String additionalNote;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "PRIMARY_FLAG")
    private Boolean primaryFlag;
    
    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + accountContactId;
        }
    }
}

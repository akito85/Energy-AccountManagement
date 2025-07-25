package com.dbs.database.crm.entities.accountmanagement;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.dbs.common.base.utils.CustomddMMyyyyDeserializer;
import com.dbs.common.base.utils.CustomddMMyyyySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.codehaus.jackson.map.ObjectMapper;

import com.dbs.common.base.entities.BaseEntities;

import lombok.Data;

@Entity
@Data
@Table(name = "M_CUSTOMER")
public class M_CUSTOMER extends BaseEntities implements Serializable {
    
    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_CUSTOMER_SEQ")
    @SequenceGenerator(sequenceName = "M_CUSTOMER_SEQ",allocationSize = 1, name = "M_CUSTOMER_SEQ")
    private Integer customerId;

    @NotNull
    @Column(name="CUSTOMER_NUMBER")
    private String customerNumber;

    @Column(name="CUSTOMER_TYPE")
    private Integer customerType; // Organization/Person

    @Column(name="FIRST_NAME")
    private String firstName;

    @Column(name="MIDDLE_NAME")
    private String middleName;

    @Column(name="LAST_NAME")
    private String lastName;

    @Column(name="CUSTOMER_NAME")
    private String customerName; // Concate First Name, Middle Name, Last Name

    @Column(name="CUSTOMER_IDENTIFICATION_NUMBER")
    private String customerIdentificationNumber;

    @Column(name="IDENTIFICATION_TYPE")
    private Integer identificationType; // NPWP/KTP

    @Column(name="POSITION_ID")
    private Integer positionId; // Akan terisi oleh position_id CM

    @Column(name="SEX")
    private Integer sex;

    @Column(name="FOUNDED_BIRTH_PLACE")
    private String foundedBirthPlace;

    @Column(name="FOUNDED_BIRTH_DATE")
    @JsonSerialize(using = CustomddMMyyyySerializer.class)
    @JsonDeserialize(using = CustomddMMyyyyDeserializer.class)
    private Date foundedBirthDate;

    @Column(name="MARITAL_STATUS")
    private Integer maritalStatus;

    @Column(name="SEARCH_KEY")
    private String searchKey;
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
            return getClass().getName() + "#" + customerId;
        }
    }
}

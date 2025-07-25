package com.dbs.database.crm.entities.accountmanagement;

import lombok.Data;
import javax.persistence.*;

import com.dbs.common.base.entities.BaseEntities;

@Entity
@Data
@Table(name = "M_CONTACT_DETAILS")
public class M_CONTACT_DETAILS extends BaseEntities {

    @Id
    @Column(name = "ID", nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_CONTACT_DETAILS_SEQ")
    @SequenceGenerator(sequenceName = "M_CONTACT_DETAILS_SEQ",allocationSize = 1, name = "M_CONTACT_DETAILS_SEQ")
    private Integer contactDetailsId; 

    @Column(name = "CONTACT_ID")
    private Integer contactId;

    @Column(name = "TYPE")
    private Integer type; // PHONE/MOBILE PHONE/EMAIL/URL/FAX/WHATSAPP/PGN MOBILE
    
    @Column(name = "INPUT_TYPE")
    private Integer inputType;
    
    @Column(name = "PREFIX_1", length=10)
    private Integer prefix1;
    
    @Column(name = "PREFIX_2", length=10)
    private Integer prefix2;
    
    @Column(name = "VALUE", length=50)
    private String value;
    
    @Column(name = "SUFIX", length=10)
    private String sufix;
    
    @Column(name = "FULL_VALUE", length=255)
    private String fullValue;
    
    @Column(name = "CONTACT_VALUE", length=255)
    private String contactValue;

//    @Column(name = "VALUE_1")
//    private String value1; // Khusus Mobile Phone Concat Value dengan Country Code (+62, dll)
//
//    @Column(name = "VALUE_2")
//    private String value2; // Diisi khusus PGN Mobile (Email pada value 1 & No Telp pada value 2) *no telp concat dengan Country Code

}

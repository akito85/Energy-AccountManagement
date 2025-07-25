/*
 * pada tab relationship di account menampilkan account yang berada pada object/subject & relationship customer  
 */
package com.dbs.database.crm.entities.accountmanagement;

import lombok.Data;

import com.dbs.common.base.entities.BaseEntities;

import java.util.Date;

import javax.persistence.*;

@Entity
@Data
@Table(name = "M_RELATIONSHIP")
public class M_RELATIONSHIP extends BaseEntities {

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_RELATIONSHIP_SEQ")
    @SequenceGenerator(sequenceName = "M_RELATIONSHIP_SEQ", allocationSize = 1, name = "M_RELATIONSHIP_SEQ")
    private Integer relationshipId;

    @Column(name = "CUSTOMER_NUMBER")
    private Integer customerNumber;

    @Column(name = "DIRECTIONAL_FLAG")
    private String directionalFlag;

    @Column(name = "RELATIONSHIP_CATEGORY")
    private String relationshipCategory;

    @Column(name = "SUBJECT_PARTY_ID")
    private Integer subjectPartyId;

    @Column(name = "OBJECT_PARTY_ID")
    private Integer objectPartyId;

    @Column(name = "RELATION_CODE")
    private Integer relationCode;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "OWNER")
    private String owner;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SOURCE")
    private String source;
}

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
@Table(name = "NX_M_RELATIONSHIP")
public class NX_M_RELATIONSHIP extends BaseEntities {

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_RELATIONSHIP_SEQ")
    @SequenceGenerator(sequenceName = "M_RELATIONSHIP_SEQ", allocationSize = 1, name = "M_RELATIONSHIP_SEQ")
    private Integer id;

    @Column(name = "DIRECTIONAL_FLAG")
    private String directionalFlag;

    @Column(name = "SUBJECT_PARTY_ID")
    private Integer subjectPartyId;

    @Column(name = "OBJECT_PARTY_ID")
    private Integer objectPartyId;

    @Column(name = "RELATIONSHIP_CATEGORY")
    private String relationshipCategory;

    @Column(name = "RELATIONSHIP_TYPE")
    private String relationType;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SOURCE")
    private String source;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;
}

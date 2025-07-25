package com.dbs.database.crm.entities.accountmanagement;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.dbs.common.base.entities.BaseEntities;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.codehaus.jackson.map.ObjectMapper;

import lombok.Data;

@Entity
@Data
@Table(name = "T_AM_TOS_SUBMISSION")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class T_AM_TOS_SUBMISSION extends BaseEntities implements Serializable {

    private static final long serialVersionUID = 8551889530978380318L;

    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "T_AM_TOS_SUBMISSION_SEQ")
    @SequenceGenerator(sequenceName = "T_AM_TOS_SUBMISSION_SEQ", allocationSize = 1, name = "T_AM_TOS_SUBMISSION_SEQ")
    private Integer id;

    @Column(name = "T_AM_SA_TOS_ID")
    private Integer saTosId;

    @Column(name = "T_AM_SA_ID")
    private Integer saId;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "APPLIED_DATE")
    private Date appliedDate;

    @Column(name = "APPHIER_ID")
    private Integer appHierId;

    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;

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

package com.dbs.database.crm.entities.accountmanagement;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.dbs.common.base.entities.BaseEntities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.codehaus.jackson.map.ObjectMapper;

import lombok.Data;

@Entity
@Data
@Table(name = "T_AM_TOS_SUBMISSION_DTL")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class T_AM_TOS_SUBMISSION_DTL extends BaseEntities implements Serializable {

    private static final long serialVersionUID = 2551334892162049546L;

    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "T_AM_TOS_SUBMISSION_DTL_SEQ")
    @SequenceGenerator(sequenceName = "T_AM_TOS_SUBMISSION_DTL_SEQ", allocationSize = 1, name = "T_AM_TOS_SUBMISSION_DTL_SEQ")
    private Integer id;

    @Column(name = "T_AM_TOS_SUBMISSION_ID")
    private Integer tosSubmissionId;

    @Column(name = "ATTRIBUTE")
    private Integer attribute;

    @Column(name = "VALUE")
    private Integer value;

    @Column(name = "UNIT")
    private Integer unit;

    @Column(name = "FROM_ITEM")
    private Integer fromItem;

    @Column(name = "REMARK")
    private String remark;

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

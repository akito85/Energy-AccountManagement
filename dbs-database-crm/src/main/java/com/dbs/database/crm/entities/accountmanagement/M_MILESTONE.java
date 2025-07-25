package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "M_MILESTONE")
@Data

public class M_MILESTONE implements Serializable {
    @Id
    @Column(name="MILESTONE_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_MILESTONE_SEQ")
    @SequenceGenerator(sequenceName = "M_MILESTONE_SEQ", allocationSize = 1, name = "M_MILESTONE_SEQ")
    private Integer milestoneId;

    @NotNull
    @Column(name="CUSTOMERNUMBER")
    private String customerNumber;

    @Column(name="TYPE")
    private String type;

    @Column(name = "DATEVALUE")
    private Date dateValue;

    @Column(name="DESCRIPTION")
    private String description;

    @Column(name="CREATED_BY")
    private String createdBy;

    @Column(name="UPDATED_BY")
    private String updatedBy;

    @Column(name="CREATED_DATE")
    private Date createdDate;

    @Column(name="UPDATED_DATE")
    private Date updatedDate;

    @Convert(converter = BooleanToYNStringConverter.class)
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + milestoneId;
        }
    }
}

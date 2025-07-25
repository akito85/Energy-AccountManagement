package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "VW_MILESTONE")
@Data

public class VW_MILESTONE implements Serializable {
    @Id
    @Column(name="MILESTONE_ID")
    private Integer milestoneId;

    @NotNull
    @Column(name="CUSTOMERNUMBER")
    private String customerNumber;

    @Column(name="TYPE_CODE")
    private String typeCode;

    @Column(name="TYPE_NAME")
    private String typeName;

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

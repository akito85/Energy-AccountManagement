package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "VW_LOCATION")

public class VW_LOCATION extends BaseEntities {
    @Id
    @Column(name = "LOCATION_ID")
    private Integer locationId;

    @Column(name = "LOCATION_CODE")
    private String locationCode;

    @Column(name = "LOCATION_NAME")
    private String locationName;

    @Column(name = "LOCATION_TYPE_ID")
    private Integer locationTypeId;

    @Column(name = "LOCATION_TYPE")
    private String locationType;

    @Column(name = "LOCATION_PARENT_TYPE_ID")
    private Integer locationParentTypeId;

    @Column(name = "LOCATION_PARENT_TYPE")
    private String locationParentType;

    @Column(name = "LOCATION_PARENT_ID")
    private Integer locationParentId;

    @Column(name = "LOCATION_PARENT")
    private String locationParent;

    @Column(name = "LOCATION_REFERENCE_ID")
    private Integer locationReferenceId;

    @Column(name = "LOCATION_REFERENCE")
    private String locationReference;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + locationId;
        }
    }
}

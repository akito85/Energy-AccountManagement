package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "M_METERREADINGROUTE")
public class M_METERREADINGROUTE {

    @Id
    @Column(name="METERREADINGROUTEID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_METERREADINGROUTE_SEQ")
    @SequenceGenerator(sequenceName = "M_METERREADINGROUTE_SEQ", allocationSize = 1, name = "M_METERREADINGROUTE_SEQ")
    private Integer meterReadingRouteId;

    @Column(name = "METERREADINGROUTE")
    private String meterReadingRoute;

    @Column(name = "AREACODE")
    private String areaCode;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;

    @Column(name = "ENTITY_ID")
    private Integer entityId;
}

package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "M_ANNOUNCEMENT")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class M_ANNOUNCEMENT extends BaseEntities implements Serializable {
    @Id
    @Column(name="ANN_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_ANNOUNCEMENT_SEQ")
    @SequenceGenerator(sequenceName = "M_ANNOUNCEMENT_SEQ", allocationSize = 1, name = "M_ANNOUNCEMENT_SEQ")
    private Integer announcementId;

    @Column(name="ANN_NAME", length = 100)
    private String annName;

    @Column(name="ANN_CONTENT", length = 4000)
    private String annContent;

    @Column(name="ENTITY_ID")
    private Integer entityId;

    @Column(name="CONTENT_TYPE", length = 100)
    private String contentType;

    @Column(name="DESCRIPTION", length = 255)
    private String description;

    @Column(name="START_DATE")
    private Date startDate;

    @Column(name="END_DATE")
    private Date endDate;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name="IS_DELETED")
    private Boolean isDeleted;

}

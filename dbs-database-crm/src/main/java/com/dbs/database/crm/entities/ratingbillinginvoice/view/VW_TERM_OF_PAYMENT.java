package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_TERM_OF_PAYMENT")
public class VW_TERM_OF_PAYMENT extends DefaultBaseEntities implements Serializable {

    @Id
    private Integer id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "TOP_TERMS")
    private Integer topTerms;
    @Column(name = "TOP_TYPE")
    private String topType;
    @Column(name = "INCLUDE_SATURDAY")
    private String includeSaturday;
    @Column(name = "INCLUDE_SUNDAY")
    private String includeSunday;
    @Column(name = "INCLUDE_CALENDAR")
    private String includeCalendar;
    @Column(name = "CRITERIAS")
    private String criterias;
    @Column(name = "CRITERIAS_DOWNLOAD")
    private String criteriasDownload;
    @Column(name = "START_DATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;
    @Column(name = "END_DATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "CCID")
    private Integer ccId;
    @Column(name = "ENTITY_ID")
    private Integer entityId;
}

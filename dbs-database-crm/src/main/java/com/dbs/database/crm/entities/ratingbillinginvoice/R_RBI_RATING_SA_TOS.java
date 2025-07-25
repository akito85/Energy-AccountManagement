package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "R_RBI_RATING_SA_TOS")
public class R_RBI_RATING_SA_TOS implements Serializable {
    @Id
    @Column(name="RATING_SA_TOS_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_RBI_RATING_SA_TOS_SEQ")
    @SequenceGenerator(sequenceName = "R_RBI_RATING_SA_TOS_SEQ", allocationSize = 1, name = "R_RBI_RATING_SA_TOS_SEQ")
    private Integer ratingSaTosId;

    @Column(name = "RATING_SA_ID")
    private Integer ratingSaId;

    @Column(name = "TOS_NAME")
    private String tosName;

    @Column(name = "START_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date startDate;

    @Column(name = "END_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date endDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "APPLIED_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date appliedDate;
}

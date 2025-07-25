package com.dbs.database.crm.entities.ratingbillinginvoice;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "R_RBI_RATING_SA_DETAIL")
public class R_RBI_RATING_SA_DETAIL implements Serializable {
    @Id
    @Column(name="RATING_SA_DETAIL_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_RBI_RATING_SA_DETAIL_SEQ")
    @SequenceGenerator(sequenceName = "R_RBI_RATING_SA_DETAIL_SEQ", allocationSize = 1, name = "R_RBI_RATING_SA_DETAIL_SEQ")
    private Integer ratingSaDetailId;

    @Column(name = "RATING_SA_ID")
    private Integer ratingSaId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "VALUE")
    private Integer value;

    @Column(name = "UNIT")
    private String unit;

    @Column(name = "DESCRIPTION")
    private String description;
}

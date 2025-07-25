package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "R_RBI_CALC_COST_CENTER")
public class R_RBI_CALCULATION_COST_CENTER extends BaseEntities implements Serializable {
    @Id
    @Column(name ="ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "R_RBI_CALCULATIONCOSTCENTER_SEQ")
    @SequenceGenerator(sequenceName = "R_RBI_CALCULATIONCOSTCENTER_SEQ",allocationSize = 1, name = "R_RBI_CALCULATIONCOSTCENTER_SEQ")
    private Integer id;

    @Column(name = "CALCULATION_CODE")
    private String calCode;

    @Column(name = "COST_CENTER")
    private Integer costCenter;
}

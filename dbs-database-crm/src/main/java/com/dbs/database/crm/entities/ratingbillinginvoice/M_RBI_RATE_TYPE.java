package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "M_RBI_RATE_TYPE")
public class M_RBI_RATE_TYPE extends BaseEntities implements Serializable {
    @Id
    @Column(name ="TYPE_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_RBI_RATE_TYPE_SEQ")
    @SequenceGenerator(sequenceName = "M_RBI_RATE_TYPE_SEQ", allocationSize = 1, name = "M_RBI_RATE_TYPE_SEQ")
    private Integer typeId;
    @Column(name ="CODE")
    private String code;
    @Column(name ="DESCRIPTION")
    private String description;
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @Column(name = "CCID")
    private Integer ccId;
}

package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "M_RBI_FACTURE_CODE")
public class M_RBI_FACTURE_CODE extends BaseEntities implements Serializable {

    private static final long serialVersionUID = -5566724441187781083L;

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_RBI_FACTURE_CODE_SEQ")
    @SequenceGenerator(sequenceName = "M_RBI_FACTURE_CODE_SEQ", allocationSize = 1, name = "M_RBI_FACTURE_CODE_SEQ")
    private Integer id;

    @Column(name = "CODE", length = 20)
    private String code;

}

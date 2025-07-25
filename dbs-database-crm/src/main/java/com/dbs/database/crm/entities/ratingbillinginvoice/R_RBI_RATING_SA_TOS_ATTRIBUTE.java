package com.dbs.database.crm.entities.ratingbillinginvoice;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "R_RBI_RATING_SA_TOS_ATTRIBUTE")
public class R_RBI_RATING_SA_TOS_ATTRIBUTE implements Serializable {
    @Id
    @Column(name="RATING_SA_TOS_ATTRIBUTE_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_RBI_RATING_SA_TOS_ATTRIBUTE_SEQ")
    @SequenceGenerator(sequenceName = "R_RBI_RATING_SA_TOS_ATTRIBUTE_SEQ", allocationSize = 1, name = "R_RBI_RATING_SA_TOS_ATTRIBUTE_SEQ")
    private Integer ratingSaTosAttributeId;

    @Column(name = "RATING_SA_TOS_ID")
    private Integer ratingSaTosId;

    @Column(name = "ATTRIBUTE")
    private Integer attribute;

    @Column(name = "VALUE")
    private Integer value;

    @Column(name = "UNIT")
    private Integer unit;

    @Column(name = "FROM_ITEM")
    private Integer fromItem;
}

package com.dbs.database.crm.entities.accountmanagement;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "VW_TOS_SUBMISSION_DTL")
public class VW_TOS_SUBMISSION_DTL implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TOS_SUBMISSION_ID")
    private Integer tosSubmissionId;

    @Column(name = "SA_ID")
    private Integer saId;

    @Column(name = "SA_NUMBER")
    private String saNumber;

    @Column(name = "ATTRIBUTE")
    private String attribute;

    @Column(name = "ATTRIBUTE_ID")
    private Integer attributeId;

    @Column(name = "FROM_ITEM")
    private String fromItem;

    @Column(name = "FROM_ITEM_ID")
    private Integer fromItemId;

    @Column(name = "UNIT")
    private String unit;

    @Column(name = "UNIT_ID")
    private Integer unitId;

    @Column(name = "VALUE")
    private String value;
}

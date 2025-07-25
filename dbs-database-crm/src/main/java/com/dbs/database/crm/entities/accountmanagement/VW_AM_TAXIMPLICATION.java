package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "VW_AM_TAXIMPLICATION")
public class VW_AM_TAXIMPLICATION extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;
    @Column(name = "TAX_IMPLICATION_NAME")
    private String taxImplicationName;
    @Column(name = "SERVICE_TYPE")
    private String serviceType;
    @Column(name = "SERVICE_TYPE_ID")
    private Integer serviceTypeId;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "CATEGORY_ID")
    private Integer categoryId;
    @Column(name = "TAX_IMPLICATION_RULE_ID")
    private Integer taxImplicationRuleId;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "GUNGGUNG")
    private String gunggung;
    @Column(name = "VAT_INV")
    private String vatInv;
    @Column(name = "TRANS_CODE")
    private Integer transCode;
    @Column(name = "TRANS_CODE_NAME")
    private String transCodeName;
    @Column(name = "IMPLICATION_TYPE")
    private String implicationType;
    @Column(name = "IMPLICATION_TYPE_ID")
    private Integer implicationTypeId;
    @Column(name = "CRITERIA")
    private String criteria;

}

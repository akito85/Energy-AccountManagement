package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Table(name = "M_ACCOUNTING_RULE")
public class M_ACCOUNTING_RULE extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ID", nullable=false, updatable=false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="M_ACCOUNTING_RULE_SEQ")
    @SequenceGenerator(sequenceName="M_ACCOUNTING_RULE_SEQ", allocationSize=1, name="M_ACCOUNTING_RULE_SEQ")
    private Integer masterAccountingRuleId;
    
    @Column(name = "CLASSIFICATION_TYPE_NAME", length=50)
    private String classificationTypeName;
    
    @Column(name = "CODE", length=25)
    private String code;

    @Column(name = "RECEIVABLE_ACCOUNT")
    private String receivableAccount;

    @Column(name = "REVENUE_ACCOUNT")
    private String revenueAccount;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ENTITY_ID")
    private Integer entityId;
}

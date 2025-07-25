package com.dbs.database.crm.entities.accountmanagement;


import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

import org.codehaus.jackson.map.ObjectMapper;

import com.dbs.database.crm.entities.product.R_PRICING_RULE_CRITERIA;
import com.dbs.database.crm.entities.product.R_PRICING_RULE_CRITERIA_DATA;
import com.dbs.database.crm.entities.product.VW_PRICING_RULE_DETAIL;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;

import lombok.Data;

@Data
@Entity
@Table(name = "M_PRICING_RULE")
public class M_PRICING_RULE implements Serializable {
    @Column(name = "CC_ID")
    private int ccId;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column
    private String description;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "ENTITY_ID")
    private Integer entityId;

    @NotEmpty(message = "Name can not be empty")
    private String name;

    @Column(name = "IS_DELETED")
    private String isDeleted;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "APPROVAL_STATUS")
    private String approvalStatus;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_PRICING_RULE_SEQ")
    @SequenceGenerator(sequenceName = "M_PRICING_RULE_SEQ", allocationSize = 1, name = "M_PRICING_RULE_SEQ")
    @Column(name = "PRICING_RULE_ID", nullable = false)
    private Integer pricingRuleId;

    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE")
    private Date startDate;

    @Column
    private String status;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "APPHIER_ID")
    private Integer apphierId;

    @JsonIgnore
    private String json;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PRICING_RULE_ID", referencedColumnName = "PRICING_RULE_ID")
    @JsonIgnore
    private List<VW_PRICING_RULE_DETAIL> mpricingRuleDetails;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PRICING_RULE_ID", referencedColumnName = "PRICING_RULE_ID")
    @JsonIgnore
    private List<R_PRICING_RULE_CRITERIA> rpricingRuleCriteriaList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_PRICING_RULE", referencedColumnName = "PRICING_RULE_ID")
    @JsonIgnore
    private List<R_PRICING_RULE_CRITERIA_DATA> rpricingRuleCriteriaDataList;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName();
        }
    }

}

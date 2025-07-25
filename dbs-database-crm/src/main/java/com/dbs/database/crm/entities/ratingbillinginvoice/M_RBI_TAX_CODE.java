package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.DefaultBaseEntities;
import java.io.Serializable;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "M_RBI_TAX_CODE")
public class M_RBI_TAX_CODE extends DefaultBaseEntities implements Serializable {
    
    @Id
    @Column(name = "TAX_CODE_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_RBI_TAX_CODE_SEQ")
    @SequenceGenerator(sequenceName = "M_RBI_TAX_CODE_SEQ", allocationSize = 1, name = "M_RBI_TAX_CODE_SEQ")
    private Integer taxCodeId;
    @Column(name = "TAX_CODE", unique = true, length = 10)
    private String taxCode;
    @Column(name = "TAX_CODE_NAME")
    private String taxCodeName;
    @Column(name = "TAX_RATE")
    private Double taxRate;
    @Column(name = "CATEGORY")
    private Integer category;   //LOV 7
    @Column(name = "GL_ACCOUNT")
    private Integer glAccount;   //LOV
    @Column(name = "START_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date startDate;
    @Column(name = "END_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;
    @Column(name = "DESCRIPTION", length = 255)
    private String description;
    @Column(name = "APP_HIER_ID")
    private Integer appHierId;
    @Column(name = "STATUS_APPROVAL", length = 20)
    private String statusApproval;
    @Column(name = "STATUS", length = 20)
    private String status;
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @Column(name = "CC_ID")
    private Integer ccId;
    @Column(name = "JSON_DATA")
    private String jsonData;
    
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "TAX_CODE_ID",referencedColumnName = "TAX_CODE_ID")
    private List<R_TAX_CODE_CRITERIA> rTaxCodeCriterias;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "TAX_CODE_ID",referencedColumnName = "TAX_CODE_ID")
    private List<R_TAX_CODE_CRITERIA_DATA> criteriasValues;
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "TAX_CODE_ID",referencedColumnName = "TAX_CODE_ID")
    private List<R_RBI_TAX_CODE_CONDITION> rTaxCodeConditions;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.taxCodeId);
        hash = 53 * hash + Objects.hashCode(this.taxCode);
        hash = 53 * hash + Objects.hashCode(this.taxCodeName);
        hash = 53 * hash + Objects.hashCode(this.taxRate);
        hash = 53 * hash + Objects.hashCode(this.category);
        hash = 53 * hash + Objects.hashCode(this.glAccount);
        hash = 53 * hash + Objects.hashCode(this.startDate);
        hash = 53 * hash + Objects.hashCode(this.endDate);
        hash = 53 * hash + Objects.hashCode(this.description);
        hash = 53 * hash + Objects.hashCode(this.appHierId);
        hash = 53 * hash + Objects.hashCode(this.statusApproval);
        hash = 53 * hash + Objects.hashCode(this.status);
        hash = 53 * hash + Objects.hashCode(this.entityId);
        hash = 53 * hash + Objects.hashCode(this.ccId);
        hash = 53 * hash + Objects.hashCode(this.jsonData);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final M_RBI_TAX_CODE other = (M_RBI_TAX_CODE) obj;
        if (!Objects.equals(this.taxCode, other.taxCode)) {
            return false;
        }
        if (!Objects.equals(this.taxCodeName, other.taxCodeName)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.statusApproval, other.statusApproval)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        if (!Objects.equals(this.jsonData, other.jsonData)) {
            return false;
        }
        if (!Objects.equals(this.taxCodeId, other.taxCodeId)) {
            return false;
        }
        if (!Objects.equals(this.taxRate, other.taxRate)) {
            return false;
        }
        if (!Objects.equals(this.category, other.category)) {
            return false;
        }
        if (!Objects.equals(this.glAccount, other.glAccount)) {
            return false;
        }
        if (!Objects.equals(this.startDate, other.startDate)) {
            return false;
        }
        long lEndDate = 0;
        long lotherEndDate = 0;
        if (this.endDate != null) {
            lEndDate = this.endDate.getTime();
        }
        if (other.endDate != null) {
            lotherEndDate = other.endDate.getTime();
        }
        if (!Objects.equals(lEndDate, lotherEndDate)) {
            return false;
        }
        if (!Objects.equals(this.appHierId, other.appHierId)) {
            return false;
        }
        if (!Objects.equals(this.entityId, other.entityId)) {
            return false;
        }
        if (!Objects.equals(this.ccId, other.ccId)) {
            return false;
        }
        return true;
    }
    
}

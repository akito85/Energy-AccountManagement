package com.dbs.database.crm.entities.accountmanagement;

import lombok.Data;

import java.io.Serializable;

import javax.persistence.*;

import org.codehaus.jackson.map.ObjectMapper;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;

@Entity
@Data
@Table(name = "M_ACCOUNT_ADDRESS")
public class M_ACCOUNT_ADDRESS extends BaseEntities implements Serializable{

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_ACCOUNT_ADDRESS_SEQ")
    @SequenceGenerator(sequenceName = "M_ACCOUNT_ADDRESS_SEQ",allocationSize = 1, name = "M_ACCOUNT_ADDRESS_SEQ")
    private Integer id;

    @Column(name = "ACCOUNT_ID")
    private Integer accountId;

    @Column(name = "ADDRESS_ID")
    private Integer addressId;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "PRIMARY_FLAG")
    private Boolean primaryFlag;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "PREMISE_FLAG")
    private Boolean premiseFlag;

    @Column(name = "DESCRIPTION")
    private String description;
    
    @Column(name = "BUSINESS_PURPOSE")
    private Integer businessPurpose;

    public M_ACCOUNT_ADDRESS() {

    }

    //Constructor

    public M_ACCOUNT_ADDRESS(Integer accountId, Integer addressId, Integer businessPurpose, Boolean primaryFlag, Boolean premiseFlag, String description) {
        this.accountId = accountId;
        this.addressId = addressId;
        this.primaryFlag = primaryFlag;
        this.premiseFlag = premiseFlag;
        this.description = description;
    }

    // Getter

    public Integer getId() {
        return id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public Boolean getPrimaryFlag() {
        return primaryFlag;
    }

    public Boolean getPremiseFlag() {
        return premiseFlag;
    }

    public String getDescription() {
        return description;
    }

    // Setter

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public void setPrimaryFlag(Boolean primaryFlag) {
        this.primaryFlag = primaryFlag;
    }

    public void setPremiseFlag(Boolean premiseFlag) {
        this.premiseFlag = premiseFlag;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + id;
        }
    }
}
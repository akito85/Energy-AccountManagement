package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import java.io.Serializable;

import javax.persistence.*;

import com.dbs.common.base.entities.BaseEntities;
import com.fasterxml.jackson.databind.ObjectMapper;


@Entity
@Data
@Table(name = "M_ACCOUNT")
public class M_ACCOUNT extends BaseEntities implements Serializable {

    private static final long serialVersionUID = 2691458113574744258L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_ACCOUNT_SEQ")
    @SequenceGenerator(sequenceName = "M_ACCOUNT_SEQ",allocationSize = 1, name = "M_ACCOUNT_SEQ")
    private Integer accountId;

    @Column(name = "CUSTOMER_ID")
    private Integer customerId;

    @Column(name = "REGISTRATION_NUMBER")
    private String registrationNumber;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "ACCOUNT_NAME")
    private String accountName;

    @Column(name = "ACCOUNT_SEGMENT")
    private Integer accountSegment;

    @Column(name = "ACCOUNT_GROUP_TYPE")
    private Integer accountGroupType;

    @Column(name = "ACCOUNT_CATEGORY")
    private Integer accountCategory;

    @Column(name = "ACCOUNT_RULE_ID")
    private Integer accountRuleId;

    @Column(name = "ACCOUNT_TYPE")
    private Integer accountType;

    @Column(name = "PAYMENT_CHANNEL")
    private Integer paymentChannel;

    @Column(name = "DESCRIPTION")
    private String description;

    // LOCATION INFORMATION

    @Column(name = "SOR") //From Cost Center (SOR) || From Cost Center (AREA)
    private Integer sor;

    @Column(name = "COST_CENTER")
    private Integer costCenter;

    @Column(name = "METER_READING_CODE")
    private Integer meterReadingCode; // Kode Buku (001, 002, dll)

    // Budget Information

    @Column(name = "BUDGET_YEAR")
    private Integer budgetYear;

    @Column(name = "BUDGET")
    private Integer budget;

    @Column(name = "TERITORY")
    private Integer teritory;

    @Column(name = "INDUSTRIAL_SECTOR")
    private Integer industrialSector;

    @Column(name = "IS_CORPORATE")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isCorporate;

    @Column(name = "PRIORITY")
    private Integer priority;

    @Column(name = "IS_EXCEPTION")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isException;

    @Column(name = "ACCOUNT_GROUP")
    private String accountGroup;

    @Column(name = "IS_BAD_DEBT")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isBadDebt;

    @Column(name = "SYNC_FLAG")
    private String syncFlag;

    @Column(name = "ACCOUNT_REFERENCE_ID") // NO SAP
    private String accountReferenceId;

    @Column(name = "ENTITY_ID")
    private Integer entityId;
    
    @Column(name="PARTY_ID")
    private Integer partyId;

   @Override
   public String toString() {
       try {
           ObjectMapper mapper = new ObjectMapper();
           return mapper.writeValueAsString(this);
       } catch (Exception e) {
           e.printStackTrace();
           return getClass().getName() + "#" + accountId;
       }
   }
}

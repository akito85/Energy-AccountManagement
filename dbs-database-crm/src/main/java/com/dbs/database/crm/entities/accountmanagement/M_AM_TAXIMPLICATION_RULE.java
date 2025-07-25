package com.dbs.database.crm.entities.accountmanagement;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.codehaus.jackson.map.ObjectMapper;

import com.dbs.common.base.entities.BaseEntities;

import lombok.Data;

@Entity
@Data
@Table(name = "M_AM_TAXIMPLICATION_RULE")
public class M_AM_TAXIMPLICATION_RULE extends BaseEntities implements Serializable {
    
    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_AM_TAXIMPLICATION_RULE_SEQ")
    @SequenceGenerator(sequenceName = "M_AM_TAXIMPLICATION_RULE_SEQ", allocationSize = 1, name = "M_AM_TAXIMPLICATION_RULE_SEQ")
    private Integer id;

    @Column(name = "M_AM_TAXIMPLICATION_ID")
    private Integer taximplicationId;

    @Column(name = "DOCUMENT_NUMBER")
    private String documentNumber;

    @Column(name = "IMPLICATION_TYPE_ID")
    private Integer implicationTypeId;

    @Column(name = "IMPLICATION_TYPE")
    private String implicationType;

    @Column(name = "IS_VAT_INV")
    private String isVatInv;

    @Column(name = "IS_GUNGGUNG")
    private String isGunggung;

    @Column(name = "TRANS_CODE")
    private Integer transCode;

//    @Column(name = "TRANS_CODE_STRING")
//    private String transCodeString;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IS_OVERRIDE")
    private String isOverride;

    @Column(name = "APPROVAL_STATUS")
    private String approvalStatus;

    @Column(name = "JSON_DATA")
    private String jsonData;

    @Column(name = "APPHIER_ID")
    private Integer apphierId;

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

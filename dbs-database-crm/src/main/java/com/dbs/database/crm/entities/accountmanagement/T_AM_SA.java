package com.dbs.database.crm.entities.accountmanagement;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.codehaus.jackson.map.ObjectMapper;

import com.dbs.common.base.entities.BaseEntities;

import lombok.Data;

@Entity
@Data
@Table(name = "T_AM_SA")
public class T_AM_SA extends BaseEntities implements Serializable {

    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "T_AM_SA_SEQ")
    @SequenceGenerator(sequenceName = "T_AM_SA_SEQ",allocationSize = 1, name = "T_AM_SA_SEQ")
    private Integer id;

    @Column(name = "ACCOUNT_ID")
    private Integer accountId;

    @Column(name = "SA_SERVICE_TYPE")
    private Integer saServiceType;

    @Column(name = "SA_NUMBER")
    private String saNumber;

    @Column(name = "SA_REFERENCE_NUMBER")
    private String saReferenceNumber;

    @Column(name = "SA_REFERENCE_ID")
    private Integer saReferenceId;

    @Column(name = "SA_TYPE")
    private Integer saType;

    @Column(name = "SA_DATE")
    private Date saDate;

    @Column(name = "PJBG_TYPE")
    private Integer pjbgType;

    @Column(name = "START_DATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date startDate;

    @Column(name = "END_DATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date endDate;

    @Column(name = "COMITMENT_DATE")
    private Date comitmentDate;

    @Column(name = "APPROVAL_STATUS")
    private String approvalStatus;

    @Column(name = "BILLING_CYCLE")
    private Integer billingCycle;

    @Column(name = "TERM_OF_PAYMENT")
    private Integer termOfPayment;

    @Column(name = "INVOICE_TEMPLATE")
    private Integer invoiceTemplate;

    @Column(name = "GAS_IN_PLAN_DATE")
    private Date gasInPlanDate;

    @Column(name = "ADDITIONAL_INFO_1")
    private String additionalInfo1;

    @Column(name = "ADDITIONAL_INFO_2")
    private String additionalInfo2;

    @Column(name = "ADDITIONAL_INFO_3")
    private String additionalInfo3;

    @Column(name = "ADDITIONAL_INFO_4")
    private String additionalInfo4;

    @Column(name = "ADDITIONAL_INFO_5")
    private String additionalInfo5;

    @Column(name = "ID_PRODUCT_VERSION")
    private Integer idProductVersion;

    @Column(name = "ID_M_PRICING")
    private Integer idMPricing;

    @Column(name = "IS_PRICING_RULE")
    private String isPricingRule;

    @Column(name = "ID_PRICING_RULE")
    private Integer idPricingRule;

    @Column(name = "IS_MAIN")
    private String isMain;

    @Column(name = "ALREADY_GAS_IN")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean alreadyGasIn;

    @Column(name = "IS_CUSTOM")
    private String isCustom;

    @Column(name = "JSON_DATA")
    private String jsonData;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "APPHIER_ID")
    private Integer appHierId;

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

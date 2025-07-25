package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.common.base.utils.Constant;
import com.dbs.database.crm.entities.ratingbillinginvoice.R_RBI_RATING_SA;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "VW_RATING_SA")
public class VW_RATING_SA {
    @Id
    @Column(name="RATING_SA_ID")
    private Integer ratingSaId;

    @Column(name = "RATING_CODE")
    private String ratingCode;

    @Column(name = "T_AM_SA_ID")
    private Integer tAmSaId;

    @Column(name = "ACCOUNT_ID")
    private Integer accountId;

    @Column(name = "SA_SERVICE_TYPE")
    private String saServiceType;

    @Column(name = "SA_NUMBER")
    private String saNumber;

    @Column(name = "SA_REFERENCE_NUMBER")
    private String saReferenceNumber;

    @Column(name = "SA_TYPE")
    private String saType;

    @Column(name = "SA_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date saDate;

    @Column(name = "PJBG_TYPE")
    private String pjbgType;

    @Column(name = "START_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date startDate;

    @Column(name = "END_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date endDate;

    @Column(name = "COMMITMENT_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date commitmentDate;

    @Column(name = "BILLING_CYCLE")
    private String billingCycle;

    @Column(name = "TERM_OF_PAYMENT")
    private String termOfPayment;

    @Column(name = "INVOICE_TEMPLATE")
    private Integer invoiceTemplate;

    @Column(name = "GAS_IN_PLAN_DATE")
    @JsonFormat(pattern = Constant.FORMAT_DATE, timezone = Constant.TIMEZONE)
    private Date gasInPlanDate;

    @Column(name = "ID_PRODUCT_VERSION")
    private Integer idProductVersion;

    @Column(name = "ID_PRICING")
    private Integer idPricing;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name ="IS_PRICING_RULE")
    private Boolean isPricingRule;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "PAYMENT_CHANNEL")
    private String paymentChannel;

    @Column(name = "DISTRIBUTION_MEDIA")
    private String distributionMedia;
}

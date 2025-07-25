package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.Constant;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_DAILY_RATES;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Entity
@Data
@Table(name = "VW_DAILY_RATE")
public class VW_DAILY_RATE extends BaseEntities implements Serializable {
    @Id
    @Column(name ="RATES_ID")
    private Integer ratesId;
    @Column(name ="RATE_TYPE")
    private String rateType;
    @Column(name ="RATE_TYPE_NAME")
    private String rateTypeName;
    @Column(name ="FROM_CURRENCY")
    private Integer fromCurrency;
    @Column(name ="FROM_CURRENCY_NAME")
    private String fromCurrencyName;
    @Column(name ="TO_CURRENCY")
    private Integer toCurrency;
    @Column(name ="TO_CURRENCY_NAME")
    private String toCurrencyName;
    @Column(name ="RATE_DATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date rateDate;
    @Column(name ="CONVERTED_RATE")
    private String convertedRate;
    @Column(name ="CONVERTED_RATE_REAL")
    private Double convertedRateReal;
    @Column(name ="DESCRIPTION")
    private String description;
    @Column(name = "APPHIER_ID")
    private Integer appHierId;
    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @Column(name = "CCID")
    private Integer ccId;
}

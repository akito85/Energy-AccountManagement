package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.CustomddMMMyyyyDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "M_RBI_DAILY_RATES")
public class M_RBI_DAILY_RATES extends BaseEntities implements Serializable {
    @Id
    @Column(name ="RATES_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_RBI_DAILY_RATES_SEQ")
    @SequenceGenerator(sequenceName = "M_RBI_DAILY_RATES_SEQ", allocationSize = 1, name = "M_RBI_DAILY_RATES_SEQ")
    private Integer ratesId;
    @Column(name ="RATE_TYPE")
    private String rateType;
    @Column(name ="FROM_CURRENCY")
    private Integer fromCurrency;
    @Column(name ="TO_CURRENCY")
    private Integer toCurrency;
    @JsonDeserialize(using = CustomddMMMyyyyDeserializer.class)
    @Column(name ="RATE_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date rateDate;
    @Column(name ="CONVERTED_RATE")
    private Double convertedRate;
    @Column(name ="DESCRIPTION")
    private String description;
    @Column(name = "TRIGGER_JSON")
    private String triggerJson;
    @Column(name = "APPHIER_ID")
    private Integer appHierId;
    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @Column(name = "CCID")
    private Integer ccId;
}

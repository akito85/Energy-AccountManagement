package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "VW_SERVICEAGREEMENT")
@Data

public class VW_SERVICEAGREEMENT extends BaseEntities implements Serializable {
    @Id
    @Column(name = "SA_HEADER_ID",nullable = false,updatable = false)
    private Integer saHeaderId;

    @Column(name = "CUSTOMERNUMBER")
    private String customerNumber;

    @Column(name = "DOCUMENTNUMBER")
    private String documentNumber;

    @Column(name = "DOCUMENTTYPECODE")
    private String documentNumberCode;

    @Column(name = "DOCUMENTTYPENAME")
    private String documentTypeName;

    @Column(name = "DOCUMENTDATE")
    private Date documentDate;

    @Column(name = "STARTDATE")
    private Date startDate;

    @Column(name = "ENDDATE")
    private Date endDate;

    @Column(name = "SAREFERENCE")
    private Integer saReference;

    @Column(name = "BILLINGCYCLEID")
    private Integer billingCycleId;

    @Column(name = "BILLINGCYCLENAME")
    private String billingCycleName;

    @Column(name = "PRODUCTID")
    private Integer productId;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRODUCTTYPECODE")
    private String productTypeCode;

    @Column(name = "PRODUCTTYPENAME")
    private String productTypeName;

    @Column(name = "PRODUCTCLASSCODE")
    private String productClassCode;

    @Column(name = "PRODUCTCLASSNAME")
    private String productClassName;

    @Column(name = "PRODUCTVERSION")
    private String productVersion;

    @Column(name = "PAYMENTTYPECODE")
    private String paymentTypeCode;

    @Column(name = "PAYMENTTYPENAME")
    private String paymentTypeName;

    @Column(name = "CURRENCYCODE")
    private String currencyCode;

    @Column(name = "CURRENCYNAME")
    private String currenyName;

    @Column(name = "TAXCODE")
    private String taxCode;

    @Column(name = "PRICETYPECODE")
    private String priceTypeCode;

    @Column(name = "PRICETYPENAME")
    private String priceTypeName;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + saHeaderId;
        }
    }
}

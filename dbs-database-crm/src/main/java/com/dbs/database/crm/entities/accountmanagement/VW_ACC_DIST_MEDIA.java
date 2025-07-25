package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Entity
@Data
@Table(name = "VW_ACC_DIST_MEDIA")
public class VW_ACC_DIST_MEDIA extends BaseEntities implements Serializable {
    @Id
    @Column(name = "ID")
    private Integer id;
            
    @Column(name = "ACCOUNT_ID")
    private Integer accountId;
            
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber; 
            
    @Column(name = "PRODUCT_NAME")
    private String productName; 
            
//    @Column(name = "PRODUCT_VERSION_ID")
//    private Integer productVersionId;
            
    @Column(name = "PRODUCT_ID")
    private Integer productId;
            
    @Column(name = "PRICE_CODE_ID")
    private Integer priceCodeId;
    
    @Column(name = "PRICE_CODE")
    private String priceCode;
            
    @Column(name = "PRICING_RULE_ID")
    private Integer pricingRuleId;
            
    @Column(name = "START_DATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date startDate;
            
    @Column(name = "END_DATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date endDate;
    
    @Column(name = "REMARK")
    private String remark;
    
    @Column(name = "DESCRIPTION")
    private String description;
            
}

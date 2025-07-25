/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */ 
package com.dbs.database.crm.entities.ratingbillinginvoice.view;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author RachmatY
 */
@Entity
@Data
@Table(name = "VW_POS_FINAL_PRICE_ADJ")
public class VW_POS_FINAL_PRICE_ADJ implements Serializable{
    @Id
    @Column(name = "ID")
    private Integer id;
    @Column(name = "PRICE_CODE")
    private String priceCode;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "NAME")
    private String name;
    @Column(name = "VALUE")
    private String value;
    @Column(name = "ADJUSTMENT_TYPE")
    private String adjustmentType;
    @Column(name = "ADJUSTMENT_VALUE")
    private String adjustmentValue;
    @Column(name = "ACCOUNT_CATEGORY")
    private Integer accountCategory;
    @Column(name = "ACCOUNT_GROUP")
    private Integer  accountGroup;
    @Column(name = "AREA")
    private Integer  area;
    @Column(name = "BUDGET")
    private Integer  budget;
    @Column(name = "CITY")
    private Integer  city;
    @Column(name = "CUSTOMER")
    private Integer  customer;
    @Column(name = "CUSTOMER_SEGMENT")
    private Integer  customerSegment;
    @Column(name = "DISTRICT")
    private Integer  district;
    @Column(name = "GSIZES")
    private Integer  gsizes;
    @Column(name = "INDUSTRIAL_SECTOR")
    private Integer  industrialSector;
    @Column(name = "PROVINCE")
    private Integer  province;
    @Column(name = "SERVICE_TYPE")
    private Integer  serviceType;
    @Column(name = "SOR")
    private Integer  sor;
    @Column(name = "SUB_DISTRICT")
    private Integer  subDistrict;
    
}

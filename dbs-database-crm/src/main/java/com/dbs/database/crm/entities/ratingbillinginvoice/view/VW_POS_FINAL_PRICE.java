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
@Table(name = "VW_POS_FINAL_PRICE")
public class VW_POS_FINAL_PRICE implements Serializable{
    @Id
    @Column(name = "ID")
    private Integer id;
    @Column(name = "PRODUCT_NAME")
    private String productName;
    @Column(name = "UOM")
    private String uom;
    @Column(name = "CURRENCY_ID")
    private Integer currencyId;
    @Column(name = "CURRENCY")
    private String currency;
    @Column(name = "VAT")
    private Integer vat;
    @Column(name = "TAX_CODE_VAT")
    private String taxCodeVat;
    @Column(name = "WITHHOLDING_TAX")
    private Integer withholdingTax;
    @Column(name = "TAX_CODE_WITHHOLDING_TAX")
    private String taxCodeWithholdingTax;
    @Column(name = "PRICE_CODE")
    private String priceCode;
    @Column(name = "PRICE")
    private String  price;
}

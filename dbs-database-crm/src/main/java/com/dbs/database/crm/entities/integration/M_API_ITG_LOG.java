/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbs.database.crm.entities.integration;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "M_API_ITG_LOG")
@Data
public class M_API_ITG_LOG implements Serializable {
    
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2",strategy = "uuid2")
    @Column(name = "UUID")
    private String uuid;
    
    @Column(name = "REQ_TIME")
    private Long reqTime;
    
    @Column(name = "RESP_TIME")
    private Long respTime;
    
    @Column(name = "STATUS")
    private Boolean status;
    
    @Column(name = "RESPONSE", length = 4000)
    private String response;
    
    @Column(name = "REQUEST", length = 4000)
    private String request;
    
    @Column(name = "HTTP_CODE")
    private Integer httpCode;
    
    @Column(name = "SERVICE")
    private String service;
    
    @Column(name = "URL_API", length = 512, nullable = false)
    private String urlApi;
    
    @Column(name = "ORIGIN", length = 255)
    private String origin;
    
    
    public String getRespTimeFormattedDate() {
        if( this.respTime != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            return sdf.format( this.respTime );
        }
        
        return "";
        
    }
    
    public String getReqTimeFormattedDate() {
        if( this.reqTime != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            return sdf.format( this.reqTime );
        }
        
        return "";
        
    }
    
}

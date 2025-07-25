package com.dbs.database.crm.entities.usermanagement.view;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "VW_USER_GROUPACCESS")
public class VW_USER_GROUPACCESS {
    @Id
    @Column(name = "ROW_NUMBER")
    private Integer rowNum;

    @Column(name = "GA_ID")
    private Integer gaId;

    @Column(name="ACCESS_CODE")
    private String accessCode;

}

package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Accessors(chain = true)
@Table(name = "T_INVOICE_LOG")
@NoArgsConstructor
public class T_INVOICE_LOG implements Serializable {
    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "T_INVOICE_LOG_SEQ")
    @SequenceGenerator(sequenceName = "T_INVOICE_LOG_SEQ", allocationSize = 1, name = "T_INVOICE_LOG_SEQ")
    private Integer id;
    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;
    @Column(name = "ACTION")
    private String action;
    @Column(name = "ACTION_BY")
    private String actionBy;
    @ManyToOne
    @JoinColumn(name = "FORMAT_OPTION")
    private R_GLOBAL_TYPE_VALUE formatOptionType;
    @Column(name = "ACTION_DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date actionDate;
    @Column(name = "REMARK")
    private String remark;
    @Column(name = "PATH")
    private String path;
    @Column(name = "STATUS_INV")
    private String statusInv;
    @Column(name = "MESSAGE")
    private String message;
}

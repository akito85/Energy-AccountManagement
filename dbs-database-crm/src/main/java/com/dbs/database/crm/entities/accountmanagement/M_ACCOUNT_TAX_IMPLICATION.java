package com.dbs.database.crm.entities.accountmanagement;

import com.dbs.common.base.entities.BaseEntities;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "M_ACCOUNT_TAX_IMPLICATION")
public class M_ACCOUNT_TAX_IMPLICATION extends BaseEntities{
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_ACCOUNT_TAX_IMPLICATION_SEQ")
    @SequenceGenerator(sequenceName = "M_ACCOUNT_TAX_IMPLICATION_SEQ",allocationSize = 1, name = "M_ACCOUNT_TAX_IMPLICATION_SEQ")
    private Integer id;
    
    @Column(name = "ACCOUNT_ID")
    private Integer accountId;
    
    @Column(name = "TAX_IMPLICATION_ID")
    private Integer taxImplicationId;
    
    @Column(name = "START_DATE")
    private Date startDate;
    
    @Column(name = "END_DATE")
    private Date endDate;
}

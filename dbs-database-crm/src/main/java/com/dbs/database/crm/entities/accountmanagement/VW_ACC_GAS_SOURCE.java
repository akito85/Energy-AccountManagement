package com.dbs.database.crm.entities.accountmanagement;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_ACC_GAS_SOURCE")
public class VW_ACC_GAS_SOURCE {
    @Id
    @Column(name = "ID")
    private Integer id;
	@Column(name = "ACCOUNT_ID")
    private Integer accountId;
	@Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;
	@Column(name = "GAS_SOURCE_CODE_ID")
    private Integer gasSourceCodeId;
	@Column(name = "CALORIE_CODE")
    private String calorieCode;
	@Column(name = "NAME")
    private String name;
	@Column(name = "UOM")
    private String uom;
	@Column(name = "UOM_ID")
    private Integer uomId;
	@Column(name = "CALORIE_TYPE")
    private String calorieType;
	@Column(name = "CALORIE_TYPE_ID")
    private Integer calorieTypeId;
	@Column(name = "START_DATE")
    private Date startdDate;
    @Column(name = "END_DATE")
    private Date endDate;
    @Column(name = "STATUS")
    private String status;
}

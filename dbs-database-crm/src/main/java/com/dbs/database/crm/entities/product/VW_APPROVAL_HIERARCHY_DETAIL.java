package com.dbs.database.crm.entities.product;

import java.io.Serializable;
import javax.persistence.*;

import org.codehaus.jackson.map.ObjectMapper;

import lombok.Data;



/**
 * The persistent class for the VW_PRICING_RULE_DETAIL database table.
 * 
 */
@Entity
@Data
//@NamedQuery(name="VW_PRICE_CODE_DETAIL.findAll", query="SELECT v FROM VW_PRICE_CODE_DETAIL v")
public class VW_APPROVAL_HIERARCHY_DETAIL implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="APPHIER_ID")
	private Integer apphierId;
	
	@Id
	private Integer ids;
	
	@Column(name="APPHIER_DTL_ID")
	private Integer apphierDtlId;
	
        @Column
	private String position;

	@Column(name="FULL_NAME")
	private String fullName;
	
	@Column(name="APPROVAL_LEVEL")
	private Integer approvalLevel;
	
	@Column(name="IS_FINAL")
	private String isFinal;

	@Column(name="IS_SUBMITTER")
	private String isSubmitter;

        @Column
	private String description;

	@Column(name="EMPLOYEE_ID")
	private Integer employeeId;
	
	@Column(name="EMPLOYEE_code")
	private String employeeCode;
	
    @Column(name="POSITION_ID")
    private Integer positionId;
	
    @Column(name="APPROVAL_NAME")
    private String approvalName;
    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + apphierId;
        }
    }

	
}
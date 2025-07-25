package com.dbs.database.crm.entities.product;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "T_APPROVAL_HISTORY")
public class T_APPROVAL_HISTORY  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "T_APPROVAL_HISTORY_SEQ")
    @SequenceGenerator(sequenceName = "T_APPROVAL_HISTORY_SEQ", allocationSize = 1, name = "T_APPROVAL_HISTORY_SEQ")
     private Integer ids;
    
    
    @Column(name="APPHIER_ID")
	private Integer apphierId;

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


	@Column(name="EMPLOYEE_code")
	private String employeeCode;

    @Column(name = "EVENT_DATE")
    private Date eventDate;

    @Column(name = "EVENT_TYPE")
    private String eventType;
    
    @Column(name = "REF_ID")
    private Integer refId;

    @Column(name = "REF_ID_STRING")
    private String refIdString;
    
    @Column(name = "APPROVAL_TYPE")
    private String approvalType;
    
    @Column
    private String category;
    
    @Column
    private String description;
    
    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;
    
    
    @Column(name = "EMPLOYEE_ID")
    private Integer employeeId;
    
    @Column(name = "T_APP_ID")
    private Integer tAppId;
    
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
            return getClass().getName() + "#" + tAppId;
        }
    }

}

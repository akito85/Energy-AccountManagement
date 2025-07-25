package com.dbs.database.crm.entities.ratingbillinginvoice;

import com.dbs.common.base.entities.DefaultBaseEntities;
import lombok.Data;

import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "M_RBI_TERMS_OF_PAYMENT")
public class M_RBI_TERMS_OF_PAYMENT extends DefaultBaseEntities implements Serializable {
    @Id
    @Column(name = "TERMS_OF_PAYMENT_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_RBI_TOP_SEQ")
    @SequenceGenerator(sequenceName = "M_RBI_TOP_SEQ", allocationSize = 1, name = "M_RBI_TOP_SEQ")
    private Integer termsOfPaymentId;
    @Column(name = "TERMS_OF_PAYMENT_NAME")
    private String termsOfPaymentName;
    @Column(name = "TOP_TYPE")
    private Integer topType;
    @Column(name = "TOP_TERMS")
    private Integer topTerms;
    @Column(name = "INCLUDE_SUNDAY")
    private Character includeSunday;
    @Column(name = "INCLUDE_SATURDAY")
    private Character includeSaturday;
    @Column(name = "INCLUDE_CALENDAR")
    private Character includeCalendar;
    @Column(name = "START_DATE")
    private Date startDate;
    @Column(name = "END_DATE")
    private Date endDate;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "APPROVAL_HIERARCHY")
    private Integer approvalHierarchy;
    @Column(name = "CC_ID")
    private Integer ccId;
    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "REMARK")
    private String remark;
    @Column(name = "ENTITY_ID")
    private Integer entityId;
    @Column(name = "JSON")
    private String json;

    @ManyToOne
	@JoinColumn(name = "TOP_TYPE", referencedColumnName = "GLB_TYPE_VAL_ID", updatable = false, insertable = false)
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private R_GLOBAL_TYPE_VALUE topTypeValue;

	@Transient
//	@JsonIgnore
	public String getTopTypeName() {
		if (topTypeValue != null)
			return getTopTypeValue().getName();
		else
			return null;
	}
}

package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "M_APPROVAL_HIERARCHY_DTL")
public class M_APPROVAL_HIERARCHY_DTL extends BaseEntities implements Serializable {
    @Id
    @Column(name = "APPHIER_DTL_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_APPROVAL_HIERARCHY_DTL_SEQ")
    @SequenceGenerator(sequenceName = "M_APPROVAL_HIERARCHY_DTL_SEQ", allocationSize = 1, name = "M_APPROVAL_HIERARCHY_DTL_SEQ")
    private Integer appHierDtlId;

    @Column(name = "APPHIER_ID")
    private Integer appHierId;

    @Column(name="APPROVAL_LEVEL")
    private Integer approvalLevel;

    @Column(name="POSITION_ID")
    private Integer positionId;

    @Column(name = "IS_FINAL")
    @Convert(converter = BooleanToYNStringConverter.class)
    private Boolean isFinal;

    @Column(name = "IS_DELETED")
    @Convert(converter = BooleanToYNStringConverter.class)
    private Boolean isDeleted;

    @Column(name = "IS_SUBMITTER")
    @Convert(converter = BooleanToYNStringConverter.class)
    private Boolean isSubmitter;
    
    @ManyToOne
   	@JoinColumn(name = "POSITION_ID", referencedColumnName = "POSITION_ID", updatable = false, insertable = false)
   	@JsonIgnore
   	@NotFound(action = NotFoundAction.IGNORE)
   	private M_POSITION mPosition;
       
       @Transient
      	public String getPositionName() {
       	if(mPosition == null)
       		return null;
       	else
       		return getMPosition().getName();
      		
      	}

    public M_APPROVAL_HIERARCHY_DTL(Integer appHierId, Integer approvalLevel, Integer positionId, Boolean isFinal,
                                    Boolean isDeleted, Boolean isSubmitter) {
        this.appHierId = appHierId;
        this.approvalLevel = approvalLevel;
        this.positionId = positionId;
        this.isFinal = isFinal;
        this.isDeleted = isDeleted;
        this.isSubmitter = isSubmitter;
    }

    public M_APPROVAL_HIERARCHY_DTL() {
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + appHierDtlId;
        }
    }
}

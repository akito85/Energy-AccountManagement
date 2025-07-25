package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "T_APPROVAL_DTL")
public class T_APPROVAL_DTL extends BaseEntities implements Serializable {
    @Id
    @Column(name="T_APP_DTL_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "T_APPROVAL_DTL_SEQ")
    @SequenceGenerator(sequenceName = "T_APPROVAL_DTL_SEQ", allocationSize = 1, name = "T_APPROVAL_DTL_SEQ")
    private Integer tAppDtlId;

    @Column(name="T_APP_ID")
    private Integer tAppId;

    @Column
    private Integer approvalLevel;

    @Column
    private Integer positionId;

    @Column
    private String userId;

    @Column
    private String description;

    @Column(name="IS_FINAL")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isFinal;

    @Column(name="IS_SUBMITTER")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isSubmitter;

    public T_APPROVAL_DTL() {
        super();
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName() + "#" + tAppDtlId;
        }
    }

    public T_APPROVAL_DTL(Integer tAppId, Integer approvalLevel, Integer positionId, Boolean isFinal, Boolean isSubmitter, String status) {
        super();
        this.setStatus(status);
        this.tAppId = tAppId;
        this.approvalLevel = approvalLevel;
        this.positionId = positionId;
        this.isFinal = isFinal;
        this.isSubmitter = isSubmitter;
    }


}

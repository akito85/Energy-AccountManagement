package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "R_GLOBAL_PROPERTIES_DTL")
public class R_GLOBAL_PROPERTIES_DTL implements Serializable {
    @Id
    @Column(name = "GLOBAL_PROPS_DTL_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_GLOBAL_PROPERTIES_DTL_SEQ")
    @SequenceGenerator(sequenceName = "R_GLOBAL_PROPERTIES_DTL_SEQ", allocationSize = 1, name = "R_GLOBAL_PROPERTIES_DTL_SEQ")
    private Integer gpDetailId;
    @Column(name = "GLOBAL_PROPS_ID", length = 250)
    private Integer gpId;
    @Column(name = "GPD_KEY")
    private String gpdKey;
    @Column(name = "GPD_VALUE")
    private String gpdVal;
    @Column(name = "IS_DELETED")
    @Convert(converter = BooleanToYNStringConverter.class)
    private Boolean isDeleted;
    @Column(name = "IS_ENCRYPT")
    @Convert(converter = BooleanToYNStringConverter.class)
    private Boolean isEncrypt;

    @Column(name = "DATA_TYPE")
    private String dataType;

    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "UPDATED_BY")
    private String updatedBy;
    @Column(name = "CREATED_DATE")
    private Date createdDate;
    @Column(name = "UPDATED_DATE")
    private Date updatedDate;
    @Column(name = "STATUS")
    private String status;

    public R_GLOBAL_PROPERTIES_DTL(Integer gpId, String keyName, String value,
                                   String dataType, String status, Boolean encrypt) {
        this.gpId = gpId;
        this.gpdKey = keyName;
        this.gpdVal = value;
        this.dataType = dataType;
        this.status = status;
        this.isEncrypt = encrypt;
    }

    public R_GLOBAL_PROPERTIES_DTL (){

    }
}

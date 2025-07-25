package com.dbs.database.crm.entities.usermanagement.view;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "VW_R_GLOBAL_PROPERTIES_DTL")
public class VW_R_GLOBAL_PROPERTIES_DTL {
    @Id
    @Column(name = "RGPDID")
    private  Integer rgpdid;
    @Column(name = "GLOBAL_PROPS_DTL_ID")
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
//    @Column(name = "DATA_TYPE_NAME")
//    private String dataTypeName;
    @Column(name = "DATA_TYPE")
    private Integer dataType;
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
}

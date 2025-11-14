package com.dbs.module.account.detail.serviceagreement.dto.helper;


import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.Convert;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class CalcRuleProductDTO implements Serializable {
    private String name;
    private Integer id;
    private BigDecimal value;
    private String uom;
    private String uomName;
    private Integer productVersionId;
    private Integer nameId;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
    private String description;
    private Integer calculationType;
    private Integer flag;

    @Convert(converter = BooleanToYNStringConverter.class)
    private boolean isDeleted;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName();
        }
    }
}

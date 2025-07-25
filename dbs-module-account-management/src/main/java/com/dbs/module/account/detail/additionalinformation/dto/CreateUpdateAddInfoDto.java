package com.dbs.module.account.detail.additionalinformation.dto;

import com.dbs.common.base.entities.DefaultBaseEntities;
import lombok.Data;

@Data
public class CreateUpdateAddInfoDto extends DefaultBaseEntities {
    private Integer id;
    private Integer accountId;
    private Integer informationType;
    private String informationTypeVal;
    private String valueStr;
    private ValueIntDdl valueIntDdl;
    private Boolean isDeleted;
}

package com.dbs.common.base.entities;

import java.io.Serializable;
import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class CostCenterResult implements Serializable{
    private static final long serialVersionUID = 42L;
     
    private Integer ccId;
    private Integer parentId;

    public CostCenterResult(Integer ccId, Integer parentId) {
        this.ccId = ccId;
        this.parentId = parentId;
    }
    
    
}

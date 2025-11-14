package com.dbs.module.account.master.assets.dto;

import com.dbs.database.crm.entities.accountmanagement.VW_ASSET_ASSIGNMENT;
import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class DetailAssetResponseDto {
    private Integer assetId;
    private AssetInformationDto informationDto;
    private AssetAttributeDto attribute;
    private AssetStatusDto assetStatusDto;
    private HistoryLogInformationDto history;
    private List<VW_ASSET_ASSIGNMENT> historyAssegment;
    private List<AUDIT_TRAIL> activeInactiveLog;
}

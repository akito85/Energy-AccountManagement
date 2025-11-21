package com.dbs.module.account.master.assets.dto;

import com.dbs.module.account.main.dto.NameValueDdlResponseDto;
import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class AssetInformationDto {
    private String serialNumber;
    private String assetName;
    private Integer assetNameId;
    private String assetType;
    private Integer assetTypeId;
    private String serviceType;
    private Integer serviceTypeId;
    private NameValueDdlResponseDto productInformation;
    private String brand;
    private Integer brandId;
    private Integer year;
    private Boolean custodyTransfer;
}

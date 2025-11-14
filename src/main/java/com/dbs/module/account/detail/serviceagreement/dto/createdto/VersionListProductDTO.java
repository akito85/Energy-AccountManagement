package com.dbs.module.account.detail.serviceagreement.dto.createdto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class VersionListProductDTO implements Serializable {
    List<VersionListDTO> versionList;
    ProductDetailDTO2 product;
}

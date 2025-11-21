package com.dbs.module.account.master.location.dto;

import com.dbs.module.account.main.dto.NameValueDdlResponseDto;
import lombok.Data;

import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class DetailLocationAllDtoResponse {
    private NameValueDdlResponseDto country;
    private NameValueDdlResponseDto province;
    private NameValueDdlResponseDto city;
    private NameValueDdlResponseDto district;
    private NameValueDdlResponseDto subdistrict;
    private NameValueDdlResponseDto postalCode;

    private String status;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;

}

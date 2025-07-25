package com.dbs.module.account.detail.premise.dto;

import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class PremiseViewDTO {
    private Integer id;
    private String fullAddress;
    private String address;
    private String status;
    private List<ServicePointViewDTO> servicePoint;
}

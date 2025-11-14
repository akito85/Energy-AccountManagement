package com.dbs.module.account.master.address.dto;

import lombok.Data;

@Data
@SuppressWarnings("java:S1068")
public class AddressCoordinateInformationDto {
    private String source;
    private String longtitude;
    private String latitude;
    private String altitude;
}

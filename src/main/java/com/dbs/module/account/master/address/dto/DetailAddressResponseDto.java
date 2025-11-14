package com.dbs.module.account.master.address.dto;

import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class DetailAddressResponseDto {
    private AddressInformationResponseDto information;
    private AddressCoordinateInformationDto coordinateInformation;
    private HistoryLogInformationResponseDto historyLogInformation;

    private List<AUDIT_TRAIL> activeInactiveLog;

}

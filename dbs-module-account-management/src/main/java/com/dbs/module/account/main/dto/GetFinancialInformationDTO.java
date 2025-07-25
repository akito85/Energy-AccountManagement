package com.dbs.module.account.main.dto;

import com.dbs.module.account.detail.address.dto.AccountAddressCreateDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class GetFinancialInformationDTO implements Serializable {
     private AccountDTO accountInformation;
     private List<AccountAddressCreateDTO> accountAddress;
}
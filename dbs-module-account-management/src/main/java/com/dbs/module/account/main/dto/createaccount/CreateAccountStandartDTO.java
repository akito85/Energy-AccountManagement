package com.dbs.module.account.main.dto.createaccount;

import com.dbs.module.account.detail.distributionmedia.dto.DismeCreateRequestDTO;
import com.dbs.module.account.main.dto.AccountDTO;
import com.dbs.module.account.main.dto.MCustomerDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class CreateAccountStandartDTO implements Serializable {

    private MCustomerDTO customerInformation;
    private AccountDTO accountInformation;
    private CreateAddressAccountStandartDTO accountAddress;
    private CreateContactAccountStandartDTO accountContact;
    private List<DismeCreateRequestDTO> distributionMedia;
    private FinancialInformationDTO financialInformation;
    private Boolean registered;
    private Integer customerId;


}

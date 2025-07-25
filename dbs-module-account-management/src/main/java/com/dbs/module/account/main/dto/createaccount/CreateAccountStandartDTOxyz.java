package com.dbs.module.account.main.dto.createaccount;

import com.dbs.module.account.detail.address.dto.AccountAddressCreateDTO;
import com.dbs.module.account.detail.contact.dto.AccountContactCreateDTO;
import com.dbs.module.account.detail.distributionmedia.dto.DismeCreateRequestDTO;
import com.dbs.module.account.main.dto.AccountDTO;
import com.dbs.module.account.main.dto.MCustomerDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class CreateAccountStandartDTOxyz implements Serializable {

    private MCustomerDTO customerInformation;
    private AccountDTO accountInformation;
    private List<AccountAddressCreateDTO> accountAddress;
    private List<AccountContactCreateDTO> accountContact;
    private List<DismeCreateRequestDTO> distributionMedia;
    private FinancialInformationDTO financialInformation;
    private Boolean registered;
    private Integer customerId;


}

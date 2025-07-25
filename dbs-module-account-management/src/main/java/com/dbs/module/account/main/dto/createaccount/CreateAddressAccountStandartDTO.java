package com.dbs.module.account.main.dto.createaccount;

import com.dbs.module.account.detail.address.dto.AccountAddressCreateDTO;
import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class CreateAddressAccountStandartDTO implements Serializable {
    
    private AccountAddressCreateDTO address1;
    private AccountAddressCreateDTO address2;
    private AccountAddressCreateDTO address3;
    private AccountAddressCreateDTO address4;
    
}

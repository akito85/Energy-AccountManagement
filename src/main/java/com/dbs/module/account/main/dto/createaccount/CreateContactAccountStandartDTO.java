package com.dbs.module.account.main.dto.createaccount;

import com.dbs.module.account.detail.contact.dto.AccountContactCreateDTO;
import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("java:S1068")
public class CreateContactAccountStandartDTO implements Serializable {
    private AccountContactCreateDTO contact1;
    private AccountContactCreateDTO contact2;
    private AccountContactCreateDTO contact3;
    private AccountContactCreateDTO contact4;
}

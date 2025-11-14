package com.dbs.module.account.main.dto;

import com.dbs.database.crm.entities.accountmanagement.VW_ACCOUNT;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_ACCOUNT_INFORMATION;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@SuppressWarnings("java:S1068")
public class CustomerInformationDTO implements Serializable {
    private Boolean registered;
    private VwCustomerDetailDTO customerInformation;
    private DetailDTO customerManagement;
    private DetailDTO cc;
    private DetailDTO sor;
    private DetailDTO accountGroup;
    List<VW_ACCOUNT_INFORMATION> accountList;
}

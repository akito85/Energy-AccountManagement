package com.dbs.module.account.detail.address.controller;

import com.dbs.database.crm.entities.accountmanagement.VW_CHOOSE_CONTACT;
import com.dbs.module.account.detail.address.dto.AccountAddressCreateDTO;
import com.dbs.module.account.detail.address.dto.AccountAddressInactiveDTO;
import com.dbs.module.account.detail.address.dto.AccountAddressUpdateDTO;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNT_ADDRESS;
import com.dbs.database.crm.entities.accountmanagement.VW_CHOOSE_ADDRESS;
import com.dbs.database.crm.entities.accountmanagement.VW_CUSTOMER_ADDDRESS;
import com.dbs.module.account.detail.address.service.AccountAddressService;
import com.dbs.module.account.master.address.service.MasterAddressService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/dbs/api/account/address")
@Api(tags = "Account_Address")
public class AccountAddressController {
    
    private final AccountAddressService accountAddressService;

    @Autowired
    private MasterAddressService masterAddressService;

    public AccountAddressController(AccountAddressService accountAddressService) {
        this.accountAddressService = accountAddressService;
    }

    @GetMapping
    public List<M_ACCOUNT_ADDRESS> getAllAccountAddress() {
        return accountAddressService.getAllAccountAddress();
    }

    @GetMapping("/view/{accountId}")
    public ResponseEntity<?> getAccountAddressByAccountId(@PathVariable Integer accountId, @Valid MaterialTablePagingRequest pagingdata, PagedResourcesAssembler<VW_CUSTOMER_ADDDRESS> assembler) {
        return accountAddressService.getAccountAddressByAccountId(accountId, pagingdata, assembler);
    }

//    @GetMapping("view/choose-address/{accountId}")
//    public ResponseEntity<?> getChooseAccountAddress(@PathVariable Integer accountId, @Valid MaterialTablePagingRequest pagingdata, PagedResourcesAssembler<VW_CHOOSE_ADDRESS> assembler) {
//        return accountAddressService.getChooseAddress(accountId, pagingdata, assembler);
//    }

    @GetMapping("/view/choose-address/{customerId}")
    public ResponseEntity<?> getChooseAccountAddress(@PathVariable Integer customerId, @Valid MaterialTablePagingRequest pagingdata, PagedResourcesAssembler<VW_CHOOSE_ADDRESS> assembler) {
        return masterAddressService.getChooseAddressCustom("customer", customerId, pagingdata, assembler);
    }

    @GetMapping("view/detail/{accountAddressId}")
    public ResponseEntity<?> getDetailAccountAddress(@PathVariable Integer accountAddressId) {
        return accountAddressService.getDetailAddressByAddressId(accountAddressId);
    }
    
    @GetMapping("view/detailChoose/{addressId}")
    public ResponseEntity<?> getDetailChoose(@PathVariable Integer addressId) {
        return accountAddressService.getDetailChoose(addressId);
    }

    @PostMapping
    public ResponseEntity<?> createAccountAddress(@RequestBody AccountAddressCreateDTO accountAddress) {
        return accountAddressService.createAccountAddress(accountAddress);
    }

    @PostMapping("/validate-create")
    public ResponseEntity<?> validateCreateAccountAddress(@RequestBody AccountAddressCreateDTO request) {
        return accountAddressService.validateCreateAccountAddress(Boolean.TRUE, request);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseObject> updateAccountAddress(@RequestBody AccountAddressUpdateDTO request) {
        return accountAddressService.updateAccountAddress(request);
    }

    @PostMapping("/validate-update")
    public ResponseEntity<?> validateUpdateAccountAddress(@RequestBody AccountAddressUpdateDTO request) {
        return accountAddressService.validateUpdateAccountAddress(Boolean.TRUE, request);
    }

    @PutMapping("/active/inactive")
    public ResponseEntity<ResponseObject> updateStatusActiveInactive(@RequestBody AccountAddressInactiveDTO accountAddressInactiveDTO) {
        return accountAddressService.updateStatusActiveInactive(accountAddressInactiveDTO);
    }

    @GetMapping("list/type")
    public ResponseEntity<ResponseObject> getListHomeType() {
        return accountAddressService.getTypeHomeList();
    }

    @GetMapping("list/businessPurpose")
    public ResponseEntity<ResponseObject> getListBusinessPurpose() {
        return accountAddressService.getBusinessPurposeList();
    }
}

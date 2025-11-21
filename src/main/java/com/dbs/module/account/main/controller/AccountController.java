package com.dbs.module.account.main.controller;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNT;
import com.dbs.database.crm.entities.accountmanagement.VW_CHOOSE_ADDRESS;
import com.dbs.database.crm.entities.accountmanagement.VW_CHOOSE_CONTACT;
import com.dbs.module.account.main.dto.AccountDTO;
import com.dbs.module.account.main.services.AccountService;
import com.dbs.module.account.master.address.service.MasterAddressService;
import com.dbs.module.account.master.contact.service.ContactService;
import io.swagger.annotations.Api;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/dbs/api/account")
@Api(tags = "Account")
public class AccountController {

    @Autowired
    public AccountService accountService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private MasterAddressService masterAddressService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<M_ACCOUNT> getAllAccountAddress() {
        return accountService.getAllAccount();
    }


    //NU
    @GetMapping("/{id}")
    public ResponseEntity<M_ACCOUNT> getAccountById(@PathVariable Integer id) {
        return accountService.getAccountById(id);
    }

    //NU
    @PostMapping
    public ResponseEntity<?> createAccountAddress(@RequestBody M_ACCOUNT account) {
        return accountService.createAccount(account);
    }

    @PutMapping("/updatePaymentChannel")
    public ResponseEntity<?> updatePaymentChannel(@RequestBody AccountDTO requestDTO) {
        return accountService.updatePaymentChannel(requestDTO);
    }
    @GetMapping("/getPaymentChannel/{accountId}")
    public ResponseEntity<?> getPaymentChannel(@PathVariable Integer accountId) {
        return accountService.getPaymentChannel(accountId);
    }
    @GetMapping("/getPaymentChannelType")
    public ResponseEntity<?> getPaymentChannelType() {
        return accountService.getPaymentChannelType();
    }
    @GetMapping("/budget-year")
    public ResponseEntity<?> getBudgetYears() {
        return accountService.getBudgetYears();
    }
    @GetMapping("/account-budget")
    public ResponseEntity<?> listAccountBudgets() {
        return accountService.getAccountBudgets();
    }
    @GetMapping("/account-teritory")
    public ResponseEntity<?> listAccountTerritories() {
        return accountService.getAccountTeritories();
    }
    @GetMapping("/industrial-sector")
    public ResponseEntity<?> listIndustrialSector() {
        return accountService.getIndustrialSectors();
    }
    @GetMapping("/account-category")
    public ResponseEntity<?> listAccountCategories() {
        return accountService.getAccountCategories();
    }
    @GetMapping("/martial-status")
    public ResponseEntity<?> listMartialStatus() {
        return accountService.getMartialStatus();
    }
    @GetMapping("/sex")
    public ResponseEntity<?> listSex() {
        return accountService.getSex();
    }
    @GetMapping("/account-priority")
    public ResponseEntity<?> listAccountPriorities() {
        return accountService.getAccountPriorities();
    }
    @GetMapping("/account-group")
    public ResponseEntity<?> listAccountGroups() {
        return accountService.getAccountGroups();
    }
    @GetMapping("/account-type")
    public ResponseEntity<?> listAccountTypes() {
        return accountService.getAccountTypes();
    }
    @GetMapping("/account-segment")
    public ResponseEntity<?> listAccountSegments() {
        return accountService.getAccountSegments();
    }
    @GetMapping("/account-group-type/{idSegment}")
    public ResponseEntity<?> listAccountGroupType(@PathVariable Integer idSegment) {
        return accountService.getAccountGroupType(idSegment);
    }
    @GetMapping("/meter-reading-code")
    public ResponseEntity<?> listMeterReadingCodes(HttpServletRequest request) {
        return accountService.getMeterReadingCodes(request);
    }
    @GetMapping("/classification-type")
    public ResponseEntity<?> listClassificationTypes() {
        return accountService.getClassificationTypes();
    }
    @GetMapping("/user-position-name")
    public ResponseEntity<?> getPositionName(HttpServletRequest request){
        return accountService.getPositionName(request);
    }
    @GetMapping("/list-address")
    public ResponseEntity<?> getChooseAccountAddress(@RequestParam(required = false) Integer customerId, @Valid MaterialTablePagingRequest pagingdata, PagedResourcesAssembler<VW_CHOOSE_ADDRESS> assembler) {
        if(customerId == null) {
            return accountService.getListChooseAccountAddress("customer", null, pagingdata, assembler);
        }
        return accountService.getListChooseAccountAddress("customer", customerId, pagingdata, assembler);
    }
    @GetMapping("/home-type")
    public ResponseEntity<ResponseObject> getListHomeType() {
        return accountService.getTypeHomeList();
    }
    @GetMapping("/business-purpose")
    public ResponseEntity<ResponseObject> getListBusinessPurpose() {
        return accountService.getBusinessPurposeList();
    }
    @GetMapping("/list-contact")
    public ResponseEntity<?> chooseForAccount(@RequestParam(required = false) Integer customerId, @Valid MaterialTablePagingRequest pagingdata, PagedResourcesAssembler<VW_CHOOSE_CONTACT> assembler) {
        return contactService.getChooseContactCustom("customer",null, customerId, pagingdata, assembler);
    }
    @GetMapping("/country-code")
    public ResponseEntity<?> getCountryCode() {
        return accountService.getCountryCode();
    }

    @GetMapping("/country-zone-code/{countryId}")
    public ResponseEntity<?> getZoneCode(@PathVariable Integer countryId) {
        return accountService.getZoneCode(countryId);
    }
    @GetMapping("/job")
    public ResponseEntity<?> getJobCode() {
        return accountService.getJob();
    }
    @GetMapping("/position")
    public ResponseEntity<?> getPositionCode() {
        return accountService.getPosition();
    }

    @GetMapping("/input-type")
    public ResponseEntity<?> getInputTypeCode() {
        return accountService.getInputType();
    }

    @GetMapping("/contact-type")
    public ResponseEntity<?> getContactTypeCode() {
        return accountService.getContactType();
    }
    @GetMapping("/tax-identifier-type")
    public ResponseEntity<?> getIdentifierType() {
        return accountService.getTaxIdentificationType();
    }
    @GetMapping("/payment-channel-type")
    public ResponseEntity<?> getAccountPaymentChannelType() {
        return accountService.getAccountPaymentChannelType();
    }
}

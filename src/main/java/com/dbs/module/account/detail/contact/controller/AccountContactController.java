package com.dbs.module.account.detail.contact.controller;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.database.crm.entities.accountmanagement.VW_CHOOSE_CONTACT;
import com.dbs.database.crm.entities.accountmanagement.VW_CUSTOMER_CONTACT;
import com.dbs.module.account.detail.contact.dto.AccountContactDTO;
import com.dbs.module.account.detail.contact.dto.AccountContactUpdateRequestDTO;
import com.dbs.module.account.detail.contact.service.AccountContactService;
import com.dbs.module.account.master.contact.service.ContactService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/dbs/api/account/contact")
@Api(tags = "Account_Contact")
public class AccountContactController {
    
    private final AccountContactService services;

    @Autowired
    private ContactService contactService;

    public AccountContactController(AccountContactService services) {
        this.services = services;
    }
    
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AccountContactDTO request) {
        return services.create(request);
    }

    @PostMapping("/validate-create")
    public ResponseEntity<?> validateCreate(@RequestBody AccountContactDTO request) {
        return services.validateCreateAccountContact(Boolean.TRUE, request);
    }
    
    @GetMapping("/viewList/{accountId}")
    public ResponseEntity<?> list(@PathVariable Integer accountId, @Valid MaterialTablePagingRequest pagingdata, PagedResourcesAssembler<VW_CUSTOMER_CONTACT> assembler) {
        return services.getListAccountContact(accountId, pagingdata, assembler);
    }
    
    @GetMapping("/detail/{accountContactId}")
    public ResponseEntity<?> detail(@PathVariable Integer accountContactId) {
        return services.getDetail(accountContactId);
    }
    
    @GetMapping("/detailChoose/{contactId}")
    public ResponseEntity<?> detailChoose(@PathVariable Integer contactId) {
        return services.getDetailChoose(contactId);
    }
    
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody AccountContactUpdateRequestDTO request) {
        return services.update(request);
    }

    @PostMapping("/validate-update")
    public ResponseEntity<?> validateUpdate(@RequestBody AccountContactUpdateRequestDTO request) {
        return services.validateUpdate(Boolean.TRUE, request);
    }
    
    @PutMapping("/inactive")
    public ResponseEntity<?> activeInactive(@RequestBody AccountContactUpdateRequestDTO request) {
        return services.activeinactive(request);
    }
    
    //GLOBAL TYPE
    @GetMapping("/getCountry")
    public ResponseEntity<?> getCountryCode() {
        return services.getCountryCode();
    }
    
    @GetMapping("/getZone/{countryId}")
    public ResponseEntity<?> getZoneCode(@PathVariable Integer countryId) {
        return services.getZoneCode(countryId);
    }
    
    @GetMapping("/getJob")
    public ResponseEntity<?> getJobCode() {
        return services.getJob();
    }
    
    @GetMapping("/getPosition")
    public ResponseEntity<?> getPositionCode() {
        return services.getPosition();
    }
    
    @GetMapping("/getInputType")
    public ResponseEntity<?> getInputTypeCode() {
        return services.getInputType();
    }
    
    @GetMapping("/getContactType")
    public ResponseEntity<?> getContactTypeCode() {
        return services.getContactType();
    }
    
    @GetMapping("/address/{accountId}")
    public ResponseEntity<?> getAddress(@PathVariable Integer accountId) {
        return services.getListAddress(accountId);
    }
}


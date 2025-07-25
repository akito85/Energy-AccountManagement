package com.dbs.module.account.master.contact.controller;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.database.crm.entities.accountmanagement.VW_CHOOSE_CONTACT;
import com.dbs.database.crm.entities.accountmanagement.view.VW_CONTACT;
import com.dbs.module.account.master.contact.dto.ContactCreateRequestDTO;
import com.dbs.module.account.master.contact.dto.ContactUpdateRequestDTO;
import com.dbs.module.account.master.contact.service.ContactService;
import com.dbs.module.account.master.gassource.dto.ActiveInactiveDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/v1/dbs/api/contact")
@Api(tags = "Contact")
public class ContactController {
    
    @Autowired
    private ContactService services;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ContactCreateRequestDTO request) {
        return services.create(request);
    }

    @PostMapping("/validate-create")
    public ResponseEntity<?> validateCreate(@RequestBody ContactCreateRequestDTO request) {
        return services.validateCreate(Boolean.TRUE, request);
    }
    
    @GetMapping("/viewList")
    public ResponseEntity<?> list(@Valid MaterialTablePagingRequest pagingRequest,
                                  PagedResourcesAssembler<VW_CONTACT> assembler, HttpServletRequest httpServletRequest) {
        return services.pagingContact(pagingRequest, assembler);
    }

    @GetMapping("/download-filter")
    public ResponseEntity<InputStreamResource>downloadFilter(@Valid MaterialTablePagingRequest pagingdata) {
        return services.downloadFilter(pagingdata);
    }
    
    @GetMapping("/detail/{contactId}")
    public ResponseEntity<?> detail(@PathVariable Integer contactId) {
        return services.getDetail(contactId);
    }
    
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody ContactUpdateRequestDTO request) {
        return services.update(request);
    }

    @PostMapping("/validate-update")
    public ResponseEntity<?> validateUpdate(@RequestBody ContactUpdateRequestDTO request) {
        return services.validateUpdate(Boolean.TRUE, request);
    }
    
    @PutMapping("/active-inactive")
    public ResponseEntity<?> activeInactive(@RequestBody ActiveInactiveDTO request) {
        return services.activeinactive(request);
    }
//    @GetMapping("/list-contact-job")
//    public ResponseEntity<?> listContactJob(@RequestBody ActiveInactiveDTO request) {
//        return services.getFromGlobalType(Constant.CONTACT_JOB_NAME);
//    }
//    @GetMapping("/list-contact-position")
//    public ResponseEntity<?> listContactPosition(@RequestBody ActiveInactiveDTO request) {
//        return services.getFromGlobalType(Constant.CONTACT_POSITION_NAME);
//    }

    @GetMapping("/bank/choose/{bankCode}")
    public ResponseEntity<?> chooseForBank(@PathVariable String bankCode, @Valid MaterialTablePagingRequest pagingdata, PagedResourcesAssembler<VW_CHOOSE_CONTACT> assembler) {
        return services.getChooseContactCustom("bank",bankCode, null, pagingdata, assembler);
    }

    @GetMapping("/bank/choose")
    public ResponseEntity<?> chooseAll(@Valid MaterialTablePagingRequest pagingdata, PagedResourcesAssembler<VW_CHOOSE_CONTACT> assembler) {
        return services.getChooseContactCustom("bank",null, null, pagingdata, assembler);
    }

    @GetMapping("/account/choose/{customerId}")
    public ResponseEntity<?> chooseForAccount(@PathVariable Integer customerId, @Valid MaterialTablePagingRequest pagingdata, PagedResourcesAssembler<VW_CHOOSE_CONTACT> assembler) {
        return services.getChooseContactCustom("customer",null, customerId, pagingdata, assembler);
    }
}

package com.dbs.module.account.master.accountingrules.controller;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNTING_RULE;
import com.dbs.module.account.master.accountingrules.dto.AccountingRulesDTO;
import com.dbs.module.account.master.accountingrules.services.AccountingRulesServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/dbs/api/accounting-rules")
@Api(tags = "accountingRules")
public class AccountingRulesController {
    @Autowired
    private AccountingRulesServiceImpl accountingRulesService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody AccountingRulesDTO request) {
        return accountingRulesService.create(request);
    }

    @PostMapping("/validate-create")
    public ResponseEntity<?> validateCreate(@RequestBody AccountingRulesDTO request) {
        return accountingRulesService.validateCreate(Boolean.TRUE, request);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody AccountingRulesDTO request) {
        return accountingRulesService.update(request);
    }

    @PostMapping("/validate-update")
    public ResponseEntity<?> validateUpdate(@RequestBody AccountingRulesDTO request) {
        return accountingRulesService.validateUpdate(Boolean.TRUE, request);
    }

    @PutMapping("/active-inactive")
    public ResponseEntity<?> activeInactive(@RequestBody AccountingRulesDTO request) {
        return accountingRulesService.activeInactive(request);
    }

    @GetMapping("/paging")
    public ResponseEntity<?> paging(@Valid MaterialTablePagingRequest pagingData,
                                    PagedResourcesAssembler<M_ACCOUNTING_RULE> assembler) {
        return accountingRulesService.paging(pagingData, assembler);
    }

    @GetMapping("/download-filter")
    public ResponseEntity<InputStreamResource>downloadFilter(@Valid MaterialTablePagingRequest pagingdata) {
        return accountingRulesService.downloadFilter(pagingdata);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable Integer id) {
        return accountingRulesService.detail(id);
    }
}

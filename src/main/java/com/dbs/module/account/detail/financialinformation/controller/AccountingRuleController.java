package com.dbs.module.account.detail.financialinformation.controller;

import com.dbs.module.account.detail.financialinformation.service.AccountingRuleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/dbs/api/accounting-rule")
@Api(tags = "Accounting_Rule")
public class AccountingRuleController {
    @Autowired
    private AccountingRuleService services;
    
    @GetMapping("/get/{accountId}")
    public ResponseEntity<?> detail(@PathVariable Integer accountId) {
        return services.detail(accountId);
    }
}

package com.dbs.module.account.detail.financialinformation.controller;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.database.crm.entities.accountmanagement.VW_TAX_IDENTIFIER;
import com.dbs.module.account.detail.financialinformation.dto.TaxIdentifierCreateDTO;
import com.dbs.module.account.detail.financialinformation.service.TaxIdentifierService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/v1/dbs/api/tax-identifier")
@Api(tags = "Tax_Identifier")
public class TaxIdentifierController {
    
    @Autowired
    private TaxIdentifierService services;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TaxIdentifierCreateDTO request) {
        return services.create(request);
    }

    @PostMapping("/validate-create")
    public ResponseEntity<?> validateCreate(@RequestBody TaxIdentifierCreateDTO request) {
        return services.validateCreate(request);
    }
    
    @GetMapping("/listTax/{accountId}")
    public ResponseEntity<?> getList(@PathVariable Integer accountId, @Valid MaterialTablePagingRequest pagingdata,
                    PagedResourcesAssembler<VW_TAX_IDENTIFIER> assembler, HttpServletRequest request) {
            return services.getListTaxIdentifier(accountId, pagingdata, assembler);
    }
    
    @GetMapping("/getTaxIdentifierType")
    public ResponseEntity<?> getIdentifierType() {
        return services.getTaxIdentificationType();
    }
    
    @GetMapping("/getAddressTax/{accountId}")
    public ResponseEntity<?> getAddressTax(@PathVariable Integer accountId) {
        return services.getListAddressTax(accountId);
    }

}

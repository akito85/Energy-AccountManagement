package com.dbs.module.account.detail.financialinformation.controller;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.database.crm.entities.accountmanagement.M_WITHOLDING_TAX;
import com.dbs.module.account.detail.financialinformation.dto.InactiveDTO;
import com.dbs.module.account.detail.financialinformation.dto.WithholdingTaxCreateDTO;
import com.dbs.module.account.detail.financialinformation.service.WithholdingTaxService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/dbs/api/withholding-tax")
@Api(tags = "Withholding_Tax")
public class WithholdingTaxController {
    
    @Autowired
    private WithholdingTaxService services;

    
    @Autowired
    private WithholdingTaxService wtaxService;
    
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody WithholdingTaxCreateDTO request) {
        return services.createWithholdingTax(request);
    }
    
    @PutMapping("/inactive")
    public ResponseEntity<?> inactiveWitax(@RequestBody InactiveDTO requestDTO) {
        return services.inactiveWitax(requestDTO);
    }
    
    @GetMapping("/view/{accountId}")
    public ResponseEntity<?> getWitaxListDataPaging(@PathVariable Integer accountId, @Valid MaterialTablePagingRequest pagingdata,
			PagedResourcesAssembler<M_WITHOLDING_TAX> assembler) {
        return services.getWapuListData(accountId, pagingdata, assembler);
    }
    
    
}

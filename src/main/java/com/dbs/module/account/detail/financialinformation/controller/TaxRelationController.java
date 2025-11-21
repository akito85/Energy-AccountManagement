package com.dbs.module.account.detail.financialinformation.controller;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.database.crm.entities.accountmanagement.VW_CHOOSE_TAX_RELATION;
import com.dbs.database.crm.entities.accountmanagement.VW_TAX_RELATION;
import com.dbs.module.account.detail.financialinformation.dto.InactiveDTO;
import com.dbs.module.account.detail.financialinformation.dto.TaxRelationCreateDTO;
import com.dbs.module.account.detail.financialinformation.service.TaxRelationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/dbs/api/tax-relation")
@Api(tags="Tax_Relation")
public class TaxRelationController {
    
    @Autowired
    private TaxRelationService services;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TaxRelationCreateDTO request) {
        return services.create(request);
    }

    @PostMapping("/validate-create")
    public ResponseEntity<?> validateCreate(@RequestBody TaxRelationCreateDTO request) {
        return services.validateCreate(request);
    }
    
    @GetMapping("/listRelation/{accountId}")
    public ResponseEntity<?> getList(@PathVariable Integer accountId, @Valid MaterialTablePagingRequest pagingdata,
                    PagedResourcesAssembler<VW_TAX_RELATION> assembler) {
            return services.getList(accountId, pagingdata, assembler);
    }
    
    @GetMapping("/ChooseTax/{accountId}")
    public ResponseEntity<?> getChoose(@PathVariable Integer accountId, @Valid MaterialTablePagingRequest pagingdata,
                    PagedResourcesAssembler<VW_CHOOSE_TAX_RELATION> assembler) {
            return services.getChooseTax(accountId, pagingdata, assembler);
    }
    
    @GetMapping("/ChooseAllTax")
    public ResponseEntity<?> getChooseAll(@Valid MaterialTablePagingRequest pagingdata,
                    PagedResourcesAssembler<VW_CHOOSE_TAX_RELATION> assembler) {
            return services.getChooseAllTax(pagingdata, assembler);
    }
    
    @PutMapping("/inactive")
    public ResponseEntity<?> inactiveWitax(@RequestBody InactiveDTO requestDTO) {
        return services.inactive(requestDTO);
    }
}

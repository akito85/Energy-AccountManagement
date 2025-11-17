package com.dbs.module.account.detail.relationship.controller;

import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNT;
import com.dbs.database.crm.entities.accountmanagement.VW_CUS_INFO_CC;
import com.dbs.database.crm.entities.accountmanagement.view.NX_VW_RELATIONSHIP;
import com.dbs.module.account.detail.relationship.service.AccountRelationshipService;
import com.dbs.module.account.main.dto.accountinformation.SearchFilterDTO;

import io.swagger.annotations.Api;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import com.dbs.module.account.detail.relationship.dto.AccountRelationshipRequestDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/dbs/api/accounts")
@Api(tags = "Account_Relationship")
public class AccountRelationshipController {
    @Autowired
    private AccountRelationshipService service;

    @PostMapping("/{accountId}/relationships")
    public ResponseEntity<?> getAll(
        @RequestBody SearchFilterDTO dto, 
        @Valid MaterialTablePagingRequest pagingData,
        HttpServletRequest request,
        @PathVariable Integer accountId,
        PagedResourcesAssembler<NX_VW_RELATIONSHIP> assembler
    ) { 
        return service.getAll(dto, pagingData, request, assembler, accountId);
    }

    @GetMapping("/{accountId}/relationships/{id}")
    public ResponseEntity<?> getById(
        @PathVariable Integer accountId,
        @PathVariable Integer id
    ) {
        return service.getById(id);
    }

    @PostMapping("/{accountId}/relationships/create")
    public ResponseEntity<?> create(
        @PathVariable Integer accountId,
        @RequestBody @Valid AccountRelationshipRequestDTO relationship
    ) {
        return service.create(accountId, relationship);
    }

    @PutMapping("/{accountId}/relationships/{id}")
    public ResponseEntity<?> update(
        @PathVariable Integer accountId,
        @PathVariable Integer id,
        @RequestBody @Valid AccountRelationshipRequestDTO relationship
    ) {
        return service.update(id, relationship);
    }

    @DeleteMapping("/{accountId}/relationships/{id}")
    public ResponseEntity<?> delete(
        @PathVariable Integer accountId,
        @PathVariable Integer id
    ) {
        return service.delete(id);
    }
}

package com.dbs.module.account.detail.financialinformation.controller;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.database.crm.entities.ratingbillinginvoice.VW_BILLING_BUCKET;
import com.dbs.module.account.detail.financialinformation.service.BillingBucketService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/dbs/api/billing-bucket")
@Api(tags = "Billing_Bucket")
public class BillingBucketController {
    
    @Autowired
    private BillingBucketService services;

    
    @GetMapping("/list/{accountId}")
    public ResponseEntity<?> list(@PathVariable Integer accountId, @Valid MaterialTablePagingRequest pagingdata, PagedResourcesAssembler<VW_BILLING_BUCKET> assembler) {
        return services.getListBillingBucket(accountId, pagingdata, assembler);
    }
}


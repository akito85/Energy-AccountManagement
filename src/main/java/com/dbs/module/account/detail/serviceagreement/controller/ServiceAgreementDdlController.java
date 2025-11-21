package com.dbs.module.account.detail.serviceagreement.controller;

import com.dbs.module.account.detail.serviceagreement.dto.CriteriaDataDTO;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.database.crm.entities.accountmanagement.M_TOS;
import com.dbs.database.crm.entities.product.VW_PRODUCT;
import com.dbs.module.account.detail.serviceagreement.dto.helper.ListProductVersionDTO;
import com.dbs.module.account.detail.serviceagreement.service.SaDdlService;
import io.swagger.annotations.Api;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/v1/dbs/api/sa/ddl")
@Api(tags = "Service Agreement Dropdown List")
public class ServiceAgreementDdlController {

    private final SaDdlService saDdlService;

    public ServiceAgreementDdlController(SaDdlService saDdlService) {
        this.saDdlService = saDdlService;
    }

    @GetMapping("/serviceType")
    public ResponseEntity<ResponseObject> getServiceType() {
        return saDdlService.getServiceType();
    }

    @GetMapping("/saType/main/{serviceTypeId}")
    public ResponseEntity<ResponseObject> getSaTypeMain(@PathVariable Integer serviceTypeId) {
        return saDdlService.getSaTypeMain(serviceTypeId);
    }

    @GetMapping("/saType/addon/{serviceTypeId}")
    public ResponseEntity<ResponseObject> getSaTypeAddon(@PathVariable Integer serviceTypeId) {
        return saDdlService.getSaTypeAddon(serviceTypeId);
    }

    @GetMapping("/pjbgType")
    public ResponseEntity<ResponseObject> getPjbgType() {
        return saDdlService.getPjbgType();
    }


    @GetMapping("/getProduct/{accountId}/{productType}/{serviceType}")
    public ResponseEntity<ResponseObject> getProductWithPaging(@Valid MaterialTablePagingRequest pagingData,
                                                               PagedResourcesAssembler<VW_PRODUCT> assembler,
                                                               @PathVariable Integer accountId,
                                                               @PathVariable Integer productType,
                                                               @PathVariable Integer serviceType,
                                                               HttpServletRequest httpServletRequest) {
        return saDdlService.getChooseProductByCriteria(pagingData, assembler, accountId, productType, serviceType, httpServletRequest);
    }

    @GetMapping("/getPriceAdjustment/{accountId}/{priceCodeId}")
    public ResponseEntity<ResponseObject> getPricingAdjustmentByCriteria(@PathVariable Integer accountId,
                                                                         @PathVariable Integer priceCodeId,
                                                               HttpServletRequest httpServletRequest) {
        return saDdlService.getPriceAdjustmentByCriteria(accountId, priceCodeId, httpServletRequest);
    }

    @PostMapping("/listVersion")
    public ResponseEntity<?> getListVersion(@RequestBody ListProductVersionDTO request, HttpServletRequest httpServletRequest) {
        return saDdlService.listVersion(request, httpServletRequest);
    }

    @GetMapping("/product/detail/{id}/{accountId}")
    public ResponseEntity<?> getListDetailVersion(@PathVariable Integer id, @PathVariable Integer accountId, HttpServletRequest httpServletRequest) {
        return saDdlService.getProductVersionInformation(id, accountId, httpServletRequest);
    }

    @GetMapping("/billingCycle")
    public ResponseEntity<?> getListBillingCycle() {
        return saDdlService.listBillingCycle();
    }

    @GetMapping("/termOfPayment/{accountId}")
    public ResponseEntity<?> getTermOfPayment(@PathVariable Integer accountId) {
        return saDdlService.listTermOfPayment(accountId);
    }

    @GetMapping("/invoiceTemplate/{accountId}")
    public ResponseEntity<?> getListInvoiceTemplate(@PathVariable Integer accountId) {
        return saDdlService.listInvoiceTemplate(accountId);
    }
    
    @PostMapping("/getTaxImplication")
    public ResponseEntity<ResponseObject> getTaxImplication(@RequestBody CriteriaDataDTO request) {
        return saDdlService.getTaxImplication(request);
    }
    
    @PostMapping("/getLatecharge")
    public ResponseEntity<ResponseObject> getLatecharge(@RequestBody CriteriaDataDTO request) {
        return saDdlService.getLateCharge(request);
    }

    @GetMapping("/getListPriceCode/{accountId}")
    public ResponseEntity<?> getPriceCode(HttpServletRequest httpServletRequest,@PathVariable Integer accountId) {
        return saDdlService.getListPriceCode(httpServletRequest, accountId);
    }

    @GetMapping("/getListPriceRule/{accountId}")
    public ResponseEntity<?> getPriceRuleData(HttpServletRequest httpServletRequest, @PathVariable Integer accountId) {

        return saDdlService.getPricingRule(httpServletRequest,accountId);
    }


    @GetMapping("/getListPriceRule/detail/{pricingRuleId}")
    public ResponseEntity<?> getPriceRuleDetail(@PathVariable Integer pricingRuleId) {
        return saDdlService.getPricingRuleById(pricingRuleId);
    }

    @GetMapping("/chooseTos/{accountId}")
    public ResponseEntity<?> chooseTosWithPaging(@Valid MaterialTablePagingRequest pagingRequest,
                                                 PagedResourcesAssembler<M_TOS> assembler,
                                                 @PathVariable Integer accountId,
                                                 HttpServletRequest httpServletRequest) {
        return saDdlService.getTosList(pagingRequest, assembler, accountId, httpServletRequest);
    }

    @GetMapping("/list-category")
    public ResponseEntity<?> getListCategory() {
        return saDdlService.listCategory();
    }
}

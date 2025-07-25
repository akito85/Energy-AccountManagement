package com.dbs.module.account.master.taximplication.controller;

import com.dbs.module.account.master.taximplication.dto.ApprovalTaxImplicationDto;
import com.dbs.module.account.master.taximplication.dto.UpdateTaxImplicationRuleDto;
import com.dbs.module.account.master.taximplication.dto.CreateMasterTaxImplicationRequestDto;
import com.dbs.module.account.master.taximplication.dto.InactiveTaxImplicationDto;
import com.dbs.module.account.master.taximplication.dto.ApprovalTaxImplicationRuleDto;
import com.dbs.module.account.master.taximplication.dto.TaxImplicationRuleRequestDto;
import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.database.crm.entities.accountmanagement.M_AM_TAXIMPLICATION_RULE;
import com.dbs.database.crm.entities.accountmanagement.VW_AM_TAXIMPLICATION;
import com.dbs.database.crm.entities.usermanagement.M_ATTACHMENT;
import com.dbs.database.crm.repositories.usermanagement.MAttachmentRepo;
import com.dbs.module.account.master.taximplication.service.TaxImplicationService;
import com.dbs.module.account.master.latecharge.service.LateChargeServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.client.util.IOUtils;
import com.jlefebure.spring.boot.minio.MinioConfigurationProperties;
import com.unboundid.util.json.JSONException;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/dbs/api/tax-implication")
@Api(tags = "Tax_Implication")
public class TaxImplicationController {
    
    @Autowired
    private final TaxImplicationService service;

    @Autowired
    private LateChargeServiceImpl lateChargeService;

    @Autowired
    MAttachmentRepo mAttachmentRepo;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioConfigurationProperties configurationProperties;

    @Autowired
    private GlobalTypeValueService globalTypeValueService;

    public TaxImplicationController(TaxImplicationService service) {
        this.service = service;
    }
    
//    @GetMapping("/getTaxImplication/{accountId}")
//    public ResponseEntity<?> getTaxImplication(@PathVariable Integer accountId) {
//        return service.getTaxImplication(accountId);
//    }

    // FOR ACCOUNT DETAIL (NOT FOR MASTER TAX IMPLICATION)
    @GetMapping("/list/{accountId}")
    public ResponseEntity<?> list(@PathVariable Integer accountId, @Valid MaterialTablePagingRequest pagingdata, PagedResourcesAssembler<VW_AM_TAXIMPLICATION> assembler) {
        return service.getListTaxImplication(accountId, pagingdata, assembler);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable Integer id) {
        return service.getDetailTaxImplication(id);
    }

    @PostMapping("/create-taximplication")
    public ResponseEntity<?> createTaxImplication(@RequestBody CreateMasterTaxImplicationRequestDto requestDto, HttpServletRequest httpServletRequest){
        return service.createTaxImplication(requestDto, httpServletRequest);
    }

    @PostMapping("/validate-create-taximplication")
    public ResponseEntity<?> validateCreateTaxImplication(@RequestBody CreateMasterTaxImplicationRequestDto requestDto, HttpServletRequest httpServletRequest){
        return service.validateCreateOrUpdate(requestDto, Constant.CREATE);
    }


    @PostMapping("/check-start-date")
    public ResponseEntity<?> checkStartDate(@RequestBody TaxImplicationRuleRequestDto dto) {
        return service.checkStartDateRule(dto);
    }

    @PostMapping("/validate-condition")
    public ResponseEntity<?> validateConditionTaxImplicationRule(@RequestBody TaxImplicationRuleRequestDto requestDto){
        return service.checkDuplicateConditionTaxImpli(requestDto);
    }

    @PostMapping("/validate-create-taximplication-rule")
    public ResponseEntity<?> validateCreateTaxImplicationRule(@RequestBody TaxImplicationRuleRequestDto requestDto, HttpServletRequest httpServletRequest){
        return service.validateCreateTaxImpliRule(requestDto, httpServletRequest, Boolean.TRUE);
    }

    @PostMapping("/create-taximplication-rule")
    public ResponseEntity<?> createTaxImplicationRule(@RequestBody TaxImplicationRuleRequestDto requestDto, HttpServletRequest httpServletRequest){
        return service.createTaxImplicationRule(requestDto, httpServletRequest);
    }

    @DeleteMapping("/delete-draft/{taxImplicationRuleId}")
    public ResponseEntity<ResponseObject> deleteDraft(@PathVariable Integer taxImplicationRuleId) {
        return service.deleteDraftRule(taxImplicationRuleId);
    }

    @PostMapping(path = "/create-taximplication-rule-attachment/{taxImplicationId}")
    public ResponseEntity<?> createTaxImplicationRuleAttachment(@RequestPart List<MultipartFile> files, @RequestParam Integer category, @PathVariable Integer taxImplicationId)
            throws
            Exception {
        return service.attachmentRule(category, taxImplicationId, files);
    }

    @PutMapping(path = "/update-taximplication")
    public ResponseEntity<?> updateTaxImplication(@RequestBody CreateMasterTaxImplicationRequestDto updateTaxImplicationDto) {
        return service.updateTaxImplication(updateTaxImplicationDto);
    }

    @PostMapping("/validate-update-taximplication")
    public ResponseEntity<?> validateUpdateTaxImplication(@RequestBody CreateMasterTaxImplicationRequestDto updateTaxImplicationDto) {
        return service.validateCreateOrUpdate(updateTaxImplicationDto, Constant.UPDATE);
    }

    @PutMapping(path = "/update-taximplication-rule")
    public ResponseEntity<?> updateTaxImplicationRule(@RequestBody UpdateTaxImplicationRuleDto updateTaxImplicationRuleDto, HttpServletRequest httpServletRequest) {
        return service.updateTaxImplicationRule(updateTaxImplicationRuleDto, httpServletRequest);
    }

    @PutMapping(path = "/validate-update-taximplication-rule")
    public ResponseEntity<?> validateUpdateTaxImplicationRule(@RequestBody UpdateTaxImplicationRuleDto updateTaxImplicationRuleDto, HttpServletRequest httpServletRequest) {
        return service.validateUpdateTaxImpliRule(updateTaxImplicationRuleDto, httpServletRequest, Boolean.TRUE);
    }

    @PostMapping("/approve-taximplication-rule")
    public ResponseEntity<?> approveTaxImplication(@RequestBody ApprovalTaxImplicationRuleDto request, HttpServletRequest httpServletRequest)throws IllegalArgumentException, JSONException, JsonProcessingException {
        return service.approveTaxImplicationRule(request, httpServletRequest);
    }
    @PostMapping("/inactive-taximplication")
    public ResponseEntity<?> inactiveTaxImplication(@RequestBody InactiveTaxImplicationDto request, HttpServletRequest httpServletRequest){
        return service.inactiveTaxImplication(request);
    }
    @PostMapping("/inactive-taximplication-rule")
    public ResponseEntity<?> inactiveTaxImplicationRule(@RequestBody InactiveTaxImplicationDto request, HttpServletRequest httpServletRequest){
        return service.inactiveTaxImplicationRule(request, httpServletRequest);
    }
//    @PostMapping("/approve-inactive-taximplication")
//    public ResponseEntity<?> approveInactiveTaxImplication(@RequestBody ApprovalTaxImplicationDto request, HttpServletRequest httpServletRequest){
//        return service.approveInactiveTaxImplication(request, httpServletRequest);
//    }
    @PostMapping("/approve-inactive-taximplication-rule")
    public ResponseEntity<?> approveInactiveTaxImplicationRule(@RequestBody ApprovalTaxImplicationDto request, HttpServletRequest httpServletRequest){
        return service.approveInactiveTaxImplicationRule(request, httpServletRequest);
    }
    @GetMapping("/pagingTaxImplication")
    public ResponseEntity<?> pagingTaxImplication(@Valid MaterialTablePagingRequest pagingData,
                                                  PagedResourcesAssembler<VW_AM_TAXIMPLICATION> assembler) {
        return service.pagingTaxImplication(pagingData, assembler);
    }
    @GetMapping("/detailTaxImplication/{id}")
    public ResponseEntity<?> detailTaxImplication(@PathVariable("id") Integer id, @Valid MaterialTablePagingRequest pagingRequest) {
        return service.detailTaxImplication(pagingRequest, id);
    }
    @GetMapping("/view-pagingTaxImplication-rule/{taxImplicationId}")
    public ResponseEntity<?> pagingTaxImplicationRule(@Valid MaterialTablePagingRequest pagingData,
                                                  PagedResourcesAssembler<M_AM_TAXIMPLICATION_RULE> assembler,
                                                      @PathVariable Integer taxImplicationId) {
        return service.viewTaxImplicationRule(pagingData, assembler, taxImplicationId);
    }

    // same criteria data response with latecharge services
    @GetMapping("/list-category")
    public ResponseEntity<?> listCategory(){
        return lateChargeService.getFromGlobalType(Constant.TAX_IMPLICATION_CATEGORY_NAME);
    }
    @GetMapping("/list-service-type")
    public ResponseEntity<?> listServiceType(){
        return lateChargeService.getFromGlobalType(Constant.TAX_IMPLICATION_SERVICE_TYPE_NAME);
    }

    @GetMapping("/get-criteria")
    public ResponseEntity<ResponseObject> getCriteria(){
        return lateChargeService.listCriteria();
    }

    @GetMapping("/list-premise-country")
    public ResponseEntity<ResponseObject> premiseCountry() {
        return lateChargeService.listPremiseCountry();
    }
    @GetMapping("/list-premise-province/{locationParent}")
    public ResponseEntity<ResponseObject> premiseProvince(@PathVariable Integer locationParent) {
        return lateChargeService.listPremiseHaveParents(Constant.LOCATION_TYPE_PROVINCE, locationParent);
    }
    @GetMapping("/list-premise-city/{locationParent}")
    public ResponseEntity<ResponseObject> premiseCity(@PathVariable Integer locationParent) {
        return lateChargeService.listPremiseHaveParents(Constant.LOCATION_TYPE_CITY, locationParent);
    }
    @GetMapping("/list-premise-district/{locationParent}")
    public ResponseEntity<ResponseObject> premiseDistrict(@PathVariable Integer locationParent) {
        return lateChargeService.listPremiseHaveParents(Constant.LOCATION_TYPE_DISTRICT, locationParent);
    }
    @GetMapping("/list-premise-sub-district/{locationParent}")
    public ResponseEntity<ResponseObject> premiseSubDistrict(@PathVariable Integer locationParent) {
        return lateChargeService.listPremiseHaveParents(Constant.LOCATION_TYPE_SUB_DISTRICT, locationParent);
    }
    @GetMapping("/list-sor")
    public ResponseEntity<ResponseObject> listSor() {
        return lateChargeService.listSor();
    }
    @GetMapping("/list-cost-center")
    public ResponseEntity<ResponseObject> listCostCenter() {
        return lateChargeService.listCostCenter();
    }
    @GetMapping("/list-account-category")
    public ResponseEntity<ResponseObject> listAccountCategory() {
        return lateChargeService.getFromGlobalType(Constant.ACCOUNT_CATEGORY);
    }
    @GetMapping("/list-account-segment")
    public ResponseEntity<?> listAccountSegment(){
        return lateChargeService.getFromGlobalType(Constant.CUSTOMER_SEGMENT_NAME);
    }
    @GetMapping("/list-account-group-type/{accountSegmentId}")
    public ResponseEntity<ResponseObject> listAccountGroupType(@PathVariable Integer accountSegmentId) {
        return lateChargeService.listAccountGroupType(Constant.ACCOUNT_GROUP_TYPE_NAME, accountSegmentId);
    }
    @GetMapping("/list-account-number")
    public ResponseEntity<ResponseObject> listAccountNumber() {
        return lateChargeService.listAccountNumber();
    }
    @GetMapping("/list-classification-type")
    public ResponseEntity<ResponseObject> listClassificationType() {
        return lateChargeService.listClassificationType();
    }
    @GetMapping("/list-sa-type")
    public ResponseEntity<?> listSaType(){
        return lateChargeService.getFromGlobalType(Constant.SA_TYPE_MAIN_NAME);
    }
    @GetMapping("/list-account-type")
    public ResponseEntity<?> listAccountType(){
        return lateChargeService.getFromGlobalType(Constant.ACCOUNT_TYPE);
    }
    @GetMapping("/list-attachment-category")
    public ResponseEntity<ResponseObject> getListCategory(HttpServletRequest httpServletRequest) {
        return lateChargeService.listCategory(httpServletRequest);
    }

    @GetMapping("/implication-type")
    public ResponseEntity<?> listImplicationType(){
        return lateChargeService.getFromGlobalType(Constant.PPN_IMPLICATION_TYPE_NAME);
    }
    @GetMapping("/transaction-code")
    public ResponseEntity<?> listTransactionCode(){
        return lateChargeService.getFromGlobalType(Constant.PARTY_TYPE_NAME);
    }
    @GetMapping("/list-condition-name")
    public ResponseEntity<?> listVariableName(){
        return lateChargeService.getFromGlobalType(Constant.TIROC_NAME);
    }
    @GetMapping("/list-operator")
    public ResponseEntity<?> listConditionOperator(){
        return lateChargeService.getFromGlobalType(Constant.MATH_EQUATION_NAME);
    }

    @GetMapping("/detail-tax-rule/{taxRuleId}")
    public ResponseEntity<?> detailTaxImplicationRule(@PathVariable("taxRuleId") Integer taxRuleId, HttpServletRequest httpServletRequest) {
        return service.detailTaxImplicationRule(taxRuleId, httpServletRequest);
    }


    @GetMapping("/list-approval-header")
    public ResponseEntity<?> listApprovalHeader(HttpServletRequest httpServletRequest){
        return service.getAllApprovalHeaderList(httpServletRequest);
    }
    @GetMapping("/list-selected-approval-header/{appHierId}")
    public ResponseEntity<?> listFromSelectedApprovalHeader(@PathVariable Integer appHierId){
        return lateChargeService.getApprovalHeaderId(appHierId);
    }

    @GetMapping("/detail-tax-rule-draft/{taxImplicationRuleId}")
    public ResponseEntity<?> detailTaxImpliRuleDraft(@PathVariable Integer taxImplicationRuleId){
        return service.detailTaxImpliRuleDraft(taxImplicationRuleId);
    }

    @GetMapping("/download2/{fileId}")
    public void downloadFile2(@PathVariable("fileId") Integer fileId, HttpServletResponse response)
            throws  IOException, InvalidArgumentException, InvalidBucketNameException,
            InsufficientDataException, XmlPullParserException, ErrorResponseException, NoSuchAlgorithmException,
            NoResponseException, InvalidKeyException, InternalException, InvalidResponseException {
        Optional<M_ATTACHMENT> vwFileOpt = mAttachmentRepo.findById(fileId);
        if(vwFileOpt.isPresent()) {
            M_ATTACHMENT vwFile =vwFileOpt.get();


            InputStream inputStream = minioClient.getObject(this.configurationProperties.getBucket(),
                    "FILE" + vwFile.getPathFile()+ vwFile.getFileName());


            // Set the content type and attachment header.
            response.addHeader("Content-disposition", "attachment;filename=" + vwFile.getFileName());
            response.setContentType(URLConnection.guessContentTypeFromName(vwFile.getFileName()));

            // Copy the stream to the response's output stream.
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        }
    }

    @GetMapping("/download-filter")
    public ResponseEntity<InputStreamResource>downloadFilter(@Valid MaterialTablePagingRequest pagingdata) {
        return service.downloadFilter(pagingdata);
    }

    @GetMapping("/approvalhistory/{refId}")
    public ResponseEntity<ResponseObject> listApprovalHistory(@PathVariable Integer refId) {
        return service.getApprovalHistory(refId);
    }

    @GetMapping("/getAccountCategory")
    public ResponseEntity<ResponseObject>getAccountCategory() {
        return lateChargeService.getAccountCategory();
    }
    
    @GetMapping("/get-facture-codes")
    public ResponseEntity<ResponseObject> getFactureCodes(){
        return service.listFactureCodes();
    }

}

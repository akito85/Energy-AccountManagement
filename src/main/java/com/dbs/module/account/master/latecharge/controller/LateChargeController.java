package com.dbs.module.account.master.latecharge.controller;

import com.dbs.module.account.detail.sourcedistribution.dto.SourceDistributionCreateUpdateDTO;
import com.dbs.module.account.master.latecharge.dto.MLateChargeRuleUpdateDto;
import com.dbs.module.account.master.latecharge.dto.InactiveLateChargeDto;
import com.dbs.module.account.master.latecharge.dto.MAmLateChargeDto;
import com.dbs.module.account.master.latecharge.dto.MAmLateChargeRuleRequestDto;
import com.dbs.module.account.master.latecharge.dto.ApprovalLateChargeDto;
import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.database.crm.entities.accountmanagement.VW_LATE_CHARGE_RULE;
import com.dbs.database.crm.entities.accountmanagement.VW_MASTER_LATE_CHARGE;
import com.dbs.database.crm.entities.usermanagement.M_ATTACHMENT;
import com.dbs.database.crm.repositories.usermanagement.MAttachmentRepo;
import com.dbs.module.account.master.latecharge.service.LateChargeServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.client.util.IOUtils;
import com.jlefebure.spring.boot.minio.MinioConfigurationProperties;
import com.unboundid.util.json.JSONException;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
@RequestMapping("/v1/dbs/api/master/late-charge")
@Api(tags = "lateCharge")
public class LateChargeController {
    @Autowired
    private LateChargeServiceImpl lateChargeService;

    @Autowired
    MAttachmentRepo mAttachmentRepo;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioConfigurationProperties configurationProperties;

    @GetMapping("/view_master")
    public ResponseEntity<?> viewMaster(@Valid MaterialTablePagingRequest pagingRequest,
                                        PagedResourcesAssembler<VW_MASTER_LATE_CHARGE> assembler){
        return lateChargeService.viewMaster(pagingRequest, assembler);
    }
    @PostMapping("/create-master")
    public ResponseEntity<?> create(@RequestBody MAmLateChargeDto requestData){
        return lateChargeService.createMasterLateCharge(requestData);
    }
    
    @PostMapping("/validate-create-master")
    public ResponseEntity<?> validateCreate(@RequestBody MAmLateChargeDto requestData){
        return lateChargeService.validateCreateOrUpdate(requestData, Constant.CREATE);
    }


    @PostMapping("/check-start-date")
    public ResponseEntity<?> checkStartDate(@RequestBody MAmLateChargeRuleRequestDto dto) {
        return lateChargeService.checkStartDateRule(dto);
    }

    @PostMapping("/validate-create-rule")
    public ResponseEntity<?> validateCreateRule(@RequestBody MAmLateChargeRuleRequestDto requestData, HttpServletRequest httpServletRequest) {
        return lateChargeService.validateCreateLatechargeRule(requestData, httpServletRequest, Boolean.TRUE);
    }

    @PostMapping("/create-rule")
    public ResponseEntity<?> createRule(@RequestBody MAmLateChargeRuleRequestDto requestData, HttpServletRequest httpServletRequest){
        return lateChargeService.createLateChargeRule(requestData, httpServletRequest);
    }

    @PostMapping("/validate-condition")
    public ResponseEntity<?> validateConditionLatecharge(@RequestBody MAmLateChargeRuleRequestDto requestData) {
        return lateChargeService.checkDuplicateConditionLatecharge(requestData);
    }

    @DeleteMapping("/delete-draft/{latechargeRuleId}")
    public ResponseEntity<ResponseObject> deleteDraft(@PathVariable Integer latechargeRuleId) {
        return lateChargeService.deleteDraftRule(latechargeRuleId);
    }

    @PostMapping(path = "/create-attachment/{lateChargeRuleId}", consumes = {MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createAttachment(@RequestPart List<MultipartFile> files, @RequestParam Integer category, @PathVariable Integer lateChargeRuleId)
            throws
            Exception {
        return lateChargeService.lateChargeAttachment(category, files, lateChargeRuleId);
    }


    @GetMapping("/list-approval-header")
    public ResponseEntity<?> listApprovalHeader(HttpServletRequest httpServletRequest){
        return lateChargeService.getAllApprovalHeaderList(httpServletRequest);
    }
    @GetMapping("/list-selected-approval-header/{appHierId}")
    public ResponseEntity<?> listFromSelectedApprovalHeader(@PathVariable Integer appHierId){
        return lateChargeService.getApprovalHeaderId(appHierId);
    }
    @PutMapping("/update-header")
    public ResponseEntity<?> updateMaster(@RequestBody MAmLateChargeDto mAmLateChargeUpdateDto){
        return lateChargeService.updateLatechargeHeader(mAmLateChargeUpdateDto);
    }

    @PostMapping("/validate-update-header")
    public ResponseEntity<?> validateUpdateMaster(@RequestBody MAmLateChargeDto mAmLateChargeUpdateDto){
        return lateChargeService.validateCreateOrUpdate(mAmLateChargeUpdateDto, Constant.UPDATE);
    }

    @GetMapping("/list-account-type")
    public ResponseEntity<?> listAccountType(){
        return lateChargeService.getFromGlobalType(Constant.ACCOUNT_TYPE);
    }

    @GetMapping("/list-currency")
    public ResponseEntity<?> listCurrency(){
        return lateChargeService.getFromGlobalType(Constant.CURRENCY_NAME);
    }
//    @GetMapping("/list-region")
//    public ResponseEntity<?> listRegion(){
//        return lateChargeService.getFromGlobalType(Constant.REGION_NAME);
//    }

    @PutMapping("/validate-update-late-charge-rule")
    public ResponseEntity<?> validateUpdateLateChargeRule(@RequestBody MLateChargeRuleUpdateDto updateRequest, HttpServletRequest httpServletRequest){
        return lateChargeService.validateUpdateLatechargeRule(updateRequest, httpServletRequest, Boolean.TRUE);
    }

    @PutMapping("/update-late-charge-rule")
    public ResponseEntity<?> updateLateChargeRule(@RequestBody MLateChargeRuleUpdateDto updateRequest, HttpServletRequest httpServletRequest){
        return lateChargeService.updateLateChargeRuleHeader(updateRequest, httpServletRequest);
    }

    @DeleteMapping("/delete-attachment/{attachmentId}")
    public ResponseEntity<?> deleteAttachment(@PathVariable Integer attachmentId){
        return lateChargeService.deleteDraft(attachmentId);
    }
    @GetMapping("/detail-latecharge/{lateChargeId}")
    public ResponseEntity<?> detailLateCharge(@PathVariable Integer lateChargeId, @Valid MaterialTablePagingRequest pagingRequest){
        return lateChargeService.viewDetail(pagingRequest, lateChargeId);
    }
    @GetMapping("/view-latecharge-rule/{lateChargeId}")
    public ResponseEntity<?> viewLateChargeRule(@PathVariable Integer lateChargeId, @Valid MaterialTablePagingRequest pagingRequest, PagedResourcesAssembler<VW_LATE_CHARGE_RULE> assembler){
        return lateChargeService.viewLateChargeRule(pagingRequest, assembler, lateChargeId);
    }
    @GetMapping("/detail-latecharge-rule/{lateChargeRuleId}")
    public ResponseEntity<?> detailLateChargeRule(@PathVariable Integer lateChargeRuleId, HttpServletRequest request){
        return lateChargeService.detailLateChargeRule(lateChargeRuleId, request);
    }
    @GetMapping("/detail-latecharge-rule-draft/{lateChargeRuleId}")
    public ResponseEntity<?> detailLateChargeRuleDraft(@PathVariable Integer lateChargeRuleId){
        return lateChargeService.detailLateChargeRuleDraft(lateChargeRuleId);
    }
    @PostMapping("/inactive-latecharge")
    public ResponseEntity<?> inactiveLateCharge(@RequestBody InactiveLateChargeDto request){
        return lateChargeService.inactiveLateCharge(request);
    }
    @PostMapping("/inactive-latecharge-rule")
    public ResponseEntity<?> inactiveLateChargeRule(@RequestBody InactiveLateChargeDto request, HttpServletRequest httpServletRequest){
        return lateChargeService.inactiveLateChargeRule(request, httpServletRequest);
    }
    @PostMapping("/approve-latecharge-rule")
    public ResponseEntity<?> approveLatechargeRule(@RequestBody ApprovalLateChargeDto request, HttpServletRequest httpServletRequest)throws IllegalArgumentException, JSONException, JsonProcessingException {
            return lateChargeService.approveLateChargeRule(request, httpServletRequest);
    }
//    @PostMapping("/approve-inactive-latecharge")
//    public ResponseEntity<?> approveInactiveLatecharge(@RequestBody ApprovalLateChargeDto request, HttpServletRequest httpServletRequest){
//        return lateChargeService.approveInactiveLateCharge(request, httpServletRequest);
//    }
    @PostMapping("/approve-inactive-latecharge-rule")
    public ResponseEntity<?> approveInactiveLatechargeRule(@RequestBody ApprovalLateChargeDto request, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        return lateChargeService.approveInactiveLateChargeRule(request, httpServletRequest);
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
//    @GetMapping("/corporate-flag")
//    public ResponseEntity<ResponseObject> getCorporateFlag(){
//        return lateChargeService.booleanDdl();
//    }
//    @GetMapping("/wapu-flag")
//    public ResponseEntity<ResponseObject> getWapuFlag(){
//        return lateChargeService.booleanDdl();
//    }

    @GetMapping("/get-criteria")
    public ResponseEntity<ResponseObject> getCriteria(){
        return lateChargeService.listCriteria();
    }

    @GetMapping("/list-sa-type")
    public ResponseEntity<?> listSaType(){
        return lateChargeService.getFromGlobalType(Constant.SA_TYPE_MAIN_NAME);
    }

    @GetMapping("/list-variable-name")
    public ResponseEntity<?> listVariableName(){
        return lateChargeService.getFromGlobalType(Constant.LATE_CHARGE_COMPONENT_NAME);
    }

    @GetMapping("/list-formula-operation")
    public ResponseEntity<?> listFormulaOperation(){
        return lateChargeService.getFromGlobalType(Constant.MATH_OPERATOR_NAME);
    }

    @GetMapping("/list-condition-operator")
    public ResponseEntity<?> listConditionOperator(){
        return lateChargeService.getFromGlobalType(Constant.MATH_EQUATION_NAME);
    }

    @GetMapping("/list-data-type")
    public ResponseEntity<?> listDataType(){
        return lateChargeService.getFromGlobalType(Constant.DATA_TYPE_GLOBAL_PROPERTIES_NAME);
    }

    @GetMapping("/download-filter")
    public ResponseEntity<InputStreamResource>downloadFilter(@Valid MaterialTablePagingRequest pagingdata) {
        return lateChargeService.downloadFilter(pagingdata);
    }

    @GetMapping("/list-category")
    public ResponseEntity<ResponseObject> getListCategory(HttpServletRequest httpServletRequest) {
        return lateChargeService.listCategory(httpServletRequest);
    }

    @GetMapping("/approvalhistory/{refId}")
    public ResponseEntity<ResponseObject> listApprovalHistory(@PathVariable Integer refId) {
        return lateChargeService.getApprovalHistory(refId);
    }

    @GetMapping("/download2/{fileId}")
    @SneakyThrows
    public void downloadFile(@PathVariable("fileId") Integer fileId, HttpServletResponse response) {
        var attachmentOptional = mAttachmentRepo.findById(fileId);
        if(attachmentOptional.isPresent()) {
            var attachment = attachmentOptional.get();

            var inputStream = minioClient.getObject(this.configurationProperties.getBucket(),
                    Constant.FILE + attachment.getPathFile()+ attachment.getFileName());

            // Set the content type and attachment header.
            response.addHeader(HttpHeaders.CONTENT_DISPOSITION, Constant.HEADER_VALUE_DOWNLOAD + attachment.getFileName());
            response.setContentType(URLConnection.guessContentTypeFromName(attachment.getFileName()));

            // Copy the stream to the response's output stream.
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        }
    }

    @GetMapping("/getAccountCategory")
    public ResponseEntity<ResponseObject>getAccountCategory() {
        return lateChargeService.getAccountCategory();
    }

//    public void downloadFile2(@PathVariable("fileId") Integer fileId, HttpServletResponse response)
//            throws  IOException, InvalidArgumentException, InvalidBucketNameException,
//            InsufficientDataException, XmlPullParserException, ErrorResponseException, NoSuchAlgorithmException,
//            NoResponseException, InvalidKeyException, InternalException, InvalidResponseException {
//        Optional<M_ATTACHMENT> vwFileOpt = mAttachmentRepo.findById(fileId);
//        if(vwFileOpt.isPresent()) {
//            M_ATTACHMENT vwFile =vwFileOpt.get();
//
//
//            InputStream inputStream = minioClient.getObject(this.configurationProperties.getBucket(),
//                    "FILE" + vwFile.getPathFile()+ vwFile.getFileName());
//
//
//            // Set the content type and attachment header.
//            response.addHeader("Content-disposition", "attachment;filename=" + vwFile.getFileName());
//            response.setContentType(URLConnection.guessContentTypeFromName(vwFile.getFileName()));
//
//            // Copy the stream to the response's output stream.
//            IOUtils.copy(inputStream, response.getOutputStream());
//            response.flushBuffer();
//        }
//    }

}
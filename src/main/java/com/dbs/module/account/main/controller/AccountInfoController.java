package com.dbs.module.account.main.controller;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.database.crm.entities.accountmanagement.VW_CUSTOMER_ADDDRESS;
import com.dbs.database.crm.entities.accountmanagement.VW_CUSTOMER_CONTACT;
import com.dbs.database.crm.entities.accountmanagement.VW_CUS_INFO_CC;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_ACCOUNT_INFORMATION;
import com.dbs.database.crm.entities.usermanagement.M_ATTACHMENT;
import com.dbs.database.crm.repositories.usermanagement.MAttachmentRepo;
import com.dbs.module.account.main.dto.UpdateAccountStandartDto;
import com.dbs.module.account.main.dto.accountinformation.ActiveInactiveDTO;
import com.dbs.module.account.main.dto.accountinformation.SearchFilterDTO;
import com.dbs.module.account.main.dto.accountinformation.UpdateCustomerDTO;
import com.dbs.module.account.main.services.AccountInfoServiceImpl;
import com.google.api.client.util.IOUtils;
import com.jlefebure.spring.boot.minio.MinioConfigurationProperties;
import com.jlefebure.spring.boot.minio.MinioException;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RestController
@RequestMapping("/v1/dbs/api/account-info")
@Api(tags = "accountingInfo")
public class AccountInfoController {
    @Autowired
    private MAttachmentRepo mAttachmentRepo;
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinioConfigurationProperties configurationProperties;
    @Autowired
    private AccountInfoServiceImpl accountInfoService;

    private static final Logger logger = LoggerFactory.getLogger(AccountInfoServiceImpl.class);

    @GetMapping("/paging-account-standart")
    public ResponseEntity<?> pagingAccountStandart(@Valid MaterialTablePagingRequest pagingData,
                                                   HttpServletRequest request,
                                                   PagedResourcesAssembler<VW_ACCOUNT_INFORMATION> assembler) {
        return accountInfoService.pagingAccount(pagingData, assembler, Constant.ACC_STANDARD, request);
    }

    @GetMapping("/paging-account-one-time")
    public ResponseEntity<?> pagingAccountOneTime(@Valid MaterialTablePagingRequest pagingData,
                                                  HttpServletRequest request,
                                                  PagedResourcesAssembler<VW_ACCOUNT_INFORMATION> assembler) {
        return accountInfoService.pagingAccount(pagingData, assembler, Constant.ACC_ONETIME, request);
    }

    @GetMapping("/download-filter-standart")
    public ResponseEntity<InputStreamResource> downloadStandart(@Valid MaterialTablePagingRequest pagingData, HttpServletRequest httpServletRequest) throws IOException {
        return accountInfoService.downloadFilterAccount(pagingData, Constant.ACC_STANDARD, httpServletRequest);
    }

    @GetMapping("/download-filter-one-time")
    public ResponseEntity<InputStreamResource> downloadOneTime(@Valid MaterialTablePagingRequest pagingData, HttpServletRequest httpServletRequest) throws IOException {
        return accountInfoService.downloadFilterAccount(pagingData, Constant.ACC_ONETIME, httpServletRequest);
    }

    @PostMapping("/paging-customer")
    public ResponseEntity<?> pagingCustomer(@RequestBody SearchFilterDTO dto, @Valid MaterialTablePagingRequest pagingData,
                                            HttpServletRequest request, PagedResourcesAssembler<VW_CUS_INFO_CC> assemblerDto) {
        return accountInfoService.pagingCustomer(dto,pagingData, request, assemblerDto);
    }

    @GetMapping("/detail-customer/{customerId}")
    public ResponseEntity<?> detailCustomer(@PathVariable Integer customerId, HttpServletRequest request) {
        return accountInfoService.detailCustomer(customerId, request);
    }

    @GetMapping("/detail-standart/{customerId}/{accountId}")
    public ResponseEntity<?> detailAccountStandart(@PathVariable Integer customerId, @PathVariable Integer accountId, HttpServletRequest request) {
        return accountInfoService.detailAccount(customerId, accountId, Constant.ACC_STANDARD, request);
    }

    @GetMapping("/detail-onetime/{customerId}/{accountId}")
    public ResponseEntity<?> detailAccountOneTime(@PathVariable Integer customerId, @PathVariable Integer accountId, HttpServletRequest request) {
        return accountInfoService.detailAccount(customerId, accountId, Constant.ACC_ONETIME, request);
    }

    @PutMapping("/customer/active-inactive")
    public ResponseEntity<?> activeInactiveCustomer(@RequestBody ActiveInactiveDTO request) {
        return accountInfoService.activeInactiveCustomer(request);
    }

    @PostMapping("/download-customer")
    public ResponseEntity<InputStreamResource> postDownloadCustomer(@RequestBody SearchFilterDTO dto,
                                                                    @Valid MaterialTablePagingRequest pagingData,
                                                                    HttpServletRequest request,
                                                                    PagedResourcesAssembler<VW_CUS_INFO_CC> assemblerDto) throws IOException {
        return accountInfoService.downloadCustomer(dto, pagingData, request, assemblerDto);
    }

    @GetMapping("/paging-customer-account/{customerId}")
    public ResponseEntity<?> pagingCustomerAccount(@Valid MaterialTablePagingRequest pagingData,
                                           PagedResourcesAssembler<VW_ACCOUNT_INFORMATION> assembler,
                                           @PathVariable Integer customerId, HttpServletRequest request) {
        return accountInfoService.pagingCustomerAccount(pagingData, assembler, customerId, request);
    }

    @GetMapping("/paging-customer-address/{customerId}")
    public ResponseEntity<?> pagingCustomerAddress(@Valid MaterialTablePagingRequest pagingData,
                                           PagedResourcesAssembler<VW_CUSTOMER_ADDDRESS> assembler,
                                           @PathVariable Integer customerId, HttpServletRequest request) {
        return accountInfoService.pagingCustomerAddress(pagingData, assembler, customerId, request);
    }

    @GetMapping("/paging-customer-contact/{customerId}")
    public ResponseEntity<?> pagingCustomerContact(@Valid MaterialTablePagingRequest pagingData,
                                                   PagedResourcesAssembler<VW_CUSTOMER_CONTACT> assembler,
                                                   @PathVariable Integer customerId, HttpServletRequest request) {
        return accountInfoService.pagingCustomerContact(pagingData, assembler, customerId, request);
    }

    @GetMapping("/paging-customer-attachment/{customerId}")
    public ResponseEntity<?> pagingCustomerAttachment(@PathVariable Integer customerId) {
        return accountInfoService.pagingCustomerAttachment(customerId);
    }

    @PutMapping("/update-customer")
    public ResponseEntity<?> updateCustomer(@RequestBody UpdateCustomerDTO request) {
        return accountInfoService.updateCustomer(request);
    }

    @PostMapping("/validate-update-customer")
    public ResponseEntity<?> validateUpdateCustomer(@RequestBody UpdateCustomerDTO request) {
        return accountInfoService.validateUpdateCustomer(request);
    }

    @GetMapping("/global-type/{id}")
    public ResponseEntity<?> getGlobalType(@PathVariable Integer id) {
        return accountInfoService.getDataGlobalType(id);
    }

    @PostMapping("/uploadAttachment/{customerId}")
    public ResponseEntity<?> uploadAttachment(@RequestParam Integer category, @RequestPart List<MultipartFile> files,
                                              @PathVariable Integer id) {
        return accountInfoService.uploadAttachment(category, files, id);
    }

    @GetMapping("/download-attachment/{fileId}")
    public void downloadAttachment(@PathVariable("fileId") Integer fileId, HttpServletResponse response)throws MinioException, IOException, InvalidArgumentException, InvalidBucketNameException,
            InsufficientDataException, XmlPullParserException, ErrorResponseException, NoSuchAlgorithmException,
            NoResponseException, InvalidKeyException, InternalException, InvalidResponseException{
        var vwFileOpt = mAttachmentRepo.findById(fileId);
        logger.info("masuk we->"+vwFileOpt);
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

    @PostMapping("/update-account")
    public ResponseEntity<?> updateAccount(@RequestBody UpdateAccountStandartDto requestBody){
        return accountInfoService.updateAccount(requestBody);
    }

    @PostMapping("/validate-update-account")
    public ResponseEntity<?> validateUpdateAccount(@RequestBody UpdateAccountStandartDto requestBody){
        return accountInfoService.validateUpdateAccount(requestBody);
    }

    @GetMapping("/list-search-condition")
    public ResponseEntity<?> listAdvanceSearchCondition(){
        return accountInfoService.listAdvanceSearchCondition();
    }

    @GetMapping("/list-search-operator")
    public ResponseEntity<?> listAdvanceSearchOperator(){
        return accountInfoService.listAdvanceSearchOperator();
    }

    @GetMapping("/list-search-cus-acc-column")
    public ResponseEntity<?> listAdvanceSearchCusAccColumn(){
        return accountInfoService.listAdvanceSearchCusAccColumn();
    }
}

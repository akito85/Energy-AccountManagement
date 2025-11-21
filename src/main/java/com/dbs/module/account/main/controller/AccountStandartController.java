package com.dbs.module.account.main.controller;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.database.crm.entities.accountmanagement.VW_CUSTOMER_INFORMATION;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_BILLING_BUCKET;
import com.dbs.database.crm.entities.usermanagement.M_ATTACHMENT;
import com.dbs.database.crm.repositories.usermanagement.MAttachmentRepo;
import com.dbs.module.account.detail.address.dto.AccountAddressCreateDTO;
import com.dbs.module.account.detail.contact.dto.AccountContactCreateDTO;
import com.dbs.module.account.main.dto.AccountDTO;
import com.dbs.module.account.main.dto.CustomerExistRequestDTO;
import com.dbs.module.account.main.dto.GetFinancialInformationDTO;
import com.dbs.module.account.main.dto.createaccount.CreateAccountStandartDTO;
import com.dbs.module.account.main.services.AccountStandartService;
import com.google.api.client.util.IOUtils;
import com.jlefebure.spring.boot.minio.MinioConfigurationProperties;
import com.jlefebure.spring.boot.minio.MinioException;
import com.jlefebure.spring.boot.minio.MinioService;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
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
import java.util.Optional;

@RestController
@RequestMapping("/v1/dbs/api/account-standart")
@Api(tags = "Account Standart")
public class AccountStandartController {
    @Autowired
    private AccountStandartService accountStandartService;
    
    @Autowired
    MAttachmentRepo mAttachmentRepo;
    
    @Autowired
    private MinioService minioService;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioConfigurationProperties configurationProperties;

    @GetMapping("/customer-type")
    public ResponseEntity<?> getCustomerSegments() {
        return accountStandartService.getCustomerTypes();
    }
    @GetMapping("/identification-type")
    public ResponseEntity<?> getIdentificationTypes() {
        return accountStandartService.getIdentificationTypes();
    }
    @PostMapping("/is-customer-exist")
    public ResponseEntity<?> checkIsCustomerExist(@RequestBody CustomerExistRequestDTO dto, HttpServletRequest request) {
        return accountStandartService.checkIsCustomerExist(dto,request);
    }
    @PostMapping("/is-contact-exist")
    public ResponseEntity<?> checkIsContactExist(@RequestBody AccountContactCreateDTO dto) {
        return accountStandartService.checkExistContact(dto);
    }
    @PostMapping("/check-premise-address")
    public ResponseEntity<?> checkPremiseAddress(@RequestBody AccountAddressCreateDTO dto) {
        return accountStandartService.checkPremiseAddress(dto);
    }
    @PostMapping("/is-registration-number-exist")
    public ResponseEntity<?> checkIsRegistrationNumberExist(@RequestBody AccountDTO dto) {
        return accountStandartService.checkExistRegistrationNumber(dto);
    }
    @PostMapping("/create-account-standart")
    public ResponseEntity<?> createAccountStandart(@RequestBody CreateAccountStandartDTO dto, HttpServletRequest request) {
        return accountStandartService.createAccount(dto,request);
    }

    @PostMapping("/validate-account-standart")
    public ResponseEntity<?> validateAccountStandart(@RequestBody CreateAccountStandartDTO dto) {
        return accountStandartService.validateCreateAccount(dto);
    }
    
    @PostMapping("/get-financial-info")
    public ResponseEntity<?> getAll(@RequestBody GetFinancialInformationDTO dto, HttpServletRequest request, @Valid MaterialTablePagingRequest pagingdata, PagedResourcesAssembler<M_RBI_BILLING_BUCKET> assembler) {
        return accountStandartService.getTaxImplication(dto, request, pagingdata, assembler);
    }
    
    @GetMapping("/download2/{fileId}")
    public void downloadFile2(@PathVariable("fileId") Integer fileId, HttpServletResponse response)
                    throws MinioException, IOException, InvalidArgumentException, InvalidBucketNameException,
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
    
    @GetMapping("/list-one-time")
    public ResponseEntity<?> listOneTime(@Valid MaterialTablePagingRequest pagingdata, PagedResourcesAssembler<VW_CUSTOMER_INFORMATION> assembler) {
        return accountStandartService.getListOneTime(pagingdata, assembler);
    }
    
    @GetMapping("/detail-one-time/{customerId}")
    public ResponseEntity<?> detailOneTime(@PathVariable Integer customerId) {
        return accountStandartService.getDetailCustomerOneTime(customerId);
    }

    //acr
    @PostMapping(path = "/uploadAttachment/{refId}", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> uploadAttachment(@RequestParam Integer category, @RequestPart MultipartFile file,
                                              @PathVariable Integer refId) {
        return accountStandartService.uploadFile(category, file, refId);
    }

    //acr
    @GetMapping("/list-attachment/{referenceId}")
    public ResponseEntity<?> getListAttachment(@PathVariable Integer referenceId) {
        return accountStandartService.getListAttachment(referenceId);
    }

    //acr
    @GetMapping("/identification-type/{customerType}")
    public ResponseEntity<?> getIdentificationTypes(@PathVariable("customerType") Integer customerType) {
        return accountStandartService.getIdentificationTypesWithCustomerType(customerType);
    }
}

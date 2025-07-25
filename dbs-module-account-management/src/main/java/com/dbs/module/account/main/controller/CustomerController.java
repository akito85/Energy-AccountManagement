package com.dbs.module.account.main.controller;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.database.crm.entities.accountmanagement.M_CUSTOMER;
import com.dbs.module.account.main.dto.CustomerAttachmentDto;
import com.dbs.module.account.main.dto.CustomerUpdateDTO;
import com.dbs.module.account.main.services.CustomerService;
import com.jlefebure.spring.boot.minio.MinioException;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/v1/dbs/api/customer")
@Api(tags = "Customer")
@RequiredArgsConstructor
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    private CustomerService services;
    
//    @GetMapping("/viewPaging")
//    public ResponseEntity<?> getCustomerListData(@Valid MaterialTablePagingRequest pagingData, PagedResourcesAssembler<M_CUSTOMER> assembler) {
//        return services.getCustomerListData(pagingData, assembler);
//    }
    
//    @PostMapping("/activeInactive/{customerId}")
//    public ResponseEntity<?> activeInactive(@PathVariable("customerId") Integer customerId) {
//        return services.activeInactiveCustomer(customerId);
//    }
    
//    @GetMapping("/view/{customerType}")
//    public ResponseEntity<?> getListByCustomerType(@PathVariable Integer customerType, @Valid MaterialTablePagingRequest pagingData, PagedResourcesAssembler<M_CUSTOMER> assembler) {
//        return services.getListCustomerByType(customerType, pagingData, assembler);
//    }
    
//    @PostMapping("/update/{id}")
//    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody CustomerUpdateDTO request) {
//        return services.updateCustomer(id, request);
//    }
    
//    @GetMapping("/detail/{id}")
//    public ResponseEntity<?> getCustomerDetail(@PathVariable Integer id) {
//        return services.getCustomerDetail(id);
//    }
    
//    @GetMapping("getCustomerType")
//    public ResponseEntity<?> getCustomerType() {
//        logger.info("Get Customer Type");
//        return services.getCustomerType();
//    }
    @GetMapping("/attachment-category")
    public ResponseEntity<?> listAttachmentCategories() {
        return services.getAttachmentCategories();
    }

    @PostMapping("/create-customer-attachment/{customerId}")
    public ResponseEntity<?> createCustomerAttachment(HttpServletRequest request,
                                                      @RequestPart MultipartFile file,
                                                      @RequestParam Integer category,
                                                      @PathVariable Integer customerId){
        return services.createCustomerAttachment(request, file, customerId, category);
    }

//    @GetMapping("/list-customer-attachment/{customerId}")
//    public ResponseEntity<?> getListCustomerAttachment(@PathVariable Integer customerId,
//                                                       @Valid MaterialTablePagingRequest pagingdata, PagedResourcesAssembler<CustomerAttachmentDto> assembler){
//        return services.getListCustomerAttachment(pagingdata, assembler, customerId);
//    }

//    @GetMapping("/download-attachment/{fileId}")
//    public void downloadAttachment(@PathVariable("fileId") Integer fileId, HttpServletResponse response)throws MinioException, IOException, InvalidArgumentException, InvalidBucketNameException,
//            InsufficientDataException, XmlPullParserException, ErrorResponseException, NoSuchAlgorithmException,
//            NoResponseException, InvalidKeyException, InternalException, InvalidResponseException{
//        services.downloadAttachment(fileId, response);
//    }
}
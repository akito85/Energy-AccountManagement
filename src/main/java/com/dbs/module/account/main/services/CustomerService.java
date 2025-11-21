package com.dbs.module.account.main.services;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.utils.FlowStatus;
import com.dbs.common.library.utils.StringUtils;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.database.crm.entities.accountmanagement.M_CUSTOMER;
import com.dbs.database.crm.entities.usermanagement.M_ATTACHMENT;
import com.dbs.database.crm.entities.usermanagement.M_GLOBAL_TYPE;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.repositories.accountmanagement.Account.MCustomerRepo;
import com.dbs.database.crm.repositories.usermanagement.MAttachmentRepo;
import com.dbs.database.crm.repositories.usermanagement.MGlobalTypeRepo;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
import com.dbs.module.account.main.dto.CustomerAttachmentDto;
import com.dbs.module.account.main.dto.CustomerDetailDTO;
import com.dbs.module.account.main.dto.CustomerUpdateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.IOUtils;
import com.jlefebure.spring.boot.minio.MinioConfigurationProperties;
import com.jlefebure.spring.boot.minio.MinioException;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    
    @Autowired
    private Validator validator;
    @Autowired
    private MCustomerRepo cusRepo;
    @Autowired
    private MGlobalTypeRepo mGlobalTypeRepo;
    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MAttachmentRepo mAttachmentRepo;
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinioConfigurationProperties configurationProperties;

        
    public ResponseEntity<ResponseObject> getCustomerListData(MaterialTablePagingRequest pagingData, PagedResourcesAssembler<M_CUSTOMER> assembler) {
        try {
            ResponseObject result;
            Page<M_CUSTOMER> data;
            Map<String, Object> filter = new HashMap<>();
            if (pagingData.getSort() != null && pagingData.getSort().isEmpty()) {
                List<String> sort = new ArrayList<>();
                sort.add("createdDate~desc");
                pagingData.setSort(sort);
            }
            if (!pagingData.getSearch().isEmpty()) {
                data = this.cusRepo.findAll(this.cusRepo.getSpecificationFromFilters(pagingData, filter),
                        PagingUtils.getPaging(pagingData));
            } else {
                data = this.cusRepo.findAll(this.cusRepo.getSpecificationDefault(filter),
                        PagingUtils.getPaging(pagingData));
            }
            List<M_CUSTOMER> listCustomer = data.getContent();
            List<LinkedHashMap<String, Object>> allData = new ArrayList<>();
            for (M_CUSTOMER e : listCustomer) {
                LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                response.put("CustomerNumber", e.getCustomerNumber());
                response.put("CustomerType", e.getCustomerType());
                response.put("CustomerName", e.getCustomerName());
                response.put("CustomerIdentificationNumber", e.getCustomerIdentificationNumber());
                response.put("IdentificationType", e.getIdentificationType());
                response.put("PositionId", e.getPositionId());
                response.put("Sex", e.getSex());
                if(Objects.equals(e.getCustomerType(), "Personal")) {
                   response.put("BirthDate", e.getFoundedBirthDate()); 
                   response.put("BirthPlace", e.getFoundedBirthDate());
                } else {
                   response.put("FoundDate", e.getFoundedBirthDate());
                   response.put("FoundPlace", e.getFoundedBirthDate());
                }
                response.put("MaritalStatus", e.getMaritalStatus());
                response.put("Status", e.getStatus());
                response.put("SearchKey", e.getSearchKey());

                allData.add(response);
            }
            PagedModel<EntityModel<M_CUSTOMER>> pagedData = assembler.toModel(data);
            Map<String, Object> d = new HashMap<>();
            d.put(Constant.RESULT, allData);
            d.put(Constant.PAGE, pagedData.getMetadata());
            d.put(Constant.LINK, pagedData.getLinks());
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_OK, d);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings({"java:S1192"})
    public ResponseEntity<ResponseObject> activeInactiveCustomer(Integer customerId) {
        try {
            ResponseObject result;
            Optional<M_CUSTOMER> data = cusRepo.findById(customerId);
            if (data.isPresent()) {
                M_CUSTOMER dataCustomer = data.get();
                if (dataCustomer.getStatus().equalsIgnoreCase(FlowStatus.INACTIVE.name())) {
                    dataCustomer.setStatus(FlowStatus.ACTIVE.name());
                    dataCustomer.setUpdatedDate(new Date());
                    dataCustomer.setUpdatedBy(StringUtils.hasValue(UserDetailUtils.getUsername()) ? UserDetailUtils.getUsername() : null);
                    cusRepo.save(dataCustomer);
                    result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            "Success Active Customer", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                } else if (dataCustomer.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())) {
                    dataCustomer.setStatus("INACTIVE");
                    dataCustomer.setUpdatedDate(new Date());
                    dataCustomer.setUpdatedBy(StringUtils.hasValue(UserDetailUtils.getUsername()) ? UserDetailUtils.getUsername() : null);
                    cusRepo.save(dataCustomer);
                    result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                            "Success Inactive Customer", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
            } else {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "Customer with id " + customerId + " not found", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Active Or Inactive Customer is Empty", ResponseUtils.DATA_EMPTY);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public ResponseEntity<ResponseObject> getListCustomerByType(Integer customerType, MaterialTablePagingRequest pagingData, PagedResourcesAssembler<M_CUSTOMER> assembler) {
        logger.info("Get List Customer");
        ResponseObject result = new ResponseObject();
        try {
            Page<M_CUSTOMER> data = null;
            Map<String, Object> filter = new HashMap<>(); // For Default Filter
            filter.put("customerType", customerType);
            if (pagingData.getSearch().size() > 0) {
                data = this.cusRepo.findAll(
                        this.cusRepo.getSpecificationFromFilters(pagingData, filter),
                        PagingUtils.getPaging(pagingData));
            } else {
                data = this.cusRepo.findAll(this.cusRepo.getSpecificationDefault(filter),
                        PagingUtils.getPaging(pagingData));
            }

            PagedModel pagedData = assembler.toModel(data);

            Map<String, Object> d = new HashMap<>();
            d.put("result", pagedData.getContent());
            d.put("page", pagedData.getMetadata());
            d.put("links", pagedData.getLinks());

            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage("Success View Header Customer");
            result.setData(d);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings({"java:S1192"})
    public ResponseEntity<ResponseObject> getCustomerDetail(Integer id) {
        try {
            ResponseObject result;
            Optional<M_CUSTOMER> dataCustomer = cusRepo.findById(id);
            if (dataCustomer.isPresent()) {
                M_CUSTOMER getCustomer = dataCustomer.get();

                CustomerDetailDTO newCustomer = new CustomerDetailDTO();
                newCustomer.setCustomerNumber(getCustomer.getCustomerNumber());
                newCustomer.setCustomerType(getCustomer.getCustomerType());
                newCustomer.setFirstName(getCustomer.getFirstName());
                newCustomer.setMiddleName(getCustomer.getMiddleName());
                newCustomer.setLastName(getCustomer.getLastName());
                newCustomer.setCustomerName(getCustomer.getCustomerName());
                newCustomer.setPersonalIdentificationNumber(getCustomer.getCustomerIdentificationNumber());
                newCustomer.setIdentificationType(getCustomer.getIdentificationType());
                newCustomer.setPosition(getCustomer.getPositionId());
                newCustomer.setSex(getCustomer.getSex());
                newCustomer.setDateOfBirth(getCustomer.getFoundedBirthDate());
                newCustomer.setPlaceOfBirth(getCustomer.getFoundedBirthPlace());
                newCustomer.setMaritalStatus(getCustomer.getMaritalStatus());
                newCustomer.setStatus(getCustomer.getStatus());
                newCustomer.setSearchKey(getCustomer.getSearchKey());
                newCustomer.setCreatedBy(getCustomer.getCreatedBy());
                newCustomer.setCreatedDate(getCustomer.getCreatedDate());
                newCustomer.setUpdatedBy(getCustomer.getUpdatedBy());
                newCustomer.setUpdatedDate(getCustomer.getUpdatedDate());

                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        "Success get data detail customer", newCustomer);
                logger.info("Response Success ->" + result);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                logger.info("customer with id " + id + " not found");
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND.value(),
                        "customer with id " + id + " not found", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings("java:S1141")
    public ResponseEntity<ResponseObject> updateCustomer(Integer id, CustomerUpdateDTO request) {
        ResponseObject result;
        logger.info("paramRequest -> {}", request);
        try {
            Optional<M_CUSTOMER> customerId = cusRepo.findById(id);
            if (customerId.isPresent()) {
                Set<ConstraintViolation<CustomerUpdateDTO>> violations = this.validator.validate(request);
                List<Map<String, Object>> violationList = new ArrayList<>();
                if (violations.size() > 0) {
                    for (ConstraintViolation<CustomerUpdateDTO> violation : violations) {
                        logger.error(violation.getMessage());
                        Map<String, Object> datas = new HashMap<>();
                        datas.put(violation.getPropertyPath().toString(), violation.getMessage());
                        violationList.add(datas);
                    }
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            ResponseUtils.MESSAGE_BAD_REQUEST, violationList);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                M_CUSTOMER updateCustomer = customerId.get();
                updateCustomer.setCustomerNumber(request.getCustomerNumber());
                updateCustomer.setCustomerType(request.getCustomerType());
                if(!updateCustomer.getCustomerType().equals(58)) {
                    updateCustomer.setFirstName(request.getFirstName());
                    updateCustomer.setMiddleName(request.getMiddleName());
                    updateCustomer.setLastName(request.getLastName());
                    updateCustomer.setCustomerName(request.getCustomerName());
                }
                updateCustomer.setCustomerIdentificationNumber(request.getPersonalIdentificationNumber());
                updateCustomer.setIdentificationType(request.getIdentificationType());
                updateCustomer.setPositionId(request.getPosition());
                updateCustomer.setSex(request.getSex());
                SimpleDateFormat formatDate = new SimpleDateFormat("dd MMM yyyy");
                try {
                updateCustomer.setFoundedBirthDate(formatDate.parse(request.getDateOfBirth()));
                } catch (Exception e) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "Invalid Date of birth", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                updateCustomer.setFoundedBirthPlace(request.getPlaceOfBirth());
                updateCustomer.setMaritalStatus(request.getMaritalStatus());
                updateCustomer.setStatus(request.getStatus());
                updateCustomer.setSearchKey(request.getSearchKey());
                updateCustomer.setUpdatedBy(UserDetailUtils.getUsername() != null ? UserDetailUtils.getUsername() : null);
                updateCustomer.setUpdatedDate(new Date());
                
                cusRepo.save(updateCustomer);
                
                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, "Success update customer", updateCustomer);
                logger.info("Response Success ->" + result);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                logger.info("customer with id " + id + " not found");
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        "customer with id " + id + " not found", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    public ResponseEntity<ResponseObject> getCustomerType() {
        logger.info("Find Customer Type");
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Customer Type", FlowStatus.ACTIVE.name(), false);
            logger.info("Customer Type " + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;
            
            if(!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name()))
                        .collect(Collectors.toList());
                List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
                if(!areas.isEmpty()) {
                    for(R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                        ar.put("id", rgtv.getGlbTypeValId());
                        ar.put("code", rgtv.getGlbValue());
                        ar.put("text", rgtv.getName());
                        areaList.add(ar);
                    }
                    
                    result.setSuccess(true);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Success get Customer Type");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Fail get Customer Type");
                    result.setData("There is no Customer Type");
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Fail get Customer Type");
                result.setData("There is no Service Type");
            }
            
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<ResponseObject> getAttachmentCategories() {
        logger.info("Find Attachment Category Customer");
        ResponseObject result = new ResponseObject();
        try {
            Optional<M_GLOBAL_TYPE> auths = mGlobalTypeRepo.findAllByGroupNameAndStatusAndIsDeleted("Attachment Category Customer",
                    FlowStatus.ACTIVE.name(), false);
            logger.info("Attachment Category Customer" + auths);
            List<R_GLOBAL_TYPE_VALUE> areas = null;

            if (!auths.isEmpty()) {
                areas = auths.get().getRGlobalTypeValues().stream().filter(b -> b.getStatus().equals(FlowStatus.ACTIVE.name())).
                        sorted((o1, o2)->o1.getName().
                                compareTo(o2.getName()))
                        .collect(Collectors.toList());
                List<LinkedHashMap<String, Object>> areaList = new LinkedList<>();
                if (!areas.isEmpty()) {
                    for (R_GLOBAL_TYPE_VALUE rgtv : areas) {
                        LinkedHashMap<String, Object> ar = new LinkedHashMap<>();
                        ar.put("id", rgtv.getGlbTypeValId());
                        ar.put("glbTypeValId", rgtv.getGlbTypeValId());
                        ar.put("name", rgtv.getName());
                        areaList.add(ar);
                    }
                    result.setSuccess(true);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Success get Attachment Category Customer");
                    result.setData(areaList);
                } else {
                    result.setSuccess(false);
                    result.setCode(HttpStatus.OK);
                    result.setMessage("Failed get Attachment Category Customer");
                    result.setData(ResponseUtils.DATA_EMPTY);
                }
            } else {
                result.setSuccess(false);
                result.setCode(HttpStatus.OK);
                result.setMessage("Failed get Attachment Category Customer");
                result.setData(ResponseUtils.DATA_EMPTY);
            }

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<ResponseObject> createCustomerAttachment(HttpServletRequest request, MultipartFile parameter,
                                                           Integer customerId, Integer categoryId){
        logger.info("Create customer attachment");
        logger.info("param : {}", parameter);
        ResponseObject result = new ResponseObject();

        try {
            String generatedFileName = UserDetailUtils.generateFileName(parameter.getOriginalFilename());
            M_ATTACHMENT mAttachment = new M_ATTACHMENT();
            mAttachment.setCategory("CUSTOMER_ATTACHMENT");
            mAttachment.setFileCategoryId(categoryId);
            mAttachment.setReferenceId(customerId);
            mAttachment.setType(parameter.getContentType());
            mAttachment.setCreatedBy(UserDetailUtils.getUsername());
            mAttachment.setCreatedDate(new Date());
            mAttachment.setPathFile("PATH_22");
            mAttachment.setFileName(generatedFileName);
            mAttachment.setFileSize(parameter.getSize());
            mAttachment.setIsDraft(Boolean.TRUE);
            mAttachment.setIsDeleted(Boolean.FALSE);

            M_ATTACHMENT saveAttachment = this.mAttachmentRepo.save(mAttachment);
            if (ObjectUtils.isEmpty(saveAttachment)){
                result.setSuccess(false);
                result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
                result.setMessage("Customer attachment not Created");
                result.setData(ResponseUtils.DATA_EMPTY);

                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            R_GLOBAL_TYPE_VALUE rGlobalTypeValue = rGlobalTypeValueRepo
                    .findTopByGlbValueIgnoreCaseAndIsDeleted("PATH_22", false);
            String fullPath = rGlobalTypeValue.getName() + generatedFileName;

            String fullobject = "FILE" + fullPath;
            this.minioClient.putObject(this.configurationProperties.getBucket(), fullobject,
                    parameter.getInputStream(), parameter.getContentType());

            Map<String, Object> responseSuccessData = new HashMap<>();
            responseSuccessData.put("attachmentId",saveAttachment.getId());
            responseSuccessData.put("attachmentFileName",saveAttachment.getFileName());
            result.setSuccess(true);
            result.setCode(HttpStatus.CREATED);
            result.setMessage("Customer attachment Created");
            result.setData(responseSuccessData);

            return new ResponseEntity<>(result, HttpStatus.OK);

        }catch (Exception e){
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> getListCustomerAttachment(MaterialTablePagingRequest pagingdata, PagedResourcesAssembler<CustomerAttachmentDto> assembler, Integer customerId){
        logger.info("list customer attachment");
        ResponseObject result = new ResponseObject();

        try{
            Page<M_ATTACHMENT> data = null;
            Page<CustomerAttachmentDto> dto = null;
            Map<String, Object> filter = new HashMap<>();
            filter.put("referenceId", customerId);
            filter.put("category", "CUSTOMER_ATTACHMENT");
            if (pagingdata.getSearch().size() > 0) {
                data = this.mAttachmentRepo.findAll(
                        this.mAttachmentRepo.getSpecificationFromFilters(pagingdata, filter),
                        PagingUtils.getPaging(pagingdata));
            } else {
                data = this.mAttachmentRepo.findAll(this.mAttachmentRepo.getSpecificationDefault(filter),
                        PagingUtils.getPaging(pagingdata));
            }

            List<M_ATTACHMENT> mAttachments = data.getContent();
            List<CustomerAttachmentDto> customerAttachmentDtos = new ArrayList<>();

            for (M_ATTACHMENT mAttachment : mAttachments){
                CustomerAttachmentDto customerAttachmentDto = (this.objectMapper.convertValue(mAttachment, CustomerAttachmentDto.class));

                customerAttachmentDto.setId(mAttachment.getId());
                customerAttachmentDto.setCategory(mAttachment.getCategory());
                customerAttachmentDto.setFileName(mAttachment.getFileName());
                customerAttachmentDto.setFileSize(mAttachment.getFileSize());
                customerAttachmentDto.setUploadDate(mAttachment.getCreatedDate());
                customerAttachmentDto.setUploadBy(mAttachment.getUpdatedBy());

                customerAttachmentDtos.add(customerAttachmentDto);
            }

            if (data.getNumberOfElements() > 0) {
                PageRequest pageable = PageRequest.of(data.getNumber(), data.getTotalPages());
                dto = new PageImpl(customerAttachmentDtos, pageable, 0);
                PagedModel pagedData = assembler.toModel(dto);

                Map<String, Object> d = new HashMap<>();
                d.put("result", pagedData.getContent());
                d.put("page", pagedData.getMetadata());
                d.put("links", pagedData.getLinks());

                result.setSuccess(true);
                result.setCode(HttpStatus.OK);
                result.setMessage("Success get list customer attachment");
                result.setData(d);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setSuccess(true);
                result.setCode(HttpStatus.OK);
                result.setMessage("cannot found Attachment data");
                result.setData(ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        }catch (Exception e){
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void downloadAttachment(Integer fileId, HttpServletResponse response)throws MinioException, IOException, InvalidArgumentException, InvalidBucketNameException,
            InsufficientDataException, XmlPullParserException, ErrorResponseException, NoSuchAlgorithmException,
            NoResponseException, InvalidKeyException, InternalException, InvalidResponseException{
        var getAttachmentData = mAttachmentRepo.findById(fileId);
        logger.info("masuk we->"+getAttachmentData);
        if(getAttachmentData.isPresent()) {
            M_ATTACHMENT attachmentData =getAttachmentData.get();
            InputStream inputStream = minioClient.getObject(this.configurationProperties.getBucket(),
                    "FILE" + attachmentData.getPathFile()+ attachmentData.getFileName());
            // Set the content type and attachment header.
            response.addHeader("Content-disposition", "attachment;filename=" + attachmentData.getFileName());
            response.setContentType(URLConnection.guessContentTypeFromName(attachmentData.getFileName()));
            // Copy the stream to the response's output stream.
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        }
    }
}

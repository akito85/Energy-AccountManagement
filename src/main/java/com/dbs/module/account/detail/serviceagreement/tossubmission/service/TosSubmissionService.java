package com.dbs.module.account.detail.serviceagreement.tossubmission.service;

import com.dbs.common.library.services.CriteriaServices;
import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.ApprovalHeaderViewDto;
import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.CreateTosSubmissionRequest;
import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.ApprovalHierarchyDetailDto;
import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.ApprovalHierarchyDto;
import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.ApprovalTosSubmissionDTO;
import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.InactiveDTO;
import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.TosSubmissionDetailDto;
import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.AttachmentDto;
import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.ChooseTosSubmissionDTO;
import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.MvTosSubmissionDTO;
import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.TosSubmissionCreateResultDto;
import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.TosSubmissionDto;
import com.dbs.common.base.utils.*;
import com.dbs.common.library.ctrl.PagingDTO;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.ApprovalServices;
import com.dbs.common.library.utils.*;
import com.dbs.database.crm.entities.accountmanagement.T_AM_TOS_SUBMISSION;
import com.dbs.database.crm.entities.accountmanagement.T_AM_TOS_SUBMISSION_DTL;
import com.dbs.database.crm.entities.accountmanagement.VW_TOS_CATALOG;
import com.dbs.database.crm.entities.accountmanagement.VW_TOS_SUBMISSION;
import com.dbs.database.crm.entities.product.T_APPROVAL_HISTORY;
import com.dbs.database.crm.entities.product.VW_APPROVAL_HIERARCHY_DETAIL;
import com.dbs.database.crm.entities.usermanagement.*;
import com.dbs.database.crm.entities.usermanagement.view.VW_HIER_FLOW;
import com.dbs.database.crm.repositories.accountmanagement.Account.TAmTosSubmissionDetailRepo;
import com.dbs.database.crm.repositories.accountmanagement.Account.TAmTosSubmissionRepo;
import com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement.VwTosCatalogDtlRepo;
import com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement.VwTosCatalogRepo;
import com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement.VwTosSubmissionDtlRepo;
import com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement.VwTosSubmissionRepo;
import com.dbs.database.crm.repositories.product.TApprovalHistoryRepo;
import com.dbs.database.crm.repositories.product.VWApprovalHierarchyDtlRepo;
import com.dbs.database.crm.repositories.usermanagement.*;
import com.dbs.database.crm.repositories.usermanagement.view.VWHierFlowRepo;
import com.dbs.module.account.utils.ConstantAccount;
import com.dbs.module.account.utils.UtilsAccount;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jlefebure.spring.boot.minio.MinioConfigurationProperties;
import com.unboundid.util.json.JSONException;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@Slf4j
@RequiredArgsConstructor
public class TosSubmissionService {

    private final TAmTosSubmissionRepo tAmTosSubmissionRepo;

    private final TAmTosSubmissionDetailRepo tAmTosSubmissionDetailRepo;

    private final MAttachmentRepo mAttachmentRepo;

    private final Environment env;

    private static final String PATH_FILE = "PATH_23";

    private final ObjectMapper objectMapper;

    private final MGlobalTypeRepo mGlobalTypeRepo;

    private final MApprovalHierarchyRepo mApprovalHierarchyRepo;

    private final VWApprovalHierarchyDtlRepo vwApprovalHierarycyDtlRepo;
    private static final String WAITING_APPROVAL = "WAITING APPROVAL";
    @Autowired
    private VwTosSubmissionRepo mvTosSubmissionRepo;
    @Autowired
    private VwTosSubmissionDtlRepo mvTosSubmissionDtlRepo;
    @Autowired
    private TApprovalHistoryRepo tApprovalHistoryRepo;
    @Autowired
    private TApprovalRepo tApprovalRepo;
    @Autowired
    private VWHierFlowRepo vWHierFlowRepo;
    @Autowired
    private MUserRepo mUserRepo;
    @Autowired
    private Validator validator;
    @Autowired
    private ApprovalServices approvalServices;
    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinioConfigurationProperties configurationProperties;
    @Autowired
    private VwTosCatalogRepo mvTosCatalogRepo;
    @Autowired
    private VwTosCatalogDtlRepo mvTosCatalogDtlRepo;
    @Autowired
    private MApprovalHierarchyDtlRepo mApprovalHierarchyDtlRepo;
    @Autowired
    private MGlobalPropertiesRepo mGlobalPropertiesRepo;
    @Autowired
    private CriteriaServices criteriaServices;
    public ResponseEntity<ResponseObject> getTosSubmissionListWithPaging(
            MaterialTablePagingRequest pagingRequest,
            PagedResourcesAssembler<T_AM_TOS_SUBMISSION> assembler) {
        log.info("getTosSubmissionList req : {}", pagingRequest);

        try {
            if (ObjectUtils.isEmpty(pagingRequest.getSize()) || pagingRequest.getSize() <= 0)
                pagingRequest.setSize(10);

            Page<T_AM_TOS_SUBMISSION> tosSubmissionPage;
            Map<String, Object> filter = new HashMap<>(); // For Default Filter

            if (!pagingRequest.getSearch().isEmpty())
                tosSubmissionPage = tAmTosSubmissionRepo.findAll(
                        tAmTosSubmissionRepo.getSpecificationFromFilters(pagingRequest, filter),
                        PagingUtils.getPaging(pagingRequest)
                );
            else
                tosSubmissionPage = tAmTosSubmissionRepo.findAll(
                        tAmTosSubmissionRepo.getSpecificationDefault(filter),
                        PagingUtils.getPaging(pagingRequest)
                );

            log.info("tosSubmissionPage : {}", tosSubmissionPage);

            var pagedData = assembler.toModel(tosSubmissionPage);
            Map<String, Object> data = new HashMap<>();
            data.put(Constant.RESULT, pagedData.getContent());
            data.put(Constant.PAGE, pagedData.getMetadata());
            data.put(Constant.LINK, pagedData.getLinks());

            return new ResponseEntity<>(
                    ResponseObject.builder()
                            .success(ResponseUtils.SUCCESS_TRUE)
                            .code(HttpStatus.OK.value())
                            .message("Success Get List Tos Submission")
                            .data(data)
                            .build(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            log.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> getTosSubmissionDetail(Integer tosSubmissionId,HttpServletRequest httpServletRequest) {
        log.info("getTosSubmissionDetail : {}", tosSubmissionId);

        if (ObjectUtils.isEmpty(tosSubmissionId) || tosSubmissionId <= 0)
            return new ResponseEntity<>(
                    ResponseObject.builder()
                            .success(ResponseUtils.SUCCESS_FALSE)
                            .code(HttpStatus.BAD_REQUEST.value())
                            .message("tosSubmissionId not valid")
                            .data(null)
                            .build(),
                    HttpStatus.BAD_REQUEST
            );

        try {
            var mvTosSubmissionOptional = mvTosSubmissionRepo.findById(tosSubmissionId);
            log.info("tosSubmissionOptional : {}", mvTosSubmissionOptional);
            if (mvTosSubmissionOptional.isEmpty())
                return new ResponseEntity<>(
                        ResponseObject.builder()
                                .success(ResponseUtils.SUCCESS_FALSE)
                                .code(HttpStatus.BAD_REQUEST.value())
                                .message("tos submission with id " +tosSubmissionId+" is not exist")
                                .data(null)
                                .build(),
                        HttpStatus.BAD_REQUEST
                );

            var mvTosSubmission = mvTosSubmissionOptional.get();
            List<AttachmentDto> attachmentListDto = new ArrayList<>();

            var mvTosSubmissionDtlList = mvTosSubmissionDtlRepo.findAllByTosSubmissionId(mvTosSubmission.getId());
            log.info("tosSubmissionDtlList : {}", mvTosSubmissionDtlList);

            Optional<List<M_ATTACHMENT>> attach = mAttachmentRepo
                    .findByReferenceIdAndIsDraftAndIsDeletedAndCategoryIgnoreCase(tosSubmissionId, false, false,
                            "TOS_SUBMISSION");
            if (attach.isPresent()) {
                M_GLOBAL_PROPERTIES mGlobalPropUrl = mGlobalPropertiesRepo.findAllByName("PATH_URL");
                    Optional<R_GLOBAL_PROPERTIES_DTL> rgpUrlViewFile = mGlobalPropUrl.getRGlobalPropertiesDtls().stream()
                            .filter(e -> e.getGpdKey().equalsIgnoreCase("URL_VIEW_FILE"))
                            .findFirst();
                    String urlViewFile = rgpUrlViewFile.isPresent() ? rgpUrlViewFile.get().getGpdVal() : "";
                for (M_ATTACHMENT dtl_ : attach.get()) {
                    AttachmentDto attachDto = (this.objectMapper.convertValue(dtl_, AttachmentDto.class));

                    attachDto.setFileType(dtl_.getType());
                    attachDto.setUrlFile1("/v1/dbs/api/tossubmission/download2/" + dtl_.getId().toString());
                    attachDto.setUrlFile2(urlViewFile + dtl_.getId());
                    attachDto.setFileCategoryName(dtl_.getFileCategoryName());
                    attachDto.setCreatedDate(dtl_.getCreatedDate());
                    attachDto.setCreatedBy(UserDetailUtils.getUsername());

                    attachmentListDto.add(attachDto);
                }
            }

            Optional<T_AM_TOS_SUBMISSION> getMTosSubmission = tAmTosSubmissionRepo.findById(mvTosSubmission.getId());

            TosSubmissionDto data = new TosSubmissionDto();
            data.setSaTosSubmissionId(mvTosSubmission.getId());
            data.setSaTosId(mvTosSubmission.getTosNameId());
            data.setSaTosName(mvTosSubmission.getTosName());
            data.setTosSubmissionDetail(mvTosSubmissionDtlList);
            data.setStartDate(mvTosSubmission.getStartDate());
            data.setEndDate(mvTosSubmission.getEndDate());
            data.setAppliedDate(mvTosSubmission.getAppliedDate());
            data.setRemark(mvTosSubmission.getRemark());
            getMTosSubmission.ifPresent(tAmTosSubmission -> data.setAppHierId(tAmTosSubmission.getAppHierId()));
            data.setStatus(mvTosSubmission.getStatus());
            data.setStatusApproval(mvTosSubmission.getStatusApproval());
            data.setCreatedDate(mvTosSubmission.getCreatedDate());
            data.setUpdatedDate(mvTosSubmission.getUpdatedDate());
            data.setCreatedBy(mvTosSubmission.getCreatedBy());
            data.setUpdatedBy(mvTosSubmission.getUpdatedBy());
            data.setMAttachments(attachmentListDto);

            // get current Approval Id
            List<String> appCategory = new ArrayList<>();
            appCategory.add("TOS_SUBMISSION");
            appCategory.add("INACTIVE_TOS_SUBMISSION");

            Optional<T_APPROVAL> appId = tApprovalRepo.findFirstByIdTransAndCategoryInAndStatus(tosSubmissionId.toString(),
                    appCategory, ApprovalStatus.WAITING_FOR_APPROVAL.name());

            if (appId.isPresent()) {
                log.info("ada approval");
                Optional<VW_HIER_FLOW> appHierDtlOpt = vWHierFlowRepo.findBytAppId(appId.get().getTAppId());
                if (appHierDtlOpt.isPresent()) {
                    VW_HIER_FLOW appHierDtl = appHierDtlOpt.get();
                    if (StringUtils.hasValue(appHierDtl.getForwardTo()) && appHierDtl.getTaskForward() > 0) {
                            Optional<M_USER> mUser = mUserRepo.findByUsername(UserDetailUtils.getUsernameFromToken(httpServletRequest));
                            String employeeCode = "";
                            if (mUser.isPresent()) {
                                employeeCode = mUser.get().getEmployee().getEmployeeCode();
                            }
                            if (appHierDtl.getForwardTo().equals(UserDetailUtils.getPositionFromToken(httpServletRequest))
                                    && (StringUtils.hasValue(appHierDtl.getEmployeeForward()) 
                                    && appHierDtl.getEmployeeForward().equalsIgnoreCase(employeeCode))) {
                                data.setAppHierId(appHierDtl.getAppHierId());
                                data.setTAppId(appId.get().getTAppId());
                                data.setApprovalType(appId.get().getCategory());
                                data.setIsApprover(Boolean.TRUE);
                            } else {
                                data.setIsApprover(Boolean.FALSE);
                            }
                        } else {
                            if (appHierDtl.getPositionId().equals(UserDetailUtils.getPositionFromToken(httpServletRequest))) {
                                data.setAppHierId(appHierDtl.getAppHierId());
                                data.setTAppId(appId.get().getTAppId());
                                data.setApprovalType(appId.get().getCategory());
                                data.setIsApprover(Boolean.TRUE);
                            } else {
                                data.setIsApprover(Boolean.FALSE);
                            }
                        }
                } else {
                    Optional<M_APPROVAL_HIERARCHY_DTL> mappHierDtlOpt = mApprovalHierarchyDtlRepo
                        .findFirstByAppHierIdAndApprovalLevel(appId.get().getAppHierId(),
                                appId.get().getApprovalLevel());
                        if (mappHierDtlOpt.isPresent()) {
                            M_APPROVAL_HIERARCHY_DTL appHierDtl = mappHierDtlOpt.get();
                            if (appHierDtl.getPositionId().equals(UserDetailUtils.getPositionFromToken(httpServletRequest))) {
                                data.setAppHierId(appHierDtl.getAppHierId());
                                data.setTAppId(appId.get().getTAppId());
                                data.setApprovalType(appId.get().getCategory());
                                data.setIsApprover(Boolean.TRUE);
                            } else {
                                data.setIsApprover(Boolean.FALSE);
                            }
                        }
                }

                Integer appHist = tApprovalHistoryRepo.findTopSubmittedByRefIdAndCategory(tosSubmissionId,
                        ApprovalCategory.TOS_SUBMISSION.name());

                Optional<T_APPROVAL_HISTORY> dtlAppOpt = tApprovalHistoryRepo.findFirstBytAppId(appHist);

                if (dtlAppOpt.isPresent()) {

                    T_APPROVAL_HISTORY dtlApp = dtlAppOpt.get();
                    ApprovalHeaderViewDto appDtl = new ApprovalHeaderViewDto();
                    appDtl.setRequestedBy(dtlApp.getFullName());
                    appDtl.setRequestedDate(dtlApp.getEventDate());
                    appDtl.setRemarks(dtlApp.getDescription());
                    appDtl.setApprovalType(dtlApp.getApprovalType());

                    data.setApprovalDetail(appDtl);
                }
            }

            return new ResponseEntity<>(
                    ResponseObject.builder()
                            .success(ResponseUtils.SUCCESS_TRUE)
                            .code(HttpStatus.OK.value())
                            .message("Success Get Detail Tos Submission")
                            .data(data)
                            .build(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            log.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> listCategory() {
        log.info("Tos Attachment Category");
        ResponseObject result;
        try {
            List<LinkedHashMap<String, Object>> allData = criteriaServices.getCriteriaByGlobalType("Tos Attachment Category");
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_OK, allData);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error(CommonVariables.ERROR_IN, e.getMessage(), e);
            throw e;
        }
//        ResponseObject result;
//        try {
//            M_GLOBAL_TYPE getCategory = mGlobalTypeRepo.findTopByGlbTypeIdAndIsDeleted(65, Boolean.FALSE);
//
//            LinkedHashMap<String, Object> responseData = new LinkedHashMap<>();
//            responseData.put("data", getCategory.getRGlobalTypeValues());
//
//            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, ResponseUtils.MESSAGE_OK,
//                    responseData);
//            return new ResponseEntity<>(result, result.getHttpCode());
//        } catch (Exception e) {
//            log.error(Constant.LOG_ERROR, e.getMessage(), e);
//            return new ResponseEntity<>(
//                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
//                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
//                    HttpStatus.INTERNAL_SERVER_ERROR);
//        }
    }

    public ResponseEntity<ResponseObject> getAllApprovalHeaderList(HttpServletRequest httpServletRequest) {
        log.info("getAllApprovalHeaderList");
        try {
            var mApprovalHierarchies = mApprovalHierarchyRepo.findApprovalHierarchyByPositionAndType(UserDetailUtils.getPositionFromToken(httpServletRequest), "Y", "APR_ACC_TOS_SUB");
            List<ApprovalHierarchyDto> approvals = new ArrayList<>();
            if (!mApprovalHierarchies.isEmpty()) {
                for (M_APPROVAL_HIERARCHY mApprovalHierarchy : mApprovalHierarchies) {
                    approvals.add(ApprovalHierarchyDto.builder()
//                            .appHierCode(mApprovalHierarchy.getApprovalCode())
                            .appHierId(mApprovalHierarchy.getAppHierId())
                            .approvalName(mApprovalHierarchy.getApprovalName())
                            .approvalType(mApprovalHierarchy.getApprovalType())
                            .desc(mApprovalHierarchy.getDesc())
                            .entityId(mApprovalHierarchy.getEntityId())
                            .build());
                }
            }

            log.info("data approvals : {}", approvals);

            return new ResponseEntity<>(
                    ResponseObject.builder()
                            .success(ResponseUtils.SUCCESS_TRUE)
                            .code(HttpStatus.OK.value())
                            .message("Success Get Data Header Approval Hierarchies")
                            .data(approvals)
                            .build(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            log.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> getApprovalHeaderById(Integer appHierId) {
        log.info("getApprovalHeaderById : {}", appHierId);

        ResponseObject result = new ResponseObject();
        try {
            Optional<List<VW_APPROVAL_HIERARCHY_DETAIL>> appDtl = vwApprovalHierarycyDtlRepo.findByApphierId(appHierId);

            List<ApprovalHierarchyDetailDto> dtos = new ArrayList<>();

            if (appDtl.isPresent()) {

                List<String> pos = vwApprovalHierarycyDtlRepo.findPositionById(appHierId);

                for (String pos_ : pos) {
                    List<HashMap<String, Object>> detailList = new ArrayList<>();
                    ApprovalHierarchyDetailDto hierarchyDetailDto = new ApprovalHierarchyDetailDto();

                    List<VW_APPROVAL_HIERARCHY_DETAIL> submitter = vwApprovalHierarycyDtlRepo
                            .findByApphierIdAndPositionAndIsSubmitterAndIsFinal(appHierId, pos_, "Y", "N").get();
                    if (!submitter.isEmpty()) {
                        hierarchyDetailDto.setApphierId(appHierId);
                        hierarchyDetailDto.setApprovalLevel("Submitter");
                        hierarchyDetailDto.setPosition(pos_);
                        for (VW_APPROVAL_HIERARCHY_DETAIL appDtl_ : submitter) {

                            HashMap<String, Object> ar = new HashMap<>();
                            ar.put("appHierId", appDtl_.getApphierId());
                            ar.put("employeeId", appDtl_.getEmployeeId());
                            ar.put("employeeName", appDtl_.getFullName());

                            detailList.add(ar);
                        }
                        hierarchyDetailDto.setEmployeeDetail(detailList);
                        dtos.add(hierarchyDetailDto);
                    }

                }
            }

            if (appDtl.isPresent()) {

                List<String> pos = vwApprovalHierarycyDtlRepo.findPositionById(appHierId);

                for (String pos_ : pos) {
                    List<HashMap<String, Object>> detailList = new ArrayList<>();
                    ApprovalHierarchyDetailDto hierarchyDetailDto = new ApprovalHierarchyDetailDto();

                    // Approver
                    for (int i = 1; i < 6; i++) {
                        List<VW_APPROVAL_HIERARCHY_DETAIL> approver;
                        approver = vwApprovalHierarycyDtlRepo
                                .findByApphierIdAndApprovalLevelAndPositionAndIsSubmitterAndIsFinal(appHierId, i, pos_,
                                        "N", "N")
                                .get();
                        if (!approver.isEmpty()) {
                            hierarchyDetailDto.setApphierId(appHierId);
                            hierarchyDetailDto.setApprovalLevel("Approver " + i);
                            hierarchyDetailDto.setPosition(pos_);
                            for (VW_APPROVAL_HIERARCHY_DETAIL appDtl_ : approver) {

                                HashMap<String, Object> ar = new HashMap<>();
                                ar.put("appHierId", appDtl_.getApphierId());
                                ar.put("employeeId", appDtl_.getEmployeeId());
                                ar.put("employeeName", appDtl_.getFullName());

                                detailList.add(ar);
                                log.info("Approver " + i + " :" + appDtl_.getFullName());
                            }
                            log.info("Size: " + detailList.size());
                            hierarchyDetailDto.setEmployeeDetail(detailList);
                            dtos.add(hierarchyDetailDto);
                        }
                    }
                }
            }

            if (appDtl.isPresent()) {
                List<String> pos = vwApprovalHierarycyDtlRepo.findPositionById(appHierId);
                for (String pos_ : pos) {
                    List<HashMap<String, Object>> detailList = new ArrayList<>();
                    ApprovalHierarchyDetailDto hierarchyDetailDto = new ApprovalHierarchyDetailDto();

                    // Final Approver
                    List<VW_APPROVAL_HIERARCHY_DETAIL> finalApp = vwApprovalHierarycyDtlRepo
                            .findByApphierIdAndPositionAndIsSubmitterAndIsFinal(appHierId, pos_, "N", "Y").get();

                    if (!finalApp.isEmpty()) {
                        hierarchyDetailDto.setApphierId(appHierId);
                        hierarchyDetailDto.setApprovalLevel("Final Approver");
                        hierarchyDetailDto.setPosition(pos_);
                        for (VW_APPROVAL_HIERARCHY_DETAIL appDtl_ : finalApp) {

                            HashMap<String, Object> ar = new HashMap<>();
                            ar.put("appHierId", appDtl_.getApphierId());
                            ar.put("employeeId", appDtl_.getEmployeeId());
                            ar.put("employeeName", appDtl_.getFullName());

                            detailList.add(ar);
                        }
                        log.info("Size: " + detailList.size());
                        hierarchyDetailDto.setEmployeeDetail(detailList);
                        dtos.add(hierarchyDetailDto);
                    }
                }
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, "Success get data", dtos);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            log.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("java:S3776")
    public ResponseEntity<ResponseObject> createTosSubmission(CreateTosSubmissionRequest request, HttpServletRequest httpServletRequest) {
        log.info("createTosSubmission : {}", request);

        if (ObjectUtils.isEmpty(request.getSaTosId()) || request.getSaTosId() <= 0)
            return new ResponseEntity<>(
                    ResponseObject.builder()
                            .success(ResponseUtils.SUCCESS_TRUE)
                            .code(HttpStatus.BAD_REQUEST.value())
                            .message("saTosId not valid")
                            .data(null)
                            .build(),
                    HttpStatus.BAD_REQUEST
            );

        if (request.getStartDate() == null)
            return new ResponseEntity<>(
                    ResponseObject.builder()
                            .success(ResponseUtils.SUCCESS_TRUE)
                            .code(HttpStatus.BAD_REQUEST.value())
                            .message("startDate can't be empty")
                            .data(null)
                            .build(),
                    HttpStatus.BAD_REQUEST
            );

        if (request.getEndDate() == null)
            return new ResponseEntity<>(
                    ResponseObject.builder()
                            .success(ResponseUtils.SUCCESS_TRUE)
                            .code(HttpStatus.BAD_REQUEST.value())
                            .message("endDate can't be empty")
                            .data(null)
                            .build(),
                    HttpStatus.BAD_REQUEST
            );

        if (CollectionUtils.isEmpty(request.getTosSubmissionDetailDtoList()))
            return new ResponseEntity<>(
                    ResponseObject.builder()
                            .success(ResponseUtils.SUCCESS_TRUE)
                            .code(HttpStatus.BAD_REQUEST.value())
                            .message("tosSubmissionDetailDtoList can't be empty")
                            .data(null)
                            .build(),
                    HttpStatus.BAD_REQUEST
            );

        if (ObjectUtils.isEmpty(request.getAppHierId()) || request.getAppHierId() <= 0)
            return new ResponseEntity<>(
                    ResponseObject.builder()
                            .success(ResponseUtils.SUCCESS_TRUE)
                            .code(HttpStatus.BAD_REQUEST.value())
                            .message("appHierId not valid")
                            .data(null)
                            .build(),
                    HttpStatus.BAD_REQUEST
            );

        try {
            List<String> statusIn = new ArrayList<>();
            statusIn.add(FlowStatus.APPROVED.name());
            statusIn.add("WAITING APPROVAL");
            if(request.getFlag()==2) {
                List<T_AM_TOS_SUBMISSION> dataTosActive = tAmTosSubmissionRepo.findAllBySaTosIdAndSaIdAndStatusApprovalIn(request.getSaId(), request.getSaTosId(), statusIn);
                SimpleDateFormat formatDate = new SimpleDateFormat(Constant.FORMAT_START_END_DATE);
                for(T_AM_TOS_SUBMISSION c : dataTosActive) {
                    if(isOverlapping(c.getStartDate(), c.getEndDate(), formatDate.parse(request.getStartDate()), formatDate.parse(request.getEndDate()))) {
                        return new ResponseEntity<>(
                                ResponseObject.builder()
                                        .success(ResponseUtils.SUCCESS_FALSE)
                                        .code(HttpStatus.BAD_REQUEST.value())
                                        .message(UtilsAccount.messageValidateExist(ConstantAccount.TOS_SA, (c.getStatusApproval().equalsIgnoreCase(ConstantAccount.APPROVED)? ConstantAccount.APPROVED : ConstantAccount.WAITING_APPROVAL)))
                                        .data(null)
                                        .build(),
                                HttpStatus.BAD_REQUEST
                        );
                    }
                }
            }

            String status = "";
            String statusApproval="";
            if (request.getFlag() == 1){
                status = FlowStatus.DRAFT.name();
                statusApproval = FlowStatus.DRAFT.name();
            }
            else if (request.getFlag() == 2) {
                status = ApprovalStatus.DRAFT.name();
                statusApproval = WAITING_APPROVAL;
            }

            T_AM_TOS_SUBMISSION tosSubmission = tAmTosSubmissionRepo.save(T_AM_TOS_SUBMISSION.builder()
                    .saTosId(request.getSaTosId())
                    .saId(request.getSaId())
                    .description(request.getDescription())
                    .startDate(CommonHelper.convertStringToDate(Constant.FORMAT_START_END_DATE, request.getStartDate()))
                    .endDate(CommonHelper.convertStringToDate(Constant.FORMAT_START_END_DATE, request.getEndDate()))
                    .appliedDate(CommonHelper.convertStringToDate(Constant.FORMAT_START_END_DATE, request.getAppliedDate()))
                    .appHierId(request.getAppHierId())
                    .status(status)
                    .statusApproval(statusApproval)
                    .createdDate(new Date())
                    .createdBy(UserDetailUtils.getUsername())
                    .build());
            log.info("tosSubmission : {}", tosSubmission);

            List<T_AM_TOS_SUBMISSION_DTL> tosSubmissionDtls = new ArrayList<>();
            int totalData = request.getTosSubmissionDetailDtoList().size();
            log.info("totalData : {}", totalData);

            if (!ObjectUtils.isEmpty(tosSubmission)) {
                int totalInsertDetails = 0;
                for (TosSubmissionDetailDto detailDto : request.getTosSubmissionDetailDtoList()) {
                    var tosSubmissionDtl = tAmTosSubmissionDetailRepo.save(T_AM_TOS_SUBMISSION_DTL.builder()
                            .tosSubmissionId(tosSubmission.getId())
                            .value(detailDto.getValue())
                            .attribute(detailDto.getAttribute())
                            .fromItem(detailDto.getFromItem())
                            .unit(detailDto.getUnit())
                            .createdDate(new Date())
                            .createdBy(UserDetailUtils.getUsername())
                            .build());
                    if (!ObjectUtils.isEmpty(tosSubmissionDtl)) {
                        tosSubmissionDtls.add(tosSubmissionDtl);
                        totalInsertDetails += 1;
                    }
                }

                log.info("totalInsertDetails : {}", totalInsertDetails);
                if (totalData != totalInsertDetails)
                    return new ResponseEntity<>(
                            ResponseObject.builder()
                                    .success(ResponseUtils.SUCCESS_FALSE)
                                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                    .message(ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR)
                                    .data(null)
                                    .build(),
                            HttpStatus.INTERNAL_SERVER_ERROR
                    );
            }

            if (request.getFlag() == 2) {
                // approval
                Integer tAppId = approvalServices.addApprovalFlowR(request.getAppHierId(),
                        ApprovalCategory.TOS_SUBMISSION.name(), tosSubmission.getId().toString(), request.getDescription());

                // Approval History
                approvalServices.setApprovalHistory(tAppId, tosSubmission.getId(), "Create new tos submission", ApprovalCategory.TOS_SUBMISSION.name(),
                        "SUBMIT", httpServletRequest);
            }

            TosSubmissionCreateResultDto data = TosSubmissionCreateResultDto.builder()
                    .id(tosSubmission.getId())
                    .saTosId(tosSubmission.getSaTosId())
                    .startDate(tosSubmission.getStartDate())
                    .endDate(tosSubmission.getEndDate())
                    .appliedDate(tosSubmission.getAppliedDate())
                    .description(tosSubmission.getDescription())
                    .tosSubmissionDtls(tosSubmissionDtls)
                    .build();

            return new ResponseEntity<>(
                    ResponseObject.builder()
                            .success(ResponseUtils.SUCCESS_TRUE)
                            .code(HttpStatus.OK.value())
                            .message("Success Create Tos Submission")
                            .data(data)
                            .build(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            log.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("java:S3776")
    public ResponseEntity<ResponseObject> dateCreateValidation(CreateTosSubmissionRequest request) {
        log.info("createTosSubmission : {}", request);

        if (ObjectUtils.isEmpty(request.getSaTosId()) || request.getSaTosId() <= 0)
            return new ResponseEntity<>(
                    ResponseObject.builder()
                            .success(ResponseUtils.SUCCESS_TRUE)
                            .code(HttpStatus.BAD_REQUEST.value())
                            .message("saTosId not valid")
                            .data(null)
                            .build(),
                    HttpStatus.BAD_REQUEST
            );

        if (request.getStartDate() == null)
            return new ResponseEntity<>(
                    ResponseObject.builder()
                            .success(ResponseUtils.SUCCESS_TRUE)
                            .code(HttpStatus.BAD_REQUEST.value())
                            .message("startDate can't be empty")
                            .data(null)
                            .build(),
                    HttpStatus.BAD_REQUEST
            );

        if (request.getEndDate() == null)
            return new ResponseEntity<>(
                    ResponseObject.builder()
                            .success(ResponseUtils.SUCCESS_TRUE)
                            .code(HttpStatus.BAD_REQUEST.value())
                            .message("endDate can't be empty")
                            .data(null)
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        try {
            List<String> statusIn = new ArrayList<>();
            statusIn.add(FlowStatus.APPROVED.name());
            statusIn.add("WAITING APPROVAL");
            List<T_AM_TOS_SUBMISSION> dataTosActive = tAmTosSubmissionRepo.findAllBySaTosIdAndSaIdAndStatusApprovalIn(request.getSaId(), request.getSaTosId(), statusIn);
            SimpleDateFormat formatDate = new SimpleDateFormat(Constant.FORMAT_START_END_DATE);
            for(T_AM_TOS_SUBMISSION c : dataTosActive) {
                if(isOverlapping(c.getStartDate(), c.getEndDate(), formatDate.parse(request.getStartDate()), formatDate.parse(request.getEndDate()))) {
                    return new ResponseEntity<>(
                            ResponseObject.builder()
                                    .success(ResponseUtils.SUCCESS_FALSE)
                                    .code(HttpStatus.BAD_REQUEST.value())
                                    .message(UtilsAccount.messageValidateExist(ConstantAccount.TOS_SA, (c.getStatusApproval().equalsIgnoreCase(ConstantAccount.APPROVED)? ConstantAccount.APPROVED : ConstantAccount.WAITING_APPROVAL)))
                                    .data(null)
                                    .build(),
                            HttpStatus.BAD_REQUEST
                    );
                }
            }

            return new ResponseEntity<>(
                    ResponseObject.builder()
                            .success(ResponseUtils.SUCCESS_TRUE)
                            .code(HttpStatus.OK.value())
                            .message("Success validate Create Tos Submission")
                            .data(null)
                            .build(),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            log.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> listTosSubmissionBySaId(MaterialTablePagingRequest pagingData,
                                                                PagedResourcesAssembler<MvTosSubmissionDTO> assembler,
                                                                Integer saId) {
        log.info("Get List Tos Submission by SA Id with Paging");
        try {
            Map<String, Object> filter = new HashMap<>();
            Page<VW_TOS_SUBMISSION> data = null;
            filter.put("saId", saId);

            if (isNotBlank(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key+"~"+value);
                }
            }

            if (!pagingData.getSearch().isEmpty()) {
                data = this.mvTosSubmissionRepo.findAll(
                        this.mvTosSubmissionRepo.getSpecificationFromFilters(pagingData, filter),
                        PagingUtils.getPaging(pagingData));
            } else {
                data = this.mvTosSubmissionRepo.findAll(this.mvTosSubmissionRepo.getSpecificationDefault(filter),
                        PagingUtils.getPaging(pagingData));
            }

            List<VW_TOS_SUBMISSION> tempData = data.getContent();
            List<MvTosSubmissionDTO> tempDtoList = new ArrayList<>();
            for(VW_TOS_SUBMISSION mts : tempData){
                MvTosSubmissionDTO newData = objectMapper.convertValue(mts, MvTosSubmissionDTO.class);
                if(StringUtils.hasValue(mts.getStartDate())){
                    newData.setStartDate(CommonHelper.convertDateToString(Constant.FORMAT_START_END_DATE,mts.getStartDate()));
                }
                if(StringUtils.hasValue(mts.getEndDate())){
                    newData.setEndDate(CommonHelper.convertDateToString(Constant.FORMAT_START_END_DATE,mts.getEndDate()));
                }
                if(StringUtils.hasValue(mts.getAppliedDate())){
                    newData.setAppliedDate(CommonHelper.convertDateToString(Constant.FORMAT_START_END_DATE,mts.getAppliedDate()));
                }
                tempDtoList.add(newData);
            }
            Page<MvTosSubmissionDTO> dataFinal = new PageImpl<>(tempDtoList);
            PagedModel<EntityModel<MvTosSubmissionDTO>> pagedDataFinal = assembler.toModel(dataFinal);
            Map<String, Object> d = new HashMap<>();
            d.put("result", pagedDataFinal.getContent());
            d.put("page", pagedDataFinal.getMetadata());
            d.put("links", pagedDataFinal.getLinks());
            ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success get List Tos Submission by SA Id with Paging", d);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional(rollbackFor = Exception.class, readOnly = false)
    public ResponseEntity<?> deleteTosSubmission(Integer tosSubmissionId, HttpServletRequest httpServletRequest) {
        log.info("delete Tos Submission Draft");
        ResponseObject result = new ResponseObject();
        try {
            Optional<T_AM_TOS_SUBMISSION> tosSubmissionOpt = tAmTosSubmissionRepo.findById(tosSubmissionId);
            if (tosSubmissionOpt.isEmpty()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK,
                        "Tos Submission with id " + tosSubmissionId + " is not exist", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }else {
                T_AM_TOS_SUBMISSION tosSubmission = tosSubmissionOpt.get();

                if(!tosSubmission.getStatus().equalsIgnoreCase("DRAFT")) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK,
                            "Tos Submission with id " + tosSubmissionId + " cannot be deleted because the status is not DRAFT", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }else {
                    // Delete Attachment
                    Optional<List<M_ATTACHMENT>> attachmentListOpt = mAttachmentRepo.findByReferenceIdAndCategoryIgnoreCase(tosSubmissionId,"TOS_SUBMISSION");
                    if(attachmentListOpt.isPresent()){
                        List<M_ATTACHMENT> attachmentList = attachmentListOpt.get();
                        for(M_ATTACHMENT ma: attachmentList){
                            ma.setIsDeleted(true);
                        }
                        mAttachmentRepo.saveAll(attachmentListOpt.get());
                    }
                    // Delete Approval
                    List<T_APPROVAL> approvalList = tApprovalRepo.findAllByIdTransAndCategoryIn(tosSubmissionId.toString(),Arrays.asList("TOS_SUBMISSION","INACTIVE_TOS_SUBMISSION"));
                    tApprovalRepo.deleteAll(approvalList);
                    // Delete Approval History
                    List<T_APPROVAL_HISTORY> approvalHistoryList = tApprovalHistoryRepo.findAllByRefIdAndCategory(tosSubmissionId,Arrays.asList("TOS_SUBMISSION","INACTIVE_TOS_SUBMISSION"));
                    tApprovalHistoryRepo.deleteAll(approvalHistoryList);
                    // Delete detail tos submission
                    List<T_AM_TOS_SUBMISSION_DTL> listDetail = tAmTosSubmissionDetailRepo.findAllByTosSubmissionId(tosSubmissionId);
                    tAmTosSubmissionDetailRepo.deleteAll(listDetail);
                    // Delete tos submission
                    tAmTosSubmissionRepo.delete(tosSubmission);
                    result.setSuccess(true);
                    result.setCode(HttpStatus.CREATED);
                    result.setMessage("Tos Submission Draft is successfully deleted!");
                    result.setData(null);

                    return new ResponseEntity<>(result, HttpStatus.OK);
                }
            }
        } catch (Exception e) {
           log.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    public ResponseEntity<ResponseObject> getApprovalHistory(Integer refId) {
        List<String> appCategory = new ArrayList<>();
        appCategory.add("TOS_SUBMISSION");
        appCategory.add("INACTIVE_TOS_SUBMISSION");

        var resp = approvalServices.getApprovalHistory(refId, ApprovalCategory.TOS_SUBMISSION.name(), appCategory);
        if (resp != null) {
            ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, "Success get data",
                    resp);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, "Data not found",
                ResponseUtils.DATA_EMPTY);
        log.info("Response Failed ");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @Transactional(rollbackFor = Exception.class, readOnly = false)
    public ResponseEntity<ResponseObject> inactivateTosSubmission(InactiveDTO request,
                                                                    HttpServletRequest httpServletRequest) {
        try {
            ResponseObject result;
            Optional<T_AM_TOS_SUBMISSION> tosSubmissionOptional = tAmTosSubmissionRepo
                    .findById(request.getTosSubmissionId());
            if (tosSubmissionOptional.isEmpty()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, "Id is not exist",
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            } else {
                if ("INACTIVE".equalsIgnoreCase(tosSubmissionOptional.get().getStatus())) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK,
                            "Tos Submission cannot be inactivated because the it is already inactivated", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
            }
            boolean isSubmiter = Objects.equals(tosSubmissionOptional.get().getCreatedBy(), UserDetailUtils.getUsername());
            if (!isSubmiter) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, "You are not Submitter",
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            Date newDate = new Date();
            T_AM_TOS_SUBMISSION tosSubmission = tosSubmissionOptional.get();
            if (WAITING_APPROVAL.equalsIgnoreCase(tosSubmission.getStatusApproval())
                    || ApprovalStatus.REJECTED.name().equalsIgnoreCase(tosSubmission.getStatus())) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK,
                        "Cannot inactive tos submission because status " + tosSubmission.getStatus(),
                        ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
            tosSubmission.setStatusApproval(WAITING_APPROVAL);
            tosSubmission.setUpdatedDate(newDate);
            tosSubmission.setUpdatedBy(UserDetailUtils.getUsername());
            tAmTosSubmissionRepo.save(tosSubmission);

            Integer tAppId = approvalServices.addApprovalFlowR(request.getAppHierId(),
                    ApprovalCategory.INACTIVE_TOS_SUBMISSION.name(),
                    tosSubmissionOptional.get().getId().toString(), request.getRemark());

            approvalServices.setApprovalHistory(tAppId, tosSubmission.getId(), request.getRemark(),
                    ApprovalCategory.TOS_SUBMISSION.name(), "SUBMIT", httpServletRequest);

            LinkedHashMap<String, Object> successTosResponse = new LinkedHashMap<>();
            successTosResponse.put("id", tosSubmission.getId());
            successTosResponse.put("description", tosSubmission.getDescription());
            successTosResponse.put("createdDate",
                    UtilsDate.dateToString(tosSubmission.getCreatedDate(), "dd-MM-yyyy"));
            successTosResponse.put("createdBy", tosSubmission.getCreatedBy());

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success request inactive tos submission", null);
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            log.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional(rollbackFor = IllegalArgumentException.class, readOnly = false)
    public ResponseEntity<ResponseObject> approveInactiveTosSubmission(ApprovalTosSubmissionDTO dto,
                                                                 HttpServletRequest httpServletRequest) {
        try {
            Optional<T_AM_TOS_SUBMISSION> tAmTosSubmissionOptional = tAmTosSubmissionRepo.findById(dto.getId());
            if (tAmTosSubmissionOptional.isPresent()) {
                T_AM_TOS_SUBMISSION tAmTosSubmission = tAmTosSubmissionOptional.get();
                boolean isForward = approvalServices.isForwardPosition(dto.getApprovalId());
                if (isForward) {
                  // APPROVAL HISTORY
                    approvalServices.setApprHistoryForward(dto.getApprovalId(), tAmTosSubmission.getId(), dto.getDescription(),
                        ApprovalCategory.TOS_SUBMISSION.name(), dto.getAction(), httpServletRequest);
                } else {
                   // APPROVAL HISTORY
                    approvalServices.setApprovalHistory(dto.getApprovalId(), tAmTosSubmission.getId(), dto.getDescription(),
                        ApprovalCategory.TOS_SUBMISSION.name(), dto.getAction(), httpServletRequest);
                }
                if (ApprovalStatus.REJECTED.name().equalsIgnoreCase(dto.getAction())) {
                    approvalServices.actionNextFlowApproval(dto.getApprovalId(), dto.getDescription(),
                            ApprovalStatus.REJECTED);
                    tAmTosSubmission.setUpdatedDate(new Date());
                    tAmTosSubmission.setUpdatedBy(UserDetailUtils.getUsername());
                    tAmTosSubmission.setStatusApproval(ApprovalStatus.REJECTED.name());
                } else {
                    boolean isFinal = approvalServices.actionNextFlowApproval(dto.getApprovalId(), dto.getDescription(),
                            ApprovalStatus.APPROVED);
                    if (isFinal) {
                        tAmTosSubmission.setUpdatedDate(new Date());
                        tAmTosSubmission.setUpdatedBy(UserDetailUtils.getUsername());
                        tAmTosSubmission.setStatusApproval(ApprovalStatus.APPROVED.name());
                        tAmTosSubmission.setStatus(FlowStatus.INACTIVE.name());
                    }
                }
                tAmTosSubmissionRepo.save(tAmTosSubmission);

                ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        ResponseUtils.MESSAGE_SUCCESS, null);

                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        "Id not existing", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

        } catch (Exception e) {
            log.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<ResponseObject> chooseTosWithPaging(MaterialTablePagingRequest pagingData,
                                                                PagedResourcesAssembler<ChooseTosSubmissionDTO> assembler,
                                                                Integer saId) {
        try {
            Map<String, Object> filter = new HashMap<>();
            filter.put("saId", saId == null ? 0 : saId);
            Specification<VW_TOS_CATALOG> specification = pagingData.getSearch().isEmpty()
                    ? mvTosCatalogRepo.getSpecificationDefault(filter)
                    : mvTosCatalogRepo.getSpecificationFromFilters(pagingData, filter);

            Page<VW_TOS_CATALOG> data = mvTosCatalogRepo.findAll(specification, PagingUtils.getPaging(pagingData));
            // Convert Page Process
            List<VW_TOS_CATALOG> tempData = data.getContent();
            List<ChooseTosSubmissionDTO> tempDtoList = new ArrayList<>();
            for(VW_TOS_CATALOG mts: tempData){
                ChooseTosSubmissionDTO newData = objectMapper.convertValue(mts, ChooseTosSubmissionDTO.class);
                var mvTosCatalogList = mvTosCatalogDtlRepo.findAllBySaTosId(mts.getId());
                newData.setSaTosDetail(mvTosCatalogList);
                tempDtoList.add(newData);
            }
            Page<ChooseTosSubmissionDTO> dataFinal = new PageImpl<>(tempDtoList);
            PagedModel<EntityModel<ChooseTosSubmissionDTO>> pagedDataFinal = assembler.toModel(dataFinal);

            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success get view paging", new PagingDTO(dataFinal.getContent(), pagedDataFinal.getMetadata(),
                    pagedDataFinal.getLinks())), HttpStatus.OK);
        } catch (Exception e) {
            log.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    @Transactional(rollbackFor = IllegalArgumentException.class, readOnly = false)
    public ResponseEntity<ResponseObject> approveTosSubmission(ApprovalTosSubmissionDTO dto, HttpServletRequest httpServletRequest)
            throws IllegalArgumentException, JSONException, JsonProcessingException {
        try {
            Optional<T_AM_TOS_SUBMISSION> tAmTosSubmissionOptional = tAmTosSubmissionRepo.findById(dto.getId());
            if (tAmTosSubmissionOptional.isPresent()) {
                T_AM_TOS_SUBMISSION tAmTosSubmission = tAmTosSubmissionOptional.get();
                boolean isForward = approvalServices.isForwardPosition(dto.getApprovalId());
                if (isForward) {
                  // Approval History
                    approvalServices.setApprHistoryForward(dto.getApprovalId(), tAmTosSubmission.getId(),
                        dto.getDescription(), ApprovalCategory.TOS_SUBMISSION.name(), dto.getAction(), httpServletRequest);

                } else {
                   // Approval History
                    approvalServices.setApprovalHistory(dto.getApprovalId(), tAmTosSubmission.getId(),
                        dto.getDescription(), ApprovalCategory.TOS_SUBMISSION.name(), dto.getAction(), httpServletRequest);

                }
                if (ApprovalStatus.REJECTED.name().equalsIgnoreCase(dto.getAction())) {
                    approvalServices.actionNextFlowApproval(dto.getApprovalId(), dto.getDescription(),
                            ApprovalStatus.REJECTED);
                    tAmTosSubmission.setStatusApproval(FlowStatus.REJECTED.name());
                } else {
                    boolean isFinal = approvalServices.actionNextFlowApproval(dto.getApprovalId(), dto.getDescription(),
                            ApprovalStatus.APPROVED);
                    if (isFinal) {
                        if (FlowStatus.ACTIVE.name().equalsIgnoreCase(tAmTosSubmission.getStatus())) {
                            Optional<List<M_ATTACHMENT>> attachOldOpt = mAttachmentRepo
                                    .findByReferenceIdAndIsDraftAndIsDeletedAndCategoryIgnoreCase(
                                            tAmTosSubmission.getId(), false, false, "TOS_SUBMISSION");

                            if (attachOldOpt.get().size() > 0) {
                                for (M_ATTACHMENT attach : attachOldOpt.get()) {
                                    attach.setIsDeleted(true);
                                    attach.setUpdatedBy(UserDetailUtils.getUsername());
                                    attach.setUpdatedDate(new Date());
                                    mAttachmentRepo.save(attach);
                                }
                            }

                            // UPDATE DRAFT ATTACHMENT

                            Optional<List<M_ATTACHMENT>> attachDraftOpt = mAttachmentRepo
                                    .findByReferenceIdAndIsDraftAndIsDeletedAndCategoryIgnoreCase(
                                            tAmTosSubmission.getId(), true, false, "TOS_SUBMISSION");

                            if (attachDraftOpt.get().size() > 0) {
                                for (M_ATTACHMENT attach2 : attachDraftOpt.get()) {
                                    attach2.setIsDraft(false);
                                    attach2.setUpdatedBy(UserDetailUtils.getUsername());
                                    attach2.setUpdatedDate(new Date());

                                    mAttachmentRepo.save(attach2);
                                }
                            }
                        }

                        tAmTosSubmission.setStatus(FlowStatus.ACTIVE.name());
                        tAmTosSubmission.setStatusApproval(ApprovalStatus.APPROVED.name());
                        tAmTosSubmission.setUpdatedDate(new Date());
                        tAmTosSubmission.setUpdatedBy(UserDetailUtils.getUsername());
                    }
                }
                tAmTosSubmissionRepo.save(tAmTosSubmission);
                
                ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, "Success get data",
                        null);
                log.info("Response Success ->" + result);
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
            else {
                ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        ResponseUtils.MESSAGE_NOT_FOUND, ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }
        } catch (Exception e) {
            log.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    public ResponseEntity<ResponseObject> uploadFile(Integer fileCategoryId, List<MultipartFile> files, Integer refId,
                                                     HttpServletRequest httpServletRequest) {
        ResponseObject result = new ResponseObject();
        try {

            Optional<T_AM_TOS_SUBMISSION> tAmTosSubmissionOptional = tAmTosSubmissionRepo.findById(refId);
            if (tAmTosSubmissionOptional.isEmpty()) {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK,
                        "Tos Submission with id : " + refId + " is not found!", ResponseUtils.DATA_EMPTY);
                return new ResponseEntity<>(result, result.getHttpCode());
            }

            for (MultipartFile file : files) {
                M_ATTACHMENT mAttachment = new M_ATTACHMENT();
                String generatedFileName = UserDetailUtils.generateFileName(file.getOriginalFilename());
                mAttachment.setCategory("TOS_SUBMISSION");
                mAttachment.setFileCategoryId(fileCategoryId);
                mAttachment.setReferenceId(refId);
                mAttachment.setType(file.getContentType());
                mAttachment.setCreatedBy(UserDetailUtils.getUsername());
                mAttachment.setCreatedDate(new Date());
                mAttachment.setPathFile(PATH_FILE);
                mAttachment.setFileName(generatedFileName);
                mAttachment.setFileSize(file.getSize());
                mAttachment.setIsDraft(tAmTosSubmissionOptional.get().getStatus().equalsIgnoreCase("ACTIVE"));
                mAttachment.setIsDeleted(Boolean.FALSE);
                mAttachmentRepo.save(mAttachment);

                R_GLOBAL_TYPE_VALUE rGlobalTypeValue = rGlobalTypeValueRepo
                        .findTopByGlbValueIgnoreCaseAndIsDeleted(PATH_FILE, false);
                String fullPath = rGlobalTypeValue.getName() + generatedFileName;

                String fullobject = "FILE" + fullPath;
                this.minioClient.putObject(this.configurationProperties.getBucket(), fullobject,
                        file.getInputStream(), file.getContentType());
            }

            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, "Success upload file",
                    ResponseUtils.DATA_EMPTY);
            log.info("Response Success ->" + result);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            log.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @SuppressWarnings({"java:S3776","java:S1192"})
    @Transactional(rollbackFor = IllegalArgumentException.class, readOnly = false)
    public ResponseEntity<ResponseObject> updateTosSubmission(CreateTosSubmissionRequest request,
                                                                  HttpServletRequest httpServletRequest) {
        ResponseObject result = new ResponseObject();
        try {
            Set<ConstraintViolation<CreateTosSubmissionRequest>> violations = this.validator.validate(request);
            List<Map<String, Object>> violationList = new ArrayList<>();

            if (!violations.isEmpty()) {
                for (ConstraintViolation<CreateTosSubmissionRequest> violation : violations) {
                    log.error(violation.getMessage());
                    Map<String, Object> datas = new HashMap<>();
                    datas.put(violation.getPropertyPath().toString(), violation.getMessage());
                    violationList.add(datas);
                }
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                        ResponseUtils.MESSAGE_BAD_REQUEST, violationList);
                return new ResponseEntity<>(result, result.getHttpCode());
            } else {
                Optional<T_AM_TOS_SUBMISSION> tosSubmissionOptional = tAmTosSubmissionRepo
                        .findById(request.getId());
                if (tosSubmissionOptional.isEmpty()) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, ResponseUtils.MESSAGE_NOT_FOUND,
                            ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                } else if ("ACTIVE".equalsIgnoreCase(tosSubmissionOptional.get().getStatus()) || WAITING_APPROVAL
                        .equalsIgnoreCase(tosSubmissionOptional.get().getStatus())
                        || "INACTIVE".equalsIgnoreCase(tosSubmissionOptional.get().getStatus())) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK,
                            "Tos Submission status must be DRAFT", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                boolean isSubmiter = Objects.equals(tosSubmissionOptional.get().getCreatedBy(), UserDetailUtils.getUsername());
                if (!isSubmiter) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK, CommonVariables.NOT_SUBMITTER,
                            ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }

                // check overlapping date
                if(request.getFlag()==2) {
                    List<String> statusIn = new ArrayList<>();
                    statusIn.add(FlowStatus.APPROVED.name());
                    statusIn.add("WAITING APPROVAL");
                    List<T_AM_TOS_SUBMISSION> dataTosActive = tAmTosSubmissionRepo.findAllBySaTosIdAndSaIdAndStatusApprovalIn(request.getSaId(), request.getSaTosId(), statusIn);
                    SimpleDateFormat formatDate = new SimpleDateFormat(Constant.FORMAT_START_END_DATE);
                    for(T_AM_TOS_SUBMISSION c : dataTosActive) {
                        if(isOverlapping(c.getStartDate(), c.getEndDate(), formatDate.parse(request.getStartDate()), formatDate.parse(request.getEndDate()))) {
                            return new ResponseEntity<>(
                                    ResponseObject.builder()
                                            .success(ResponseUtils.SUCCESS_FALSE)
                                            .code(HttpStatus.BAD_REQUEST.value())
                                            .message(UtilsAccount.messageValidateExist(ConstantAccount.TOS_SA, (c.getStatusApproval().equalsIgnoreCase(ConstantAccount.APPROVED)? ConstantAccount.APPROVED : ConstantAccount.WAITING_APPROVAL)))
                                            .data(null)
                                            .build(),
                                    HttpStatus.BAD_REQUEST
                            );
                        }
                    }
                }

                T_AM_TOS_SUBMISSION tAmTosSubmission = tosSubmissionOptional.get();

                // update tos submission
                tAmTosSubmission.setSaTosId(request.getSaTosId());
                tAmTosSubmission.setStartDate(CommonHelper.convertStringToDate(Constant.FORMAT_START_END_DATE, request.getStartDate()));
                tAmTosSubmission.setEndDate(CommonHelper.convertStringToDate(Constant.FORMAT_START_END_DATE, request.getEndDate()));
                tAmTosSubmission.setAppliedDate(CommonHelper.convertStringToDate(Constant.FORMAT_START_END_DATE, request.getAppliedDate()));
                tAmTosSubmission.setDescription(request.getDescription());
                tAmTosSubmission.setAppHierId(request.getAppHierId());
                tAmTosSubmission.setUpdatedDate(new Date());
                tAmTosSubmission.setUpdatedBy(UserDetailUtils.getUsername());
                tAmTosSubmission.setStatusApproval(ApprovalStatus.DRAFT.name());
                if (request.getFlag() == 1) {
                    tAmTosSubmission.setStatusApproval(ApprovalStatus.DRAFT.name());

                } else if (request.getFlag() == 2) {
                    tAmTosSubmission.setStatusApproval(WAITING_APPROVAL);

                    // Approval History
                    Integer tAppId = approvalServices.addApprovalFlowR(request.getAppHierId(),
                            ApprovalCategory.TOS_SUBMISSION.name(), tAmTosSubmission.getId().toString(),
                            null);

                    approvalServices.setApprovalHistory(tAppId, tAmTosSubmission.getId(),
                            "Update draft tos submission", ApprovalCategory.TOS_SUBMISSION.name(),
                            ApprovalStatus.SUBMIT.name(), httpServletRequest);
                }
                // update detail
                List<TosSubmissionDetailDto> detailList =  request.getTosSubmissionDetailDtoList();
                for(TosSubmissionDetailDto tdd : detailList){
                    if(tdd.getId()==null) {
                        List<T_AM_TOS_SUBMISSION_DTL> removeTosDetail = tAmTosSubmissionDetailRepo.findAllByTosSubmissionId(request.getId());
                        tAmTosSubmissionDetailRepo.deleteAll(removeTosDetail);
                        List<T_AM_TOS_SUBMISSION_DTL> tosSubmissionDtls = new ArrayList<>();
                        int totalData = request.getTosSubmissionDetailDtoList().size();
                        int totalInsertDetails = 0;
                        for (TosSubmissionDetailDto detailDto : request.getTosSubmissionDetailDtoList()) {
                            var tosSubmissionDtl = tAmTosSubmissionDetailRepo.save(T_AM_TOS_SUBMISSION_DTL.builder()
                                    .tosSubmissionId(tosSubmissionOptional.get().getId())
                                    .value(detailDto.getValue())
                                    .attribute(detailDto.getAttribute())
                                    .fromItem(detailDto.getFromItem())
                                    .unit(detailDto.getUnit())
                                    .createdDate(new Date())
                                    .createdBy(UserDetailUtils.getUsername())
                                    .build());
                            if (!ObjectUtils.isEmpty(tosSubmissionDtl)) {
                                tosSubmissionDtls.add(tosSubmissionDtl);
                                totalInsertDetails += 1;
                            }
                        }

                        log.info("totalInsertDetails : {}", totalInsertDetails);
                        if (totalData != totalInsertDetails)
                            return new ResponseEntity<>(
                                    ResponseObject.builder()
                                            .success(ResponseUtils.SUCCESS_FALSE)
                                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                            .message(ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR)
                                            .data(null)
                                            .build(),
                                    HttpStatus.INTERNAL_SERVER_ERROR
                            );
                    } else {
                        Optional<T_AM_TOS_SUBMISSION_DTL> tAmTosSubmissionDtlOptional = tAmTosSubmissionDetailRepo.findById(tdd.getId());
                        if(tAmTosSubmissionDtlOptional.isEmpty()){
                            result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.OK,
                                    "Tos Submission Detail with id " + tdd.getId() + " is not exist", ResponseUtils.DATA_EMPTY);
                            return new ResponseEntity<>(result, result.getHttpCode());
                        }else{
                            T_AM_TOS_SUBMISSION_DTL tAmTosSubmissionDtl = tAmTosSubmissionDtlOptional.get();
                            tAmTosSubmissionDtl.setTosSubmissionId(request.getId());
                            tAmTosSubmissionDtl.setAttribute(tdd.getAttribute());
                            tAmTosSubmissionDtl.setValue(tdd.getValue());
                            tAmTosSubmissionDtl.setUnit(tdd.getUnit());
                            tAmTosSubmissionDtl.setFromItem(tdd.getFromItem());
                            tAmTosSubmissionDtl.setUpdatedBy(UserDetailUtils.getUsername());
                            tAmTosSubmissionDtl.setUpdatedDate(new Date());

                            tAmTosSubmissionDetailRepo.save(tAmTosSubmissionDtl);
                        }
                    }
                }
                tAmTosSubmissionRepo.save(tAmTosSubmission);

                result.setSuccess(true);
                result.setCode(HttpStatus.OK);
                result.setMessage("Success update Tos Submission");
                result.setData(tAmTosSubmission);

            }
            return new ResponseEntity<>(result, result.getHttpCode());

        } catch (Exception e) {
            log.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static boolean isOverlapping(Date start1, Date end1, Date start2, Date end2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.FORMAT_START_END_DATE);
            String startt1 = sdf.format(start1);
            String endd1 = sdf.format(end1);
            String startt2 = sdf.format(start2);
            String endd2 = sdf.format(end2);
            return (start1.before(end2) || startt1.equals(endd2)) && (end1.after(start2) || startt2.equals(endd1));

        } catch (Exception e) {
            log.error(Constant.LOG_ERROR, e.getMessage(), e);
            return false;
        }
    }
}

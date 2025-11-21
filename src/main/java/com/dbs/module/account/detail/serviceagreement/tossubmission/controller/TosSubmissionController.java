package com.dbs.module.account.detail.serviceagreement.tossubmission.controller;

import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.CreateTosSubmissionRequest;
import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.ApprovalTosSubmissionDTO;
import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.InactiveDTO;
import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.ChooseTosSubmissionDTO;
import com.dbs.module.account.detail.serviceagreement.tossubmission.dto.MvTosSubmissionDTO;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.database.crm.entities.accountmanagement.T_AM_TOS_SUBMISSION;
import com.dbs.database.crm.entities.usermanagement.M_ATTACHMENT;
import com.dbs.database.crm.repositories.usermanagement.MAttachmentRepo;
import com.dbs.module.account.detail.serviceagreement.tossubmission.service.TosSubmissionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.client.util.IOUtils;
import com.jlefebure.spring.boot.minio.MinioConfigurationProperties;
import com.jlefebure.spring.boot.minio.MinioException;
import com.unboundid.util.json.JSONException;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/v1/dbs/api/tossubmission")
@Api(tags = "Tos Submission")
@RequiredArgsConstructor
public class TosSubmissionController {

    private final TosSubmissionService service;
    @Autowired
    MAttachmentRepo mAttachmentRepo;
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinioConfigurationProperties configurationProperties;

    @GetMapping("/get-list")
    public ResponseEntity<?> getTosSubmissionListWithPaging(
            @Valid MaterialTablePagingRequest pagingRequest,
            PagedResourcesAssembler<T_AM_TOS_SUBMISSION> assembler) {
        return service.getTosSubmissionListWithPaging(pagingRequest, assembler);
    }

    @GetMapping("/get-detail/{tosSubmissionId}")
    public ResponseEntity<?> getTosSubmissionDetail(@PathVariable Integer tosSubmissionId,HttpServletRequest httpServletRequest) {
        return service.getTosSubmissionDetail(tosSubmissionId,httpServletRequest);
    }

    @GetMapping("/list-category")
    public ResponseEntity<?> getListCategory() {
        return service.listCategory();
    }

    @GetMapping("/get-list-approval-hierarchies")
    public ResponseEntity<?> getAllApprovalHeaderList(HttpServletRequest httpServletRequest) {
        return service.getAllApprovalHeaderList(httpServletRequest);
    }

    @GetMapping("/get-approval-hierarchies/{id}")
    public ResponseEntity<?> getApprovalHeaderById(@PathVariable Integer id) {
        return service.getApprovalHeaderById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTosSubmission(@RequestBody CreateTosSubmissionRequest request, HttpServletRequest httpServletRequest) {
        return service.createTosSubmission(request, httpServletRequest);
    }
    @PostMapping("/date-validation")
    public ResponseEntity<?> dateCreateValidation(@RequestBody CreateTosSubmissionRequest request) {
        return service.dateCreateValidation(request);
    }
    @GetMapping("/list-tos-submission/{saId}")
    public ResponseEntity<?> listTosSubmissionBySaId(@Valid MaterialTablePagingRequest pagingData,
                                               PagedResourcesAssembler<MvTosSubmissionDTO> assembler,
                                               @PathVariable Integer saId) {
        return service.listTosSubmissionBySaId(pagingData, assembler, saId);
    }
    @DeleteMapping("/delete-tos-submission/{tosSubmissionId}")
    public ResponseEntity<?> deleteTosSubmission(@PathVariable Integer tosSubmissionId, HttpServletRequest httpServletRequest) {
        return service.deleteTosSubmission(tosSubmissionId, httpServletRequest);
    }
    @GetMapping("/approval-history/{refId}")
    public ResponseEntity<?> listApprovalHistory(@PathVariable Integer refId) {
        return service.getApprovalHistory(refId);
    }
    @PostMapping(path = "/inactivate")
    public ResponseEntity<?> inactivateTosSubmission(@RequestBody InactiveDTO request,
                                                         HttpServletRequest httpServletRequest) {
        return service.inactivateTosSubmission(request, httpServletRequest);
    }
    @PostMapping(path = "/approve-inactive-tos-submission")
    public ResponseEntity<ResponseObject> approveInactiveTosSubmission(@RequestBody ApprovalTosSubmissionDTO request, HttpServletRequest httpServletRequest) {
        return service.approveInactiveTosSubmission(request, httpServletRequest);
    }
    @GetMapping("/choose-tos-submission/{saId}")
    public ResponseEntity<?> chooseTosWithPaging(@Valid MaterialTablePagingRequest pagingRequest,
                                                 PagedResourcesAssembler<ChooseTosSubmissionDTO> assembler, @PathVariable Integer saId) {
        return service.chooseTosWithPaging(pagingRequest, assembler,saId);
    }
    @PostMapping(path = "/approve-tos-submission")
    public ResponseEntity<?> approveTosSubmission(@RequestBody ApprovalTosSubmissionDTO request, HttpServletRequest httpServletRequest) throws IllegalArgumentException, JSONException {
        try {
            return service.approveTosSubmission(request, httpServletRequest);
        } catch (JsonProcessingException | IllegalArgumentException | JSONException e) {
            e.printStackTrace();
        }
        ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "request failed",
                ResponseUtils.DATA_EMPTY);
        return new ResponseEntity<>(result, result.getHttpCode());
    }
    @PostMapping("/uploadAttachment/{id}")
    public ResponseEntity<?> uploadAttachment(@RequestParam Integer category, @RequestPart List<MultipartFile> files,
                                              @PathVariable Integer id, HttpServletRequest httpServletRequest) {
        return service.uploadFile(category, files, id, httpServletRequest);
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
    @PostMapping("/update")
    public ResponseEntity<?> updateTosSubmission(@RequestBody CreateTosSubmissionRequest request, HttpServletRequest httpServletRequest) {
        return service.updateTosSubmission(request, httpServletRequest);
    }
}

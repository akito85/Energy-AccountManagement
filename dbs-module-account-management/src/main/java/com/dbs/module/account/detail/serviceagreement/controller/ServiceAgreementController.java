package com.dbs.module.account.detail.serviceagreement.controller;

import com.dbs.database.crm.entities.accountmanagement.VW_SA;
import com.dbs.module.account.detail.serviceagreement.dto.SaCreateDTO;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.database.crm.entities.accountmanagement.T_AM_SA;
import com.dbs.database.crm.entities.usermanagement.M_ATTACHMENT;
import com.dbs.database.crm.repositories.usermanagement.MAttachmentRepo;
import com.dbs.module.account.detail.serviceagreement.dto.helper.ApprovalSaDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.InactiveSaDTO;
import com.dbs.module.account.detail.serviceagreement.dto.helper.ValidationCreateSaDTO;
import com.dbs.module.account.detail.serviceagreement.service.SAService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.client.util.IOUtils;
import com.jlefebure.spring.boot.minio.MinioConfigurationProperties;
import com.unboundid.util.json.JSONException;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/v1/dbs/api/sa")
@Api(tags = "Service Agreement")
public class ServiceAgreementController {

    private final SAService saService;

    @Autowired
    private final MAttachmentRepo mAttachmentRepo;

    @Autowired
    private final MinioConfigurationProperties configurationProperties;

    @Autowired
    private final MinioClient minioClient;

    public ServiceAgreementController(SAService saService, MAttachmentRepo mAttachmentRepo,
                                      MinioClient minioClient, MinioConfigurationProperties configurationProperties) {
        this.saService = saService;
        this.mAttachmentRepo = mAttachmentRepo;
        this.minioClient = minioClient;
        this.configurationProperties = configurationProperties;
    }
    
    @GetMapping("/view/{accountId}")
    public ResponseEntity<?> getListViewServiceAgreement(
            @Valid MaterialTablePagingRequest pagingData,
            PagedResourcesAssembler<VW_SA> assembler,
            @PathVariable Integer accountId
    ) {
        return saService.serviceAgreementView(pagingData, assembler, accountId);
    }

    @GetMapping("/view/detail/{saId}")
    public ResponseEntity<ResponseObject> getListViewDetailServiceAgreement(@PathVariable Integer saId, HttpServletRequest httpServletRequest) {
        return saService.serviceAgreementViewDetail(saId, httpServletRequest);
    }

    @GetMapping("/view/detail/draft/{saId}")
    public ResponseEntity<ResponseObject> getListViewDetailDraftSa(@PathVariable Integer saId) {
        return saService.serviceAgreementViewDetailDraft(saId);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createSaMain(@RequestBody SaCreateDTO request, HttpServletRequest httpServletRequest) {
        return saService.serviceAgreementCreate(request, httpServletRequest);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateSa(@RequestBody Map<String,Object> data, HttpServletRequest httpServletRequest) {
        return saService.updateServiceAgreement(data, httpServletRequest);
    }

    @PutMapping("/inactive")
    public ResponseEntity<ResponseObject> inactiveSa(@RequestBody InactiveSaDTO requestDTO, HttpServletRequest httpServletRequest) {
        return saService.inactiveServiceAgreement(requestDTO, httpServletRequest);
    }

    @DeleteMapping("/delete/{saId}")
    public ResponseEntity<ResponseObject> deleteDraft(@PathVariable Integer saId) {
        return saService.deleteSaDraft(saId);
    }

    @GetMapping("/get-list-approval-hierarchies")
    public ResponseEntity<?> getAllApprovalHeaderList(HttpServletRequest httpServletRequest) {
        return saService.getAllApprovalHeaderList(httpServletRequest);
    }

    @GetMapping("/get-approval-hierarchies/{id}")
    public ResponseEntity<?> getApprovalHeaderById(@PathVariable Integer id) {
        return saService.getApprovalHeaderById(id);
    }

    @PostMapping(path = "/approve")
    public ResponseEntity<?> approveServiceAgreement(@RequestBody ApprovalSaDTO request, HttpServletRequest httpServletRequest) throws IllegalArgumentException, JSONException, JsonProcessingException {
        return saService.approveSa(request, httpServletRequest);
    }

    @PostMapping(path = "/approveInactive")
    public ResponseEntity<?> approveInactivePricing(@RequestBody ApprovalSaDTO request, HttpServletRequest httpServletRequest) {
        return saService.approveInactive(request, httpServletRequest);
    }

    @PostMapping(path = "/uploadAttachment/{saId}", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> uploadAttachment(@RequestParam Integer category, Boolean isUpdate, @RequestPart List<MultipartFile> files,
                                              @PathVariable Integer saId, HttpServletRequest httpServletRequest) {
        return saService.uploadFile(category, isUpdate, files, saId);
    }

    @GetMapping("/download/{fileId}")
    public void downloadFile2(@PathVariable("fileId") Integer fileId, HttpServletResponse response)
            throws IOException, InvalidArgumentException, InvalidBucketNameException,
            InsufficientDataException, XmlPullParserException, ErrorResponseException, NoSuchAlgorithmException,
            NoResponseException, InvalidKeyException, InternalException, InvalidResponseException {
        Optional<M_ATTACHMENT> vwFileOpt = mAttachmentRepo.findById(fileId);
        if(vwFileOpt.isPresent()) {
            M_ATTACHMENT vwFile = vwFileOpt.get();

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

    @GetMapping("/get-list-approval-history/{refId}")
    public ResponseEntity<?> listApprovalHistory(@PathVariable Integer refId) {
        return saService.getApprovalHistory(refId);
    }

    @PostMapping("/checkValidateCreateSa")
    public ResponseEntity<?> checkGasSourceAndServicePoint(@RequestBody ValidationCreateSaDTO request) {
        return saService.checkServicePointAndGasSource(request);
    }
}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbs.common.library.services;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.library.entities.TApprovalObject;
import com.dbs.common.library.utils.*;
import com.dbs.common.library.utils.dto.ApprovalHistoryApproverDto;
import com.dbs.common.library.utils.dto.ApprovalHistoryDataDto;
import com.dbs.common.library.utils.dto.EmployeeDetailDto;
import com.dbs.database.crm.entities.product.T_APPROVAL_HISTORY;
import com.dbs.database.crm.entities.product.VW_APPROVAL_HIERARCHY_DETAIL;
import com.dbs.database.crm.entities.usermanagement.M_APPROVAL_HIERARCHY;
import com.dbs.database.crm.entities.usermanagement.M_APPROVAL_HIERARCHY_DTL;
import com.dbs.database.crm.entities.usermanagement.M_POSITION;
import com.dbs.database.crm.entities.usermanagement.M_USER;
import com.dbs.database.crm.entities.usermanagement.T_APPROVAL;
import com.dbs.database.crm.entities.usermanagement.T_APPROVAL_DTL;
import com.dbs.database.crm.entities.usermanagement.view.VW_HIER_FLOW;
import com.dbs.database.crm.repositories.product.TApprovalHistoryRepo;
import com.dbs.database.crm.repositories.product.VWApprovalHierarchyDtlRepo;
import com.dbs.database.crm.repositories.usermanagement.MApprovalHierarchyDtlRepo;
import com.dbs.database.crm.repositories.usermanagement.MApprovalHierarchyRepo;
import com.dbs.database.crm.repositories.usermanagement.MPositionRepo;
import com.dbs.database.crm.repositories.usermanagement.MUserRepo;
import com.dbs.database.crm.repositories.usermanagement.TApprovalDtlRepo;
import com.dbs.database.crm.repositories.usermanagement.TApprovalRepo;
import com.dbs.database.crm.repositories.usermanagement.view.VWHierFlowRepo;

import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author RachmatY
 */
@Component
@SuppressWarnings("java:S6813")
public class ApprovalServices {

    private static final Logger logger = LoggerFactory.getLogger(ApprovalServices.class);

    @Autowired
    private TApprovalRepo tApprovalRepo;

    @Autowired
    private VWHierFlowRepo vwHierFlowRepo;

    @Autowired
    private MApprovalHierarchyRepo mApprovalHierRepo;

    @Autowired
    private TApprovalDtlRepo tApprovalDtlRepo;

    @Autowired
    private MUserRepo mUserRepo;

    @Autowired
    private VWApprovalHierarchyDtlRepo vwApprovalHierarchyDtlRepo;

    @Autowired
    private TApprovalHistoryRepo tApprovalHistoryRepo;

    @Autowired
    private MApprovalHierarchyDtlRepo mApprovalHierDtlRepo;
    
    @Autowired
    private MPositionRepo mPositionRepo;

    public T_APPROVAL registerTransactionApproval(HttpServletRequest request, TApprovalObject approvalObj) {
        String username = UserDetailUtils.getUsername();
        Date date = new Date();
        T_APPROVAL app = new T_APPROVAL();
        app.setCategory(approvalObj.getCategory());
        app.setApprovalType(approvalObj.getApprovalType().name());
        app.setDescription(approvalObj.getDescription());
        app.setEntityId(UserDetailUtils.getUserEntity());
        app.setIdTrans(approvalObj.getIdTrans());
        app.setStatus(ApprovalStatus.WAITING_FOR_APPROVAL.name());
        app.setCreatedBy(username); // SUBMITER
        app.setCreatedDate(date);
        app.setUpdatedBy(username);
        app.setUpdatedDate(date);
        app.setSubmitterId(UserDetailUtils.getUserId());
        app.setSubmitterPositionId(UserDetailUtils.getPositionFromToken(request));
        app.setTriggerJson(approvalObj.getJsonString().trim());
        tApprovalRepo.save(app);
        startApprovalFlow(app.getTAppId(), approvalObj.getAppHierId());
        return app;
    }

    public Boolean checkExistingApproval(TApprovalObject approvalObj) {
        Optional<T_APPROVAL> check = tApprovalRepo.findFirstByIdTransAndApprovalTypeAndStatus(approvalObj.getIdTrans(),
                approvalObj.getApprovalType().name(), ApprovalStatus.WAITING_FOR_APPROVAL.name());

        return check.isEmpty();
    }

    @SuppressWarnings({"java:S3655", "java:S2201"})
    public T_APPROVAL getApprovalDetail(Integer approvalId) {
        Optional<T_APPROVAL> opt2 = tApprovalRepo.findById(approvalId);
        T_APPROVAL tAppHeader = opt2.get();
        tAppHeader.getTApprovalDtl().stream().sorted(Comparator.comparingInt(T_APPROVAL_DTL::getApprovalLevel))
                .collect(Collectors.toList());
        return tAppHeader;
    }

    @SuppressWarnings("java:S3655")
    public List<M_APPROVAL_HIERARCHY> getApprovalFlowList(HttpServletRequest request, String appCode) {
        Integer submitterPositionId = UserDetailUtils.getPositionFromToken(request);

        List<Integer> appHierList = new ArrayList<>();
        Optional<List<VW_HIER_FLOW>> opt = vwHierFlowRepo.findAllByPositionIdAndIsSubmitter(submitterPositionId, true);
        List<VW_HIER_FLOW> availableFlow = opt.get();
        availableFlow.forEach(a -> appHierList.add(a.getAppHierId()));

        Optional<List<M_APPROVAL_HIERARCHY>> opt3 = mApprovalHierRepo.findAllByAppHierIdInAndApprovalType(appHierList,
                appCode);

        return opt3.get();
    }

    @SuppressWarnings("java:S3655")
    public T_APPROVAL startApprovalFlow(Integer approvalId, Integer approvalHierId) {
        String username = UserDetailUtils.getUsername();
        Optional<M_APPROVAL_HIERARCHY> opt = mApprovalHierRepo.findById(approvalHierId);
        M_APPROVAL_HIERARCHY apphier = opt.get();

        Optional<T_APPROVAL> opt2 = tApprovalRepo.findById(approvalId);
        T_APPROVAL tAppHeader = opt2.get();

        Integer tApprovalId = tAppHeader.getTAppId();
        Date now = new Date();

        // Copy Flow
        List<M_APPROVAL_HIERARCHY_DTL> flow = apphier.getMApprovalHierarchyDtl().stream()
                .sorted(Comparator.comparingInt(M_APPROVAL_HIERARCHY_DTL::getApprovalLevel))
                .collect(Collectors.toList());

        List<T_APPROVAL_DTL> flowCopy = new ArrayList<>();
        flow.stream().filter(a -> a.getStatus().equals(FlowStatus.ACTIVE.name())).forEach(dt -> {
            T_APPROVAL_DTL item = new T_APPROVAL_DTL(tApprovalId, dt.getApprovalLevel(), dt.getPositionId(),
                    dt.getIsFinal(), dt.getIsSubmitter(), ApprovalStatus.PENDING.name());
            item.setCreatedBy(username);
            item.setCreatedDate(now);
            item.setUpdatedBy(username);
            item.setUpdatedDate(now);
            flowCopy.add(item);
        });

        // Fill Submitter
        T_APPROVAL_DTL submitter = flowCopy.stream().filter(a -> a.getIsSubmitter().equals(true)).findFirst().get();
        if (Objects.equals(submitter.getPositionId(), tAppHeader.getSubmitterPositionId())) {
            submitter.setStatus(ApprovalStatus.SUBMIT.name());
            submitter.setUserId(tAppHeader.getSubmitterId());
            submitter.setCreatedBy(tAppHeader.getCreatedBy());
            submitter.setCreatedDate(tAppHeader.getCreatedDate());
            submitter.setUpdatedBy(tAppHeader.getUpdatedBy());
            submitter.setUpdatedDate(tAppHeader.getUpdatedDate());
        }
        tApprovalDtlRepo.saveAll(flowCopy);

        tAppHeader.setUpdatedDate(now);
        tAppHeader.setUpdatedBy(UserDetailUtils.getUsername());
        tAppHeader.setAppHierId(approvalHierId);
        tAppHeader.setApprovalName(apphier.getApprovalName());
        tAppHeader.setStatus(ApprovalStatus.WAITING_FOR_APPROVAL.name());
        tApprovalRepo.save(tAppHeader);

        opt2 = tApprovalRepo.findById(approvalId);
        tAppHeader = opt2.get();
        return tAppHeader;
    }

    @SuppressWarnings("java:S3655")
    public T_APPROVAL actionApprovalFlow(HttpServletRequest request, Integer approvalId, String description,
                                         ApprovalStatus type) {
        String status = "";
        String headerStatus = "";
        switch (type) {
            case APPROVE:
                status = ApprovalStatus.APPROVE.name();
                headerStatus = ApprovalStatus.APPROVED.name();
                break;
            case REJECT:
                status = ApprovalStatus.REJECT.name();
                headerStatus = ApprovalStatus.REJECTED.name();
                break;
            default:
                break;
        }

        Optional<T_APPROVAL> opt2 = tApprovalRepo.findById(approvalId);
        T_APPROVAL tAppHeader = opt2.get();

        List<T_APPROVAL_DTL> flow = tAppHeader.getTApprovalDtl();
        Integer positionId = UserDetailUtils.getPositionFromToken(request);

        // Variable Used in Lambda **
        String finalStatus = status;
        // **

        // CHECK IF FINAL
        Optional<T_APPROVAL_DTL> currFlowOpt = flow.stream().filter(
                        a -> a.getPositionId().equals(positionId) && !a.getStatus().equals(ApprovalStatus.PASSED.name()))
                .findFirst();
        T_APPROVAL_DTL currFlow;
        Boolean isFinal = false;
        if (currFlowOpt.isPresent()) {
            currFlow = currFlowOpt.get();
            isFinal = currFlow.getIsFinal();
        }

        // UPDATE APPROVAL FLOW
        flow.stream().filter(
                        a -> a.getPositionId().equals(positionId) && !a.getStatus().equals(ApprovalStatus.PASSED.name()))
                .findFirst().ifPresent(a -> {
                    a.setUserId(UserDetailUtils.getUserId());
                    a.setDescription(description);
                    a.setUpdatedBy(UserDetailUtils.getUsername());
                    a.setUpdatedDate(new Date());
                    a.setStatus(finalStatus);
                });

        tApprovalDtlRepo.saveAll(flow);

        if (Boolean.TRUE == isFinal) {
            tAppHeader.setStatus(headerStatus);
        }
        tAppHeader.setUpdatedDate(new Date());
        tAppHeader.setUpdatedBy(UserDetailUtils.getUsername());
        tApprovalRepo.save(tAppHeader);

        opt2 = tApprovalRepo.findById(approvalId);
        tAppHeader = opt2.get();
        return tAppHeader;
    }

    public Boolean checkIsSubmitter(HttpServletRequest request, Integer apphierId) {
        Integer submitterPositionId = UserDetailUtils.getPositionFromToken(request);

        Optional<VW_HIER_FLOW> opt = vwHierFlowRepo.findByAppHierIdAndPositionIdAndIsSubmitter(apphierId,
                submitterPositionId, true);
        return opt.isPresent();
    }

    public void addApprovalFlow(Integer approvalHierId, String appCat, String refId) {
        Optional<M_APPROVAL_HIERARCHY> opt = mApprovalHierRepo.findById(approvalHierId);
        if (opt.isPresent()) {
            M_APPROVAL_HIERARCHY apphier = opt.get();
            // subtract level from submitter to approval
            int levelApp = 2;
            if ((apphier.getMApprovalHierarchyDtl().size() - 1) <= 1)
                levelApp = apphier.getMApprovalHierarchyDtl().size();


            int finalLevelApp = levelApp;
            Optional<M_APPROVAL_HIERARCHY_DTL> apphierDtlOpt = apphier.getMApprovalHierarchyDtl().stream()
                    .filter(a -> a.getApprovalLevel().equals(finalLevelApp)).findFirst();
            if (apphierDtlOpt.isPresent()) {
                M_APPROVAL_HIERARCHY_DTL apphierDtl = apphierDtlOpt.get();
                Date now = new Date();
                T_APPROVAL tAppHeader = new T_APPROVAL();
                tAppHeader.setCreatedDate(now);
                tAppHeader.setIdTrans(refId);
                tAppHeader.setCategory(appCat);
                tAppHeader.setCreatedBy(UserDetailUtils.getUsername());
                tAppHeader.setAppHierId(approvalHierId);
                tAppHeader.setAppHierDtlId(apphierDtl.getAppHierDtlId());
                tAppHeader.setApprovalLevel(apphierDtl.getApprovalLevel());
                tAppHeader.setApprovalName(apphier.getApprovalName());
                tAppHeader.setApprovalType(apphier.getApprovalType());
                tAppHeader.setDescription(apphier.getDesc());
                tAppHeader.setEntityId(apphier.getEntityId());
                tAppHeader.setStatus(ApprovalStatus.WAITING_FOR_APPROVAL.name());
                tApprovalRepo.save(tAppHeader);
            }
        }
    }

    @SuppressWarnings("java:S3655")
    public void addFlowApproval(Integer approvalHierId, String appCat, String refId, String json) {
        Optional<M_APPROVAL_HIERARCHY> opt = mApprovalHierRepo.findById(approvalHierId);
        M_APPROVAL_HIERARCHY apphier = opt.get();
        // subtract level from submitter to approval
        Integer levelApp = apphier.getMApprovalHierarchyDtl().size() - 1;

        M_APPROVAL_HIERARCHY_DTL apphierDtl = apphier.getMApprovalHierarchyDtl().stream()
                .filter(a -> a.getApprovalLevel().equals(levelApp)).findFirst().get();

        Date now = new Date();
        T_APPROVAL tAppHeader = new T_APPROVAL();
        tAppHeader.setCreatedDate(now);
        tAppHeader.setIdTrans(refId);
        tAppHeader.setCategory(appCat);
        tAppHeader.setCreatedBy(UserDetailUtils.getUsername());
        tAppHeader.setAppHierId(approvalHierId);
        tAppHeader.setAppHierDtlId(apphierDtl.getAppHierDtlId());
        tAppHeader.setApprovalLevel(apphierDtl.getApprovalLevel());
        tAppHeader.setApprovalName(apphier.getApprovalName());
        tAppHeader.setApprovalType(apphier.getApprovalType());
        tAppHeader.setDescription(apphier.getDesc());
        tAppHeader.setEntityId(apphier.getEntityId());
        tAppHeader.setStatus(ApprovalStatus.WAITING_FOR_APPROVAL.name());
        tAppHeader.setTriggerJson(json);
        tApprovalRepo.save(tAppHeader);
    }

    @SuppressWarnings("java:S3655")
    public Integer addApprovalFlowR(Integer approvalHierId, String appCat, String refId, String desc) {
        // Return generated Approval ID
        Optional<M_APPROVAL_HIERARCHY> opt = mApprovalHierRepo.findById(approvalHierId);
        M_APPROVAL_HIERARCHY apphier = opt.get();
        // subtract level from submitter to approval
        int levelApp;
        if ((apphier.getMApprovalHierarchyDtl().size() - 1) <= 1)
            levelApp = apphier.getMApprovalHierarchyDtl().size();
        else {
            levelApp = 2;
        }

        M_APPROVAL_HIERARCHY_DTL apphierDtl = apphier.getMApprovalHierarchyDtl().stream()
                .filter(a -> a.getApprovalLevel().equals(levelApp)).findFirst().get();

        Date now = new Date();
        T_APPROVAL tAppHeader = new T_APPROVAL();
        tAppHeader.setCreatedDate(now);
        tAppHeader.setIdTrans(refId);
        tAppHeader.setCategory(appCat);
        tAppHeader.setCreatedBy(UserDetailUtils.getUsername());
        tAppHeader.setAppHierId(approvalHierId);
        tAppHeader.setAppHierDtlId(apphierDtl.getAppHierDtlId());
        tAppHeader.setApprovalLevel(apphierDtl.getApprovalLevel());
        tAppHeader.setApprovalName(apphier.getApprovalName());
        tAppHeader.setApprovalType(apphier.getApprovalType());
        tAppHeader.setDescription(desc == null ? apphier.getDesc() : desc);
        tAppHeader.setEntityId(apphier.getEntityId());
        tAppHeader.setStatus(ApprovalStatus.WAITING_FOR_APPROVAL.name());
        tApprovalRepo.save(tAppHeader);

        return tAppHeader.getTAppId();

    }

    public Integer addApprovalFlowRForUserDelegation(String refId, String appCat, String description){
        String username = UserDetailUtils.getUsername();
        Integer userEntity = UserDetailUtils.getUserEntity();
        T_APPROVAL tApproval = new T_APPROVAL();
        tApproval.setIdTrans(refId);
        tApproval.setCategory(appCat);
        tApproval.setCreatedBy(username);
        tApproval.setDescription(description);
        tApproval.setEntityId(userEntity);
        tApproval.setStatus(ApprovalStatus.WAITING_FOR_APPROVAL.name());
        T_APPROVAL saveTApproval = tApprovalRepo.save(tApproval);

        return saveTApproval.getTAppId();
    }

    @SuppressWarnings("java:S3655")
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean actionNextFlowApproval(Integer approvalId, String description, ApprovalStatus type) {
        String status = "";
        switch (type) {
            case APPROVE:
                status = ApprovalStatus.APPROVED.name();
                break;
            case REJECT:
                status = ApprovalStatus.REJECTED.name();
                break;
            default:
                break;
        }

        boolean isFinal = false;
        Optional<T_APPROVAL> tApprovalOpt = tApprovalRepo.findById(approvalId);
        if (tApprovalOpt.isPresent() && tApprovalOpt.get().getStatus().equals(ApprovalStatus.WAITING_FOR_APPROVAL.name())) {
            T_APPROVAL tApproval = tApprovalOpt.get();
            Optional<VW_HIER_FLOW> currFlow = vwHierFlowRepo.findBytAppIdAndApphierDtlId(tApproval.getTAppId(), tApproval.getAppHierDtlId());
            if (currFlow.isPresent()) {
                if (ApprovalStatus.REJECT.name().equals(type.name())) {
                    tApproval.setStatus(status);
                    tApproval.setUpdatedDate(new Date());
                    tApproval.setUpdatedBy(UserDetailUtils.getUsername());
                    tApproval.setDescription(description);
                    tApprovalRepo.save(tApproval);
                    isFinal = currFlow.get().getIsFinal();
                } else {
                    if (currFlow.get().getIsFinal().equals(Boolean.TRUE)) {
                        tApproval.setStatus(status);
                        tApproval.setUpdatedDate(new Date());
                        tApproval.setUpdatedBy(UserDetailUtils.getUsername());
                        tApproval.setDescription(description);
                        tApprovalRepo.save(tApproval);
                        isFinal = currFlow.get().getIsFinal();
                    } else {
                        List<VW_HIER_FLOW> flow = vwHierFlowRepo.findByAppHierId(tApproval.getAppHierId());
                        // get next approval
                        Integer nextLevel = currFlow.get().getApprovalLevel() + 1;
                        Optional<VW_HIER_FLOW> nextFlow = flow.stream()
                                .filter(a -> a.getApprovalLevel().equals(nextLevel)).findFirst();
                        Date now = new Date();
                        T_APPROVAL tAppHeader = new T_APPROVAL();
                        tAppHeader.setIdTrans(tApproval.getIdTrans());
                        tAppHeader.setCategory(tApproval.getCategory());
                        tAppHeader.setCreatedDate(now);
                        tAppHeader.setCreatedBy(UserDetailUtils.getUsername());
                        tAppHeader.setAppHierId(nextFlow.get().getAppHierId());
                        tAppHeader.setAppHierDtlId(nextFlow.get().getApphierDtlId());
                        tAppHeader.setApprovalLevel(nextFlow.get().getApprovalLevel());
                        tAppHeader.setApprovalName(tApproval.getApprovalName());
                        tAppHeader.setApprovalType(tApproval.getApprovalType());
                        tAppHeader.setDescription(tApproval.getDescription());
                        tAppHeader.setEntityId(tApproval.getEntityId());
                        tAppHeader.setStatus(ApprovalStatus.WAITING_FOR_APPROVAL.name());
                        tApprovalRepo.save(tAppHeader);

                        tApproval.setStatus(status);
                        tApproval.setDescription(description);
                        tApproval.setUpdatedDate(new Date());
                        tApproval.setUpdatedBy(UserDetailUtils.getUsername());
                        tApprovalRepo.save(tApproval);
                        isFinal = currFlow.get().getIsFinal();
                    }
                }
            }
        }
        return isFinal;
    }

    public void actionApproval(Integer approvalId, String description, ApprovalStatus type, HttpServletRequest httpServletRequest){
        String status = "";
        String userName = UserDetailUtils.getUsernameFromToken(httpServletRequest);
        switch (type) {
            case APPROVE:
                status = ApprovalStatus.APPROVED.name();
                break;
            case REJECT:
                status = ApprovalStatus.REJECTED.name();
                break;
            default:
                break;
        }

        Optional<T_APPROVAL> tApprovalOpt = tApprovalRepo.findById(approvalId);
        if(tApprovalOpt.isPresent()){
            T_APPROVAL tApproval = tApprovalOpt.get();
            if(ApprovalStatus.REJECT.name().equals(type.name())){
                tApproval.setStatus(status);
                tApproval.setUpdatedDate(new Date());
                tApproval.setUpdatedBy(userName);
                tApproval.setDescription(description);
                tApprovalRepo.save(tApproval);
            }else{
                tApproval.setStatus(status);
                tApproval.setUpdatedBy(userName);
                tApproval.setUpdatedDate(new Date());
                tApproval.setDescription(description);
                tApprovalRepo.save(tApproval);
            }
        }
    }

    @SuppressWarnings({"java:S1481", "java:S1854"})
    public boolean isFinalApprover(Integer approvalId, String description, ApprovalStatus type) {
        String status = "";
        switch (type) {
            case APPROVE:
                status = ApprovalStatus.APPROVED.name();
                break;
            case REJECT:
                status = ApprovalStatus.REJECTED.name();
                break;
            default:
                break;
        }

        boolean isFinal = false;
        Optional<T_APPROVAL> tApprovalOpt = tApprovalRepo.findById(approvalId);
        if (tApprovalOpt.isPresent() && tApprovalOpt.get().getStatus().equals(ApprovalStatus.WAITING_FOR_APPROVAL.name())) {
            T_APPROVAL tApproval = tApprovalOpt.get();
//            Optional<VW_HIER_FLOW> currFlow = vwHierFlowRepo.findById(tApproval.getAppHierDtlId());
            Optional<VW_HIER_FLOW> currFlow = vwHierFlowRepo.findBytAppIdAndApphierDtlId(tApproval.getTAppId(), tApproval.getAppHierDtlId());
            if (currFlow.isPresent()) {
                if (ApprovalStatus.REJECT.name().equals(type.name())) {
                    isFinal = currFlow.get().getIsFinal();
                } else {
                    if (currFlow.get().getIsFinal().equals(Boolean.TRUE)) {
                        isFinal = currFlow.get().getIsFinal();
                    } else {
                        List<VW_HIER_FLOW> flow = vwHierFlowRepo.findByAppHierId(tApproval.getAppHierId());
                        // get next approval
                        Integer nextLevel = currFlow.get().getApprovalLevel() + 1;
                        Optional<VW_HIER_FLOW> nextFlow = flow.stream()
                                .filter(a -> a.getApprovalLevel().equals(nextLevel)).findFirst();
                        isFinal = currFlow.get().getIsFinal();
                    }
                }
            }
        }
        return isFinal;
    }

    public String getDataApprovalJson(Integer approvalId) {
        Optional<T_APPROVAL> domain = tApprovalRepo.findById(approvalId);
        return domain.map(T_APPROVAL::getTriggerJson).orElse(null);
    }

    public void setApprovalHistory(Integer tAppId, Integer refId, String description, String category, String evtType,
                                   HttpServletRequest request) {
        Integer posId = UserDetailUtils.getPositionFromToken(request);
        Optional<T_APPROVAL> tappOpt = tApprovalRepo.findById(tAppId);
        Optional<Integer> tApprovalHistoryOptional = tApprovalHistoryRepo.findTopApprovalLevelByTAppId(tAppId);
        boolean isExist = tApprovalHistoryOptional.isPresent() && tApprovalHistoryOptional.get() > 1;

        if (tappOpt.isPresent() && !isExist) {
            Optional<M_USER> mUser = mUserRepo.findByUsername(UserDetailUtils.getUsernameFromToken(request));
            Integer employeeId = 0;
            if (mUser.isPresent()) {
                employeeId = mUser.get().getEmployeeId();
                T_APPROVAL tapp = tappOpt.get();
                Integer approvalLevel = 0;
                if (ApprovalStatus.SUBMIT.name().equalsIgnoreCase(evtType)) {
                    approvalLevel = 1;
                } else {
                    approvalLevel = tapp.getApprovalLevel();
                }
                Optional<VW_APPROVAL_HIERARCHY_DETAIL> appDtl = vwApprovalHierarchyDtlRepo
                        .findFirstByApphierIdAndPositionIdAndApprovalLevelAndEmployeeId(tapp.getAppHierId(), posId,
                                approvalLevel, employeeId);
                if (appDtl.isPresent()) {
                    VW_APPROVAL_HIERARCHY_DETAIL dtl = appDtl.get();
                    T_APPROVAL_HISTORY appHistory = new T_APPROVAL_HISTORY();

                    appHistory.setTAppId(tapp.getTAppId());
                    appHistory.setApphierDtlId(dtl.getApphierDtlId());
                    appHistory.setApphierId(dtl.getApphierId());
                    appHistory.setApprovalLevel(approvalLevel);
                    appHistory.setApprovalType(tapp.getCategory());
                    appHistory.setEmployeeCode(dtl.getEmployeeCode());
                    appHistory.setEmployeeId(dtl.getEmployeeId());
                    appHistory.setFullName(dtl.getFullName());

                    appHistory.setDescription(description);
                    appHistory.setCreatedBy(UserDetailUtils.getUsername());
                    appHistory.setCreatedDate(new Date());
                    appHistory.setRefId(refId);
                    appHistory.setCategory(category);
                    appHistory.setPosition(dtl.getPosition());
                    appHistory.setPositionId(dtl.getPositionId());
                    appHistory.setCreatedDate(new Date());
                    appHistory.setCreatedBy(UserDetailUtils.getUsername());
                    appHistory.setApprovalName(dtl.getApprovalName());

                    appHistory.setEventDate(new Date());
                    appHistory.setEventType(evtType);

                    tApprovalHistoryRepo.save(appHistory);
                }
            }
        }
    }

    public void setApprovalHistoryRefString(Integer tAppId, String refId, String description, String category, String evtType,
            HttpServletRequest request) {
        Integer posId = UserDetailUtils.getPositionFromToken(request);
        Optional<T_APPROVAL> tappOpt = tApprovalRepo.findById(tAppId);
        Optional<Integer> tApprovalHistoryOptional = tApprovalHistoryRepo.findTopApprovalLevelByTAppId(tAppId);
        boolean isExist = tApprovalHistoryOptional.isPresent() && tApprovalHistoryOptional.get() > 1;

        if (tappOpt.isPresent() && !isExist) {
            Optional<M_USER> mUser = mUserRepo.findByUsername(UserDetailUtils.getUsername());
            Integer employeeId = 0;
            if (mUser.isPresent()) {
                employeeId = mUser.get().getEmployeeId();
                T_APPROVAL tapp = tappOpt.get();
                Integer approvalLevel = 0;
                if (ApprovalStatus.SUBMIT.name().equalsIgnoreCase(evtType)) {
                    approvalLevel = 1;
                } else {
                    approvalLevel = tapp.getApprovalLevel();
                }

                Optional<VW_APPROVAL_HIERARCHY_DETAIL> appDtl = vwApprovalHierarchyDtlRepo
                        .findFirstByApphierIdAndPositionIdAndApprovalLevelAndEmployeeId(tapp.getAppHierId(), posId,
                                approvalLevel, employeeId);

                if (appDtl.isPresent()) {

                    VW_APPROVAL_HIERARCHY_DETAIL dtl = appDtl.get();
                    T_APPROVAL_HISTORY appHistory = new T_APPROVAL_HISTORY();

                    appHistory.setTAppId(tapp.getTAppId());
                    appHistory.setApphierDtlId(dtl.getApphierDtlId());
                    appHistory.setApphierId(dtl.getApphierId());
                    appHistory.setApprovalLevel(approvalLevel);
                    appHistory.setApprovalType(tapp.getCategory());
                    appHistory.setEmployeeCode(dtl.getEmployeeCode());
                    appHistory.setEmployeeId(dtl.getEmployeeId());
                    appHistory.setFullName(dtl.getFullName());

                    appHistory.setDescription(description);
                    appHistory.setCreatedBy(UserDetailUtils.getUsername());
                    appHistory.setCreatedDate(new Date());
                    appHistory.setRefIdString(refId);
                    appHistory.setCategory(category);
                    appHistory.setPosition(dtl.getPosition());
                    appHistory.setPositionId(dtl.getPositionId());
                    appHistory.setCreatedDate(new Date());
                    appHistory.setCreatedBy(UserDetailUtils.getUsername());
                    appHistory.setApprovalName(dtl.getApprovalName());

                    appHistory.setEventDate(new Date());
                    appHistory.setEventType(evtType);

                    tApprovalHistoryRepo.save(appHistory);
                }
            }
        }
    }

    @SuppressWarnings("java:S3776")
    public Map<String, Object> getApprovalHistory(Integer refId, String category, List<String> appCategory) {
        Optional<List<T_APPROVAL_HISTORY>> appHistory = tApprovalHistoryRepo.findByRefIdAndCategory(refId, category);
        HashMap<String, Object> resp = new HashMap<>();

        if (appHistory.isPresent() && !appHistory.get().isEmpty()) {
            HashMap<String, Object> approver = new HashMap<>();
            HashMap<String, Object> history = new HashMap<>();
            for (String cat : appCategory) {
                List<ApprovalHistoryApproverDto> approverDto = new ArrayList<>();
                List<ApprovalHistoryDataDto> dataDto = new ArrayList<>();

                try {
                    Integer appHist = tApprovalHistoryRepo.findTopSubmittedByRefIdAndCategoryAndType(refId, category,
                            cat);
                    Optional<List<T_APPROVAL_HISTORY>> tappOpt = tApprovalHistoryRepo
                            .findAllApproverByCategoryAndType(refId, appHist, category, cat);

                    if (tappOpt.isPresent() && !tappOpt.get().isEmpty()) {

                        List<M_APPROVAL_HIERARCHY_DTL> hierDtlOpt = mApprovalHierDtlRepo.findByAppHierIdAndStatus(
                                tappOpt.get().get(0).getApphierId(), FlowStatus.ACTIVE.name(),
                                Sort.by(Sort.Direction.ASC, Constant.APPROVAL_LEVEL));
                        if (!hierDtlOpt.isEmpty()) {

                            for (M_APPROVAL_HIERARCHY_DTL appHist_ : hierDtlOpt) {
                                ApprovalHistoryApproverDto dt = new ApprovalHistoryApproverDto();

                                dt.setId(appHist_.getAppHierId());
                                dt.setRole(appHist_.getPositionName());

                                T_APPROVAL_HISTORY appHistDtl = tappOpt.get().stream()
                                        .filter(p -> p.getApprovalLevel().equals(appHist_.getApprovalLevel())).findAny()
                                        .orElse(null);

                                String fullName = appHistDtl == null ? "-" : appHistDtl.getFullName();
                                String status = appHistDtl == null ? null : appHistDtl.getEventType();

                                dt.setStatus(status);

                                dt.setName(fullName);
                                approverDto.add(dt);
                            }
                        }
                        approver.put(cat, approverDto);
                    }
                    Optional<List<T_APPROVAL_HISTORY>> apphistDtlOpt = tApprovalHistoryRepo
                            .findByRefIdAndCategoryAndApprovalType(refId, category, cat,
                                    Sort.by(Sort.Direction.DESC, "ids"));
                    if (apphistDtlOpt.isPresent() && !apphistDtlOpt.get().isEmpty()) {
                        for (T_APPROVAL_HISTORY appHist_ : apphistDtlOpt.get()) {
                            ApprovalHistoryDataDto dth = new ApprovalHistoryDataDto();
                            if (appHist_.getEventType() != null) {
                                dth.setId(appHist_.getIds());
                                dth.setActionDate(appHist_.getEventDate());
                                dth.setDescription(appHist_.getDescription());
                                dth.setHierarchy(appHist_.getApprovalName());
                                dth.setName(appHist_.getFullName());
                                dth.setRole(appHist_.getPosition());
                                dth.setStatus(appHist_.getEventType());
                                Optional<T_APPROVAL> tApprovalDateOpt = tApprovalRepo.findById(appHist_.getTAppId());
                                tApprovalDateOpt.ifPresent(tApproval -> dth.setTaskDate(tApproval.getCreatedDate()));

                                dataDto.add(dth);
                            }
                        }
                        history.put(cat, dataDto);
                    }
                } catch (Exception e) {
                    logger.error(String.format(CommonVariables.ERROR_IN_PAR, e.getMessage()));
                }
                resp.put(Constant.DATA_APPROVER, approver);
                resp.put(Constant.DATA_HISTORY, history);
            }

        }
        return resp;
    }

    @SuppressWarnings("java:S3776")
    public Map<String, Object> getApprovalHistoryRefString(String refIdString, String category, List<String> appCategory) {
        Optional<List<T_APPROVAL_HISTORY>> appHistory = tApprovalHistoryRepo.findByRefIdStringAndCategory(refIdString, category);

        HashMap<String, Object> resp = new HashMap<>();

        if (appHistory.isPresent() && !appHistory.get().isEmpty()) {
            HashMap<String, Object> approver = new HashMap<>();
            HashMap<String, Object> history = new HashMap<>();
            for (String cat : appCategory) {
                List<ApprovalHistoryApproverDto> approverDto = new ArrayList<>();
                List<ApprovalHistoryDataDto> dataDto = new ArrayList<>();

                try {
                    Integer appHist = tApprovalHistoryRepo.findTopSubmittedByRefIdAndCategoryAndType(refIdString, category,
                            cat);
                    Optional<List<T_APPROVAL_HISTORY>> tappOpt = tApprovalHistoryRepo
                            .findAllApproverByCategoryAndTypeRefString(refIdString, appHist, category, cat);

                    if (tappOpt.isPresent() && !tappOpt.get().isEmpty()) {

                        List<M_APPROVAL_HIERARCHY_DTL> hierDtlOpt = mApprovalHierDtlRepo.findByAppHierIdAndStatus(
                                tappOpt.get().get(0).getApphierId(), FlowStatus.ACTIVE.name(),
                                Sort.by(Sort.Direction.ASC, Constant.APPROVAL_LEVEL));
                        if (!hierDtlOpt.isEmpty()) {

                            for (M_APPROVAL_HIERARCHY_DTL appHist_ : hierDtlOpt) {
                                ApprovalHistoryApproverDto dt = new ApprovalHistoryApproverDto();

                                dt.setId(appHist_.getAppHierId());
                                dt.setRole(appHist_.getPositionName());

                                T_APPROVAL_HISTORY appHistDtl = tappOpt.get().stream()
                                        .filter(p -> p.getApprovalLevel().equals(appHist_.getApprovalLevel())).findAny()
                                        .orElse(null);

                                String fullName = appHistDtl == null ? "-" : appHistDtl.getFullName();
                                String status = appHistDtl == null ? null : appHistDtl.getEventType();

                                dt.setStatus(status);

                                dt.setName(fullName);
                                approverDto.add(dt);
                            }
                        }
                        approver.put(cat, approverDto);
                    }
                    Optional<List<T_APPROVAL_HISTORY>> apphistDtlOpt = tApprovalHistoryRepo
                            .findByRefIdStringAndCategoryAndApprovalType(refIdString, category, cat,
                                    Sort.by(Sort.Direction.DESC, "ids"));
                    if (apphistDtlOpt.isPresent() && !apphistDtlOpt.get().isEmpty()) {
                        for (T_APPROVAL_HISTORY appHist_ : apphistDtlOpt.get()) {
                            ApprovalHistoryDataDto dth = new ApprovalHistoryDataDto();
                            if (appHist_.getEventType() != null) {
                                dth.setId(appHist_.getIds());
                                dth.setActionDate(appHist_.getEventDate());
                                dth.setDescription(appHist_.getDescription());
                                dth.setHierarchy(appHist_.getApprovalName());
                                dth.setName(appHist_.getFullName());
                                dth.setRole(appHist_.getPosition());
                                dth.setStatus(appHist_.getEventType());
                                Optional<T_APPROVAL> tApprovalDateOpt = tApprovalRepo.findById(appHist_.getTAppId());
                                tApprovalDateOpt.ifPresent(tApproval -> dth.setTaskDate(tApproval.getCreatedDate()));

                                dataDto.add(dth);
                            }
                        }
                        history.put(cat, dataDto);
                    }
                } catch (Exception e) {
                    logger.error(String.format(CommonVariables.ERROR_IN_PAR, e.getMessage()));
                }
                resp.put(Constant.DATA_APPROVER, approver);
                resp.put(Constant.DATA_HISTORY, history);
            }

        }
        return resp;
    }
    
    public void setApprovalHistoryForUsage(Integer tAppId, Integer refId, String description, String category,
										  String evtType, Integer positionId, String userName) {
        Optional<T_APPROVAL> tappOpt = tApprovalRepo.findById(tAppId);
		if (tappOpt.isPresent()) {

			Optional<M_USER> mUser = mUserRepo.findByUsername(userName);
			Integer employeeId;
			if (mUser.isPresent()) {
				employeeId = mUser.get().getEmployeeId();
				T_APPROVAL tapp = tappOpt.get();
				Integer approvalLevel;
				if (ApprovalStatus.SUBMIT.name().equalsIgnoreCase(evtType))
					approvalLevel = 1;
				else
					approvalLevel = tapp.getApprovalLevel();

				Optional<VW_APPROVAL_HIERARCHY_DETAIL> appDtl = vwApprovalHierarchyDtlRepo
						.findFirstByApphierIdAndPositionIdAndApprovalLevelAndEmployeeId(tapp.getAppHierId(), positionId,
								approvalLevel, employeeId);

				if (appDtl.isPresent()) {

					VW_APPROVAL_HIERARCHY_DETAIL dtl = appDtl.get();
					T_APPROVAL_HISTORY appHistory = new T_APPROVAL_HISTORY();

					appHistory.setTAppId(tapp.getTAppId());
					appHistory.setApphierDtlId(dtl.getApphierDtlId());
					appHistory.setApphierId(dtl.getApphierId());
					appHistory.setApprovalLevel(approvalLevel);
					appHistory.setApprovalType(tapp.getCategory());
					appHistory.setEmployeeCode(dtl.getEmployeeCode());
					appHistory.setEmployeeId(dtl.getEmployeeId());
					appHistory.setFullName(dtl.getFullName());

					appHistory.setDescription(description);
					appHistory.setCreatedBy(UserDetailUtils.getUsername());
					appHistory.setCreatedDate(new Date());
					appHistory.setRefId(refId);
					appHistory.setCategory(category);
					appHistory.setPosition(dtl.getPosition());
					appHistory.setPositionId(dtl.getPositionId());
					appHistory.setCreatedDate(new Date());
					appHistory.setCreatedBy(UserDetailUtils.getUsername());
					appHistory.setApprovalName(dtl.getApprovalName());

					appHistory.setEventDate(new Date());
					appHistory.setEventType(evtType);

					tApprovalHistoryRepo.save(appHistory);
				}

			}

		}
	}

    @SuppressWarnings("java:S3776")
    public Map<String, Object> getApprovalHistorySa(Integer refId, List<String> appCategory) {

        List<T_APPROVAL_HISTORY> appHistory = tApprovalHistoryRepo.findAllByRefIdAndCategory(refId, appCategory);

        HashMap<String, Object> resp = new HashMap<>();

        if (appHistory != null && !appHistory.isEmpty()) {
            HashMap<String, Object> approver = new HashMap<>();
            HashMap<String, Object> history = new HashMap<>();
            for (String cat : appCategory) {
                List<ApprovalHistoryApproverDto> approverDto = new ArrayList<>();
                List<ApprovalHistoryDataDto> dataDto = new ArrayList<>();

                try {
                    Integer appHist = tApprovalHistoryRepo.findTopSubmittedByRefIdAndCategoryAndType(refId, ApprovalCategory.SERVICE_AGREEMENT.name(),
                            cat);
                    Optional<List<T_APPROVAL_HISTORY>> tappOpt = tApprovalHistoryRepo
                            .findAllApproverByCategoryAndType(refId, appHist, ApprovalCategory.SERVICE_AGREEMENT.name(), cat);

                    if (tappOpt.isPresent() && !tappOpt.get().isEmpty()) {

                        List<M_APPROVAL_HIERARCHY_DTL> hierDtlOpt = mApprovalHierDtlRepo.findByAppHierIdAndStatus(
                                tappOpt.get().get(0).getApphierId(), FlowStatus.ACTIVE.name(),
                                Sort.by(Sort.Direction.ASC, Constant.APPROVAL_LEVEL));
                        if (!hierDtlOpt.isEmpty()) {

                            for (M_APPROVAL_HIERARCHY_DTL appHist_ : hierDtlOpt) {
                                T_APPROVAL_HISTORY appHistDtl = tappOpt.get().stream()
                                        .filter(p -> p.getApprovalLevel().equals(appHist_.getApprovalLevel())).findAny()
                                        .orElse(null);
                                if (appHistDtl != null) {
                                    ApprovalHistoryApproverDto dt = new ApprovalHistoryApproverDto();
                                    dt.setId(appHist_.getAppHierId());
                                    dt.setRole(appHist_.getPositionName());
                                    dt.setStatus(appHistDtl.getEventType());
                                    dt.setName(appHistDtl.getFullName());
                                    approverDto.add(dt);
                                }
                            }
                        }
                        approver.put(cat, approverDto);
                    }
                    Optional<List<T_APPROVAL_HISTORY>> apphistDtlOpt = tApprovalHistoryRepo
                            .findByRefIdAndCategoryAndApprovalType(refId, ApprovalCategory.SERVICE_AGREEMENT.name(), cat,
                                    Sort.by(Sort.Direction.DESC, "ids"));
                    if (apphistDtlOpt.isPresent() && !apphistDtlOpt.get().isEmpty()) {
                        for (T_APPROVAL_HISTORY appHist_ : apphistDtlOpt.get()) {
                            ApprovalHistoryDataDto dth = new ApprovalHistoryDataDto();
                            if (appHist_.getEventType() != null) {
                                dth.setId(appHist_.getIds());
                                dth.setActionDate(appHist_.getEventDate());
                                dth.setDescription(appHist_.getDescription());
                                dth.setHierarchy(appHist_.getApprovalName());
                                dth.setName(appHist_.getFullName());
                                dth.setRole(appHist_.getPosition());
                                dth.setStatus(appHist_.getEventType());
                                Optional<T_APPROVAL> tApprovalDateOpt = tApprovalRepo.findById(appHist_.getTAppId());
                                tApprovalDateOpt.ifPresent(tApproval -> dth.setTaskDate(tApproval.getCreatedDate()));
                                dataDto.add(dth);
                            }
                        }
                        history.put(cat, dataDto);
                    }
                } catch (Exception e) {
                    logger.error(String.format(CommonVariables.ERROR_IN_PAR, e.getMessage()));
                }
            }
            resp.put(Constant.DATA_APPROVER, approver);
            resp.put(Constant.DATA_HISTORY, history);

        }
        return resp;
    }

    @SuppressWarnings({"java:S3776", "java:S6541"})
    public Map<String, Object> getApprovalHistoryForUsage(Integer refId, String category, List<String> appCategory) {

        Optional<List<T_APPROVAL_HISTORY>> appHistory = tApprovalHistoryRepo.findByRefIdAndCategory(refId, category);

        HashMap<String, Object> resp = new HashMap<>();

        if (appHistory.isPresent() && !appHistory.get().isEmpty()) {
            HashMap<String, Object> approver = new HashMap<>();
            HashMap<String, Object> history = new HashMap<>();
            for (String cat : appCategory) {
                Integer appHist = null;
                List<ApprovalHistoryApproverDto> approverDto = new ArrayList<>();
                List<ApprovalHistoryDataDto> dataDto = new ArrayList<>();

                try {
                    if(cat.equalsIgnoreCase(ApprovalCategory.APPROVE_REJECT_USAGE.name())){
                        appHist = tApprovalHistoryRepo.findTopSubmittedByRefIdAndCategoryAndTypeForApprove(
                                refId,
                                cat,
                                category
                        );
                    }else{
                        appHist = tApprovalHistoryRepo.findTopSubmittedByRefIdAndCategoryAndType(refId, cat, category);

                    }
                    Optional<List<T_APPROVAL_HISTORY>> tappOpt = tApprovalHistoryRepo
                            .findAllApproverByCategoryAndType(refId, appHist, cat, category);

                    if (tappOpt.isPresent() && !tappOpt.get().isEmpty()) {

                        List<M_APPROVAL_HIERARCHY_DTL> hierDtlOpt = mApprovalHierDtlRepo.findByAppHierIdAndStatus(
                                tappOpt.get().get(0).getApphierId(), FlowStatus.ACTIVE.name(),
                                Sort.by(Sort.Direction.ASC, Constant.APPROVAL_LEVEL));
                        if (!hierDtlOpt.isEmpty()) {

                            for (M_APPROVAL_HIERARCHY_DTL appHist_ : hierDtlOpt) {
                                ApprovalHistoryApproverDto dt = new ApprovalHistoryApproverDto();

                                dt.setId(appHist_.getAppHierId());
                                dt.setRole(appHist_.getPositionName());

                                T_APPROVAL_HISTORY appHistDtl = tappOpt.get().stream()
                                        .filter(p -> p.getApprovalLevel().equals(appHist_.getApprovalLevel())).findAny()
                                        .orElse(null);

                                String fullName = appHistDtl == null ? "-" : appHistDtl.getFullName();
                                String status = appHistDtl == null ? null : appHistDtl.getEventType();

                                dt.setStatus(status);

                                dt.setName(fullName);
                                approverDto.add(dt);
                            }
                        }
                        approver.put(cat, approverDto);
                    }
                    Optional<List<T_APPROVAL_HISTORY>> apphistDtlOpt = tApprovalHistoryRepo
                            .findByRefIdAndCategoryAndApprovalType(refId, category, cat,
                                    Sort.by(Sort.Direction.DESC, "ids"));
                    if (apphistDtlOpt.isPresent() && !apphistDtlOpt.get().isEmpty()) {
                        for (T_APPROVAL_HISTORY appHist_ : apphistDtlOpt.get()) {
                            ApprovalHistoryDataDto dth = new ApprovalHistoryDataDto();
                            if (appHist_.getEventType() != null) {
                                dth.setId(appHist_.getIds());
                                dth.setActionDate(appHist_.getEventDate());
                                dth.setDescription(appHist_.getDescription());
                                dth.setHierarchy(appHist_.getApprovalName());
                                dth.setName(appHist_.getFullName());
                                dth.setRole(appHist_.getPosition());
                                dth.setStatus(appHist_.getEventType());
                                Optional<T_APPROVAL> tApprovalDateOpt = tApprovalRepo.findById(appHist_.getTAppId());
                                tApprovalDateOpt.ifPresent(tApproval -> dth.setTaskDate(tApproval.getCreatedDate()));

                                dataDto.add(dth);
                            }
                        }
                        history.put(cat, dataDto);
                    }
                } catch (Exception e) {
                    logger.error(String.format(CommonVariables.ERROR_IN_PAR, e.getMessage()));
                }
                resp.put(Constant.DATA_APPROVER, approver);
                resp.put(Constant.DATA_HISTORY, history);
            }

        }
        return resp;
    }

    public List<EmployeeDetailDto> getEmployeeDetailList(List<VW_APPROVAL_HIERARCHY_DETAIL> list) {
        List<EmployeeDetailDto> employeeDetails = new ArrayList<>();
        for (var detail : list) {
            employeeDetails.add(EmployeeDetailDto.builder()
                    .apphierId(detail.getApphierId())
                    .employeeId(detail.getEmployeeId())
                    .employeeName(detail.getFullName())
                    .build());
        }
        return employeeDetails;
    }
    
    public boolean isForwardPosition(Integer approvalId) {
        Optional<VW_HIER_FLOW> appHierDtlOpt = vwHierFlowRepo.findBytAppId(approvalId);
        return appHierDtlOpt.isPresent() && StringUtils.hasValue(appHierDtlOpt.get().getForwardTo()) && appHierDtlOpt.get().getTaskForward() > 0;
    }
    
    public void setApprHistoryForward(Integer tAppId, Integer refId, String description, String category, String evtType,
                                   HttpServletRequest request) {
        Optional<T_APPROVAL> tappOpt = tApprovalRepo.findById(tAppId);
        Optional<Integer> tApprovalHistoryOptional = tApprovalHistoryRepo.findTopApprovalLevelByTAppId(tAppId);
        boolean isExist = tApprovalHistoryOptional.isPresent() && tApprovalHistoryOptional.get() > 1;

        if (tappOpt.isPresent() && !isExist) {
            Optional<M_USER> mUser = mUserRepo.findByUsername(UserDetailUtils.getUsernameFromToken(request));
            Integer employeeId;
            String fullname;
            if (mUser.isPresent()) {
                employeeId = mUser.get().getEmployeeId();
                fullname = mUser.get().getEmployee().getFullName();
                T_APPROVAL tapp = tappOpt.get();
                Integer approvalLevel = 0;
                if (ApprovalStatus.SUBMIT.name().equalsIgnoreCase(evtType)) {
                    approvalLevel = 1;
                } else {
                    approvalLevel = tapp.getApprovalLevel();
                }
                Optional<VW_HIER_FLOW> appDtl = vwHierFlowRepo.findBytAppId(tappOpt.get().getTAppId());
                
                if (appDtl.isPresent()) {
                    VW_HIER_FLOW dtl = appDtl.get();
                    Optional<M_POSITION> posOpt = mPositionRepo.findById(dtl.getForwardTo());
                    T_APPROVAL_HISTORY appHistory = new T_APPROVAL_HISTORY();
                    appHistory.setTAppId(tapp.getTAppId());
                    appHistory.setApphierDtlId(dtl.getApphierDtlId());
                    appHistory.setApphierId(dtl.getAppHierId());
                    appHistory.setApprovalLevel(approvalLevel);
                    appHistory.setApprovalType(tapp.getCategory());
                    appHistory.setEmployeeCode(dtl.getEmployeeForward());
                    appHistory.setEmployeeId(employeeId);
                    appHistory.setFullName(fullname);

                    appHistory.setDescription(description);
                    appHistory.setCreatedBy(UserDetailUtils.getUsername());
                    appHistory.setCreatedDate(new Date());
                    appHistory.setRefId(refId);
                    appHistory.setCategory(category);
                    appHistory.setPosition(posOpt.map(M_POSITION::getName).orElse(null));
                    appHistory.setPositionId(posOpt.map(M_POSITION::getPositionId).orElse(null));
                    appHistory.setCreatedDate(new Date());
                    appHistory.setCreatedBy(UserDetailUtils.getUsername());
                    appHistory.setApprovalName(tappOpt.get().getApprovalName());

                    appHistory.setEventDate(new Date());
                    appHistory.setEventType(evtType);

                    tApprovalHistoryRepo.save(appHistory);
                }
            }
        }
    }
    
    
    public void setApprHistoryForwardRefString(Integer tAppId, String refId, String description, String category, String evtType,
            HttpServletRequest request) {
        Optional<T_APPROVAL> tappOpt = tApprovalRepo.findById(tAppId);
        Optional<Integer> tApprovalHistoryOptional = tApprovalHistoryRepo.findTopApprovalLevelByTAppId(tAppId);
        boolean isExist = tApprovalHistoryOptional.isPresent() && tApprovalHistoryOptional.get() > 1;

        if (tappOpt.isPresent() && !isExist) {
            Optional<M_USER> mUser = mUserRepo.findByUsername(UserDetailUtils.getUsernameFromToken(request));
            Integer employeeId;
            String fullname;
            if (mUser.isPresent()) {
                employeeId = mUser.get().getEmployeeId();
                fullname = mUser.get().getEmployee().getFullName();
                T_APPROVAL tapp = tappOpt.get();
                Integer approvalLevel = 0;
                if (ApprovalStatus.SUBMIT.name().equalsIgnoreCase(evtType)) {
                    approvalLevel = 1;
                } else {
                    approvalLevel = tapp.getApprovalLevel();
                }

                Optional<VW_HIER_FLOW> appDtl = vwHierFlowRepo.findBytAppId(tappOpt.get().getTAppId());
                
                if (appDtl.isPresent()) {
                    VW_HIER_FLOW dtl = appDtl.get();
                    Optional<M_POSITION> posOpt = mPositionRepo.findById(dtl.getForwardTo());
                    T_APPROVAL_HISTORY appHistory = new T_APPROVAL_HISTORY();

                    appHistory.setTAppId(tapp.getTAppId());
                    appHistory.setApphierDtlId(dtl.getApphierDtlId());
                    appHistory.setApphierId(dtl.getAppHierId());
                    appHistory.setApprovalLevel(approvalLevel);
                    appHistory.setApprovalType(tapp.getCategory());
                    appHistory.setEmployeeCode(dtl.getEmployeeForward());
                    appHistory.setEmployeeId(employeeId);
                    appHistory.setFullName(fullname);

                    appHistory.setDescription(description);
                    appHistory.setCreatedBy(UserDetailUtils.getUsername());
                    appHistory.setCreatedDate(new Date());
                    appHistory.setRefIdString(refId);
                    appHistory.setCategory(category);
                    appHistory.setPosition(posOpt.map(M_POSITION::getName).orElse(null));
                    appHistory.setPositionId(posOpt.map(M_POSITION::getPositionId).orElse(null));
                    appHistory.setCreatedDate(new Date());
                    appHistory.setCreatedBy(UserDetailUtils.getUsername());
                    appHistory.setApprovalName(tappOpt.get().getApprovalName());

                    appHistory.setEventDate(new Date());
                    appHistory.setEventType(evtType);

                    tApprovalHistoryRepo.save(appHistory);
                }
            }
        }
    }

    public void setCommonApprovalHistory(
            Integer approvalId,
            Integer refId,
            String remark,
            String category,
            String action,
            HttpServletRequest httpServletRequest) {
        if (isForwardPosition(approvalId)) {
            // Set Approval History
            setApprHistoryForward(
                    approvalId,
                    refId,
                    remark,
                    category,
                    action,
                    httpServletRequest
            );
        } else {
            // Set Approval History
            setApprovalHistory(
                    approvalId,
                    refId,
                    remark,
                    category,
                    action,
                    httpServletRequest
            );
        }
    }
}

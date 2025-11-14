package com.dbs.module.account.detail.distributionmedia.service;

import com.dbs.common.base.utils.*;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.services.GlobalTypeValueService;
import com.dbs.common.library.utils.FlowStatus;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.common.library.utils.UtilsDate;
import com.dbs.database.crm.entities.accountmanagement.M_DISTRIBUTION_MEDIA;
import com.dbs.database.crm.entities.accountmanagement.VW_ACC_DIST_MEDIA;
import com.dbs.database.crm.entities.product.M_PRODUCT_DETAIL;
import com.dbs.database.crm.entities.product.M_PRODUCT_VERSION;
import com.dbs.database.crm.entities.product.VW_PRODUCT;
import com.dbs.database.crm.entities.usermanagement.AUDIT_TRAIL;
import com.dbs.database.crm.entities.usermanagement.M_USER;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.repositories.accountmanagement.Account.MAccountRepo;
import com.dbs.database.crm.repositories.accountmanagement.Account.MDistributionMediaRepo;
import com.dbs.database.crm.repositories.accountmanagement.Account.VwAccDistMediaRepo;
import com.dbs.database.crm.repositories.product.MProductDetailRepo;
import com.dbs.database.crm.repositories.product.MProductVersionRepo;
import com.dbs.database.crm.repositories.product.VWProductRepo;
import com.dbs.database.crm.repositories.usermanagement.AuditTrailRepo;
import com.dbs.database.crm.repositories.usermanagement.MUserRepo;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
import com.dbs.module.account.detail.distributionmedia.dto.DismeCreateRequestDTO;
import com.dbs.module.account.detail.distributionmedia.dto.DismeInactiveRequestDTO;
import com.dbs.module.account.utils.ConstantAccount;
import com.dbs.module.account.utils.UtilsAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.dbs.common.base.utils.CommonHelper.removeSpace;
import com.dbs.common.library.utils.PricingRuleUtils;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class DistributionMediaService {
    
    private static final Logger logger = LoggerFactory.getLogger(DistributionMediaService.class);
    
    @Autowired
    private Validator validator;
    @Autowired
    private MDistributionMediaRepo dismeRepo;
    @Autowired
    private VWProductRepo vwProductRepo;
    @Autowired
    private MProductDetailRepo mProductDetailRepo;
    @Autowired
    private MProductVersionRepo mProductVersionRepo;
    @Autowired
    private MAccountRepo acRepo;
    @Autowired
    private MUserRepo userRepo;
    @Autowired
    private AuditTrailRepo auditTrailRepo;
    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;
    @Autowired 
    private GlobalTypeValueService globalTypeService;
    @Autowired
    private VwAccDistMediaRepo vwDismeRepo;
    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings({"java:S3776","java:S1192","java:S1141"})
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ResponseEntity<ResponseObject> createDistributionMedia (DismeCreateRequestDTO request) {
        logger.info("paramsRequest -> {}", request);
        ResponseObject result;
        try {

            ResponseEntity<ResponseObject> validate = this.validateCreateDistributionMedia(request);
            if(!validate.getBody().getSuccess()) {
                return validate;
            }
            
            M_DISTRIBUTION_MEDIA distributionMedia = new M_DISTRIBUTION_MEDIA();
            SimpleDateFormat formatDate = new SimpleDateFormat("dd MMM yyyy");
            distributionMedia.setAccountId(request.getAccountId());
            distributionMedia.setProductId(request.getProductId());
            distributionMedia.setStartDate(formatDate.parse(request.getStartDate()));
            distributionMedia.setDescription(removeSpace(request.getDescription()));
            distributionMedia.setCreatedBy(UserDetailUtils.getUsername());
            distributionMedia.setStatus(FlowStatus.ACTIVE.name());
            distributionMedia.setCreatedDate(new Date());
            dismeRepo.save(distributionMedia);
            
            result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.CREATED, "Success create distribution media", distributionMedia);
            return new ResponseEntity<>(result, result.getHttpCode());
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ResponseObject> validateCreateDistributionMedia (DismeCreateRequestDTO request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            List<Map<String, Object>> violationHeaderList = new ArrayList<>();
            List<String> validateHeader = new ArrayList<>();
            for (var violation : violations) {
                logger.error(violation.getMessage());
                Map<String, Object> data = new HashMap<>();
                validateHeader.add(violation.getMessage());
                data.put(violation.getPropertyPath().toString(), violation.getMessage());
                violationHeaderList.add(data);
            }
            ResponseObject result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, validateHeader.get(0), violationHeaderList);
            return new ResponseEntity<>(result, result.getHttpCode());
        }

        Optional<M_DISTRIBUTION_MEDIA> chekProductExist = dismeRepo.findTopByAccountIdAndProductIdAndStatus(request.getAccountId(), request.getProductId(), FlowStatus.ACTIVE.name());
        if(chekProductExist.isPresent()){
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                    "The selected product already exists on this account", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
        }

        SimpleDateFormat formatDate = new SimpleDateFormat("dd MMM yyyy");
        try {
            formatDate.parse(request.getStartDate());
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                    "Invalid start date!", ResponseUtils.DATA_EMPTY), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                UtilsAccount.messageSuccess(ConstantAccount.VALIDATE, ConstantAccount.DISTRIBUTION_MEDIA), ResponseUtils.DATA_EMPTY), HttpStatus.OK);
    }


    
    @SuppressWarnings({"java:S3776","java:S1192"})
    public ResponseEntity<ResponseObject> getListDistributionMedia(Integer accountId, MaterialTablePagingRequest pagingData,
			PagedResourcesAssembler<VW_ACC_DIST_MEDIA> assembler) {
            logger.info("Get List Distribution Media");
            try {
                Map<String, Object> filter = new HashMap<>();
                filter.put("accountId", accountId);
                
                Page<VW_ACC_DIST_MEDIA> data;

                if (isNotBlank(pagingData.getSearchs())) {
                    Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                    for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        pagingData.getSearch().add(key+"~"+value);
                    }
                }

                if(!pagingData.getSearch().isEmpty()) {
                    data = vwDismeRepo.findAll(vwDismeRepo.getSpecificationFromFilters(pagingData, filter), PagingUtils.getPaging(pagingData));
                } else {
                    data = vwDismeRepo.findAll(vwDismeRepo.getSpecificationDefault(filter), PagingUtils.getPaging(pagingData));
                }
                
                List<LinkedHashMap<String, Object>> allData = data.getContent().stream()
                    .map(g -> {
                        List<LinkedHashMap<String, Object>> allDetail = new ArrayList<>();
                        LinkedHashMap<String, Object> disme = new LinkedHashMap<>();
                        disme.put("id", g.getId());
                        disme.put("productId", g.getProductId());
                        disme.put("productVersionId", g.getProductId()); // isinya productId, dr FE udh set nama variabelnya productVersionId
                        String dateString = "yyyy-MM-dd";
                        disme.put("startDate", CommonHelper.convertDateToString(dateString, g.getStartDate()));
                        if(g.getEndDate() != null) {
                            disme.put("endDate", CommonHelper.convertDateToString(dateString, g.getEndDate()));
                        } else {
                            disme.put("endDate", null);
                        }

                        disme.put("status", g.getStatus());
                        disme.put("remark", g.getRemark());
                        disme.put("productName", g.getProductName());
                        disme.put("priceCode", g.getPriceCode());
                        disme.put("description", g.getDescription());
                        Optional<M_PRODUCT_VERSION> getVersion = mProductVersionRepo.findTopByProductIdAndStatusIgnoreCase(g.getProductId(), FlowStatus.ACTIVE.name());
                        List<M_PRODUCT_DETAIL> allPd = mProductDetailRepo.findAllByProductVersionId(getVersion.get().getId());
                        for(M_PRODUCT_DETAIL pd : allPd) {
                            LinkedHashMap<String, Object> pDetail = new LinkedHashMap<>();
                            pDetail.put("name", pd.getName());
                            pDetail.put("value", pd.getValue());
                            if(pd.getUom() != null) {
                                Optional<R_GLOBAL_TYPE_VALUE> unit = rGlobalTypeValueRepo.findByGlbTypeValId(Integer.parseInt(pd.getUom()));
                                pDetail.put("unitId", pd.getUom());
                                pDetail.put("unit", unit.get().getName());
                            } else {
                                pDetail.put("unit", null);
                            }
                            pDetail.put("detailDescription", pd.getDescription());
                            allDetail.add(pDetail);

                        }
                        
                        disme.put("detail", allDetail);
                        disme.put("createdDate", g.getCreatedDate());
                        disme.put("createdBy", g.getCreatedBy());
                        disme.put("updatedDate", g.getUpdatedDate());
                        disme.put("updatedBy", g.getUpdatedBy());
                        return disme;
                    })
                    .collect(Collectors.toList());
          
            PagedModel<EntityModel<VW_ACC_DIST_MEDIA>> pagedData = assembler.toModel(data);
            Map<String, Object> d = new HashMap<>();
            d.put("result", allData);
            d.put("page", pagedData.getMetadata());
            d.put("links", pagedData.getLinks());
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success get view paging", d), HttpStatus.OK);
        } catch (Exception e) {
           logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
   @SuppressWarnings({"java:S3776","java:S1141"})
    public ResponseEntity<ResponseObject> inactiveDistributionMedia(DismeInactiveRequestDTO requestDTO) {
        ResponseObject result;
        try {
            ObjectMapper mapper = new ObjectMapper();
            SimpleDateFormat formatDate = new SimpleDateFormat("dd MMM yyyy");

            Optional<M_DISTRIBUTION_MEDIA> data = dismeRepo.findById(requestDTO.getDistributionMediaId());
            if(data.isPresent()) {
                M_DISTRIBUTION_MEDIA dm = data.get();

                if(dm.getEndDate()!=null) {
                    return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND, "Distribution media will be inactive on " + UtilsDate.dateToString(dm.getEndDate(), Constant.FORMAT_START_END_DATE), ResponseUtils.DATA_EMPTY), HttpStatus.NOT_FOUND);
                }

                if(!UtilsDate.validateDate(requestDTO.getEndDate())) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "End date must be greater than Current Date!", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }

                if(formatDate.parse(requestDTO.getEndDate()).getTime() < dm.getStartDate().getTime()) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "End date date must be greater than start date!", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }

                AUDIT_TRAIL auditTrail = new AUDIT_TRAIL();
                String oldValue = mapper.writeValueAsString(dm);
                auditTrail.setOldValue(oldValue);
                auditTrail.setRemark(requestDTO.getRemark());
                auditTrail.setOperation(ConstantAccount.MODIFY);
                auditTrail.setTableName("M_DISTRIBUTION_MEDIA");
                auditTrail.setDataId(dm.getDistributionMediaId().toString());
                Optional<M_USER> user = userRepo.findByUsername(UserDetailUtils.getUsername());
                if (user.isEmpty()) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST,
                            "user with username " + UserDetailUtils.getUsername() + " not found", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                R_GLOBAL_TYPE_VALUE rUserLevel = globalTypeService.getGlobalTypeByGlbValue(Constant.USER_LEVEL, user.get().getUserLevel());
                String userLevel = rUserLevel.getName() != null ? rUserLevel.getName() : "";
                auditTrail.setUserLevel(userLevel);

                try {
                    dm.setEndDate(formatDate.parse(requestDTO.getEndDate()));
                } catch (Exception e) {
                    result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.BAD_REQUEST, "Invalid end date", ResponseUtils.DATA_EMPTY);
                    return new ResponseEntity<>(result, result.getHttpCode());
                }
                dm.setUpdatedBy(UserDetailUtils.getUsername());
                dm.setUpdatedDate(new Date());
                dismeRepo.save(dm);

                String newValue = mapper.writeValueAsString(dm);
                auditTrail.setNewValue(newValue);
                auditTrail.setCreatedBy(UserDetailUtils.getUsername());
                auditTrail.setCreatedDate(new Date());
                auditTrailRepo.save(auditTrail);

                result = new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                        ResponseUtils.MESSAGE_INACTIVE, dm);
                return new ResponseEntity<>(result, result.getHttpCode());
            } else {
                result = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.NOT_FOUND,
                        UtilsAccount.messageDataNotFound(ConstantAccount.DISTRIBUTION_MEDIA, requestDTO.getDistributionMediaId()), ResponseUtils.DATA_EMPTY);
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
    
    @SuppressWarnings("java:S3776")
    public ResponseEntity<ResponseObject> viewDistributionMedia(HttpServletRequest httpServletRequest) {
		logger.info("Get List distributionMedia");
		ResponseObject result = new ResponseObject();
		try {
			List<VW_PRODUCT> productOpt = vwProductRepo.findAllByServiceTypeNameAndProductTypeNameAndStatus("Informasi Tagihan Pelanggan", "Service", FlowStatus.ACTIVE.name());
                        List<LinkedHashMap<String, Object>> datas = new LinkedList<>();
                        for(VW_PRODUCT vp : productOpt) {
                            LinkedHashMap<String, Object> data = new LinkedHashMap<>();
                            data.put("productName", vp.getProductName());
                            data.put("pricing", vp.getPricing());
                            data.put("productVersionId", vp.getId()); // isinya productId, dr FE udh set nama variabelnya productVersionId
                            data.put("productId", vp.getId());
                            data.put("description", vp.getProductDescription());
                            List<LinkedHashMap<String, Object>> allDetail = new ArrayList<>();
                            Optional<List<M_PRODUCT_VERSION>> getPvId = mProductVersionRepo.findAllByProductId(vp.getId());
                            if(getPvId.isPresent()) {
                                for(M_PRODUCT_VERSION prodver : getPvId.get()) {
                                    if(prodver.getStatus().equals(FlowStatus.ACTIVE.name())) {

                                        List<M_PRODUCT_DETAIL> allPd = mProductDetailRepo.findAllByProductVersionId(prodver.getId());
                                        for(M_PRODUCT_DETAIL pd : allPd) {
                                            LinkedHashMap<String, Object> pDetail = new LinkedHashMap<>();
                                            pDetail.put("name", pd.getName());
                                            pDetail.put("value", pd.getValue());
                                            if(pd.getUom() != null) {
                                                Optional<R_GLOBAL_TYPE_VALUE> unit = rGlobalTypeValueRepo.findByGlbTypeValId(Integer.parseInt(pd.getUom()));
                                                pDetail.put("unitId", pd.getUom());
                                                pDetail.put("unit", unit.get().getName());
                                            } else {
                                                pDetail.put("unit", null);
                                            }
                                            pDetail.put("detailDescription", pd.getDescription());
                                            allDetail.add(pDetail);

                                        }
                                    }
                                }
                                
                            data.put("productDetail", allDetail);
                            datas.add(data);
                                
                            } else {
                                data.put("productVersion", null);
                                data.put("productDetail", null);
                            }
                            
                        }
                        
                        
			if (productOpt.isEmpty()) {
				result = PricingRuleUtils.responseFail("No product distribution media active data found");
			} else
				result = PricingRuleUtils.responseSuccess("Success", datas);

			return new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {
			logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

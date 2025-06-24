package com.dbs.module.account.detail.premise.service;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.utils.FlowStatus;
import com.dbs.database.crm.entities.accountmanagement.M_ASSETS;
import com.dbs.database.crm.entities.accountmanagement.M_ASSETS_ASSIGNMENT_HISTORY;
import com.dbs.database.crm.entities.accountmanagement.M_SERVICE_POINT;
import com.dbs.database.crm.entities.accountmanagement.VW_CUSTOMER_ADDDRESS;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.repositories.accountmanagement.Account.*;
import com.dbs.database.crm.repositories.usermanagement.AuditTrailRepo;
import com.dbs.database.crm.repositories.usermanagement.MUserRepo;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
import com.dbs.module.account.detail.address.service.AccountAddressService;
import com.dbs.module.account.detail.premise.dto.PremiseViewDTO;
import com.dbs.module.account.detail.premise.dto.ServicePointViewDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class PremiseService {

    private static final Logger logger = LoggerFactory.getLogger(AccountAddressService.class);

    @Autowired
    private MAccountAddressRepo accountAddressRepo;
    @Autowired
    private MUserRepo userRepo;
    @Autowired
    private AuditTrailRepo auditTrailRepo;
    @Autowired
    private MaddressRepo maddressRepo;
    @Autowired
    private MServicePointRepo mServicePointRepo;
    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;
    @Autowired
    private MassetsRepo massetsRepo;
    @Autowired
    private MAssetAssignmentRepo mAssetAssignmentRepo;
    @Autowired
    private Validator validator;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private VWCustomerAddressRepo vwCustomerAddressRepo;

    @SuppressWarnings("java:S3776")
    public ResponseEntity<ResponseObject> getPremiseByAccountId(MaterialTablePagingRequest pagingData,
			PagedResourcesAssembler<VW_CUSTOMER_ADDDRESS> assembler, Integer accountId) {
        logger.info("Get List Premise By Account Address Id");

        ResponseObject result = new ResponseObject();

        try {
            Page<VW_CUSTOMER_ADDDRESS> accountAddress;
            Map<String, Object> filter = new HashMap<>(); // For Default Filter
            filter.put("accountId", accountId);
            filter.put("status", FlowStatus.ACTIVE.name());
            filter.put("premiseFlag", Boolean.TRUE);

            if (isNotBlank(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key+"~"+value);
                }
            }
            if (!pagingData.getSearch().isEmpty()) {
                accountAddress = this.vwCustomerAddressRepo.findAll(this.vwCustomerAddressRepo.getSpecificationFromFilters(pagingData, filter),
                        PagingUtils.getPaging(pagingData));
            } else {
                accountAddress = this.vwCustomerAddressRepo.findAll(this.vwCustomerAddressRepo.getSpecificationDefault(filter),
                        PagingUtils.getPaging(pagingData));
            }

            List<VW_CUSTOMER_ADDDRESS> newData = accountAddress.getContent();

            int index = 0;

            List<PremiseViewDTO> newDto = new ArrayList<>();
            for (VW_CUSTOMER_ADDDRESS premise : newData) {
                PremiseViewDTO addData = new PremiseViewDTO();
                addData.setId(premise.getAccountAddressId());
                addData.setFullAddress(premise.getFullAddress());
                addData.setStatus(premise.getStatus());

                List<M_SERVICE_POINT> getSp = mServicePointRepo.findAllByAccountAddressId(premise.getAccountAddressId());
                List<ServicePointViewDTO> newSpList = new ArrayList<>();
                if(getSp.size() > 0) {
                    int index2 = 0;
                    for (M_SERVICE_POINT sp : getSp) {
                        ServicePointViewDTO addDataSp = new ServicePointViewDTO();
                        addDataSp.setServicePointId(sp.getId());
                        Optional<R_GLOBAL_TYPE_VALUE> getServicePointName = rGlobalTypeValueRepo.findByGlbTypeValIdAndGlobalType(sp.getServicePointName(), 37);

                        if(getServicePointName.isPresent()) {
                            addDataSp.setServicePointName(getServicePointName.get().getName());
                        }
                        addDataSp.setDescription(sp.getDescription());
                        addDataSp.setStatus(sp.getStatus());

                        //get Installed date
                        List<M_ASSETS_ASSIGNMENT_HISTORY> getAssetAssigment = mAssetAssignmentRepo.findByServicePointId(sp.getId());

                        if(getAssetAssigment.size() > 0) {
                            addDataSp.setInstalledDate(getAssetAssigment.get(0).getInstallDate());

                            //get Serial Number
                            Optional<M_ASSETS> getAsset = massetsRepo.findById(getAssetAssigment.get(0).getAssetId());
                            if(getAsset.isPresent()) {
                                addDataSp.setAssetSerialNumber(getAsset.get().getSerialNumber());
                            }

                            //get Asset Name
                            if(getAsset.get().getAssetName() != null) {
                                Optional<R_GLOBAL_TYPE_VALUE> getAssetName = rGlobalTypeValueRepo.findByGlbTypeValId(getAsset.get().getAssetName());
                                if(getAssetName.isPresent()) {
                                    addDataSp.setAssetName(getAssetName.get().getName());
                                }
                            }
                        }
                        newSpList.add(index2, addDataSp);
                        index2++;
                    }
                }

                addData.setServicePoint(newSpList);

                newDto.add(index, addData);
                index++;
            }

            PagedModel pagedData = assembler.toModel(accountAddress);

            Map<String, Object> d = new HashMap<>();
            d.put("result", newDto);
            d.put("page", pagedData.getMetadata());
            d.put("links", pagedData.getLinks());

            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage("Success View Detail Premise");
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
}

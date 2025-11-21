package com.dbs.module.account.detail.financialinformation.service;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNT_BILLING_BUCKET;
import com.dbs.database.crm.entities.ratingbillinginvoice.VW_BILLING_BUCKET;
import com.dbs.database.crm.repositories.accountmanagement.Account.MAccountBillingBucketRepo;
import com.dbs.database.crm.repositories.accountmanagement.VWAccountCriteriaRepo;
import com.dbs.database.crm.repositories.rbi.MBillingBucketCriteriaDataRepo;
import com.dbs.database.crm.repositories.rbi.MBillingBucketDetailRepo;
import com.dbs.database.crm.repositories.rbi.MRbiBillingBucketRepo;
import com.dbs.database.crm.repositories.rbi.view.VwBillingBucketRepo;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
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

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class BillingBucketService {
    private static final Logger logger = LoggerFactory.getLogger(BillingBucketService.class);
    
    @Autowired
    private VWAccountCriteriaRepo vwAccountCriteriaRepo;
    
    @Autowired
    private RGlobalTypeValueRepo glbRepo;
    
    @Autowired
    private MBillingBucketCriteriaDataRepo criteriaRepo;
    
    @Autowired
    private MRbiBillingBucketRepo billingRepo;
    
    @Autowired
    private MBillingBucketDetailRepo detailRepo;
    
    @Autowired
    private MAccountBillingBucketRepo accountBBRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VwBillingBucketRepo vwBillingBucketRepo;
    
    public ResponseEntity<ResponseObject> getListBillingBucket(Integer accountId, MaterialTablePagingRequest pagingData,
			PagedResourcesAssembler<VW_BILLING_BUCKET> assembler) {
            logger.info("Get Billing Bucket");
            try {
                
                Map<String, Object> filter = new HashMap<>();
                
                List<M_ACCOUNT_BILLING_BUCKET> allBB = accountBBRepo.findAllByAccountId(accountId);
                List<String> idBB = new ArrayList<>();
                for(M_ACCOUNT_BILLING_BUCKET allID : allBB){
                    idBB.add(allID.getBillingBucketId());
                }
                filter.put("listCode", idBB);

                Page<VW_BILLING_BUCKET> data;
                if (isNotBlank(pagingData.getSearchs())) {
                    Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                    for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        pagingData.getSearch().add(key+"~"+value);
                    }
                }
                if(!pagingData.getSearch().isEmpty()) {
                    data = vwBillingBucketRepo.findAll(vwBillingBucketRepo.getSpecificationFromFilters(pagingData, filter), PagingUtils.getPaging(pagingData));
                } else {
                    data = vwBillingBucketRepo.findAll(vwBillingBucketRepo.getSpecificationDefault(filter), PagingUtils.getPaging(pagingData));
                }
                
                List<LinkedHashMap<String, Object>> allData = data.getContent().stream()
                    .map(g -> {
                        
                        LinkedHashMap<String, Object> aa = new LinkedHashMap<>();
                        aa.put("billingBucketCode", g.getBillingBucketCode());
                        aa.put("billingBucketName", g.getBillingBucketName());
                        aa.put("category", "-");
                        aa.put("description", g.getDescription());

                        List<LinkedHashMap<String, Object>> bdetail = detailRepo.findAll().stream()
                            .filter(e -> Objects.equals(g.getBillingBucketCode(), e.getBillingBucketCode()))
                            .map(e -> {
                                LinkedHashMap<String, Object> ee = new LinkedHashMap<>();
                                ee.put("billingCode", e.getBillingItemCode());
                                ee.put("billingItem", "-");
                                ee.put("priority", e.getPriority());
                                ee.put("glAccount", "-");
                                ee.put("type", "-");
                                return ee;
                            })
                            .collect(Collectors.toList());

                        aa.put("details", bdetail);
                        return aa;
                    })
                    .collect(Collectors.toList());
                                
          
            PagedModel<EntityModel<VW_BILLING_BUCKET>> pagedData = assembler.toModel(data);
            Map<String, Object> d = new HashMap<>();
            d.put("result", allData);
            d.put("page", pagedData.getMetadata());
            d.put("links", pagedData.getLinks());
            return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK,
                    "Success get view", d), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(Constant.LOG_ERROR, e.getMessage(), e);
            return new ResponseEntity<>(
                    new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.INTERNAL_SERVER_ERROR,
                            ResponseUtils.MESSAGE_INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
}

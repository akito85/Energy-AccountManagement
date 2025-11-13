package com.dbs.module.account.detail.relationship.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.dbs.common.base.utils.AdvanceFilter;
import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.ctrl.PagingDTO;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.common.library.utils.UserDetailUtils;
import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNT;
import com.dbs.database.crm.entities.accountmanagement.VW_CUS_INFO_CC;
import com.dbs.database.crm.entities.accountmanagement.view.VW_RELATIONSHIP;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_ACCOUNT_INFORMATION;
import com.dbs.database.crm.entities.usermanagement.M_POSITION;
import com.dbs.database.crm.entities.usermanagement.M_USER;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.repositories.accountmanagement.Account.MAccountAddressRepo;
import com.dbs.database.crm.repositories.accountmanagement.Account.MAccountRepo;
import com.dbs.database.crm.repositories.accountmanagement.Account.MaddressRepo;
import com.dbs.database.crm.repositories.accountmanagement.Account.view.ViewRelationshipRepo;
import com.dbs.database.crm.repositories.usermanagement.MUserRepo;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
import com.dbs.module.account.main.dto.accountinformation.CustomerInformationDto;
import com.dbs.module.account.main.dto.accountinformation.FilterRequestDTO;
import com.dbs.module.account.main.dto.accountinformation.SearchFilterDTO;
import com.dbs.module.account.detail.relationship.dto.AccountRelationshipDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class AccountRelationshipService {
    private static Logger logger = LoggerFactory.getLogger(AccountRelationshipService.class);
    
    @Autowired
    private ViewRelationshipRepo ViewRelationshipRepo;

    @Autowired
    private MAccountAddressRepo mAccountAddressRepo;

    @Autowired
    private MaddressRepo maddressRepo;

    @Autowired
    private MAccountRepo mAccountRepo;

    @Autowired
    private MUserRepo mUserRepo;

    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("java:S1192")
    private final List<String> customerColumn = Arrays.asList("customerName","customerNumber","customerType","foundedBirthDate","firstName","identificationType","lastName","maritalStatus","middleName","foundedBirthPlace","searchKey","sex","customerIdentificationNumber","description");
    @SuppressWarnings("java:S1192")
    private final List<String> accountColumn = Arrays.asList("registrationNumber","accountGroup","accountNumber","accountName","accountSegment","accountGroupType","accountCategory","classificationType","accountType","paymentChannel","accountDescription","sor","costCenter","meterReadingCode","industrialSector","budgetYear","budget","teritory","taxIdentifierType","taxIdentifierNumber","taxIdentifierName","taxIdentifierAddress","taxRelationIdentifierType","taxRelationIdentifierNumber","taxRelationIdentifierName","taxRelationIdentifierAddress","priority","isCorporate","isException","isBadDebt","isSync","accountReferenceId","accountStatus","customerManagement","customerDescription","createdDate","createdBy","updatedDate","updatedBy");
    

    public ResponseEntity<ResponseObject> getAll(
        SearchFilterDTO dto, 
        MaterialTablePagingRequest pagingData,
        HttpServletRequest request,
        PagedResourcesAssembler<VW_RELATIONSHIP> assemblerDto,
        Integer accountId
    ) {
        ResponseObject result = new ResponseObject();
        try {
            Map<String, Object> filter = new HashMap<>();
            Map<String, Object> filterAccount = new HashMap<>();
            Page<VW_RELATIONSHIP> data;
            List<VW_RELATIONSHIP> listAccountData = null;
            
            // =======================================
            // FILTER SUPER USER
            // =======================================

            // boolean isItSuperUser = isItSuperUser(Integer.parseInt(UserDetailUtils.getUserId()));
            // logger.info("current entity --> " + UserDetailUtils.getUserEntity());
            // filter.put("entityId", UserDetailUtils.getUserEntity());
            // if(!isItSuperUser){
            //     Optional<M_POSITION> cekPosition = mPositionRepo.findById(UserDetailUtils.getPositionFromToken(request));
            //     if(cekPosition.isPresent() && cekPosition.get().getName().contains("CM ")) {
            //         filter.put("positionId", UserDetailUtils.getPositionFromToken(request));
            //         filter.put("pagingCustomerCm", "USE");
            //         logger.info("current positionId --> " + UserDetailUtils.getPositionFromToken(request));
            //     } else {
            //         filter.put("accountCostCenterId", UserDetailUtils.getPositionFromToken(request));
            //         filter.put("pagingCustomerHead", "USE");
            //         logger.info("current accountCostCenterId --> " + UserDetailUtils.getPositionFromToken(request));
            //     }

            //     // FILTER ACCOUNT
            //     filterAccount.put(Constant.UNIQUE_ACCOUNT, "USE");
            //     Optional<M_USER> user = userRepo.findByUsername(UserDetailUtils.getUsername());
            //     Optional<M_POSITION> cekPositionAccount = (user.get().getUserType().equalsIgnoreCase("EMP")?mPositionRepo.findById(UserDetailUtils.getPositionFromToken(request)): Optional.empty());
            //     filterAccount.put(Constant.ENTITY_ID, UserDetailUtils.getUserEntity());
            //     // admin entity
            //     if(user.get().getUserLevel().equalsIgnoreCase("AE")) {
            //         List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(UserDetailUtils.getPositionFromToken(request), GET_CC_CHILD);
            //         filterAccount.put("costCenterId", ccList);
            //         logger.info("AE costCenterId : " + ccList);
            //     }
            //     // end user (CM)
            //     else if(cekPositionAccount.isPresent() && Pattern.matches(".*\\bCM\\b.*", cekPositionAccount.get().getName())) {
            //         filterAccount.put("customerManagementId", UserDetailUtils.getPositionFromToken(request));
            //         logger.info("customerManagementId : " + UserDetailUtils.getPositionFromToken(request).toString() + " - " + cekPositionAccount.get().getName());
            //     }
            //     // end user (employee)
            //     else if(user.get().getUserType().equalsIgnoreCase("EMP")) {
            //         List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(UserDetailUtils.getPositionFromToken(request), GET_CC_CHILD);
            //         filterAccount.put("costCenterId", ccList);
            //         logger.info("costCenterId : " + ccList);
            //     }
            //     // end user (non employee)
            //     else if(user.get().getUserType().equalsIgnoreCase("NON_EMP")) {
            //         return new ResponseEntity<>(new ResponseObject(ResponseUtils.SUCCESS_TRUE, HttpStatus.OK, "Not employee", null), HttpStatus.OK);
            //     }
            // }
            // ==================================

            if (isNotBlank(pagingData.getSearchs())) {
                Map<String, Object> searchMap = objectMapper.readValue(pagingData.getSearchs(), HashMap.class);
                for (Map.Entry<String, Object> entry : searchMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    pagingData.getSearch().add(key+"~"+value);
                }
            }

            Specification<VW_RELATIONSHIP> specification = pagingData.getSearch().isEmpty()
                    ? ViewRelationshipRepo.getSpecificationDefault(filter)
                    : ViewRelationshipRepo.getSpecificationFromFilters(pagingData, filter);

            Specification<VW_RELATIONSHIP> specificationAccount;

            // ADVANCED FILTER
            List<FilterRequestDTO> filterList= dto.getInputFields();
            List<AdvanceFilter> advanceFilterCustomer = new ArrayList<>();
            List<AdvanceFilter> advanceFilterAccount = new ArrayList<>();
            if(!filterList.isEmpty() && !ObjectUtils.isEmpty(filterList.get(0).getColumn())){
                int i = 0 ;
                for(FilterRequestDTO frd: filterList){
                    AdvanceFilter advanceFilter = new AdvanceFilter();
                    Optional<R_GLOBAL_TYPE_VALUE> columnOpt = rGlobalTypeValueRepo.findByGlbTypeValId(frd.getColumn());
                    Optional<R_GLOBAL_TYPE_VALUE> operatorOpt = rGlobalTypeValueRepo.findByGlbTypeValId(frd.getOperator());
                    if(i ==0){
                        advanceFilter.setCondition("AND");
                    }else{
                        Optional<R_GLOBAL_TYPE_VALUE> conditionOpt = rGlobalTypeValueRepo.findByGlbTypeValId(frd.getCondition());
                        if(conditionOpt.isPresent()){
                            advanceFilter.setCondition(conditionOpt.get().getGlbValue());
                        }
                    }

                    if(operatorOpt.isPresent()){
                        advanceFilter.setOperator(operatorOpt.get().getGlbValue());
                    }
                    advanceFilter.setValue(frd.getValue());
                    if(columnOpt.isPresent()){
                        advanceFilter.setColumn(columnOpt.get().getGlbValue());
                        if(this.customerColumn.contains(columnOpt.get().getGlbValue())){
                            advanceFilterCustomer.add(advanceFilter);
                        }else if(this.accountColumn.contains(columnOpt.get().getGlbValue())){
                            advanceFilterAccount.add(advanceFilter);
                        }
                    }
                    i++;
                }

                // ACCOUNT
                // if(!advanceFilterAccount.isEmpty()){
                //     specificationAccount = ViewRelationshipRepo.getSpecificationFromAdvanceFilters(advanceFilterAccount, filterAccount);
                //     listAccountData = ViewRelationshipRepo.findAll(specificationAccount);
                //     List<Integer> customerIdFiltering = listAccountData.stream()
                //             .map(VW_RELATIONSHIP::getId)
                //             .distinct()
                //             .collect(Collectors.toList());
                //     specification = ViewRelationshipRepo.getSpecificationFromAdvanceFiltersWithAccount(advanceFilterCustomer, specification, customerIdFiltering);
                // } else {
                //     specification = ViewRelationshipRepo.getSpecificationFromAdvanceFilters(advanceFilterCustomer, specification);
                // }
            }

            data = ViewRelationshipRepo.findAll(specification, PagingUtils.getPaging(pagingData));
            List<AccountRelationshipDTO> listData = new ArrayList<>();

            for(VW_RELATIONSHIP c : data.getContent()) {
                AccountRelationshipDTO accountRelationshipDTO = new AccountRelationshipDTO();
                accountRelationshipDTO.setId(c.getId());
                accountRelationshipDTO.setDirectionalFlag(c.getDirectionalFlag());
                accountRelationshipDTO.setRelationshipType(c.getRelationshipType());
                accountRelationshipDTO.setRelationshipCategory(c.getRelationshipCategory());
                accountRelationshipDTO.setSubjectId(c.getSubjectId());
                accountRelationshipDTO.setSubjectName(c.getSubjectName());
                accountRelationshipDTO.setSubjectValue(c.getSubjectValue());
                accountRelationshipDTO.setObjectId(c.getObjectId());
                accountRelationshipDTO.setObjectName(c.getObjectName());
                accountRelationshipDTO.setObjectValue(c.getObjectValue());
                accountRelationshipDTO.setStartDate(c.getStartDate());
                accountRelationshipDTO.setEndDate(c.getEndDate());
                accountRelationshipDTO.setDescription(c.getDescription());
                accountRelationshipDTO.setStatus(c.getStatus());
                accountRelationshipDTO.setStatusApproval(c.getStatusApproval());
                
                // LIST ACCOUNT
                filterAccount.put("id", c.getId());

                // if(!advanceFilterAccount.isEmpty()){
                //     specificationAccount = ViewRelationshipRepo.getSpecificationFromAdvanceFilters(advanceFilterAccount, filterAccount);
                //     listAccountData = ViewRelationshipRepo.findAll(specificationAccount);
                // }else{
                //     specificationAccount = ViewRelationshipRepo.getSpecificationDefault2(filterAccount);
                //     listAccountData = ViewRelationshipRepo.findAll(specificationAccount);
                // }
                // accountRelationshipDTO.setAllAccount(listAccountData);

                // if(!advanceFilterAccount.isEmpty() && listAccountData.isEmpty()) {
                //     continue;
                // } else {
                // }
                listData.add(accountRelationshipDTO);
            }
            PagedModel pagedData = assemblerDto.toModel(data);

            Map<String, Object> d = new HashMap<>();
            d.put(Constant.RESULT, listData);
            d.put(Constant.PAGE, pagedData.getMetadata());
            d.put(Constant.LINK, pagedData.getLinks());

            result.setSuccess(true);
            result.setCode(HttpStatus.OK);
            result.setMessage("Success Get List Customer");
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

    // =======================================
    //  GET DETAIL BY ID
    // =======================================
    public ResponseEntity<VW_RELATIONSHIP> getById(Integer id) {
        Optional<VW_RELATIONSHIP> relationship = ViewRelationshipRepo.findById(id);
        if (relationship.isPresent()) {
            return ResponseEntity.ok(relationship.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // =======================================
    // helper function
    // =======================================
    private Boolean isItSuperUser(Integer userId){
        Optional<M_USER> userInformation = mUserRepo.findByUserId(userId);
        if(!userInformation.isPresent()){
            return Boolean.FALSE;
        }

        return  userInformation.get().getUserLevel().equalsIgnoreCase("SU") ? Boolean.TRUE : Boolean.FALSE;
    }

    
}

package com.dbs.database.crm.repositories.accountmanagement;

import com.dbs.common.base.utils.AdvanceFilter;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_ACCOUNT_INFORMATION;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import static com.dbs.common.base.utils.Constant.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VWAccountInfoRepo extends PagingAndSortingRepository<VW_ACCOUNT_INFORMATION,Integer>, JpaSpecificationExecutor<VW_ACCOUNT_INFORMATION> {
    default Specification<VW_ACCOUNT_INFORMATION> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_ACCOUNT_INFORMATION> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_ACCOUNT_INFORMATION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter,false);
        return specification;
    }
    default Specification<VW_ACCOUNT_INFORMATION> getSpecificationFromAdvanceFilters(List<AdvanceFilter> pagingdata, Map<String, Object> filter) {
        Specification<VW_ACCOUNT_INFORMATION> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (AdvanceFilter sr : pagingdata) {
            if(i == 0){
                specification = (Specification<VW_ACCOUNT_INFORMATION>) where(PagingUtils.createSpecification(sr.getColumn()+"~"+ sr.getValue(), sr.getOperator()));
            }else{
                if(sr.getCondition().equalsIgnoreCase("AND")){
                    specification = specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createSpecification(sr.getColumn(), sr.getValue(), sr.getOperator()));
                }else if(sr.getCondition().equalsIgnoreCase("OR")){
                    specification = specification.or((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createSpecification(sr.getColumn(), sr.getValue(), sr.getOperator()));
                }
            }
            i++;
        }
        specification = specification.and(addDefaultFilters2(specification, filter, true));

        return specification;
    }

    default Specification<VW_ACCOUNT_INFORMATION> getCustomerIdSpecification(Specification<VW_ACCOUNT_INFORMATION> specification, Integer customerId){
        specification = specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createSpecification("customerId~"+customerId.toString(),EQUALS_SELECTOR));
        return specification;

    }

//    default Specification<VW_ACCOUNT_INFORMATION> getSpecificationFromAdvanceFiltersTry(List<AdvanceFilter> pagingdata, Map<String, Object> filter) {
//        Specification<VW_ACCOUNT_INFORMATION> specification = null;
//        //Add Filter From Front End
//        int i = 0;
//        for (AdvanceFilter sr : pagingdata) {
//            if(i == 0){
//                specification = (Specification<VW_ACCOUNT_INFORMATION>) where(PagingUtils.createSpecification(sr.getColumn()+"~"+ sr.getValue(), sr.getOperator()));
//            }else{
//                if(sr.getCondition().equalsIgnoreCase("AND")){
//                    specification = specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createSpecification(sr.getColumn(), sr.getValue(), sr.getOperator()));
//                }else if(sr.getCondition().equalsIgnoreCase("OR")){
//                    specification = specification.or((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createSpecification(sr.getColumn(), sr.getValue(), sr.getOperator()));
//                }
//            }
//            i++;
//        }
//        specification = addCustomerFilterTry(specification,filter);
//
//        return specification;
//    }

    default Specification<VW_ACCOUNT_INFORMATION> getInCustomerIdSpecificationAndCustomerManagementIdSpecification(Integer customerManagementId, List<Integer> listCustomer){
        Specification<VW_ACCOUNT_INFORMATION> specification = (Specification<VW_ACCOUNT_INFORMATION>) where(PagingUtils.createINSpecification("customerId",listCustomer));
        specification = specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createSpecification("customerManagementId",customerManagementId.toString(),EQUALS_SELECTOR));
        return specification;

    }
    default Specification<VW_ACCOUNT_INFORMATION> getInCustomerIdSpecificationAndCostCenterIdSpecification(Integer costCenterId, List<Integer> listCustomer){
        Specification<VW_ACCOUNT_INFORMATION> specification = (Specification<VW_ACCOUNT_INFORMATION>) where(PagingUtils.createINSpecification("customerId",listCustomer));
        specification = specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createSpecification("costCenterId",costCenterId.toString(),EQUALS_SELECTOR));
        return specification;

    }

    default Specification<VW_ACCOUNT_INFORMATION> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_ACCOUNT_INFORMATION> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_ACCOUNT_INFORMATION> addDefaultFilters(Specification<VW_ACCOUNT_INFORMATION> specification, Map<String, Object> filter, Boolean isFirst){
        if(filter.get("accountGroup") != null) {
            specification = (Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createAccountGroupFilter(specification, filter.get("accountGroup").toString(), Boolean.TRUE);
        }

        if(filter.get("entityId") != null){
            if(specification != null) {
                specification = specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst));
            } else {
                specification = (Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
            }

        }
        if(filter.get("customerManagementId") != null){
            specification = specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createSpecification("customerManagementId"+"~"+filter.get("customerManagementId").toString(), EQUALS_SELECTOR));
        }
        if(filter.get("costCenterId") != null){
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("costCenterId").toString()), GET_CC_CHILD);
            specification = specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createCostCenterFilterForCostCenterIdColumnName(specification, ccList, false));
        }
        if(filter.get("customerId") != null){
            specification = specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createSpecification("customerId"+"~"+filter.get("customerId").toString(), EQUALS_SELECTOR));
        }
        if(filter.get("uniqueAccount") != null){
            if(specification != null) {
                specification = specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createSpecification("uniqueAccount"+"~"+filter.get("uniqueAccount").toString(), EQUALS_SELECTOR));
            } else {
                specification = (Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createSpecification("uniqueAccount"+"~"+filter.get("uniqueAccount").toString(), EQUALS_SELECTOR);
            }
        }
        if(filter.get("positionId") != null){
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()), GET_CC_CHILD);
            specification = (Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createCostCenterFilterForCostCenterIdColumnName(specification, ccList, false);
        }
        return specification;
    }
    default Specification<VW_ACCOUNT_INFORMATION> addCustomerFilter(Specification<VW_ACCOUNT_INFORMATION> specification, Map<String, Object> filter){
        if(filter.get("customerId") != null){
            specification = specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createSpecification("customerId"+"~"+filter.get("customerId"), EQUALS_SELECTOR));
//            if(listCustomer.size()>0){
//                specification = specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createINSpecification("customerId", listCustomer));
//            }
        }
        return specification;
    }

//    default Specification<VW_ACCOUNT_INFORMATION> addCustomerFilterTry(Specification<VW_ACCOUNT_INFORMATION> specification, Map<String, Object> filter){
//        if(filter != null){
//            specification = specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createSpecification("customerId"+"~"+filter.get("customerId"), EQUALS_SELECTOR));
////            if(listCustomer.size()>0){
////                specification = specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createINSpecification("customerId", listCustomer));
////            }
//        }
//        return specification;
//    }

    default Specification<VW_ACCOUNT_INFORMATION> getSpecificationFromFilters2(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_ACCOUNT_INFORMATION> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_ACCOUNT_INFORMATION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters2(specification, filter,false);
        return specification;
    }

    default Specification<VW_ACCOUNT_INFORMATION> getSpecificationDefault2(Map<String, Object> filter) {
        Specification<VW_ACCOUNT_INFORMATION> specification = null;
        specification = addDefaultFilters2(specification, filter, true);
        return specification;
    }

    default Specification<VW_ACCOUNT_INFORMATION> addDefaultFilters2(Specification<VW_ACCOUNT_INFORMATION> specification, Map<String, Object> filter, Boolean isFirst){
        if(filter.get("accountGroup") != null) {
            if(specification != null) {
                specification = specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createAccountGroupFilter(specification, filter.get("accountGroup").toString(), Boolean.TRUE));
            } else {
                specification = (Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createAccountGroupFilter(specification, filter.get("accountGroup").toString(), Boolean.TRUE);
            }
        }
        if(filter.get("uniqueAccount") != null){
            if(specification != null) {
                specification = specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createSpecification("uniqueAccount"+"~"+filter.get("uniqueAccount").toString(), EQUALS_SELECTOR));
            } else {
                specification = (Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createSpecification("uniqueAccount"+"~"+filter.get("uniqueAccount").toString(), EQUALS_SELECTOR);
            }
        }

        if(filter.get("entityId") != null){
            if(specification != null) {
                specification = specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst));
            } else {
                specification = (Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
            }

        }
        if(filter.get("customerManagementId") != null){
            if(specification != null) {
                specification = specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createSpecification("customerManagementId"+"~"+filter.get("customerManagementId").toString(), EQUALS_SELECTOR));
            } else {
                specification = (Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createSpecification("customerManagementId"+"~"+filter.get("customerManagementId").toString(), EQUALS_SELECTOR);
            }
        }
        if(filter.get("costCenterId") != null){
            List<Integer> ccList = (List<Integer>) filter.get("costCenterId");
            if(specification != null) {
                specification =  specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createINSpecification("costCenterId", ccList));
            } else {
                specification = (Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createINSpecification("costCenterId", ccList);
            }
        }
        if(filter.get("customerId") != null){
            if(specification != null) {
                specification = specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createSpecification("customerId"+"~"+filter.get("customerId").toString(), EQUALS_SELECTOR));
            } else {
                specification = (Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createSpecification("customerId"+"~"+filter.get("customerId").toString(), EQUALS_SELECTOR);
            }
        }
        if(filter.get("positionId") != null){
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()), GET_CC_CHILD);
            if(specification != null) {
                specification = specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createCostCenterFilterForCostCenterIdColumnName(specification, ccList, false));
            } else {
                specification = (Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createCostCenterFilterForCostCenterIdColumnName(specification, ccList, false);
            }
        }
        return specification;
    }

    Optional<VW_ACCOUNT_INFORMATION> findByCustomerIdAndAccountId(Integer customerId, Integer accountId);
    Optional<VW_ACCOUNT_INFORMATION> findByAccountNumber(String accountNumber);
    List<VW_ACCOUNT_INFORMATION> findAllByAccountStatus(String accountStatus);

    Optional<VW_ACCOUNT_INFORMATION> findTopByCustomerId(Integer customerId);

    @Query(nativeQuery = true, value = "SELECT * FROM VW_ACCOUNT_INFORMATION \n" +
            "WHERE SOR_ID = :sorId \n" +
            "AND ENTITY_ID = :entityId")
    List<VW_ACCOUNT_INFORMATION> findAccNumbWSor (
            Integer sorId,
            Integer entityId);

    @Query(nativeQuery = true, value = "SELECT * FROM VW_ACCOUNT_INFORMATION \n" +
            "WHERE SOR_ID = :sorId \n" +
            "AND COST_CENTER_ID in :costCenterId \n" +
            "AND ENTITY_ID = :entityId")
    List<VW_ACCOUNT_INFORMATION> findAccNumbWCostCenter (
            Integer sorId,
            List<Integer> costCenterId,
            Integer entityId);

    @Query(nativeQuery = true, value = "SELECT * FROM VW_ACCOUNT_INFORMATION \n" +
            "WHERE SOR_ID = :sorId \n" +
            "AND ACCOUNT_SEGMENT_ID in :accountSegmentId \n " +
            "AND ENTITY_ID = :entityId")
    List<VW_ACCOUNT_INFORMATION> findAccNumbWAccSegment (
            Integer sorId,
            List<Integer> accountSegmentId,
            Integer entityId);

    @Query(nativeQuery = true, value = "SELECT * FROM VW_ACCOUNT_INFORMATION \n" +
            "WHERE SOR_ID = :sorId \n" +
            "AND COST_CENTER_ID in :costCenterId \n" +
            "AND ACCOUNT_SEGMENT_ID in :accountSegmentId \n" +
            "AND ENTITY_ID = :entityId")
    List<VW_ACCOUNT_INFORMATION> findAccNumbCcNAccSeg (
            Integer sorId,
            List<Integer> costCenterId,
            List<Integer> accountSegmentId,
            Integer entityId);

    @Query(nativeQuery = true, value = "SELECT * FROM VW_ACCOUNT_INFORMATION \n" +
            "WHERE SOR_ID = :sorId \n" +
            "AND COST_CENTER_ID in :costCenterId \n" +
            "AND METER_READING_CODE_ID in :meterReadingCodeId \n" +
            "AND ENTITY_ID = :entityId")
    List<VW_ACCOUNT_INFORMATION> findAccNumbWMeter (
            Integer sorId,
            List<Integer> costCenterId,
            List<Integer> meterReadingCodeId,
            Integer entityId);

    @Query(nativeQuery = true, value = "SELECT * FROM VW_ACCOUNT_INFORMATION \n" +
            "WHERE SOR_ID = :sorId \n" +
            "AND COST_CENTER_ID in :costCenterId \n" +
            "AND METER_READING_CODE_ID in :meterReadingCodeId \n" +
            "AND ACCOUNT_SEGMENT_ID in :accountSegmentId \n" +
            "AND ENTITY_ID = :entityId")
    List<VW_ACCOUNT_INFORMATION> findMeterNAccSeg (
            Integer sorId,
            List<Integer> costCenterId,
            List<Integer> meterReadingCodeId,
            List<Integer> accountSegmentId,
            Integer entityId);
    @Query(nativeQuery = true, value = "SELECT * FROM VW_ACCOUNT_INFORMATION \n" +
            "WHERE SOR_ID = :sorId \n" +
            "AND COST_CENTER_ID in :costCenterId \n" +
            "AND ACCOUNT_GROUP_TYPE_ID in :accountGroupTypeId \n" +
            "AND ACCOUNT_SEGMENT_ID in :accountSegmentId \n" +
            "AND ENTITY_ID = :entityId")
    List<VW_ACCOUNT_INFORMATION> findAccGroupNAccSeg (
            Integer sorId,
            List<Integer> costCenterId,
            List<Integer> accountGroupTypeId,
            List<Integer> accountSegmentId,
            Integer entityId);

    @Query(nativeQuery = true, value = "SELECT * FROM VW_ACCOUNT_INFORMATION \n" +
            "WHERE SOR_ID = :sorId \n" +
            "AND ACCOUNT_SEGMENT_ID in :accountSegmentId \n" +
            "AND ACCOUNT_GROUP_TYPE_ID in :accountGroupTypeId \n" +
            "AND ENTITY_ID = :entityId")
    List<VW_ACCOUNT_INFORMATION> findAccNumbWAccGroup (
            Integer sorId,
            List<Integer> accountSegmentId,
            List<Integer> accountGroupTypeId,
            Integer entityId);

    @Query(nativeQuery = true, value = "SELECT * FROM VW_ACCOUNT_INFORMATION \n" +
            "WHERE SOR_ID = :sorId \n" +
            "AND COST_CENTER_ID in :costCenterId \n" +
            "AND METER_READING_CODE_ID in :meterReadingCodeId \n" +
            "AND ACCOUNT_SEGMENT_ID in :accountSegmentId \n" +
            "AND ACCOUNT_GROUP_TYPE_ID in :accountGroupTypeId \n" +
            "AND ENTITY_ID = :entityId")
    List<VW_ACCOUNT_INFORMATION> findIn (
            Integer sorId,
            List<Integer> costCenterId,
            List<Integer> meterReadingCodeId,
            List<Integer> accountSegmentId,
            List<Integer> accountGroupTypeId,
            Integer entityId);

    List<VW_ACCOUNT_INFORMATION> findAllByCustomerId(Integer customerId);
    
    List<VW_ACCOUNT_INFORMATION> findAllByCustomerIdAndCustomerManagementId(Integer customerId, Integer customerManagementId);
    
    List<VW_ACCOUNT_INFORMATION> findAllByCustomerIdAndCostCenterId(Integer customerId, Integer costCenterId);
    
    List<VW_ACCOUNT_INFORMATION> findAllBySorIdAndEntityId(Integer sorId, Integer entityId);

    List<VW_ACCOUNT_INFORMATION> findAllByMeterReadingCodeIdAndEntityId(Integer mRc, Integer entityId);

    Optional<VW_ACCOUNT_INFORMATION> findByAccountId(Integer accountId);

    // FILTERING ADMIN ENTITY and EMPLOYEE
    List<VW_ACCOUNT_INFORMATION> findAllByCustomerIdAndUniqueAccountAndEntityIdAndCostCenterIdIn(Integer customerId, String uniqueAccount, Integer entityId, List<Integer> costCenterId);

    // FILTERING CUSTOMER MANAGEMENT
    List<VW_ACCOUNT_INFORMATION> findAllByCustomerIdAndUniqueAccountAndEntityIdAndCustomerManagementId(Integer customerId, String uniqueAccount, Integer entityId, Integer customerManagementId);

    // FILTERING FOR SU
    List<VW_ACCOUNT_INFORMATION> findAllByCustomerIdAndUniqueAccountAndEntityId(Integer customerId, String uniqueAccount, Integer entityId);
}

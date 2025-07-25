package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_AM_LATECHARGE_RULE;
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

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MAmLateChargeRuleRepo extends PagingAndSortingRepository<M_AM_LATECHARGE_RULE, Integer>, JpaSpecificationExecutor<M_AM_LATECHARGE_RULE> {

    default Specification<M_AM_LATECHARGE_RULE> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        org.springframework.data.jpa.domain.Specification<com.dbs.database.crm.entities.accountmanagement.M_AM_LATECHARGE_RULE> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<com.dbs.database.crm.entities.accountmanagement.M_AM_LATECHARGE_RULE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<com.dbs.database.crm.entities.accountmanagement.M_AM_LATECHARGE_RULE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFilters(specification, filter, false);
        /*code here*/

        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    default Specification<M_AM_LATECHARGE_RULE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_AM_LATECHARGE_RULE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_AM_LATECHARGE_RULE> addDefaultFilters(Specification<M_AM_LATECHARGE_RULE> specification, Map<String, Object> filter, Boolean isFirst){
//        if(specification == null) {
//    		specification =(Specification<M_AM_LATECHARGE>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR);
//        } else {
//    		specification =specification.and((Specification<M_AM_LATECHARGE>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR));
//        }
        if(isFirst){
            if(!ObjectUtils.isEmpty(filter.get("latechargeId"))){
                specification = (Specification<M_AM_LATECHARGE_RULE>) where(PagingUtils.createSpecification("latechargeId~"+filter.get("latechargeId").toString(),EQUALS_SELECTOR));
            }
        }else{
            if(!ObjectUtils.isEmpty(filter.get("latechargeId"))){
                specification =  specification.and((Specification<M_AM_LATECHARGE_RULE>) PagingUtils.createSpecification("latechargeId~"+filter.get("latechargeId").toString(),EQUALS_SELECTOR));
            }
        }
        return specification;
    }
    @Query(value = "SELECT * FROM M_AM_LATECHARGE_RULE WHERE M_AM_LATECHARGE_ID =:latechargeId AND STATUS = 'ACTIVE'", nativeQuery = true)
    Optional<M_AM_LATECHARGE_RULE> findByLatechargeIdAndStatusActive(Integer latechargeId);
    
    Optional<M_AM_LATECHARGE_RULE> findTopByLatechargeIdAndStatusOrderByCreatedDateDesc(Integer latechargeId, String status);

    Optional<M_AM_LATECHARGE_RULE> findByIdAndLatechargeId(Integer id, Integer lateChargeId);
    List<M_AM_LATECHARGE_RULE> findByLatechargeIdAndStatus(Integer latechargeId, String status);

    Optional<M_AM_LATECHARGE_RULE> findById(Integer id);

    Optional<List<M_AM_LATECHARGE_RULE>> findAllByLatechargeIdAndStatus(Integer latechargeId, String status);
    Optional<List<M_AM_LATECHARGE_RULE>> findAllByLatechargeIdAndStatusAndApprovalStatus(Integer latechargeId, String status, String approvalStatus);

    @Query(value = "SELECT * FROM M_AM_LATECHARGE_RULE malr WHERE malr.M_AM_LATECHARGE_ID = :latechargeId AND (malr.STATUS = 'ACTIVE' OR malr.APPROVAL_STATUS = 'WAITING_APPROVAL')", nativeQuery = true)
    List<M_AM_LATECHARGE_RULE> findAllByLatechargeIdAndStatusActiveOrWaitingApproval(Integer latechargeId);

    Optional<M_AM_LATECHARGE_RULE> findTopByLatechargeIdAndDocumentNumberIgnoreCase(Integer latechargeId, String documentNumber);


}

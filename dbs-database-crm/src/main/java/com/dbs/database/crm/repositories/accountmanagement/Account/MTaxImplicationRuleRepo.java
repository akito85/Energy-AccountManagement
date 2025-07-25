package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_AM_TAXIMPLICATION_RULE;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value= "crmTransactionManager")
public interface MTaxImplicationRuleRepo extends PagingAndSortingRepository<M_AM_TAXIMPLICATION_RULE, Integer>, JpaSpecificationExecutor<M_AM_TAXIMPLICATION_RULE> {

    default Specification<M_AM_TAXIMPLICATION_RULE> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        org.springframework.data.jpa.domain.Specification<com.dbs.database.crm.entities.accountmanagement.M_AM_TAXIMPLICATION_RULE> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<com.dbs.database.crm.entities.accountmanagement.M_AM_TAXIMPLICATION_RULE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<com.dbs.database.crm.entities.accountmanagement.M_AM_TAXIMPLICATION_RULE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_AM_TAXIMPLICATION_RULE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_AM_TAXIMPLICATION_RULE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_AM_TAXIMPLICATION_RULE> addDefaultFilters(Specification<M_AM_TAXIMPLICATION_RULE> specification, Map<String, Object> filter, Boolean isFirst){
//        if(specification == null) {
//    		specification =(Specification<M_AM_LATECHARGE>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR);
//        } else {
//    		specification =specification.and((Specification<M_AM_LATECHARGE>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR));
//        }
        if(isFirst){
            if(!ObjectUtils.isEmpty(filter.get("taximplicationId"))){
                specification = (Specification<M_AM_TAXIMPLICATION_RULE>) where(PagingUtils.createSpecification("taximplicationId~"+filter.get("taximplicationId").toString(),EQUALS_SELECTOR));
            }
        }else{
            if(!ObjectUtils.isEmpty(filter.get("taximplicationId"))){
                specification =  specification.and((Specification<M_AM_TAXIMPLICATION_RULE>) PagingUtils.createSpecification("taximplicationId~"+filter.get("taximplicationId").toString(),EQUALS_SELECTOR));
            }
        }
        return specification;
    }
    Optional<M_AM_TAXIMPLICATION_RULE> findByIdAndTaximplicationId(Integer id, Integer taxImplicationId);
    M_AM_TAXIMPLICATION_RULE findByTaximplicationId(Integer taximplicationId);
    List<M_AM_TAXIMPLICATION_RULE> findAll();
    Optional<M_AM_TAXIMPLICATION_RULE> findByTaximplicationIdAndStatus(Integer taxImplicationId, String status);

    List<M_AM_TAXIMPLICATION_RULE> findAllByTaximplicationId(Integer taxImplicationId);

    Optional<List<M_AM_TAXIMPLICATION_RULE>> findAllByTaximplicationIdAndStatus(Integer taximplicationId, String status);

    @Query(value = "SELECT * FROM M_AM_TAXIMPLICATION_RULE malr WHERE malr.M_AM_TAXIMPLICATION_ID = :taximplicationId AND (malr.STATUS = 'ACTIVE' OR malr.APPROVAL_STATUS = 'WAITING_APPROVAL')", nativeQuery = true)
    List<M_AM_TAXIMPLICATION_RULE> findAllByTaximplicationIdAndStatusActiveOrWaitingApproval(Integer taximplicationId);

    Optional<List<M_AM_TAXIMPLICATION_RULE>> findAllByTaximplicationIdAndStatusAndApprovalStatus(Integer taximplicationId, String status, String approvalStatus);

    Optional<M_AM_TAXIMPLICATION_RULE> findTopByTaximplicationIdAndStatusOrderByCreatedDateDesc(Integer taximplicationId, String status);

    Optional<M_AM_TAXIMPLICATION_RULE> findTopByTaximplicationIdAndDocumentNumberIgnoreCase(Integer taximplicationId, String documentNumber);
}

package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_POS;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MRbiPosRepo extends PagingAndSortingRepository<M_RBI_POS,Integer>, JpaSpecificationExecutor<M_RBI_POS> {
    default Specification<M_RBI_POS> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_RBI_POS> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_RBI_POS>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_RBI_POS>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFilters(specification, filter,false);
        /*code here*/

        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    default Specification<M_RBI_POS> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_RBI_POS> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_RBI_POS> addDefaultFilters(Specification<M_RBI_POS> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        specification = (Specification<M_RBI_POS>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);

        /*INFO: Add Cost Center Filter*/
//        List<String> tes = null;//Get Cost Center From Username To Be Done
        return specification;
    }

    List<M_RBI_POS> findAll ();
    
    Optional<List<M_RBI_POS>> findByPrefixCode(String code);
    Optional<List<M_RBI_POS>> findByPrefixCodeOrderByPosNumberDesc(String code);
    
    Optional<M_RBI_POS> findByPosNumber(String posNumber);
    
    Optional<M_RBI_POS> findByIdAndPosNumber(Integer id, String posNumber);
    
    Optional<List<M_RBI_POS>> findAllByStatusApproval(String statusApproval);

    List<M_RBI_POS> findAllByAccountNumberAndStatusApprovalIgnoreCaseOrderByBillingPeriodAsc(String accountNumber, String statusApproval);
    
    @Query(value = "SELECT COALESCE(TO_NUMBER(MAX(SUBSTR(POS_NUMBER, 8, 12))),0) FROM M_RBI_POS WHERE PREFIX_CODE = :prefix", nativeQuery = true)
    Integer findMaxCodeByPrefix(String prefix);
}

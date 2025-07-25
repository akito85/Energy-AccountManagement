package com.dbs.database.crm.repositories.rbi;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;

import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.domain.Specification;
import static org.springframework.data.jpa.domain.Specification.where;

import com.dbs.database.crm.entities.ratingbillinginvoice.T_ADJUSTMENT_BILLING_DETAIL;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository
//@Transactional(value = "crmTransactionManager")
public interface TAdjustmentBillingDetailRepo extends PagingAndSortingRepository<T_ADJUSTMENT_BILLING_DETAIL, Integer>, 
        JpaSpecificationExecutor<T_ADJUSTMENT_BILLING_DETAIL>, 
        JpaRepository<T_ADJUSTMENT_BILLING_DETAIL, Integer> {
    
    default Specification<T_ADJUSTMENT_BILLING_DETAIL> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<T_ADJUSTMENT_BILLING_DETAIL> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<T_ADJUSTMENT_BILLING_DETAIL>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<T_ADJUSTMENT_BILLING_DETAIL>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<T_ADJUSTMENT_BILLING_DETAIL> getSpecificationDefault(Map<String, Object> filter) {
        Specification<T_ADJUSTMENT_BILLING_DETAIL> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<T_ADJUSTMENT_BILLING_DETAIL> addDefaultFilters(Specification<T_ADJUSTMENT_BILLING_DETAIL> specification, Map<String, Object> filter, Boolean isFirst){
        if(specification == null) {
    		specification =(Specification<T_ADJUSTMENT_BILLING_DETAIL>) PagingUtils.createSpecification("billingBucketCode~" + filter.get("billingBucketCode"), EQUALS_SELECTOR);
        } else {
    		specification =specification.and((Specification<T_ADJUSTMENT_BILLING_DETAIL>) PagingUtils.createSpecification("billingBucketCode~" + filter.get("billingBucketCode"), EQUALS_SELECTOR));
        }
        return specification;
    }
    List<T_ADJUSTMENT_BILLING_DETAIL> findAllByAdjustmentId(Integer adjustmentId);

    @Query(nativeQuery = true, value = "SELECT * FROM T_ADJUSTMENT_BILLING_DETAIL tabd WHERE ADJUSTMENT_ID = :adjustmentId")
    List<T_ADJUSTMENT_BILLING_DETAIL> findAllByAdjustmentIdAndNotInItemCode (Integer adjustmentId);
    
    @Query("SELECT a FROM T_ADJUSTMENT_BILLING_DETAIL a WHERE a.adjustmentId = :adjustmentId AND a.id NOT IN :detailIds")
    List<T_ADJUSTMENT_BILLING_DETAIL> findNotIn(Integer adjustmentId, List<Integer> detailIds);
}

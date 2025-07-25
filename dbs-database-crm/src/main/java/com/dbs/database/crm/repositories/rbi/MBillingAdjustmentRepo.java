package com.dbs.database.crm.repositories.rbi;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import java.util.Map;
import org.springframework.data.jpa.domain.Specification;
import static org.springframework.data.jpa.domain.Specification.where;

import com.dbs.database.crm.entities.ratingbillinginvoice.T_RBI_ADJUSTMENT_BILLING;

//@Repository
//@Transactional(value = "crmTransactionManager")
public interface MBillingAdjustmentRepo extends PagingAndSortingRepository<T_RBI_ADJUSTMENT_BILLING, Integer>, JpaSpecificationExecutor<T_RBI_ADJUSTMENT_BILLING> {
    
    default Specification<T_RBI_ADJUSTMENT_BILLING> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<T_RBI_ADJUSTMENT_BILLING> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<T_RBI_ADJUSTMENT_BILLING>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<T_RBI_ADJUSTMENT_BILLING>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<T_RBI_ADJUSTMENT_BILLING> getSpecificationDefault(Map<String, Object> filter) {
        Specification<T_RBI_ADJUSTMENT_BILLING> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<T_RBI_ADJUSTMENT_BILLING> addDefaultFilters(Specification<T_RBI_ADJUSTMENT_BILLING> specification, Map<String, Object> filter, Boolean isFirst){
        if(specification == null) {
    		specification =(Specification<T_RBI_ADJUSTMENT_BILLING>) PagingUtils.createSpecification("billingBucketCode~" + filter.get("billingBucketCode"), EQUALS_SELECTOR);
        } else {
    		specification =specification.and((Specification<T_RBI_ADJUSTMENT_BILLING>) PagingUtils.createSpecification("billingBucketCode~" + filter.get("billingBucketCode"), EQUALS_SELECTOR));
        }
        return specification;
    }
}

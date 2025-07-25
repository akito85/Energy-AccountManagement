package com.dbs.database.crm.repositories.mastermanagement;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.R_PRICING_ADJUSTMENT_DETAIL;
import com.dbs.database.crm.entities.mastermanagement.R_PRICING_DETAIL;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface RPricingAdjustmentDetailRepo extends PagingAndSortingRepository<R_PRICING_ADJUSTMENT_DETAIL, Integer>,
        JpaSpecificationExecutor<R_PRICING_ADJUSTMENT_DETAIL> {
    default Specification<R_PRICING_ADJUSTMENT_DETAIL> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata,
                                                                                   Map<String, Object> filter) {
        Specification<R_PRICING_ADJUSTMENT_DETAIL> specification = null;
        // Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification = i == 0
                    ? (Specification<R_PRICING_ADJUSTMENT_DETAIL>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                    : specification
                    .and((Specification<R_PRICING_ADJUSTMENT_DETAIL>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /* INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc. */
        specification = addDefaultFilters(specification, filter, false);
        /* code here */

        /*------------------------------------------*/
        /* INFO: Add Addition Filter If Any, ex: filter with Join Column. */
        /* code here */

        return specification;
    }

    default Specification<R_PRICING_ADJUSTMENT_DETAIL> getSpecificationDefault(Map<String, Object> filter) {
        Specification<R_PRICING_ADJUSTMENT_DETAIL> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<R_PRICING_ADJUSTMENT_DETAIL> addDefaultFilters(Specification<R_PRICING_ADJUSTMENT_DETAIL> specification,
                                                                         Map<String, Object> filter, Boolean isFirst) {

        /* INFO: Add Entity Filter */
        return (Specification<R_PRICING_ADJUSTMENT_DETAIL>) PagingUtils.createSpecification
                ("pricingAdjustmentId~" + filter.get("pricingAdjustmentId"), DEFAULT_SELECTOR);
    }
    
    List<R_PRICING_ADJUSTMENT_DETAIL> findAllByPricingAdjustmentId(Integer pricingAdjustmentId);
    
	@Query("SELECT a FROM R_PRICING_ADJUSTMENT_DETAIL a WHERE a.pricingAdjustmentId = :pricingAdjustmentId AND a.id NOT IN :detailIds")
    List<R_PRICING_ADJUSTMENT_DETAIL> findNotIn(Integer pricingAdjustmentId, List<Integer> detailIds);
}

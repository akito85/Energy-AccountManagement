package com.dbs.database.crm.repositories.product;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.product.R_CRITERIA_DATA_PRICING_RULE;
import com.dbs.database.crm.entities.product.R_CRITERIA_DATA_PRICING_RULE;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface RCriteriaDataPricingRuleRepo extends PagingAndSortingRepository<R_CRITERIA_DATA_PRICING_RULE,String>, JpaSpecificationExecutor<R_CRITERIA_DATA_PRICING_RULE> {

    default Specification<R_CRITERIA_DATA_PRICING_RULE> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<R_CRITERIA_DATA_PRICING_RULE> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<R_CRITERIA_DATA_PRICING_RULE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<R_CRITERIA_DATA_PRICING_RULE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<R_CRITERIA_DATA_PRICING_RULE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<R_CRITERIA_DATA_PRICING_RULE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<R_CRITERIA_DATA_PRICING_RULE> addDefaultFilters(Specification<R_CRITERIA_DATA_PRICING_RULE> specification, Map<String, Object> filter, Boolean isFirst){

        /*INFO: Add Entity Filter*/
        return (Specification<R_CRITERIA_DATA_PRICING_RULE>) PagingUtils.createSpecification("pricingRuleId~"+filter.get("pricingRuleId").toString(), DEFAULT_SELECTOR);
    }
    Page<R_CRITERIA_DATA_PRICING_RULE> findAllByPricingRuleId(Integer id,Pageable var1);

    R_CRITERIA_DATA_PRICING_RULE findTopById(Integer id);
    List<R_CRITERIA_DATA_PRICING_RULE> findByPricingRuleId(Integer id);

	List<R_CRITERIA_DATA_PRICING_RULE> findAllByPricingRuleIdInAndIsDeleted(List<Integer> criteriaDataIds,
			Boolean b);
}

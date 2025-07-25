package com.dbs.database.crm.repositories.product;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.database.crm.entities.product.R_PRICING_RULE_CRITERIA_DATA;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.Query;


@Repository
@Transactional(value = "crmTransactionManager")
public interface RPricingRuleCriteriaDataRepo extends PagingAndSortingRepository<R_PRICING_RULE_CRITERIA_DATA, Integer>, JpaSpecificationExecutor<R_PRICING_RULE_CRITERIA_DATA> {

    default Specification<R_PRICING_RULE_CRITERIA_DATA> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<R_PRICING_RULE_CRITERIA_DATA> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<R_PRICING_RULE_CRITERIA_DATA>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<R_PRICING_RULE_CRITERIA_DATA>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<R_PRICING_RULE_CRITERIA_DATA> getSpecificationDefault(Map<String, Object> filter) {
        Specification<R_PRICING_RULE_CRITERIA_DATA> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<R_PRICING_RULE_CRITERIA_DATA> addDefaultFilters(Specification<R_PRICING_RULE_CRITERIA_DATA> specification, Map<String, Object> filter, Boolean isFirst){

        /*INFO: Add Entity Filter*/
        return (Specification<R_PRICING_RULE_CRITERIA_DATA>) PagingUtils.createSpecification("idPricingRule~"+filter.get("pricingRuleId"), DEFAULT_SELECTOR);
    }

    R_PRICING_RULE_CRITERIA_DATA findTopById(Integer id);
    
    List<R_PRICING_RULE_CRITERIA_DATA> findAllByIdPricingRule(Integer idPricingRule);

    @Query(value = "SELECT a FROM R_PRICING_RULE_CRITERIA_DATA a WHERE a.idPricingRule = :pricingRuleId AND a.id NOT IN :criteriaDataIds")
	List<R_PRICING_RULE_CRITERIA_DATA> findNotIn(Integer pricingRuleId, List<Integer> criteriaDataIds);

    List<R_PRICING_RULE_CRITERIA_DATA> findAllByAllCriteria (Boolean allCrit);
    
}

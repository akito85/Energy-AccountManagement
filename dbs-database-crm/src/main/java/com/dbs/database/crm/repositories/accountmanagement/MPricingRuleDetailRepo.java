package com.dbs.database.crm.repositories.accountmanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_PRICING_RULE_DETAIL;
import com.dbs.database.crm.entities.product.M_PRODUCT_CALCULATION_RULE;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MPricingRuleDetailRepo extends PagingAndSortingRepository<M_PRICING_RULE_DETAIL, Integer>, JpaSpecificationExecutor<M_PRICING_RULE_DETAIL> {
    default Specification<M_PRICING_RULE_DETAIL> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_PRICING_RULE_DETAIL> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_PRICING_RULE_DETAIL>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_PRICING_RULE_DETAIL>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_PRICING_RULE_DETAIL> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_PRICING_RULE_DETAIL> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_PRICING_RULE_DETAIL> addDefaultFilters(Specification<M_PRICING_RULE_DETAIL> specification, Map<String, Object> filter, Boolean isFirst){

        specification =  isFirst ? (Specification<M_PRICING_RULE_DETAIL>) where(PagingUtils.createSpecification("pricingRuleId~"+filter.get("pricingRuleId"), EQUALS_SELECTOR))
                :
                specification.and((Specification<M_PRICING_RULE_DETAIL>)PagingUtils.createSpecification("pricingRuleId~"+filter.get("pricingRuleId"),EQUALS_SELECTOR));
        specification = specification.and((Specification<M_PRICING_RULE_DETAIL>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), false));

        /*INFO: Add Cost Center Filter*/
        return specification;
    }

    List<M_PRICING_RULE_DETAIL> findAllByPricingRuleDetailIdInAndIsDeleted(List<Integer> pricingRuleDetailId, String isDeleted);

    List<M_PRICING_RULE_DETAIL> findAllByPricingRuleIdAndIsDeleted(Integer id,Boolean deleted);

    List<M_PRICING_RULE_DETAIL> findAllByPricingRuleId(Integer id);
    
    @Query("SELECT a FROM M_PRICING_RULE_DETAIL a WHERE a.pricingRuleId = :pricingRuleId AND a.pricingRuleDetailId NOT IN :detailIds")
    	List<M_PRICING_RULE_DETAIL> findNotIn(Integer pricingRuleId, List<Integer> detailIds);
}

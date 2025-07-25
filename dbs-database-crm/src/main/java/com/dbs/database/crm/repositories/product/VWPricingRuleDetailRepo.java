package com.dbs.database.crm.repositories.product;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_PRICING_RULE_DETAIL;
import com.dbs.database.crm.entities.product.VW_PRICING_RULE_DETAIL;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
public interface VWPricingRuleDetailRepo extends PagingAndSortingRepository<VW_PRICING_RULE_DETAIL, Integer>, JpaSpecificationExecutor<VW_PRICING_RULE_DETAIL> {
    default Specification<VW_PRICING_RULE_DETAIL> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_PRICING_RULE_DETAIL> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_PRICING_RULE_DETAIL>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_PRICING_RULE_DETAIL>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_PRICING_RULE_DETAIL> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_PRICING_RULE_DETAIL> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_PRICING_RULE_DETAIL> addDefaultFilters(Specification<VW_PRICING_RULE_DETAIL> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        return (Specification<VW_PRICING_RULE_DETAIL>) (PagingUtils.createSpecification("pricingRuleId~"+filter.get("pricingRuleId"),EQUALS_SELECTOR));
    }


    List<M_PRICING_RULE_DETAIL> findAllByPricingRuleDetailIdInAndIsDeleted(List<Integer> pricingRuleDetailId, String isDeleted);

    List<M_PRICING_RULE_DETAIL> findAllByPricingRuleIdAndIsDeleted(Integer id,Boolean deleted);
    
    List<VW_PRICING_RULE_DETAIL> findAllByPricingRuleId(Integer pricingRuleId);
}

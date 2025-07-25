package com.dbs.database.crm.repositories.product;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_PRICING_RULE_DETAIL;
import com.dbs.database.crm.entities.product.R_PRICING_RULE_CRITERIA;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface RPricingRuleCriteriaRepo extends PagingAndSortingRepository<R_PRICING_RULE_CRITERIA, Integer>,
		JpaSpecificationExecutor<R_PRICING_RULE_CRITERIA> {
	default Specification<R_PRICING_RULE_CRITERIA> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata,
			Map<String, Object> filter) {
		Specification<R_PRICING_RULE_CRITERIA> specification = null;
		// Add Filter From Front End
		int i = 0;
		for (String sr : pagingdata.getSearch()) {
			specification = i == 0
					? (Specification<R_PRICING_RULE_CRITERIA>) where(
							PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
					: specification.and((Specification<R_PRICING_RULE_CRITERIA>) PagingUtils.createSpecification(sr,
							DEFAULT_SELECTOR));
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

	default Specification<R_PRICING_RULE_CRITERIA> getSpecificationDefault(Map<String, Object> filter) {
		Specification<R_PRICING_RULE_CRITERIA> specification = null;
		specification = addDefaultFilters(specification, filter, true);
		return specification;
	}

	default Specification<R_PRICING_RULE_CRITERIA> addDefaultFilters(
			Specification<R_PRICING_RULE_CRITERIA> specification, Map<String, Object> filter, Boolean isFirst) {
		/* INFO: Add Entity Filter */
		specification = (Specification<R_PRICING_RULE_CRITERIA>) PagingUtils.createEntityFilter(specification,
				Integer.parseInt(filter.get("entityId").toString()), isFirst);

		return specification;
	}

	List<R_PRICING_RULE_CRITERIA> findAll();

	List<R_PRICING_RULE_CRITERIA> findAllByPricingRuleCriteriaIdInAndIsDeleted(List<Integer> detailIds, String b);

	Optional<R_PRICING_RULE_CRITERIA> findByPricingRuleCriteriaIdAndIsDeleted(Integer pricingRuleCriteriaId, String b);

	List<R_PRICING_RULE_CRITERIA> findByPricingRuleIdAndIsDeleted(Integer pricingRuleId, String b);

	@Query("SELECT a FROM R_PRICING_RULE_CRITERIA a WHERE a.pricingRuleId = :pricingRuleId AND a.pricingRuleCriteriaId NOT IN :detailIds")
	List<R_PRICING_RULE_CRITERIA> findNotIn(Integer pricingRuleId, List<Integer> detailIds);
}

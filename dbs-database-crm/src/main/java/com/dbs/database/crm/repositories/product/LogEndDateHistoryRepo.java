package com.dbs.database.crm.repositories.product;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.product.LOG_END_DATE_HISTORY;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.GET_CC_PARENT;
import java.util.Optional;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface LogEndDateHistoryRepo extends PagingAndSortingRepository<LOG_END_DATE_HISTORY, String>,
		JpaSpecificationExecutor<LOG_END_DATE_HISTORY> {

	default Specification<LOG_END_DATE_HISTORY> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata,
			Map<String, Object> filter) {
		Specification<LOG_END_DATE_HISTORY> specification = null;
		// Add Filter From Front End
		int i = 0;
		for (String sr : pagingdata.getSearch()) {
			specification = i == 0
					? (Specification<LOG_END_DATE_HISTORY>) where(
							PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
					: specification.and((Specification<LOG_END_DATE_HISTORY>) PagingUtils.createSpecification(sr,
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

	default Specification<LOG_END_DATE_HISTORY> getSpecificationDefault(Map<String, Object> filter) {
		Specification<LOG_END_DATE_HISTORY> specification = null;
		specification = addDefaultFilters(specification, filter, true);
		return specification;
	}

	default Specification<LOG_END_DATE_HISTORY> addDefaultFilters(
			Specification<LOG_END_DATE_HISTORY> specification, Map<String, Object> filter, Boolean isFirst) {

		specification = specification != null
				? specification.and((Specification<LOG_END_DATE_HISTORY>) PagingUtils
						.createSpecification("refId~" + filter.get("refId").toString(), "EQUALS"))
				: ((Specification<LOG_END_DATE_HISTORY>) PagingUtils
						.createSpecification("refId~" + filter.get("refId").toString(), "EQUALS"));

		return specification;
	}

	LOG_END_DATE_HISTORY findTopById(Integer id);

	Optional<LOG_END_DATE_HISTORY> findById(Integer id);

	Optional<List<LOG_END_DATE_HISTORY>> findAllByRefId(Integer refId);

}

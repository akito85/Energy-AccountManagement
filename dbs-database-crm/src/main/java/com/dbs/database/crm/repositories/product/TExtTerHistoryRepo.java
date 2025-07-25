package com.dbs.database.crm.repositories.product;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.product.T_EXT_TER_HISTORY;
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
public interface TExtTerHistoryRepo
		extends PagingAndSortingRepository<T_EXT_TER_HISTORY, String>, JpaSpecificationExecutor<T_EXT_TER_HISTORY> {

	default Specification<T_EXT_TER_HISTORY> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata,
			Map<String, Object> filter) {
		Specification<T_EXT_TER_HISTORY> specification = null;
		// Add Filter From Front End
		int i = 0;
		for (String sr : pagingdata.getSearch()) {
			specification = i == 0
					? (Specification<T_EXT_TER_HISTORY>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
					: specification
							.and((Specification<T_EXT_TER_HISTORY>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

	default Specification<T_EXT_TER_HISTORY> getSpecificationDefault(Map<String, Object> filter) {
		Specification<T_EXT_TER_HISTORY> specification = null;
		specification = addDefaultFilters(specification, filter, true);
		return specification;
	}

	default Specification<T_EXT_TER_HISTORY> addDefaultFilters(Specification<T_EXT_TER_HISTORY> specification,
			Map<String, Object> filter, Boolean isFirst) {

		specification = specification != null
				? specification.and((Specification<T_EXT_TER_HISTORY>) PagingUtils
						.createSpecification("refId~" + filter.get("refId").toString(), "EQUALS"))
				: ((Specification<T_EXT_TER_HISTORY>) PagingUtils
						.createSpecification("refId~" + filter.get("refId").toString(), "EQUALS"));

		specification = specification.and((Specification<T_EXT_TER_HISTORY>) PagingUtils
				.createSpecification("category~" + filter.get("category").toString(), "EQUALS"));
		return specification;
	}

	T_EXT_TER_HISTORY findTopById(Integer id);

	Optional<T_EXT_TER_HISTORY> findById(Integer id);

	Optional<List<T_EXT_TER_HISTORY>> findAllByCategoryAndRefId(String cat, Integer refId);
	
	Optional<List<T_EXT_TER_HISTORY>> findAllByCategoryAndRefIdAndType(String cat, Integer refId, String type);
	
	Optional<T_EXT_TER_HISTORY> findFirstByCategoryAndRefIdAndTypeAndStatus(String cat, Integer refId, String type, String status);

	T_EXT_TER_HISTORY findTopByRefId(Integer refId);
	
	T_EXT_TER_HISTORY findByTappId(Integer id);
}

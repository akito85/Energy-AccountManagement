package com.dbs.database.crm.repositories.product;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.product.M_PRODUCT_VERSION;
import com.dbs.database.crm.utils.CostCenterUtils;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.GET_CC_PARENT;
import static org.springframework.data.jpa.domain.Specification.where;

public interface MProductVersionRepo
		extends PagingAndSortingRepository<M_PRODUCT_VERSION, String>, JpaSpecificationExecutor<M_PRODUCT_VERSION> {

	default Specification<M_PRODUCT_VERSION> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata,
			Map<String, Object> filter) {
		Specification<M_PRODUCT_VERSION> specification = null;
		// Add Filter From Front End
		int i = 0;
		for (String sr : pagingdata.getSearch()) {
			specification = i == 0
					? (Specification<M_PRODUCT_VERSION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
					: specification.and(
							(Specification<M_PRODUCT_VERSION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
			i++;
		}
		/*------------------------------------------*/
		/* INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc. */
		specification = addDefaultFilters(specification, filter, specification == null ? true : false);
		/* code here */

		/*------------------------------------------*/
		/* INFO: Add Addition Filter If Any, ex: filter with Join Column. */
		/* code here */

		return specification;
	}

	default Specification<M_PRODUCT_VERSION> getSpecificationDefault(Map<String, Object> filter) {
		Specification<M_PRODUCT_VERSION> specification = null;
		specification = addDefaultFilters(specification, filter, true);
		return specification;
	}

	default Specification<M_PRODUCT_VERSION> addDefaultFilters(Specification<M_PRODUCT_VERSION> specification,
			Map<String, Object> filter, Boolean isFirst) {

		/* INFO: Add Entity Filter */
		specification = (Specification<M_PRODUCT_VERSION>) PagingUtils.createEntityFilter(specification,
				Integer.parseInt(filter.get("entityId").toString()), isFirst);
		CostCenterUtils costCenterUtils = new CostCenterUtils();
                List<Integer> ccList = costCenterUtils
				.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()), GET_CC_PARENT);

		/* INFO: Add Cost Center Filter */
		specification = (Specification<M_PRODUCT_VERSION>) PagingUtils.createCostCenterFilter(specification, ccList,
				false);
		// Add Product Id Filter //
		specification = specification.and((Specification<M_PRODUCT_VERSION>) PagingUtils
				.createSpecification("productId~" + filter.get("productId").toString(), DEFAULT_SELECTOR));

		return specification;
	}

	Optional<M_PRODUCT_VERSION> findTopById(Integer id);

	Optional<M_PRODUCT_VERSION> findById(Integer id);

	Optional<List<M_PRODUCT_VERSION>> findByProductIdAndStatus(Integer id, String status);

	Optional<M_PRODUCT_VERSION> findTopByProductIdAndStatusIgnoreCase(Integer productId, String status);

	Optional<List<M_PRODUCT_VERSION>> findByProductId(Integer id);

	Optional<List<M_PRODUCT_VERSION>> findAllByProductId(Integer id);

	@Query("SELECT a FROM M_PRODUCT_VERSION a WHERE a.productId = :id AND a.status IN ('ACTIVE', 'INACTIVE') ORDER BY a.createdDate DESC")
	Optional<List<M_PRODUCT_VERSION>> findAllByProductIdOrderByCreatedDateDesc(Integer id);

	@Query("SELECT a FROM M_PRODUCT_VERSION a WHERE a.productId = :id ORDER BY a.createdDate DESC")
	List<M_PRODUCT_VERSION> findByProductIdOrderByCreatedDateDesc(Integer id);

	M_PRODUCT_VERSION findByProductIdAndVersion(Integer productId, Integer version);

	Optional<M_PRODUCT_VERSION> findByIdAndStatus(Integer id, String status);
}

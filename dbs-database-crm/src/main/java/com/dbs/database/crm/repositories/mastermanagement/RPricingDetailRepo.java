package com.dbs.database.crm.repositories.mastermanagement;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.database.crm.entities.mastermanagement.R_PRICING_DETAIL;
import com.dbs.database.crm.entities.product.M_PRODUCT_PRICING;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;


@Repository
@Transactional(value = "crmTransactionManager")
public interface RPricingDetailRepo extends PagingAndSortingRepository<R_PRICING_DETAIL,String>, JpaSpecificationExecutor<R_PRICING_DETAIL> {

	default Specification<R_PRICING_DETAIL> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
		Specification<R_PRICING_DETAIL> specification = null;
		//Add Filter From Front End
		int i = 0;
		for (String sr : pagingdata.getSearch()) {
			specification =
					i == 0 ?
							(Specification<R_PRICING_DETAIL>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
							: specification.and((Specification<R_PRICING_DETAIL>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
			i++;
		}
		/*------------------------------------------*/
		/*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
		specification = addDefaultFilters(specification, filter, false);
		/*code here*/

		/*------------------------------------------*/
		/*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
		/*code here*/

		return specification;
	}

	default Specification<R_PRICING_DETAIL> getSpecificationDefault(Map<String, Object> filter) {
		Specification<R_PRICING_DETAIL> specification = null;
		specification = addDefaultFilters(specification, filter, true);
		return specification;
	}

	default Specification<R_PRICING_DETAIL> addDefaultFilters(Specification<R_PRICING_DETAIL> specification, Map<String, Object> filter, Boolean isFirst) {

		/*INFO: Add Entity Filter*/
		return (Specification<R_PRICING_DETAIL>) PagingUtils.createSpecification("idPricing~" + filter.get("idPricing"), DEFAULT_SELECTOR);
	}

	Optional<R_PRICING_DETAIL> findById(Integer idPricing);

	List<R_PRICING_DETAIL> findAllByIdPricingAndCurrency(Integer idPricing, String currency);

	List<R_PRICING_DETAIL> findAllByIdPricingAndCurrencyAndUom(Integer idPricing, String currency, String uom);

	List<R_PRICING_DETAIL> findAllByIdPricing(Integer idPricing);

	Optional<List<R_PRICING_DETAIL>> findByIdPricing(Integer idPricing);

	@Query("SELECT DISTINCT a.idPricing FROM R_PRICING_DETAIL a WHERE (a.endDate > CURRENT_DATE OR a.endDate is null) AND a.startDate <= CURRENT_DATE")
	Optional<List<Integer>> findByEndDate();

	@Query("SELECT a FROM R_PRICING_DETAIL a WHERE a.idPricing = :idPricing AND a.id NOT IN :detailIds")
	List<R_PRICING_DETAIL> findNotIn(Integer idPricing, List<Integer> detailIds);

}
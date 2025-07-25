package com.dbs.database.crm.repositories.accountmanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.R_PRICING_CRITERIA_DATA;
import com.dbs.database.crm.entities.mastermanagement.R_PRICING_CRITERIA;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface RPricingCriteriaDataRepo extends PagingAndSortingRepository<R_PRICING_CRITERIA_DATA, Integer>, JpaSpecificationExecutor<R_PRICING_CRITERIA_DATA> {
    default Specification<R_PRICING_CRITERIA_DATA> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<R_PRICING_CRITERIA_DATA> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<R_PRICING_CRITERIA_DATA>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<R_PRICING_CRITERIA_DATA>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<R_PRICING_CRITERIA_DATA> getSpecificationDefault(Map<String, Object> filter) {
        Specification<R_PRICING_CRITERIA_DATA> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<R_PRICING_CRITERIA_DATA> addDefaultFilters(Specification<R_PRICING_CRITERIA_DATA> specification, Map<String, Object> filter, Boolean isFirst) {

        return (Specification<R_PRICING_CRITERIA_DATA>)PagingUtils.createSpecification("idPricing~"+filter.get("idPricing"),DEFAULT_SELECTOR);
    }
    
    List<R_PRICING_CRITERIA_DATA> findAllByIdPricing(Integer idPricing);
	@Query("SELECT a FROM R_PRICING_CRITERIA_DATA a WHERE a.idPricing = :idPricing AND a.id NOT IN :detailIds")
	List<R_PRICING_CRITERIA_DATA> findNotIn(Integer idPricing, List<Integer> detailIds);
}

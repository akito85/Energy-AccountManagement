package com.dbs.database.crm.repositories.product;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.product.R_CRITERIA_DATA_PRODUCT_TAS;
import com.dbs.database.crm.entities.product.R_CRITERIA_DATA_PRODUCT_TAS;
import com.dbs.database.crm.entities.product.R_CRITERIA_DATA_PRODUCT_TAS;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface RCriteriaDataProductTasRepo extends PagingAndSortingRepository<R_CRITERIA_DATA_PRODUCT_TAS,Integer>, JpaSpecificationExecutor<R_CRITERIA_DATA_PRODUCT_TAS> {

    default Specification<R_CRITERIA_DATA_PRODUCT_TAS> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<R_CRITERIA_DATA_PRODUCT_TAS> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<R_CRITERIA_DATA_PRODUCT_TAS>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<R_CRITERIA_DATA_PRODUCT_TAS>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<R_CRITERIA_DATA_PRODUCT_TAS> getSpecificationDefault(Map<String, Object> filter) {
        Specification<R_CRITERIA_DATA_PRODUCT_TAS> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<R_CRITERIA_DATA_PRODUCT_TAS> addDefaultFilters(Specification<R_CRITERIA_DATA_PRODUCT_TAS> specification, Map<String, Object> filter, Boolean isFirst){

        return (Specification<R_CRITERIA_DATA_PRODUCT_TAS>) PagingUtils.createSpecification("idProductTas~"+filter.get("idProductTas").toString(), DEFAULT_SELECTOR);
    }
    Page<R_CRITERIA_DATA_PRODUCT_TAS> findAllByIdProductTas(Integer id,Pageable var1);

    R_CRITERIA_DATA_PRODUCT_TAS findTopById(Integer id);
    List<R_CRITERIA_DATA_PRODUCT_TAS> findByIdProductTas(Integer id);

	List<R_CRITERIA_DATA_PRODUCT_TAS> findAllByIdProductTasInAndIsDeleted(List<Integer> criteriaDataIds,
			Boolean b);
	List<R_CRITERIA_DATA_PRODUCT_TAS> findAllByIdProductTas(Integer criteriaDataId);
	
	@Query("SELECT a FROM R_CRITERIA_DATA_PRODUCT_TAS a WHERE a.idProductTas = :productId AND a.id NOT IN :detailIds")
    List<R_CRITERIA_DATA_PRODUCT_TAS> findNotIn(Integer productId, List<Integer> detailIds);
}

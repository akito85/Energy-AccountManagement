package com.dbs.database.crm.repositories.product;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.product.M_PRODUCT_DETAIL;
import com.dbs.database.crm.entities.product.M_PRODUCT_DETAIL;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
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
public interface MProductDetailRepo extends PagingAndSortingRepository<M_PRODUCT_DETAIL, Integer>, JpaSpecificationExecutor<M_PRODUCT_DETAIL> {
    default Specification<M_PRODUCT_DETAIL> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_PRODUCT_DETAIL> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_PRODUCT_DETAIL>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_PRODUCT_DETAIL>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_PRODUCT_DETAIL> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_PRODUCT_DETAIL> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_PRODUCT_DETAIL> addDefaultFilters(Specification<M_PRODUCT_DETAIL> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
    	if(specification == null)
    		specification =(Specification<M_PRODUCT_DETAIL>) PagingUtils.createSpecification("productVersionId~" + filter.get("productId"), EQUALS_SELECTOR);
    	else
    		specification =specification.and((Specification<M_PRODUCT_DETAIL>) PagingUtils.createSpecification("productVersionId~" + filter.get("productId"), EQUALS_SELECTOR));
    	return specification;
    }
    Page<M_PRODUCT_DETAIL> findAllByProductVersionId(Integer id,Pageable var1);

   Optional<M_PRODUCT_DETAIL> findTopById(Integer id);
   
    @Query("SELECT a FROM M_PRODUCT_DETAIL a WHERE a.productVersionId = :productId AND a.id NOT IN :detailIds")
    List<M_PRODUCT_DETAIL> findNotIn(Integer productId, List<Integer> detailIds);
    
    List<M_PRODUCT_DETAIL> findAllByProductVersionId(Integer productVersionId);
}

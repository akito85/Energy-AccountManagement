package com.dbs.database.crm.repositories.product;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.product.R_PRODUCT_TOS;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;
import java.util.Map;

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
public interface RProductTermOfServiceRepo extends PagingAndSortingRepository<R_PRODUCT_TOS,Integer>, JpaSpecificationExecutor<R_PRODUCT_TOS> {

    default Specification<R_PRODUCT_TOS> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<R_PRODUCT_TOS> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<R_PRODUCT_TOS>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<R_PRODUCT_TOS>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<R_PRODUCT_TOS> getSpecificationDefault(Map<String, Object> filter) {
        Specification<R_PRODUCT_TOS> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<R_PRODUCT_TOS> addDefaultFilters(Specification<R_PRODUCT_TOS> specification, Map<String, Object> filter, Boolean isFirst){

        /*INFO: Add Entity Filter*/
        return (Specification<R_PRODUCT_TOS>) PagingUtils.createSpecification("idMProductTos~"+filter.get("idMProductTos").toString(), DEFAULT_SELECTOR);
    }
    Page<R_PRODUCT_TOS> findAllByIdMProductTos(Integer id,Pageable var1);

    List<R_PRODUCT_TOS> findAllByIdMProductTosOrderByIdMProductTosAsc(Integer idMProductTos);

    R_PRODUCT_TOS findTopById(Integer id);
    List<R_PRODUCT_TOS> findByIdMProductTos(Integer id);
    List<R_PRODUCT_TOS> findAllByIdMProductTos(Integer idMProductTos);

    @Query("SELECT a FROM R_PRODUCT_TOS a WHERE a.idMProductTos = :productId AND a.id NOT IN :detailIds")
    List<R_PRODUCT_TOS> findNotIn(Integer productId, List<Integer> detailIds);

}

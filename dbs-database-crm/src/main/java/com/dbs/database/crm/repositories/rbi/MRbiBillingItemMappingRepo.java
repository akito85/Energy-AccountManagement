package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_BILLING_ITEM_CATEGORY;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_BILLING_ITEM_MAPPING;
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
public interface MRbiBillingItemMappingRepo extends PagingAndSortingRepository<M_RBI_BILLING_ITEM_MAPPING, Integer>, JpaSpecificationExecutor<M_RBI_BILLING_ITEM_MAPPING> {
    default Specification<M_RBI_BILLING_ITEM_MAPPING> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_RBI_BILLING_ITEM_MAPPING> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_RBI_BILLING_ITEM_MAPPING>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_RBI_BILLING_ITEM_MAPPING>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_RBI_BILLING_ITEM_MAPPING> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_RBI_BILLING_ITEM_MAPPING> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_RBI_BILLING_ITEM_MAPPING> addDefaultFilters(Specification<M_RBI_BILLING_ITEM_MAPPING> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        return specification;
    }
    List<M_RBI_BILLING_ITEM_MAPPING> findAll();

    Optional<M_RBI_BILLING_ITEM_MAPPING> findByrMappingId(Integer rMappingId);

    @Query(value = "SELECT a.* FROM M_BILLING_ITEM_MAPPING a JOIN M_BILLING_ITEM_CATEGORY b ON b.R_CATEGORY_ID = a.R_CATEGORY_ID AND a.STATUS IN ('ACTIVE','DRAFT') AND a.END_DATE IS NULL OR a.END_DATE >= SYSDATE where a.item = :item and b.CATEGORY = :categoryId", nativeQuery = true)
    List<Optional<M_RBI_BILLING_ITEM_MAPPING>> findByCategoryAndItem(Integer categoryId, String item);

    @Query("SELECT a FROM M_RBI_BILLING_ITEM_MAPPING a WHERE a.rCategoryId IN :rCategoryId AND a.rMappingId NOT IN :rMappingId")
    List<M_RBI_BILLING_ITEM_MAPPING> findNotIn(List<Integer> rCategoryId, List<Integer> rMappingId);
    
    @Query("SELECT a FROM M_RBI_BILLING_ITEM_MAPPING a WHERE a.rCategoryId IN :rCategoryId")
    List<M_RBI_BILLING_ITEM_MAPPING> findIn(List<Integer> rCategoryId);
    
    List<M_RBI_BILLING_ITEM_MAPPING> findByrCategoryId(Integer rCategoryId);
}

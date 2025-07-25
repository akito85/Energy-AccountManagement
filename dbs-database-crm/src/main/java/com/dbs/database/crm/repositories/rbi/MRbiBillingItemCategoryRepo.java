package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_BILLING_ITEM_CATEGORY;
import com.dbs.database.crm.entities.usermanagement.T_EMP_ASSIGNMENT;
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
public interface MRbiBillingItemCategoryRepo extends PagingAndSortingRepository<M_RBI_BILLING_ITEM_CATEGORY, Integer>, JpaSpecificationExecutor<M_RBI_BILLING_ITEM_CATEGORY> {
    default Specification<M_RBI_BILLING_ITEM_CATEGORY> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_RBI_BILLING_ITEM_CATEGORY> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_RBI_BILLING_ITEM_CATEGORY>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_RBI_BILLING_ITEM_CATEGORY>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_RBI_BILLING_ITEM_CATEGORY> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_RBI_BILLING_ITEM_CATEGORY> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_RBI_BILLING_ITEM_CATEGORY> addDefaultFilters(Specification<M_RBI_BILLING_ITEM_CATEGORY> specification, Map<String, Object> filter, Boolean isFirst) {
        /*INFO: Add Entity Filter*/
        return specification;
    }

    List<M_RBI_BILLING_ITEM_CATEGORY> findByBillingItemCode(String billingItemCode);

    Optional<M_RBI_BILLING_ITEM_CATEGORY> findByrCategoryId(Integer rCategoryId);

    @Query("SELECT a FROM M_RBI_BILLING_ITEM_CATEGORY a WHERE a.billingItemCode = :billingItemCode AND a.rCategoryId NOT IN :rCategoryId")
    List<M_RBI_BILLING_ITEM_CATEGORY> findNotIn(String billingItemCode, List<Integer> rCategoryId);
    
    Optional<List<M_RBI_BILLING_ITEM_CATEGORY>> findByCategory(Integer category);
}

package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_BILLING_ITEM_TAX;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwBillingItemTaxRepo extends PagingAndSortingRepository<VW_BILLING_ITEM_TAX, Integer>, JpaSpecificationExecutor<VW_BILLING_ITEM_TAX> {
    default Specification<VW_BILLING_ITEM_TAX> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_BILLING_ITEM_TAX> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_BILLING_ITEM_TAX>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_BILLING_ITEM_TAX>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_BILLING_ITEM_TAX> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_BILLING_ITEM_TAX> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_BILLING_ITEM_TAX> addDefaultFilters(Specification<VW_BILLING_ITEM_TAX> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        return specification;
    }
    Optional<VW_BILLING_ITEM_TAX> findByItem(String item);
}

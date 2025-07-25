package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_BILLING_ITEM;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MRbiBillingItemRepo extends PagingAndSortingRepository<M_RBI_BILLING_ITEM, Integer>, JpaSpecificationExecutor<M_RBI_BILLING_ITEM> {
    List<M_RBI_BILLING_ITEM> findAllByStatus(String status);
    List<M_RBI_BILLING_ITEM> findAllByStatusAndBillingType(String status, String billingType);
    Optional<M_RBI_BILLING_ITEM> findByBillingItemCode(String code);
    Optional<M_RBI_BILLING_ITEM> findByBillingItemNameIgnoreCase(String billingItemName);
    @Override
    public List<M_RBI_BILLING_ITEM> findAll();
    default Specification<M_RBI_BILLING_ITEM> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<M_RBI_BILLING_ITEM> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_RBI_BILLING_ITEM>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_RBI_BILLING_ITEM>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<M_RBI_BILLING_ITEM> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_RBI_BILLING_ITEM> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_RBI_BILLING_ITEM> addDefaultFilters(Specification<M_RBI_BILLING_ITEM> specification,
                                                                Map<String, Object> filter, Boolean isFirst) {
        return specification;
    }

    Optional<M_RBI_BILLING_ITEM> findByBillingItemNameIgnoreCaseAndStatus(String billingItemName, String status);
    
    @Query(value = "SELECT COALESCE(TO_NUMBER(MAX(SUBSTR(BILLING_ITEM_CODE, 2, 3))), 0) FROM M_RBI_BILLING_ITEM WHERE SUBSTR(BILLING_ITEM_CODE, 0, 1) = :category", nativeQuery = true)
    Integer getMaxValueByCategory(String category);
}

package com.dbs.database.crm.repositories.rbi;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_BILLING_BUCKET_DETAIL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MBillingBucketDetailRepo extends PagingAndSortingRepository<M_RBI_BILLING_BUCKET_DETAIL, Integer>, JpaSpecificationExecutor<M_RBI_BILLING_BUCKET_DETAIL> {
    
    default Specification<M_RBI_BILLING_BUCKET_DETAIL> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_RBI_BILLING_BUCKET_DETAIL> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_RBI_BILLING_BUCKET_DETAIL>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_RBI_BILLING_BUCKET_DETAIL>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_RBI_BILLING_BUCKET_DETAIL> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_RBI_BILLING_BUCKET_DETAIL> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_RBI_BILLING_BUCKET_DETAIL> addDefaultFilters(Specification<M_RBI_BILLING_BUCKET_DETAIL> specification, Map<String, Object> filter, Boolean isFirst){
        return specification;
    }
    
    Page<M_RBI_BILLING_BUCKET_DETAIL> findAll(Specification<M_RBI_BILLING_BUCKET_DETAIL> specification, Pageable paging);
    
    List<M_RBI_BILLING_BUCKET_DETAIL> findAll();
    Optional<M_RBI_BILLING_BUCKET_DETAIL> findAllByBillingBucketCodeAndBillingItemCodeAndCurrency(String billingBucketCode, String billingItemCode, Integer currency);
    List<M_RBI_BILLING_BUCKET_DETAIL> findAllByBillingBucketCode(String billingBucketCode);
    @Query(value = "SELECT a.* FROM M_RBI_BILLING_BUCKET_DETAIL a WHERE a.BILLING_BUCKET_CODE = :billingBucketCode AND a.id NOT IN :detailDataIds", nativeQuery = true)
    List<M_RBI_BILLING_BUCKET_DETAIL> findNotIn(String billingBucketCode, List<Integer> detailDataIds);

}

package com.dbs.database.crm.repositories.rbi;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.VW_AM_TAXIMPLICATION;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_BILLING_BUCKET;
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
import org.springframework.util.ObjectUtils;

@Repository
@Transactional(value= "crmTransactionManager")
public interface MRbiBillingBucketRepo extends PagingAndSortingRepository<M_RBI_BILLING_BUCKET, Integer>, JpaSpecificationExecutor<M_RBI_BILLING_BUCKET> {
    
    default Specification<M_RBI_BILLING_BUCKET> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_RBI_BILLING_BUCKET> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_RBI_BILLING_BUCKET>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_RBI_BILLING_BUCKET>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_RBI_BILLING_BUCKET> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_RBI_BILLING_BUCKET> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_RBI_BILLING_BUCKET> addDefaultFilters(Specification<M_RBI_BILLING_BUCKET> specification, Map<String, Object> filter, Boolean isFirst){
        if(specification == null) {
            if(!ObjectUtils.isEmpty(filter.get("billingBucketCode"))) {
                specification = (Specification<M_RBI_BILLING_BUCKET>) PagingUtils.createSpecification("billingBucketCode~" + filter.get("billingBucketCode"), EQUALS_SELECTOR);
            }
            if(!ObjectUtils.isEmpty(filter.get("listCode"))){
                specification = (Specification<M_RBI_BILLING_BUCKET>) where(PagingUtils.createINSpecification("billingBucketCode", (List<Integer>)filter.get("listCode")));
            }
        } else {
            if(!ObjectUtils.isEmpty(filter.get("billingBucketCode"))) {
                specification = specification.and((Specification<M_RBI_BILLING_BUCKET>) PagingUtils.createSpecification("billingBucketCode~" + filter.get("billingBucketCode"), EQUALS_SELECTOR));
            }
            if(!ObjectUtils.isEmpty(filter.get("listCode"))){
                specification = specification.and((Specification<M_RBI_BILLING_BUCKET>) where(PagingUtils.createINSpecification("billingBucketCode", (List<Integer>)filter.get("listCode"))));
            }
        }
        return specification;
    }
    
    Page<M_RBI_BILLING_BUCKET> findAll(Specification<M_RBI_BILLING_BUCKET> specification, Pageable paging);
    
    List<M_RBI_BILLING_BUCKET> findAll();

    List<M_RBI_BILLING_BUCKET> findAllByBillingBucketCode(String billingBucketCode);

    Optional<M_RBI_BILLING_BUCKET> findByBillingBucketCodeAndStatus(String billingBucketCode, String status);

//    @Query("SELECT a FROM M_RBI_BILLING_BUCKET a WHERE a.billingBucketCode IN :billingBucketCode")

    Optional<M_RBI_BILLING_BUCKET> findByBillingBucketCodeIgnoreCase(String billingBucketCode);

    Optional<M_RBI_BILLING_BUCKET> findByBilingBucketNameIgnoreCase (String bucketName);
    Optional<M_RBI_BILLING_BUCKET> findByIdAndBillingBucketCode(Integer id, String billingBucketCode);
    Optional<M_RBI_BILLING_BUCKET> findByBillingBucketCodeAndCcIdAndEntityId(String billingBucketCode, Integer ccId, Integer entityId);
    M_RBI_BILLING_BUCKET findTopByIdAndEntityIdAndCcIdIn(Integer billingBucketId, Integer entityId, List<Integer> ccId);

    @Query(value = "SELECT MRBB.* FROM M_RBI_BILLING_BUCKET MRBB WHERE UPPER(MRBB.BILLING_BUCKET_CODE)=UPPER(:billingBucketCode) OR UPPER(MRBB.BILLING_BUCKET_NAME)=UPPER(:billingBucketName)", nativeQuery = true)
    List<M_RBI_BILLING_BUCKET> findByBillingBucketCodeSensiteiveCaseAndStatus(String billingBucketCode, String billingBucketName);

    Optional<M_RBI_BILLING_BUCKET> findByBilingBucketNameIgnoreCaseAndStatus(String billingBucketName, String status);

//    List<String> findBillingBucketCodeByStatus(String status);
    List<M_RBI_BILLING_BUCKET> findAllByStatus(String status);
}

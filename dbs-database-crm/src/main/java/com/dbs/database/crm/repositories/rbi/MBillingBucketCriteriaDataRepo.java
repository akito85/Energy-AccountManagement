package com.dbs.database.crm.repositories.rbi;

import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_BILLING_BUCKET_CRITERIA_DATA;
import java.util.List;
import java.util.Optional;

import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_BILLING_BUCKET_DETAIL;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MBillingBucketCriteriaDataRepo extends PagingAndSortingRepository<M_RBI_BILLING_BUCKET_CRITERIA_DATA, Integer>, JpaSpecificationExecutor<M_RBI_BILLING_BUCKET_CRITERIA_DATA> {
    
    List<M_RBI_BILLING_BUCKET_CRITERIA_DATA> findAll();
    List<M_RBI_BILLING_BUCKET_CRITERIA_DATA> findAllByBillingBucketCodeIn(List<String> billingBucketCode);

    Optional<M_RBI_BILLING_BUCKET_CRITERIA_DATA> findById(Integer id);
    List<M_RBI_BILLING_BUCKET_CRITERIA_DATA> findAllByBillingBucketCode(String billingBucketCode);
    @Query(value = "SELECT a.* FROM M_RBI_BILLING_BUCKET_CRITERIA_DATA a WHERE a.BILLING_BUCKET_CODE = :billingBucketCode AND a.id NOT IN :criteriaDataIds", nativeQuery = true)
    List<M_RBI_BILLING_BUCKET_CRITERIA_DATA> findNotIn(String billingBucketCode, List<Integer> criteriaDataIds);
}

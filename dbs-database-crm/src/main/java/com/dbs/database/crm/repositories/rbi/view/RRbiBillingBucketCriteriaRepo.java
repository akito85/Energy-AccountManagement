package com.dbs.database.crm.repositories.rbi.view;
import com.dbs.database.crm.entities.ratingbillinginvoice.R_RBI_BILLING_BUCKET_CRITERIA;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface RRbiBillingBucketCriteriaRepo extends PagingAndSortingRepository<R_RBI_BILLING_BUCKET_CRITERIA, Integer>, JpaSpecificationExecutor<R_RBI_BILLING_BUCKET_CRITERIA> {
    @Query(value = "SELECT a.* FROM R_RBI_BILLING_BUCKET_CRITERIA a WHERE a.BILLING_BUCKET_CODE = :billingBucketCode AND a.BILLING_BUCKET_CRITERIA_ID NOT IN :detailIds", nativeQuery = true)
    List<R_RBI_BILLING_BUCKET_CRITERIA> findNotIn(String billingBucketCode, List<Integer> detailIds);

    List<R_RBI_BILLING_BUCKET_CRITERIA> findAllByBillingBucketCode(String billingBucketCode);
}

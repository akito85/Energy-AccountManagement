package com.dbs.database.crm.repositories.rbi;

import com.dbs.database.crm.entities.ratingbillinginvoice.R_RBI_TOP_CRITERIA;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface RRbiTopCriteriaRepo extends PagingAndSortingRepository<R_RBI_TOP_CRITERIA, Integer>, JpaSpecificationExecutor<R_RBI_TOP_CRITERIA> {
    List<R_RBI_TOP_CRITERIA> findAllByTermOfPaymentId(Integer termOfPaymentId);
    Optional<R_RBI_TOP_CRITERIA> findByTermOfPaymentCriteriaId(Integer termOfPaymentCriteriaId);
    @Query(value = "SELECT a.* FROM R_RBI_TOP_CRITERIA a WHERE a.TERM_OF_PAYMENT_ID = :termOfPaymentId AND a.TERM_OF_PAYMENT_CRITERIA_ID NOT IN :criteriaIds", nativeQuery = true)
    List<R_RBI_TOP_CRITERIA> findNotIn(Integer termOfPaymentId, List<Integer> criteriaIds);

}

package com.dbs.database.crm.repositories.rbi;

import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_TOP_CRITERIA_DATA;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface RRbiTopCriteriaDataRepo extends PagingAndSortingRepository<M_RBI_TOP_CRITERIA_DATA, Integer>, JpaSpecificationExecutor<M_RBI_TOP_CRITERIA_DATA> {
    List<M_RBI_TOP_CRITERIA_DATA> findAllByTermOfPaymentId(Integer topId);

    @Query(value = "SELECT a.* FROM M_RBI_TOP_CRITERIA_DATA a WHERE a.TERM_OF_PAYMENT_ID = :termOfPaymentId AND a.ID NOT IN :criteriaDataIds", nativeQuery = true)
    List<M_RBI_TOP_CRITERIA_DATA> findNotIn(Integer termOfPaymentId, List<Integer> criteriaDataIds);
}

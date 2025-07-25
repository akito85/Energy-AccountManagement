package com.dbs.database.crm.repositories.rbi;

import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_TERMS_OF_PAYMENT;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MRbiTermsOfPaymentRepo extends PagingAndSortingRepository<M_RBI_TERMS_OF_PAYMENT, Integer>, JpaSpecificationExecutor<M_RBI_TERMS_OF_PAYMENT> {
    List<M_RBI_TERMS_OF_PAYMENT> findAllByStatus(String status);
    List<M_RBI_TERMS_OF_PAYMENT> findAllByTermsOfPaymentIdIn(List<Integer> termsOfPaymentId);
    List<M_RBI_TERMS_OF_PAYMENT> findAll();
    Optional<M_RBI_TERMS_OF_PAYMENT> findByTermsOfPaymentIdAndCcIdAndEntityId(Integer topId, Integer ccId, Integer entityId);
    M_RBI_TERMS_OF_PAYMENT findTopByTermsOfPaymentIdAndEntityIdAndCcIdIn(Integer topId, Integer entityId, List<Integer> ccId);

    @Query(value = "SELECT MRTOP.* FROM M_RBI_TERMS_OF_PAYMENT MRTOP WHERE UPPER(MRTOP.TERMS_OF_PAYMENT_NAME)=UPPER(:name)", nativeQuery = true)
    List<M_RBI_TERMS_OF_PAYMENT> findAllByTermsOfPaymentName(String name);

    Optional<M_RBI_TERMS_OF_PAYMENT> findByTermsOfPaymentNameIgnoreCase (String name);
}

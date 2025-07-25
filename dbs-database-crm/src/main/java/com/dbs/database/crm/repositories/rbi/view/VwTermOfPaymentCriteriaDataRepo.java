package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_TERM_OF_PAYMENT_CRITERIA_DATA;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwTermOfPaymentCriteriaDataRepo extends PagingAndSortingRepository<VW_TERM_OF_PAYMENT_CRITERIA_DATA,Integer>, JpaSpecificationExecutor<VW_TERM_OF_PAYMENT_CRITERIA_DATA> {

    List<VW_TERM_OF_PAYMENT_CRITERIA_DATA> findAllByTermOfPaymentId(Integer paymentId);
}

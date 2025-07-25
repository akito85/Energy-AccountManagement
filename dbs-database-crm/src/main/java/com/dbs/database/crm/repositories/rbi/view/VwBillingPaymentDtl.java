package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_BILLING_PAYMENT_DTL;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwBillingPaymentDtl extends PagingAndSortingRepository<VW_BILLING_PAYMENT_DTL, Long>, JpaSpecificationExecutor<VW_BILLING_PAYMENT_DTL> {
    List<VW_BILLING_PAYMENT_DTL> findAllByBillingCode(String billCode);
}

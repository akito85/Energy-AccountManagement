package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_RBI_INVOICE_BILLING_LIST;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwRbiInvoiceBillingListRepo extends PagingAndSortingRepository<VW_RBI_INVOICE_BILLING_LIST,String>, JpaSpecificationExecutor<VW_RBI_INVOICE_BILLING_LIST> {
}

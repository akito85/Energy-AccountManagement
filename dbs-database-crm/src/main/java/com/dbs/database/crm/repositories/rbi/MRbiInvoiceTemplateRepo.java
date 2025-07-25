package com.dbs.database.crm.repositories.rbi;

import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_INVOICE_TEMPLATE;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MRbiInvoiceTemplateRepo extends PagingAndSortingRepository<M_RBI_INVOICE_TEMPLATE, Integer>, JpaSpecificationExecutor<M_RBI_INVOICE_TEMPLATE> {

    boolean existsByInvoiceNameEqualsIgnoreCase(String invoiceName);

    boolean existsByIdNotAndInvoiceNameEqualsIgnoreCase(Integer id, String invoiceName);

    List<M_RBI_INVOICE_TEMPLATE> findAllByStatus(String status);
    List<M_RBI_INVOICE_TEMPLATE> findAllByIdIn(List<Integer> id);
}

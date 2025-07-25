package com.dbs.database.crm.repositories.rbi;

import com.dbs.database.crm.entities.ratingbillinginvoice.T_INVOICE_LOG;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface TInvoiceLogRepository extends PagingAndSortingRepository<T_INVOICE_LOG, Integer>, JpaSpecificationExecutor<T_INVOICE_LOG>, JpaRepository<T_INVOICE_LOG, Integer> {
    List<T_INVOICE_LOG> findByInvoiceNumber(String invoiceNumber);
    Optional<T_INVOICE_LOG> findTopByInvoiceNumberAndStatusInvOrderByActionDateDesc(String invoiceNumber, String status);
    Optional<T_INVOICE_LOG> findTopByInvoiceNumberAndStatusInvOrderByIdDesc(String invoiceNumber, String status);
    
}

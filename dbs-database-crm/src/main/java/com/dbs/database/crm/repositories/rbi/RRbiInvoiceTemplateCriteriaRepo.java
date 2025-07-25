package com.dbs.database.crm.repositories.rbi;

import com.dbs.database.crm.entities.ratingbillinginvoice.R_RBI_INVOICE_TEMPLATE_CRITERIA;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface RRbiInvoiceTemplateCriteriaRepo extends PagingAndSortingRepository<R_RBI_INVOICE_TEMPLATE_CRITERIA, Integer>, JpaSpecificationExecutor<R_RBI_INVOICE_TEMPLATE_CRITERIA> {

    void deleteAllByIdNotInAndInvoiceTemplateId(List<Integer> idList, Integer invoiceTemplateId);
}

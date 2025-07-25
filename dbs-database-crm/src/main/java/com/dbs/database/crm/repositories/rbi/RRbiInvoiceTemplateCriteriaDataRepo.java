package com.dbs.database.crm.repositories.rbi;

import com.dbs.database.crm.entities.ratingbillinginvoice.R_RBI_INVOICE_TEMPLATE_CRITERIA_DATA;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface RRbiInvoiceTemplateCriteriaDataRepo extends PagingAndSortingRepository<R_RBI_INVOICE_TEMPLATE_CRITERIA_DATA, Integer>, JpaSpecificationExecutor<R_RBI_INVOICE_TEMPLATE_CRITERIA_DATA> {

    void deleteAllByIdNotInAndInvoiceTemplateId(List<Integer> idList, Integer invoiceTemplateId);

    List<R_RBI_INVOICE_TEMPLATE_CRITERIA_DATA> findAllByInvoiceTemplateId(Integer invoiceTemplateId);
}

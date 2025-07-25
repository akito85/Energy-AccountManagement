package com.dbs.database.crm.repositories.payment;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.payment.M_RECEIPT;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.BETWEEN_SELECTOR;
import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface ReceiptRepo extends PagingAndSortingRepository<M_RECEIPT, Long>, JpaSpecificationExecutor<M_RECEIPT> {

    @SuppressWarnings("unchecked")
    default Specification<M_RECEIPT> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<M_RECEIPT> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            if (sr.contains("receiptDate") || sr.contains("rateDate") || sr.contains("bankStatementDate")) {
                specification =
                        i == 0 ?
                                (Specification<M_RECEIPT>) where(PagingUtils.createSpecification(sr, BETWEEN_SELECTOR))
                                : specification.and((Specification<M_RECEIPT>) PagingUtils.createSpecification(sr, BETWEEN_SELECTOR));

            } else {
                specification =
                        i == 0 ?
                                (Specification<M_RECEIPT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                                : specification.and((Specification<M_RECEIPT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));

            }
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<M_RECEIPT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_RECEIPT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<M_RECEIPT> addDefaultFilters(Specification<M_RECEIPT> specification,
                                                    Map<String, Object> filter, Boolean isFirst) {
        specification = (Specification<M_RECEIPT>)
                PagingUtils.createIsDeletedFilter(specification, false, isFirst);
        return specification;
    }

    @Query("SELECT mr FROM M_RBI_BILLING mrb JOIN M_RBI_INVOICE mri ON mrb.billingCode = mri.billingCode \n" +
            "JOIN R_ALLOCATION ra ON ra.invoiceNumber = mri.invoiceNumber JOIN M_RECEIPT mr ON mr.id = ra.receiptId \n" +
            "WHERE mri.billingCode = :billingCode AND mr.isReconciled ='N'")
    List<M_RECEIPT> findCurrentReceiptBilling(String billingCode);

    @Query("SELECT mr FROM M_RBI_BILLING mrb JOIN M_RBI_INVOICE mri ON mrb.billingCode = mri.billingCode \n" +
            "JOIN R_ALLOCATION ra ON ra.invoiceNumber = mri.invoiceNumber JOIN M_RECEIPT mr ON mr.id = ra.receiptId \n" +
            "WHERE mri.billingCode = :billingCode AND mr.isReconciled ='Y'")
    List<M_RECEIPT> findPreviousReceiptBilling(String billingCode);
}

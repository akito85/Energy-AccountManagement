package com.dbs.database.crm.repositories.payment;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.payment.R_PAY_RECEIPT_ALLOCATION;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface ReceiptAllocationRepo extends PagingAndSortingRepository<R_PAY_RECEIPT_ALLOCATION, Long>, JpaSpecificationExecutor<R_PAY_RECEIPT_ALLOCATION> {

    @SuppressWarnings("unchecked")
    default Specification<R_PAY_RECEIPT_ALLOCATION> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<R_PAY_RECEIPT_ALLOCATION> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<R_PAY_RECEIPT_ALLOCATION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<R_PAY_RECEIPT_ALLOCATION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<R_PAY_RECEIPT_ALLOCATION> getSpecificationDefault(Map<String, Object> filter) {
        Specification<R_PAY_RECEIPT_ALLOCATION> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<R_PAY_RECEIPT_ALLOCATION> addDefaultFilters(Specification<R_PAY_RECEIPT_ALLOCATION> specification,
                                                          Map<String, Object> filter, Boolean isFirst) {
        specification = (Specification<R_PAY_RECEIPT_ALLOCATION>) PagingUtils.createReceiptIdFilter(specification,
                Long.parseLong(filter.get("receiptId").toString()), isFirst);
        return specification;
    }

    @Query(value = "SELECT ALLOCATION_NUMBER FROM R_PAY_RECEIPT_ALLOCATION WHERE RECEIPT_ID=:receiptId ORDER BY ALLOCATION_NUMBER DESC FETCH FIRST 1 ROW ONLY", nativeQuery = true)
    String findTopOneAllocationNumberDesc(Long receiptId);

    List<R_PAY_RECEIPT_ALLOCATION> findAllByReceiptIdAndAllocationStatusIgnoreCaseOrderByBillingBucketIdAsc(Long receiptId, String allocationStatus);

    @Query(value = "SELECT * FROM R_PAY_RECEIPT_ALLOCATION WHERE ACCOUNT_NUMBER =:accountNumber AND AMOUNT<>0 AND ALLOCATION_STATUS='Partially Paid'\n" +
            "ORDER BY BILLING_BUCKET_ID DESC FETCH FIRST 1 ROW ONLY", nativeQuery = true)
    Optional<R_PAY_RECEIPT_ALLOCATION> findFirstByAccountNumber(String accountNumber);

    List<R_PAY_RECEIPT_ALLOCATION> findAllByReceiptIdAndBillingBucketIdIn(Long receiptId, List<Integer> bucketIdList);
}

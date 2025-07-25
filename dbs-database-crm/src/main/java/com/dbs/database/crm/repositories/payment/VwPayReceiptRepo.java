package com.dbs.database.crm.repositories.payment;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.payment.view.VW_PAY_RECEIPT;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.BETWEEN_SELECTOR;
import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwPayReceiptRepo extends PagingAndSortingRepository<VW_PAY_RECEIPT, Long>, JpaSpecificationExecutor<VW_PAY_RECEIPT> {

    @SuppressWarnings("unchecked")
    default Specification<VW_PAY_RECEIPT> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<VW_PAY_RECEIPT> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            if (sr.contains("receiptDate") || sr.contains("rateDate") || sr.contains("bankStatementDate") || sr.contains("createdDate")) {
                specification =
                        i == 0 ?
                                (Specification<VW_PAY_RECEIPT>) where(PagingUtils.createSpecificationPayment(sr, BETWEEN_SELECTOR))
                                : specification.and((Specification<VW_PAY_RECEIPT>) PagingUtils.createSpecificationPayment(sr, BETWEEN_SELECTOR));

            } else {
                specification =
                        i == 0 ?
                                (Specification<VW_PAY_RECEIPT>) where(PagingUtils.createSpecificationPayment(sr, DEFAULT_SELECTOR))
                                : specification.and((Specification<VW_PAY_RECEIPT>) PagingUtils.createSpecificationPayment(sr, DEFAULT_SELECTOR));

            }
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<VW_PAY_RECEIPT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_PAY_RECEIPT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_PAY_RECEIPT> addDefaultFilters(Specification<VW_PAY_RECEIPT> specification,
                                                        Map<String, Object> filter, Boolean isFirst) {
        return specification;
    }

    @Query(value = "SELECT ID, ACCOUNT_NUMBER, AMOUNT_REAL, to_char(RECEIPT_DATE, 'DD/MM/YY HH24:MI:SS') FROM VW_PAY_RECEIPT " +
            "WHERE BANK LIKE '%BCA%' " +
            "AND RECEIPT_DATE BETWEEN CAST(SYSTIMESTAMP AT TIME ZONE SESSIONTIMEZONE AS TIMESTAMP)-30 AND CAST(SYSTIMESTAMP AT TIME ZONE SESSIONTIMEZONE AS TIMESTAMP) ORDER BY ID ASC", nativeQuery = true)
    List<Tuple> getAllReceiptCustomBca();

    @Query(value = "SELECT ID, ACCOUNT_NUMBER, AMOUNT_REAL, to_char(RECEIPT_DATE, 'YYMMDD') FROM VW_PAY_RECEIPT " +
            "WHERE BANK=:bankName AND RECEIPT_DATE BETWEEN CAST(SYSTIMESTAMP AT TIME ZONE SESSIONTIMEZONE AS TIMESTAMP)-30 AND CAST(SYSTIMESTAMP AT TIME ZONE SESSIONTIMEZONE AS TIMESTAMP)" +
            "ORDER BY ID ASC", nativeQuery = true)
    List<Tuple> getAllReceiptCustomBumnBank(String bankName);

    @Query(value = "SELECT ID FROM VW_PAY_RECEIPT WHERE ACCOUNT_NUMBER=:accountNumber AND AMOUNT_REAL=:amount AND to_char(RECEIPT_DATE, 'DD/MM/YY HH24:MI:SS')=:receiptDate AND BANK LIKE '%BCA%' AND IS_RECONCILED='N'", nativeQuery = true)
    Optional<Long> findMatchOpBca(String accountNumber, BigDecimal amount, String receiptDate);

    @Query(value = "SELECT ID FROM VW_PAY_RECEIPT WHERE :accountNumber LIKE '%' || ACCOUNT_NUMBER || '%' AND AMOUNT_REAL=:amount AND to_char(RECEIPT_DATE, 'YYMMDD')=:receiptDate AND IS_RECONCILED='N' AND BANK LIKE %:bankName%", nativeQuery = true)
    Optional<Long> findMatchOpBumnBank(String accountNumber, BigDecimal amount, String receiptDate, String bankName);

//    @Query("SELECT mr FROM M_RBI_BILLING mrb JOIN M_RBI_INVOICE mri ON mrb.billingCode = mri.billingCode \n" +
//            "JOIN VW_RECEIPT_ALLOCATION_V1 ra ON ra.invoiceNumber = mri.invoiceNumber JOIN VW_PAY_RECEIPT mr ON mr.id = ra.receiptId \n" +
//            "WHERE mri.billingCode = :billingCode AND mr.isReconciled ='N'")
//    List<VW_PAY_RECEIPT> findCurrentReceiptBilling(String billingCode);

    @Query("SELECT mr FROM M_RBI_BILLING mrb JOIN M_RBI_INVOICE mri ON mrb.billingCode = mri.billingCode \n" +
            "JOIN VW_RECEIPT_ALLOCATION_V1 ra ON ra.invoiceNumber = mri.invoiceNumber JOIN VW_PAY_RECEIPT mr ON mr.id = ra.receiptId \n" +
            "WHERE mri.billingCode = :billingCode AND mr.isReconciled ='Y'")
    List<VW_PAY_RECEIPT> findPreviousReceiptBilling(String billingCode);

    @Query(nativeQuery = true, value = "SELECT * FROM VW_RBI_BILLING mrb \n" +
            "JOIN VW_RECEIPT_ALLOCATION_V1 vrav ON mrb.ACCOUNT_NUMBER = vrav.ACCOUNT_NUMBER\n" +
            "AND MRB.BILLING_PERIOD = VRAV.BILLING_PERIOD \n" +
            "JOIN VW_PAY_RECEIPT vpr ON VPR.ID = vrav.RECEIPT_ID \n" +
            "WHERE VRAV.STATUS_RECEIPT = 'Applied' \n" +
            "AND VRAV.STATUS_APPROVAL_RECEIPT = 'Approved'\n" +
            "AND VRAV.ALLOCATION_TYPE = 'BILLING'\n" +
            "AND MRB.BILLING_CODE = :billingCode ")
    List<VW_PAY_RECEIPT> findCurrentReceiptBilling(String billingCode);
    @Query(nativeQuery = true, value = "SELECT * FROM VW_RBI_BILLING vrb \n" +
            "LEFT JOIN M_RBI_PERIOD mrp ON VRB.BILLING_PERIOD_ID = MRP.ID\n" +
            "LEFT JOIN VW_RECEIPT_ALLOCATION_V1 vrav ON VRB.ACCOUNT_NUMBER = vrav.ACCOUNT_NUMBER AND VRAV.BILLING_PERIOD = VRB.BILLING_PERIOD \n" +
            "LEFT JOIN VW_PAY_RECEIPT vpr ON VPR.ID = vrav.RECEIPT_ID \n" +
            "WHERE VRAV.STATUS_RECEIPT = 'Applied' \n" +
            "AND VRAV.STATUS_APPROVAL_RECEIPT = 'Approved'\n" +
            "AND VRAV.ALLOCATION_TYPE = 'BILLING'\n" +
            "AND VRB.ACCOUNT_NUMBER = :accNumb \n" +
            "AND VRB.BILLING_PERIOD = :billPeriod ")
    List<VW_PAY_RECEIPT> findPreviousPayment (String accNumb, String billPeriod);
}

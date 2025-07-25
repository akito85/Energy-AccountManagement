package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_BILLING;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MRbiBillingRepo extends PagingAndSortingRepository<M_RBI_BILLING,String>, JpaSpecificationExecutor<M_RBI_BILLING> {
    default Specification<M_RBI_BILLING> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_RBI_BILLING> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_RBI_BILLING>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_RBI_BILLING>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFilters(specification, filter);
        /*code here*/

        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    default Specification<M_RBI_BILLING> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_RBI_BILLING> specification = null;
        specification = addDefaultFilters(specification, filter);
        return specification;
    }

    default Specification<M_RBI_BILLING> addDefaultFilters(Specification<M_RBI_BILLING> specification, Map<String, Object> filter){
        /*if(type.equals("REQUEST")){
            specification = (Specification<M_RBI_BILLING>) where(PagingUtils.createSpecification("statusApproval~REJECTED", EQUALS_SELECTOR));
            specification = specification.or((Specification<M_RBI_BILLING>) PagingUtils.createSpecification("statusApproval~REJECTED", EQUALS_SELECTOR));
        }else if(type.equals("APPROVAL")){
            specification = specification.and((Specification<M_RBI_BILLING>) PagingUtils.createSpecification("statusApproval~REJECTED", EQUALS_SELECTOR));
        }*/
        /*INFO: Add Entity Filter*/
        return specification;
    }
    List<M_RBI_BILLING> findAll();
    @Query("SELECT a FROM M_RBI_BILLING a WHERE a.statusApproval IS NULL or a.statusApproval ='REJECTED'")
    Page<M_RBI_BILLING> findAvailableRequest(@Nullable Specification<M_RBI_BILLING> var1, Pageable var2);

    @Query(value = "SELECT a.* FROM M_RBI_BILLING a " +
            "WHERE a.DUE_DATE+1 <= CAST(SYSTIMESTAMP AT TIME ZONE SESSIONTIMEZONE AS TIMESTAMP) " +
            "AND a.PAYMENT_STATUS <> 'PAID' AND a.STATUS_APPROVAL = 'APPROVED' " +
            "AND a.ACCOUNT_SEGMENT <> 'KI'", nativeQuery = true)
    List<M_RBI_BILLING> findAllByDueDate();

    List<M_RBI_BILLING> findAllByStatusApproval(String status);
    @Query("SELECT a FROM M_RBI_BILLING a WHERE a.statusApproval ='WAITING APPROVAL'")
    Page<M_RBI_BILLING> findAvailableApprove(@Nullable Specification<M_RBI_BILLING> var1, Pageable var2);
    @Query("SELECT a FROM M_RBI_BILLING a WHERE a.billingCode = :billingCode")
    Optional<M_RBI_BILLING> findByBillingCode(String billingCode);
    @Query("SELECT a FROM M_RBI_BILLING a WHERE a.accountNumber = :accountNumber AND a.saNumber =:saNumber AND a.billingCode != :billingCode AND ROWNUM <= 1 ORDER BY a.transactionDate DESC")
    Optional<M_RBI_BILLING> findPreviousBilling(String billingCode, String accountNumber, String saNumber);

    List<M_RBI_BILLING> findAllByAccountNumberAndStatusApprovalIgnoreCase(String accountNumber, String statusApproval);

    @Query(value = "select * from m_rbi_billing where :accountNumber like '%' || account_number || '%' and status_approval collate binary_ci=:statusApproval", nativeQuery = true)
    List<M_RBI_BILLING> findAllByAccountNumberLikeAndStatusApprovalIgnoreCase(String accountNumber, String statusApproval);

    @Query(nativeQuery = true, value = "SELECT * FROM M_RBI_BILLING RB\n" +
            "JOIN M_RBI_PERIOD RP ON RP.PERIOD = RB.BILLING_PERIOD\n" +
            "WHERE RP.ID = :billPeriod AND RP.BILLING_CYCLE_ID = :billCycId AND RB.ACCOUNT_NUMBER = :accNumb")
    Optional<M_RBI_BILLING> findBilling (
            Integer billPeriod,
            Integer billCycId,
            String accNumb
    );
    @Query(nativeQuery = true, value = "SELECT * FROM M_RBI_BILLING WHERE CALCULATION_CODE = :calCode \n" +
            "AND ACCOUNT_NUMBER IN :accNumb AND STATUS_APPROVAL = 'APPROVED'")
    List<M_RBI_BILLING> findAllByCalculationCodeAndAccountNumber (
            String calCode,
            List<String> accNumb
    );
    @Query(nativeQuery = true, value = "SELECT * FROM M_RBI_BILLING WHERE INVOICE_NUMBER IN :invNumb")
    List<M_RBI_BILLING> findAllByInvoiceNumber (List<String> invNumb);

    Optional<M_RBI_BILLING> findByAccountNumberAndStatusApproval (String accNumb, String statusAppr);

    List<M_RBI_BILLING> findAllByBillingCodeIn(Set<String> billCodes);
}

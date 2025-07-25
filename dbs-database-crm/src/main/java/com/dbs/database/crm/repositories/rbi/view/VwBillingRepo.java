package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_RBI_BILLING;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.GET_CC_CHILD;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwBillingRepo extends PagingAndSortingRepository<VW_RBI_BILLING,String>, JpaSpecificationExecutor<VW_RBI_BILLING> {
    default Specification<VW_RBI_BILLING> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_RBI_BILLING> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_RBI_BILLING>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_RBI_BILLING>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFilters(specification, filter,false);
        /*code here*/

        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    default Specification<VW_RBI_BILLING> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_RBI_BILLING> specification = null;
        specification = addDefaultFilters(specification, filter,true);
        return specification;
    }

    default Specification<VW_RBI_BILLING> addDefaultFilters(Specification<VW_RBI_BILLING> specification, Map<String, Object> filter, Boolean isFirst){
        /*if(type.equals("REQUEST")){
            specification = (Specification<VW_RBI_BILLING>) where(PagingUtils.createSpecification("statusApproval~REJECTED", EQUALS_SELECTOR));
            specification = specification.or((Specification<VW_RBI_BILLING>) PagingUtils.createSpecification("statusApproval~REJECTED", EQUALS_SELECTOR));
        }else if(type.equals("APPROVAL")){
            specification = specification.and((Specification<VW_RBI_BILLING>) PagingUtils.createSpecification("statusApproval~REJECTED", EQUALS_SELECTOR));
        }*/
        /*INFO: Add Entity Filter*/

        if(filter.get("positionId") != null) {
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()),GET_CC_CHILD);
            specification = (Specification<VW_RBI_BILLING>) PagingUtils.createCostCenterFilter(specification, ccList, isFirst);
        }
        return specification;
    }
    List<VW_RBI_BILLING> findAll();
    @Query("SELECT a FROM VW_RBI_BILLING a WHERE a.ccId IN :ccList AND (a.statusApproval ='NEED REVIEW' or a.statusApproval ='REJECTED')" )
    List<VW_RBI_BILLING> findAvailableRequest(List<Integer> ccList);

    @Query(value = "SELECT a.* FROM VW_RBI_BILLING a WHERE a.PAYMENT_STATUS = 'Unpaid' AND DUE_DATE = TRUNC(SYSDATE) - 1", nativeQuery = true)
    List<VW_RBI_BILLING> findAllByPaymentStatusUnpaid();
    List<VW_RBI_BILLING> findAllByStatusApproval(String status);
    @Query("SELECT a FROM VW_RBI_BILLING a WHERE a.statusApproval ='WAITING APPROVAL' AND a.ccId IN :ccList")
    List<VW_RBI_BILLING> findAvailableApprove(List<Integer> ccList);
    @Query("SELECT a FROM VW_RBI_BILLING a WHERE a.accountNumber = :accountNumber AND a.saNumber =:saNumber AND a.billingCode != :billingCode AND ROWNUM <= 1 ORDER BY a.transactionDate DESC")
    Optional<VW_RBI_BILLING> findPreviousBilling(String billingCode, String accountNumber, String saNumber);

    @Query(nativeQuery = true, value = "SELECT * FROM VW_RBI_BILLING RB\n" +
            "WHERE RB.ACCOUNT_NUMBER = :accNumb AND RB.BILLING_PERIOD_ID = :billPeriodId\n" +
            "AND RB.STATUS_APPROVAL = 'APPROVED'")
    List<VW_RBI_BILLING> findAllByAccountNumber(String accNumb, Integer billPeriodId);

    Optional<VW_RBI_BILLING> findByAccountNumberAndBillingPeriodMinusAndStatusApproval(String accNumb, String billPeriodMin,String statusApproval);

    Optional<VW_RBI_BILLING> findByBillingCode (String billingCode);

    Optional<VW_RBI_BILLING> findByBillingPeriodAndAccountNumber (String billPeriod, String accNumb);

    Optional<VW_RBI_BILLING> findByAccountNumberAndBillingPeriodId(String accNumb, Integer billPeriod);

    Optional<VW_RBI_BILLING> findByBillingPeriodMinusAndAccountNumberAndCalculationCode (String billPeriodMin, String accNumb, String calCode);
}

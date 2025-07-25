package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.T_RBI_BILLING_BUCKET;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface TRbiBillingBucket extends PagingAndSortingRepository<T_RBI_BILLING_BUCKET,Integer>, JpaSpecificationExecutor<T_RBI_BILLING_BUCKET> {
    default Specification<T_RBI_BILLING_BUCKET> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<T_RBI_BILLING_BUCKET> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<T_RBI_BILLING_BUCKET>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<T_RBI_BILLING_BUCKET>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<T_RBI_BILLING_BUCKET> getSpecificationDefault(Map<String, Object> filter) {
        Specification<T_RBI_BILLING_BUCKET> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<T_RBI_BILLING_BUCKET> addDefaultFilters(Specification<T_RBI_BILLING_BUCKET> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        if (!ObjectUtils.isEmpty(filter.get("billingCodeList"))) {
            List<String> billingCodeList = (List<String>) filter.get("billingCodeList");
            specification = (Specification<T_RBI_BILLING_BUCKET>) PagingUtils.createINSpecification("billingCode", billingCodeList);
        }

        return specification;
    }
    List<T_RBI_BILLING_BUCKET> findAllByBillingCode(String billingCode);

    @Query(value = "SELECT trbb.* FROM T_RBI_BILLING_BUCKET trbb \n" +
            "WHERE EXISTS (SELECT 1 FROM M_RBI_BILLING mrb WHERE mrb.ACCOUNT_NUMBER =:accountNumber and mrb.BILLING_CODE=trbb.BILLING_CODE AND mrb.STATUS_APPROVAL='APPROVED')\n" +
            "ORDER BY trbb.PRIORITY ASC", nativeQuery = true)
    List<T_RBI_BILLING_BUCKET> findAllByBillingCodeAndAccountNumber(String accountNumber);

    List<T_RBI_BILLING_BUCKET> findAllByBillingCodeAndPriority(String billingCode, boolean priority);

    List<T_RBI_BILLING_BUCKET> findAllByBillingCodeAndPriorityAndPaymentStatusIgnoreCase(String billingCode, boolean priority, String paymentStatus);

    List<T_RBI_BILLING_BUCKET> findAllByBillingCodeAndLateCharge(String billingCode, boolean lateCharge);
}

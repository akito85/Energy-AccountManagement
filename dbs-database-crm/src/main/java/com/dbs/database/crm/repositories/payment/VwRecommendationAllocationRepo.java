package com.dbs.database.crm.repositories.payment;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.payment.view.VW_RECOMMENDATION_ALLOCATION;
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
public interface VwRecommendationAllocationRepo extends PagingAndSortingRepository<VW_RECOMMENDATION_ALLOCATION, Long>, JpaSpecificationExecutor<VW_RECOMMENDATION_ALLOCATION> {

    @SuppressWarnings("unchecked")
    default Specification<VW_RECOMMENDATION_ALLOCATION> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<VW_RECOMMENDATION_ALLOCATION> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_RECOMMENDATION_ALLOCATION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_RECOMMENDATION_ALLOCATION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<VW_RECOMMENDATION_ALLOCATION> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_RECOMMENDATION_ALLOCATION> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<VW_RECOMMENDATION_ALLOCATION> addDefaultFilters(Specification<VW_RECOMMENDATION_ALLOCATION> specification,
                                                               Map<String, Object> filter, Boolean isFirst) {
        specification = (Specification<VW_RECOMMENDATION_ALLOCATION>)
                PagingUtils.createCommonColumnVarcharEqualsFilter(specification, "createdBy", "SYSTEM", isFirst);

        if (!ObjectUtils.isEmpty(filter.get("billingCodeList"))) {
            List<String> billingCodeList = (List<String>) filter.get("billingCodeList");
            specification = specification.and((Specification<VW_RECOMMENDATION_ALLOCATION>) PagingUtils.createINSpecification("billingCode", billingCodeList));
        }
        return specification;
    }

    List<VW_RECOMMENDATION_ALLOCATION> findAllByBillingCodeIn(List<String> billingCodeList);

    @Query(value = "SELECT \n" +
            "\ta.ID,\n" +
            "\ta.TRANS_CODE BILLING_CODE,\n" +
            "\ta.BILLING_ITEM,\n" +
            "\tb.INVOICE_NUMBER,\n" +
            "\ta.CURRENCY INVOICE_CURRENCY,\n" +
            "\tb.BILLING_CYCLE,\n" +
            "\ta.BILLING_PERIOD,\n" +
            "\ta.TOTAL_AMOUNT BILLING_ITEM_AMOUNT,\n" +
            "\ta.TRANS_TYPE TYPE,\n" +
            "\tnvl(a.paid_amount, 0) ALLOCATION_AMOUNT,\n" +
            "\ta.TOTAL_AMOUNT - NVL(a.PAID_AMOUNT, 0) BILLING_ITEM_BALANCE,\n" +
            "\t'' ALLOCATION_STATUS,\n" +
            "\tFC_CONVERT_CURRENCY(a.CURRENCY) CONVERTED_CURRENCY,\n" +
            "\t0 EQUIVALENT_AMOUNT,\n" +
            "\tSYSDATE CREATED_DATE,\n" +
            "\t'SYSTEM' CREATED_BY\n" +
            "FROM \n" +
            "\t(\n" +
            "\t\tSELECT * FROM VW_BUCKET_ALLOCATION aa\n" +
            "\t\tWHERE \n" +
            "\t\t\tPAYMENT_STATUS COLLATE BINARY_CI <> 'PAID'\n" +
            "\t\t\tAND NOT EXISTS \n" +
            "\t\t\t(\n" +
            "\t\t\t\tSELECT 1 FROM R_PAY_RECEIPT_ALLOCATION bb\n" +
            "\t\t\t\tWHERE \n" +
            "\t\t\t\t\tbb.BILLING_BUCKET_ID = aa.ID\n" +
            "\t\t\t\t\tAND bb.RECEIPT_ID=:receiptId\n" +
            "\t\t\t)\n" +
            "\t\t\tAND aa.TRANS_CODE IN :billingCodeList\n" +
            "\t) a\n" +
            "LEFT JOIN M_RBI_BILLING b ON a.TRANS_CODE = b.BILLING_CODE\n" +
            "LEFT JOIN M_RBI_INVOICE c ON a.TRANS_CODE = c.BILLING_CODE\n" +
            "GROUP BY\n" +
            "\ta.ID,\n" +
            "\ta.TRANS_CODE,\n" +
            "\ta.BILLING_ITEM,\n" +
            "\tb.INVOICE_NUMBER,\n" +
            "\ta.CURRENCY,\n" +
            "\tb.BILLING_CYCLE,\n" +
            "\ta.BILLING_PERIOD,\n" +
            "\ta.TOTAL_AMOUNT,\n" +
            "\ta.PAID_AMOUNT,\n" +
            "\tb.ACCOUNT_NUMBER,\n" +
            "\ta.PRIORITY,\n" +
            "\ta.TRANS_TYPE\n" +
            "ORDER BY a.PRIORITY DESC", nativeQuery = true)
    List<VW_RECOMMENDATION_ALLOCATION> findAllByBillingCodeIn(List<String> billingCodeList, Long receiptId);
}

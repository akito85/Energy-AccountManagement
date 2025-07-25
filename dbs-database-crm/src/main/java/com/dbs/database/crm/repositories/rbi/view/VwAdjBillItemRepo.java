package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_ADJUSTMENT_BILLING_ITEM;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwAdjBillItemRepo extends PagingAndSortingRepository<VW_ADJUSTMENT_BILLING_ITEM,Integer>, JpaSpecificationExecutor<VW_ADJUSTMENT_BILLING_ITEM> {
    default Specification<VW_ADJUSTMENT_BILLING_ITEM> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_ADJUSTMENT_BILLING_ITEM> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_ADJUSTMENT_BILLING_ITEM>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_ADJUSTMENT_BILLING_ITEM>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_ADJUSTMENT_BILLING_ITEM> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_ADJUSTMENT_BILLING_ITEM> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_ADJUSTMENT_BILLING_ITEM> addDefaultFilters(Specification<VW_ADJUSTMENT_BILLING_ITEM> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        specification = (Specification<VW_ADJUSTMENT_BILLING_ITEM>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);

        /*INFO: Add Cost Center Filter*/
//        List<String> tes = null;//Get Cost Center From Username To Be Done
        return specification;
    }
    @Query(nativeQuery = true, value = "SELECT * FROM VW_ADJUSTMENT_BILLING_ITEM \n" +
            "WHERE (BILLING_CODE = :billingCode OR BILLING_CODE IS NULL)" +
            "AND BILLING_BUCKET_CODE = :billBuckCode")
    List<VW_ADJUSTMENT_BILLING_ITEM> findByBillCode(String billingCode, String billBuckCode);

    @Query(nativeQuery = true, value = "SELECT \n" +
            "SUM(ABI.TOTAL_AMOUNT) \n" +
            "FROM VW_ADJUSTMENT_BILLING_ITEM ABI \n" +
            "WHERE ABI.BILLING_CODE = :billingCode")
    Double totalAmount(String billingCode);

    @Query(nativeQuery = true, value = "SELECT * FROM VW_ADJUSTMENT_BILLING_ITEM vabi \n" +
            "WHERE BILLING_CODE = :billCode \n" +
            "AND BILLING_BUCKET_CODE = :billBuckCode \n" +
            "AND ITEM_ADJUSTMENT NOT IN ('B014')")
    List<VW_ADJUSTMENT_BILLING_ITEM> findAllByBillingCodeAndBillingBucketCode (String billCode, String billBuckCode);
}

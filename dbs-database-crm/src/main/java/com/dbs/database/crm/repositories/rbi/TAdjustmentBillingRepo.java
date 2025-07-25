package com.dbs.database.crm.repositories.rbi;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import static org.springframework.data.jpa.domain.Specification.where;

import com.dbs.database.crm.entities.ratingbillinginvoice.T_ADJUSTMENT_BILLING;

//@Repository
//@Transactional(value = "crmTransactionManager")
public interface TAdjustmentBillingRepo extends PagingAndSortingRepository<T_ADJUSTMENT_BILLING, Integer>, JpaSpecificationExecutor<T_ADJUSTMENT_BILLING> {
    
    default Specification<T_ADJUSTMENT_BILLING> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<T_ADJUSTMENT_BILLING> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<T_ADJUSTMENT_BILLING>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<T_ADJUSTMENT_BILLING>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<T_ADJUSTMENT_BILLING> getSpecificationDefault(Map<String, Object> filter) {
        Specification<T_ADJUSTMENT_BILLING> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<T_ADJUSTMENT_BILLING> addDefaultFilters(Specification<T_ADJUSTMENT_BILLING> specification, Map<String, Object> filter, Boolean isFirst){
        if(specification == null) {
    		specification =(Specification<T_ADJUSTMENT_BILLING>) PagingUtils.createSpecification("billingBucketCode~" + filter.get("billingBucketCode"), EQUALS_SELECTOR);
        } else {
    		specification =specification.and((Specification<T_ADJUSTMENT_BILLING>) PagingUtils.createSpecification("billingBucketCode~" + filter.get("billingBucketCode"), EQUALS_SELECTOR));
        }
        return specification;
    }
    
    List<T_ADJUSTMENT_BILLING> findAll();
    Optional<T_ADJUSTMENT_BILLING> findByAdjustmentNumber(String adjNumb);

    Optional<List<T_ADJUSTMENT_BILLING>> findByPrefixCode (String prefixCode);
    @Query(nativeQuery = true, value = "SELECT * FROM T_ADJUSTMENT_BILLING AB\n" +
            "WHERE AB.STATUS_APPROVAL = 'APPROVED' \n" +
            "AND AB.STATUS = 'PENDING' \n" +
            "AND AB.ACCOUNT_NUMBER = :accNumb ")
    List<T_ADJUSTMENT_BILLING> findAllByAccountNumber (String accNumb);
    Optional<List<T_ADJUSTMENT_BILLING>> findByPrefixCodeOrderByAdjustmentNumberDesc(String prefixCode);

    @Query(nativeQuery = true, value = "SELECT * FROM T_ADJUSTMENT_BILLING tab \n" +
            "WHERE TAB.ACCOUNT_NUMBER = :accNumb \n" +
            "AND TAB.BILLING_PERIOD = :billPeriod \n" +
            "AND TAB.CREATED_BY = 'Created By System'")
    Optional<T_ADJUSTMENT_BILLING> findAdjustmentBilling(String accNumb, Integer billPeriod);
    
    @Query(value = "SELECT COALESCE(TO_NUMBER(MAX(SUBSTR(ADJUSTMENT_NUMBER, 8, 12))),0) FROM T_ADJUSTMENT_BILLING WHERE PREFIX_CODE = :prefix", nativeQuery = true)
    Integer findMaxCodeByPrefix(String prefix);
}

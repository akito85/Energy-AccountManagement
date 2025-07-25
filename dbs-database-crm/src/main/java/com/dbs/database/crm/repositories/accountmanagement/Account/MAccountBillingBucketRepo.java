package com.dbs.database.crm.repositories.accountmanagement.Account;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNT_BILLING_BUCKET;
import java.util.List;
import static java.util.Locale.filter;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MAccountBillingBucketRepo extends PagingAndSortingRepository<M_ACCOUNT_BILLING_BUCKET, Integer>, JpaSpecificationExecutor<M_ACCOUNT_BILLING_BUCKET> {
    default Specification<M_ACCOUNT_BILLING_BUCKET> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_ACCOUNT_BILLING_BUCKET> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_ACCOUNT_BILLING_BUCKET>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_ACCOUNT_BILLING_BUCKET>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_ACCOUNT_BILLING_BUCKET> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_ACCOUNT_BILLING_BUCKET> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_ACCOUNT_BILLING_BUCKET> addDefaultFilters(Specification<M_ACCOUNT_BILLING_BUCKET> specification, Map<String, Object> filter, Boolean isFirst){
//        /*INFO: Add Entity Filter*/
        
        if(specification == null) {
    		specification =(Specification<M_ACCOUNT_BILLING_BUCKET>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR);
        } else {
    		specification =specification.and((Specification<M_ACCOUNT_BILLING_BUCKET>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR));
        }
        /*INFO: Add Cost Center Filter*/
        return specification;
    }
    
    Page<M_ACCOUNT_BILLING_BUCKET> findAllByAccountId(Integer accountId, Specification<M_ACCOUNT_BILLING_BUCKET> specification, Pageable paging);
    
    List<M_ACCOUNT_BILLING_BUCKET> findAll();
    
    List<M_ACCOUNT_BILLING_BUCKET> findAllByAccountId(Integer accountId);
}

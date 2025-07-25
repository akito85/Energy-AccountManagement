package com.dbs.database.crm.repositories.accountmanagement.Account;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.VW_CUSTOMER_CONTACT_DTL;
import java.util.List;
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
public interface VwCustomerContactDtlRepo extends PagingAndSortingRepository<VW_CUSTOMER_CONTACT_DTL, Integer>, JpaSpecificationExecutor<VW_CUSTOMER_CONTACT_DTL>{
    
    
    default Specification<VW_CUSTOMER_CONTACT_DTL> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_CUSTOMER_CONTACT_DTL> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_CUSTOMER_CONTACT_DTL>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_CUSTOMER_CONTACT_DTL>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_CUSTOMER_CONTACT_DTL> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_CUSTOMER_CONTACT_DTL> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_CUSTOMER_CONTACT_DTL> addDefaultFilters(Specification<VW_CUSTOMER_CONTACT_DTL> specification, Map<String, Object> filter, Boolean isFirst){
//        /*INFO: Add Entity Filter*/
        return specification;
    }
    
    @Override
    Page<VW_CUSTOMER_CONTACT_DTL> findAll(Specification<VW_CUSTOMER_CONTACT_DTL> specification, Pageable paging);
    
    @Override
    List<VW_CUSTOMER_CONTACT_DTL> findAll();
}

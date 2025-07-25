package com.dbs.database.crm.repositories.accountmanagement.Account;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_TAX_IDENTIFIER;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
public interface MTaxIdentifierRepo extends PagingAndSortingRepository<M_TAX_IDENTIFIER, Integer>, JpaSpecificationExecutor<M_TAX_IDENTIFIER> {
    default Specification<M_TAX_IDENTIFIER> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_TAX_IDENTIFIER> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_TAX_IDENTIFIER>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_TAX_IDENTIFIER>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_TAX_IDENTIFIER> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_TAX_IDENTIFIER> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_TAX_IDENTIFIER> addDefaultFilters(Specification<M_TAX_IDENTIFIER> specification, Map<String, Object> filter, Boolean isFirst){
        if(specification == null) {
    		specification =(Specification<M_TAX_IDENTIFIER>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR);
        } else {
    		specification =specification.and((Specification<M_TAX_IDENTIFIER>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR));
        }       
        return specification;
    }
    
    Page<M_TAX_IDENTIFIER> findAll(Specification<M_TAX_IDENTIFIER> specification, Pageable paging);
    
    List<M_TAX_IDENTIFIER> findAll();
    
    List<M_TAX_IDENTIFIER> findAllByAccountId(Integer accountId);
    
    Optional<M_TAX_IDENTIFIER> findByAccountIdAndStatus(Integer accountId, String status);
    
    Optional<M_TAX_IDENTIFIER> findTopByAccountIdAndTaxIdentifierAddressAndStatus(Integer accountId, Integer taxIdentifierAddress, String status);
    
}

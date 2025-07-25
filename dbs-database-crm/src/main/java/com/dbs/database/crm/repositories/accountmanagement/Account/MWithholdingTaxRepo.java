package com.dbs.database.crm.repositories.accountmanagement.Account;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_WITHOLDING_TAX;
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
public interface MWithholdingTaxRepo extends PagingAndSortingRepository<M_WITHOLDING_TAX, Integer>, JpaSpecificationExecutor<M_WITHOLDING_TAX> {
    
    default Specification<M_WITHOLDING_TAX> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_WITHOLDING_TAX> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_WITHOLDING_TAX>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_WITHOLDING_TAX>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFilters(specification, filter, false);
        /*code here*/

        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    default Specification<M_WITHOLDING_TAX> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_WITHOLDING_TAX> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_WITHOLDING_TAX> addDefaultFilters(Specification<M_WITHOLDING_TAX> specification, Map<String, Object> filter, Boolean isFirst){
        if(specification == null) {
    		specification =(Specification<M_WITHOLDING_TAX>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR);
        } else {
    		specification =specification.and((Specification<M_WITHOLDING_TAX>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR));
        }
        return specification;
    } 
    
    Page<M_WITHOLDING_TAX> findAll(Specification<M_WITHOLDING_TAX> specification, Pageable paging);
    
    List<M_WITHOLDING_TAX> findAll();
    
    Optional<M_WITHOLDING_TAX> findById(Integer id);
    
    List<M_WITHOLDING_TAX> findAllById(Integer accountId);
    
    Optional<M_WITHOLDING_TAX> findTopByAccountIdOrderByCreatedDateDesc(Integer accountId);
    
    List<M_WITHOLDING_TAX> findAllByAccountId(Integer accountId);
    
    
}

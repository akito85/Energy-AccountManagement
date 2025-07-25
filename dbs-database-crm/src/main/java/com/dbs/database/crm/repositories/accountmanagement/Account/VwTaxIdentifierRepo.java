package com.dbs.database.crm.repositories.accountmanagement.Account;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.VW_TAX_IDENTIFIER;
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
public interface VwTaxIdentifierRepo extends PagingAndSortingRepository<VW_TAX_IDENTIFIER, Integer>, JpaSpecificationExecutor<VW_TAX_IDENTIFIER> {
    default Specification<VW_TAX_IDENTIFIER> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_TAX_IDENTIFIER> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_TAX_IDENTIFIER>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_TAX_IDENTIFIER>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_TAX_IDENTIFIER> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_TAX_IDENTIFIER> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_TAX_IDENTIFIER> addDefaultFilters(Specification<VW_TAX_IDENTIFIER> specification, Map<String, Object> filter, Boolean isFirst) {
        if(specification == null) {
    		specification =(Specification<VW_TAX_IDENTIFIER>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR);
        } else {
    		specification =specification.and((Specification<VW_TAX_IDENTIFIER>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR));
        }  
        /*INFO: Add Entity Filter*/
//        specification = (Specification<VW_TAX_IDENTIFIER>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);

        /*INFO: Add Cost Center Filter*/
//        List<String> tes = null;//Get Cost Center From Username To Be Done
        return specification;
    }
    
    Page<VW_TAX_IDENTIFIER> findAll(Specification<VW_TAX_IDENTIFIER> specification, Pageable paging);

    Optional<VW_TAX_IDENTIFIER> findTopByAccountIdAndStatusOrderByCreatedDateDesc(Integer integer, String status);
}

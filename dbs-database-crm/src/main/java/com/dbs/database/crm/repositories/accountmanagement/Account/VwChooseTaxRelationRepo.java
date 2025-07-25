package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.VW_CHOOSE_TAX_RELATION;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import static com.dbs.common.base.utils.Constant.*;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwChooseTaxRelationRepo extends PagingAndSortingRepository<VW_CHOOSE_TAX_RELATION, Integer>, JpaSpecificationExecutor<VW_CHOOSE_TAX_RELATION> {

    // ACCOUNT ID NOT IN (FOR CHOOSE AT ACCOUNT DETAIL)
    default Specification<VW_CHOOSE_TAX_RELATION> getSpecificationFromFiltersNot(MaterialTablePagingRequest pagingdata, Map<String, Object> filter, Integer accountId) {
        Specification<VW_CHOOSE_TAX_RELATION> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_CHOOSE_TAX_RELATION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_CHOOSE_TAX_RELATION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFiltersNot(specification, filter,false, accountId);
        /*code here*/

        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    default Specification<VW_CHOOSE_TAX_RELATION> getSpecificationDefaultNot(Map<String, Object> filter, Integer accountId) {
        Specification<VW_CHOOSE_TAX_RELATION> specification = null;
        specification = addDefaultFiltersNot(specification, filter, true, accountId);
        return specification;
    }

    default Specification<VW_CHOOSE_TAX_RELATION> addDefaultFiltersNot(Specification<VW_CHOOSE_TAX_RELATION> specification, Map<String, Object> filter, Boolean isFirst, Integer accountId){
        if(accountId!=null) {
            specification = (Specification<VW_CHOOSE_TAX_RELATION>) where(PagingUtils.createSpecification(("accountId~" + accountId), NOT_EQUALS_SELECTOR));
        }

        return specification;
    }
    
//    @Query("SELECT a FROM VW_CHOOSE_TAX_RELATION a WHERE a.accountId NOT IN :accountId")
//    List<VW_CHOOSE_TAX_RELATION> findAllNotIn(Integer accountId);
    
    List<VW_CHOOSE_TAX_RELATION> findAll();
    
    @Query(value ="SELECT * FROM VW_CHOOSE_TAX_RELATION WHERE ACCOUNT_ID NOT IN :accountId AND TAX_IDENTIFIER_TYPE IS NOT NULL", nativeQuery = true)
    Page<VW_CHOOSE_TAX_RELATION> findAllNotInAccountId(Pageable pageable, Integer accountId);
}

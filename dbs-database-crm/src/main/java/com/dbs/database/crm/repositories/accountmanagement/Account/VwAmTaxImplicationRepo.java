package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.VW_AM_TAXIMPLICATION;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwAmTaxImplicationRepo extends PagingAndSortingRepository<VW_AM_TAXIMPLICATION, Integer>, JpaSpecificationExecutor<VW_AM_TAXIMPLICATION> {
    default Specification<VW_AM_TAXIMPLICATION> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata) {
        Specification<VW_AM_TAXIMPLICATION> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            if (i == 0) {
                specification = (Specification<VW_AM_TAXIMPLICATION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            } else {
                specification.and((Specification<VW_AM_TAXIMPLICATION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            }
            i++;
        }
        return specification;
    }
    default Specification<VW_AM_TAXIMPLICATION> getSpecificationDefault() {
        return null;
    }

    default Specification<VW_AM_TAXIMPLICATION> getSpecificationFromFiltersAccount(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_AM_TAXIMPLICATION> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_AM_TAXIMPLICATION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_AM_TAXIMPLICATION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFiltersAccount(specification, filter, false);
        /*code here*/

        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    default Specification<VW_AM_TAXIMPLICATION> getSpecificationDefaultAccount(Map<String, Object> filter) {
        Specification<VW_AM_TAXIMPLICATION> specification = null;
        specification = addDefaultFiltersAccount(specification, filter, true);
        return specification;
    }

    default Specification<VW_AM_TAXIMPLICATION> addDefaultFiltersAccount(Specification<VW_AM_TAXIMPLICATION> specification, Map<String, Object> filter, Boolean isFirst){
        if(isFirst){
            if(!ObjectUtils.isEmpty(filter.get("id"))){
                specification = (Specification<VW_AM_TAXIMPLICATION>) where(PagingUtils.createINSpecification("id", (List<Integer>)filter.get("id")));
            }
        }else{
            if(!ObjectUtils.isEmpty(filter.get("id"))){
                specification =  specification.and((Specification<VW_AM_TAXIMPLICATION>) where(PagingUtils.createINSpecification("id", (List<Integer>)filter.get("id"))));
            }
        }
        return specification;
    }

    Optional<VW_AM_TAXIMPLICATION> findTopById(Integer id);

}

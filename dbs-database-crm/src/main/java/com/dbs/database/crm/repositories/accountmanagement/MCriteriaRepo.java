package com.dbs.database.crm.repositories.accountmanagement;


import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.database.crm.entities.accountmanagement.M_CRITERIA;
import java.util.Optional;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MCriteriaRepo extends PagingAndSortingRepository<M_CRITERIA,Integer>, JpaSpecificationExecutor<M_CRITERIA> {

    default Specification<M_CRITERIA> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_CRITERIA> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_CRITERIA>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_CRITERIA>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_CRITERIA> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_CRITERIA> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_CRITERIA> addDefaultFilters(Specification<M_CRITERIA> specification, Map<String, Object> filter, Boolean isFirst){

        specification = (Specification<M_CRITERIA>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        specification = specification.and((Specification<M_CRITERIA>)PagingUtils.createSpecification("mPricingId~"+filter.get("mTosId"),DEFAULT_SELECTOR));
        return specification;
    }
    
    Optional<List<M_CRITERIA>> findByReferenceId(Integer referenceId);

}

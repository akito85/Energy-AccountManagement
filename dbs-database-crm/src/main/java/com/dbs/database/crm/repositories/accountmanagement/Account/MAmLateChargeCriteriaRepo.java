package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_AM_LATECHARGE_CRITERIA;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MAmLateChargeCriteriaRepo extends PagingAndSortingRepository<M_AM_LATECHARGE_CRITERIA, Integer>, JpaSpecificationExecutor<M_AM_LATECHARGE_CRITERIA> {

    default Specification<M_AM_LATECHARGE_CRITERIA> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_AM_LATECHARGE_CRITERIA> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_AM_LATECHARGE_CRITERIA>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_AM_LATECHARGE_CRITERIA>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_AM_LATECHARGE_CRITERIA> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_AM_LATECHARGE_CRITERIA> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_AM_LATECHARGE_CRITERIA> addDefaultFilters(Specification<M_AM_LATECHARGE_CRITERIA> specification, Map<String, Object> filter, Boolean isFirst){

        return specification;
    }

    List<M_AM_LATECHARGE_CRITERIA> findAllByLatechargeId(Integer latechargeId);
}

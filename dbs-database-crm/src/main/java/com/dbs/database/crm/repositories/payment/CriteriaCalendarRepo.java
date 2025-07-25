package com.dbs.database.crm.repositories.payment;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.payment.M_PAY_CRITERIA;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface CriteriaCalendarRepo extends PagingAndSortingRepository<M_PAY_CRITERIA, Integer>, JpaSpecificationExecutor<M_PAY_CRITERIA> {

    @SuppressWarnings("unchecked")
    default Specification<M_PAY_CRITERIA> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_PAY_CRITERIA> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_PAY_CRITERIA>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_PAY_CRITERIA>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_PAY_CRITERIA> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_PAY_CRITERIA> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<M_PAY_CRITERIA> addDefaultFilters(Specification<M_PAY_CRITERIA> specification, Map<String, Object> filter, Boolean isFirst){

        /*INFO: Add Entity Filter*/
        specification = (Specification<M_PAY_CRITERIA>) PagingUtils
                .createCommonColumnNumberEqualsFilter(specification, "referenceId", Long.parseLong(filter.get("referenceId").toString()), isFirst);
        specification = specification.and((Specification<M_PAY_CRITERIA>)PagingUtils.createCommonColumnVarcharEqualsFilter(specification, "modulType", filter.get("modulType").toString(), false));

        return specification;
    }

    List<M_PAY_CRITERIA> findAllByReferenceIdAndModulType(Integer referenceId, String modulName);

    void deleteAllByIdNotInAndReferenceId(List<Integer> idList, Integer referenceId);
}

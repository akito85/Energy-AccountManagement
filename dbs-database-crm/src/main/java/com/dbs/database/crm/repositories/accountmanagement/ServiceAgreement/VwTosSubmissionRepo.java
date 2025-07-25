package com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.VW_TOS_SUBMISSION;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwTosSubmissionRepo extends PagingAndSortingRepository<VW_TOS_SUBMISSION, Integer>, JpaSpecificationExecutor<VW_TOS_SUBMISSION> {
    default Specification<VW_TOS_SUBMISSION> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_TOS_SUBMISSION> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_TOS_SUBMISSION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_TOS_SUBMISSION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_TOS_SUBMISSION> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_TOS_SUBMISSION> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_TOS_SUBMISSION> addDefaultFilters(Specification<VW_TOS_SUBMISSION> specification, Map<String, Object> filter, Boolean isFirst){
        if(filter.get("saId") != null) {
            if(specification != null) {
                specification = specification.and((Specification<VW_TOS_SUBMISSION>) PagingUtils.createSpecification("saId"+"~"+filter.get("saId").toString(), EQUALS_SELECTOR));
            } else {
                specification = (Specification<VW_TOS_SUBMISSION>) PagingUtils.createSpecification("saId"+"~"+filter.get("saId").toString(), EQUALS_SELECTOR);
            }
        }

        return specification;
    }
    List<VW_TOS_SUBMISSION> findAll();
    List<VW_TOS_SUBMISSION> findAllBySaId(Integer saId);
}

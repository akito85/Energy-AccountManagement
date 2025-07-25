package com.dbs.database.crm.repositories.usermanagement.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.view.VW_CC_SIBLINGS;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VWCCSiblingsRepo extends PagingAndSortingRepository<VW_CC_SIBLINGS, Integer>, JpaSpecificationExecutor<VW_CC_SIBLINGS> {

    default Specification<VW_CC_SIBLINGS> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_CC_SIBLINGS> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_CC_SIBLINGS>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_CC_SIBLINGS>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_CC_SIBLINGS> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_CC_SIBLINGS> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_CC_SIBLINGS> addDefaultFilters(Specification<VW_CC_SIBLINGS> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        specification = (Specification<VW_CC_SIBLINGS>) PagingUtils.createHeaderFilter(specification, "parentId",Integer.parseInt(filter.get("parentId").toString()), isFirst);

        /*INFO: Is Deleted Filter*/
        specification = (Specification<VW_CC_SIBLINGS>) PagingUtils.createIsDeletedFilter(specification, false, false);

        return specification;
    }
}


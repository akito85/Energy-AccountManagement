package com.dbs.database.crm.repositories.usermanagement.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.view.VW_M_DATA_ACCESS_HIERARCHY;
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
public interface VWMHierarchyRepo extends PagingAndSortingRepository<VW_M_DATA_ACCESS_HIERARCHY, Integer>, JpaSpecificationExecutor<VW_M_DATA_ACCESS_HIERARCHY> {

    default Specification<VW_M_DATA_ACCESS_HIERARCHY> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_M_DATA_ACCESS_HIERARCHY> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_M_DATA_ACCESS_HIERARCHY>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_M_DATA_ACCESS_HIERARCHY>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_M_DATA_ACCESS_HIERARCHY> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_M_DATA_ACCESS_HIERARCHY> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_M_DATA_ACCESS_HIERARCHY> addDefaultFilters(Specification<VW_M_DATA_ACCESS_HIERARCHY> specification, Map<String, Object> filter, Boolean isFirst) {
        if (filter.get("entityId") != null) {
            specification = (Specification<VW_M_DATA_ACCESS_HIERARCHY>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        }
        return specification;
    }

    List<VW_M_DATA_ACCESS_HIERARCHY> findAll();
}

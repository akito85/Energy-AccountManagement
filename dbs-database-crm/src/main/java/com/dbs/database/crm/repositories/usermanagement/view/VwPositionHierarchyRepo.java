package com.dbs.database.crm.repositories.usermanagement.view;


import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.M_POSITION_HIERARCHY;
import com.dbs.database.crm.entities.usermanagement.view.VW_POSITION_HIERARCHY;
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
public interface VwPositionHierarchyRepo extends PagingAndSortingRepository<VW_POSITION_HIERARCHY, Integer>, JpaSpecificationExecutor<VW_POSITION_HIERARCHY> {
    default Specification<VW_POSITION_HIERARCHY> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_POSITION_HIERARCHY> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_POSITION_HIERARCHY>) where(PagingUtils.createSpecification(sr,DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_POSITION_HIERARCHY>) PagingUtils.createSpecification(sr,DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFilters(specification, filter,false);

        return specification;
    }

    default Specification<VW_POSITION_HIERARCHY> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_POSITION_HIERARCHY> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_POSITION_HIERARCHY> addDefaultFilters(Specification<VW_POSITION_HIERARCHY> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        if (filter.get("entityId") != null) {
            specification = (Specification<VW_POSITION_HIERARCHY>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        }

        specification = (Specification<VW_POSITION_HIERARCHY>) PagingUtils.createIsDeletedFilter(specification, false, isFirst);

        return specification;
    }

    List<VW_POSITION_HIERARCHY> findAll();
}
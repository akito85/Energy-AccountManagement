package com.dbs.database.crm.repositories.usermanagement.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_PROPERTIES_DTL;
import com.dbs.database.crm.entities.usermanagement.view.VW_R_GLOBALTYPE_VALUE;
import com.dbs.database.crm.entities.usermanagement.view.VW_R_GLOBAL_PROPERTIES_DTL;
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
public interface VWRGlobalPropertiesDtlRepo extends PagingAndSortingRepository<VW_R_GLOBAL_PROPERTIES_DTL, Integer>, JpaSpecificationExecutor<VW_R_GLOBAL_PROPERTIES_DTL> {
    default Specification<VW_R_GLOBAL_PROPERTIES_DTL> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_R_GLOBAL_PROPERTIES_DTL> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_R_GLOBAL_PROPERTIES_DTL>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_R_GLOBAL_PROPERTIES_DTL>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFilters(specification, filter, false);

        return specification;
    }

    default Specification<VW_R_GLOBAL_PROPERTIES_DTL> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_R_GLOBAL_PROPERTIES_DTL> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_R_GLOBAL_PROPERTIES_DTL> addDefaultFilters(Specification<VW_R_GLOBAL_PROPERTIES_DTL> specification, Map<String, Object> filter, Boolean isFirst) {
        /*INFO: Entity Filter*/
        specification = (Specification<VW_R_GLOBAL_PROPERTIES_DTL>) PagingUtils.createHeaderFilter(specification, "gpId", Integer.parseInt(filter.get("gpId").toString()), isFirst);

        /*INFO: Is Deleted Filter*/
        specification = (Specification<VW_R_GLOBAL_PROPERTIES_DTL>) PagingUtils.createIsDeletedFilter(specification, false, false);

        return specification;
    }

    List<VW_R_GLOBAL_PROPERTIES_DTL> findAllByGpId(Integer gpId);
}

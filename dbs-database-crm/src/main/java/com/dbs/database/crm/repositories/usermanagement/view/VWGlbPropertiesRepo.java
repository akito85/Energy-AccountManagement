package com.dbs.database.crm.repositories.usermanagement.view;

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
import com.dbs.database.crm.entities.usermanagement.view.VW_GLB_PROPERTIES;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VWGlbPropertiesRepo extends PagingAndSortingRepository<VW_GLB_PROPERTIES, Integer>, JpaSpecificationExecutor<VW_GLB_PROPERTIES> {
    default Specification<VW_GLB_PROPERTIES> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<VW_GLB_PROPERTIES> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_GLB_PROPERTIES>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_GLB_PROPERTIES>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<VW_GLB_PROPERTIES> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_GLB_PROPERTIES> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_GLB_PROPERTIES> addDefaultFilters(Specification<VW_GLB_PROPERTIES> specification,
                                                     Map<String, Object> filter, Boolean isFirst) {
        if (filter.get("entityId") != null) {
            specification = (Specification<VW_GLB_PROPERTIES>) PagingUtils.createEntityFilter(specification,
                    Integer.parseInt(filter.get("entityId").toString()), isFirst);
        }
        return specification;
    }

    List<VW_GLB_PROPERTIES> findAll();
}

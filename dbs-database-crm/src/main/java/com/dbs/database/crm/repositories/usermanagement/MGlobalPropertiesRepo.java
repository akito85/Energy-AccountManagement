package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.M_GLOBAL_PROPERTIES;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.Query;

public interface MGlobalPropertiesRepo extends PagingAndSortingRepository<M_GLOBAL_PROPERTIES, Integer>, JpaSpecificationExecutor<M_GLOBAL_PROPERTIES> {
    List<M_GLOBAL_PROPERTIES> findAllByIsDeleted(Boolean isDeleted);

    Optional<M_GLOBAL_PROPERTIES> findByGpId(Integer gpId);

    M_GLOBAL_PROPERTIES findAllByGpId (Integer gpId);

    default Specification<M_GLOBAL_PROPERTIES> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_GLOBAL_PROPERTIES> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_GLOBAL_PROPERTIES>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_GLOBAL_PROPERTIES>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFilters(specification, filter, false);

        return specification;
    }

    default Specification<M_GLOBAL_PROPERTIES> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_GLOBAL_PROPERTIES> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_GLOBAL_PROPERTIES> addDefaultFilters(Specification<M_GLOBAL_PROPERTIES> specification, Map<String, Object> filter, Boolean isFirst) {
        if (filter.get("entityId") != null) {
            specification = (Specification<M_GLOBAL_PROPERTIES>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
            specification = (Specification<M_GLOBAL_PROPERTIES>) PagingUtils.createIsDeletedFilter(specification, false, false);
        }
        return specification;
    }

    List<M_GLOBAL_PROPERTIES> findAll();
    
    M_GLOBAL_PROPERTIES findAllByName(String name);

    @Query("SELECT u FROM M_GLOBAL_PROPERTIES u WHERE LOWER(u.name) = LOWER(:name) AND u.status = 'ACTIVE'")
    Optional<M_GLOBAL_PROPERTIES> findByName(String name);
}

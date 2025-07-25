package com.dbs.database.crm.repositories.usermanagement;


import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.M_POSITION_HIERARCHY;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.database.crm.entities.usermanagement.M_LOGIN_BACKGROUND;
import java.util.Optional;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MPositionHierarchyRepo extends PagingAndSortingRepository<M_POSITION_HIERARCHY, Integer>, JpaSpecificationExecutor<M_POSITION_HIERARCHY> {
    List<M_POSITION_HIERARCHY> findAllByIsDeleted(Boolean isDeleted);
    List<M_POSITION_HIERARCHY> findAllByStatus(String isDeleted);

    default Specification<M_POSITION_HIERARCHY> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_POSITION_HIERARCHY> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_POSITION_HIERARCHY>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_POSITION_HIERARCHY>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFilters(specification, filter,false);

        return specification;
    }

    default Specification<M_POSITION_HIERARCHY> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_POSITION_HIERARCHY> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_POSITION_HIERARCHY> addDefaultFilters(Specification<M_POSITION_HIERARCHY> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        if (filter.get("entityId") != null) {
            specification = (Specification<M_POSITION_HIERARCHY>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        }

        specification = (Specification<M_POSITION_HIERARCHY>) PagingUtils.createIsDeletedFilter(specification, false, isFirst);

        return specification;
    }

//    Optional<List<M_POSITION_HIERARCHY>> findByName(String name);
    
    Optional<M_POSITION_HIERARCHY> findByStatus(String status);
    
    List<M_POSITION_HIERARCHY> findAll();
    
    @Query("SELECT u FROM M_POSITION_HIERARCHY u WHERE LOWER(u.name) = LOWER(:name)")
    Optional<M_POSITION_HIERARCHY> findByName(@Param("name") String name);
    
    boolean existsByNameIgnoreCase(String name);

}

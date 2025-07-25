package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_PROPERTIES_DTL;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
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
public interface RGlobalPropertiesRepo extends JpaRepository<R_GLOBAL_PROPERTIES_DTL, Integer> {
    List<R_GLOBAL_PROPERTIES_DTL> findAllByGpIdAndIsDeleted(Integer gpId, Boolean isDeleted);

//    default Specification<R_GLOBAL_PROPERTIES_DTL> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
//        Specification<R_GLOBAL_PROPERTIES_DTL> specification = null;
//        //Add Filter From Front End
//        int i = 0;
//        for (String sr : pagingdata.getSearch()) {
//            specification =
//                    i == 0 ?
//                            (Specification<R_GLOBAL_PROPERTIES_DTL>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
//                            : specification.and((Specification<R_GLOBAL_PROPERTIES_DTL>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
//            i++;
//        }
//        /*------------------------------------------*/
//        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
//        specification = addDefaultFilters(specification, filter, false);
//
//        return specification;
//    }
//
//    default Specification<R_GLOBAL_PROPERTIES_DTL> getSpecificationDefault(Map<String, Object> filter) {
//        Specification<R_GLOBAL_PROPERTIES_DTL> specification = null;
//        specification = addDefaultFilters(specification, filter, true);
//        return specification;
//    }
//
//    default Specification<R_GLOBAL_PROPERTIES_DTL> addDefaultFilters(Specification<R_GLOBAL_PROPERTIES_DTL> specification, Map<String, Object> filter, Boolean isFirst) {
//        /*INFO: Entity Filter*/
//        specification = (Specification<R_GLOBAL_PROPERTIES_DTL>) PagingUtils.createHeaderFilter(specification, "gpId", Integer.parseInt(filter.get("gpId").toString()), isFirst);
//
//        /*INFO: Is Deleted Filter*/
//        specification = (Specification<R_GLOBAL_PROPERTIES_DTL>) PagingUtils.createIsDeletedFilter(specification, false, false);
//
//        return specification;
//    }

    Optional<R_GLOBAL_PROPERTIES_DTL> findByGpDetailId(Integer gpId);

    List<R_GLOBAL_PROPERTIES_DTL> findAllByGpId(Integer gpId);

    @Query("SELECT a FROM R_GLOBAL_PROPERTIES_DTL a WHERE a.gpId = :gpId AND a.gpDetailId NOT IN :gpDetailId")
    List<R_GLOBAL_PROPERTIES_DTL> findNotIn(Integer gpId, List<Integer> gpDetailId);

    List<R_GLOBAL_PROPERTIES_DTL> findAll();
}

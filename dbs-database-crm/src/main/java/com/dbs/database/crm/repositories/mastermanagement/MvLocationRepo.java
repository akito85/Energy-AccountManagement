package com.dbs.database.crm.repositories.mastermanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.MV_LOCATION;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MvLocationRepo extends PagingAndSortingRepository<MV_LOCATION, Integer>, JpaSpecificationExecutor<MV_LOCATION> {
    default Specification<MV_LOCATION> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        org.springframework.data.jpa.domain.Specification<com.dbs.database.crm.entities.accountmanagement.MV_LOCATION> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<com.dbs.database.crm.entities.accountmanagement.MV_LOCATION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<com.dbs.database.crm.entities.accountmanagement.MV_LOCATION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<MV_LOCATION> getSpecificationDefault(Map<String, Object> filter) {
        Specification<MV_LOCATION> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<MV_LOCATION> addDefaultFilters(Specification<MV_LOCATION> specification, Map<String, Object> filter, Boolean isFirst){
        return specification;
    }

    @Modifying
    @Query(value = "BEGIN DBMS_MVIEW.REFRESH(:materializedViewName); END;", nativeQuery = true)
    void refreshMaterializedView(@Param("materializedViewName") String materializedViewName);

    Optional<MV_LOCATION> findTopBySubDistrictId(Integer subDistrictId);
}

package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.M_COSTCENTER;
import com.dbs.database.crm.entities.usermanagement.M_POSITION;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MPositionRepo extends PagingAndSortingRepository<M_POSITION, Integer>, JpaSpecificationExecutor<M_POSITION> {
    @Query("SELECT a.ccId FROM M_POSITION a WHERE a.positionId = :posId AND a.status = 'ACTIVE' AND a.isDeleted = false")
    Optional<M_COSTCENTER> findFirstByPositionIdAndStatusAndIsDeleted(@Param("posId") Integer posId);

    List<M_POSITION> findAllByCcId(Integer ccId);

    List<M_POSITION> findAllByIsDeleted(Boolean isDeleted);

    List<M_POSITION> findAllByIsDeletedAndStatusAndEntityId(Boolean isDeleted, String status, Integer entityId);

    List<M_POSITION> findAllByIsDeletedAndStatus(Boolean isDeleted, String status);

    default Specification<M_POSITION> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_POSITION> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_POSITION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_POSITION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFilters(specification, filter, false);

        return specification;
    }

    default Specification<M_POSITION> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_POSITION> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_POSITION> addDefaultFilters(Specification<M_POSITION> specification, Map<String, Object> filter, Boolean isFirst) {
        /*INFO: Add Entity Filter*/
        specification = (Specification<M_POSITION>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);

        /*INFO: IsDeleted Filter*/
        specification = (Specification<M_POSITION>) PagingUtils.createIsDeletedFilter(specification, false, false);

        return specification;
    }

    default Specification<M_POSITION> getSpecifitaionInPosition(List<Integer> listPosition){
        return where((Specification<M_POSITION>) PagingUtils.createINSpecification("positionId", listPosition));
    }

    Optional<M_POSITION> findByPositionId(Integer positionId);

    M_POSITION findByPositionIdAndIsDeleted(Integer positionId, Boolean isDeleted);

    @Query(value = "SELECT * FROM M_POSITION WHERE STATUS =:status AND IS_DELETED = :isDeleted", nativeQuery = true)
    List<M_POSITION> findStatusAndIsDeleted(String status, String isDeleted);
    
    List<M_POSITION> findAllByStatusAndIsDeletedAndEntityId(String status, Boolean isDeleted, Integer entityId);

    Optional<M_POSITION> findByPositionIdAndStatus(Integer positionId, String status);

    Optional<M_POSITION> findByNameAndStatus(String name, String status);

    List<M_POSITION> findAllByOrderByPositionIdAsc();
    @Query("SELECT u FROM M_POSITION u WHERE LOWER(u.name) = LOWER(:name)")
    Optional<M_POSITION> findByName(String name);
    List<M_POSITION> findAllByName(String name);

    List<M_POSITION> findAll();

    List<M_POSITION> findAllByIsDeletedAndEntityId(Boolean isDeleted, Integer entityId);
    Optional<M_POSITION> findByPositionIdAndStatusAndIsDeleted(Integer positionId, String status, Boolean isDeleted);
    
    Optional<M_POSITION> findByNameIgnoreCaseAndStatusAndIsDeleted(String name, String status, Boolean isDeleted);
}

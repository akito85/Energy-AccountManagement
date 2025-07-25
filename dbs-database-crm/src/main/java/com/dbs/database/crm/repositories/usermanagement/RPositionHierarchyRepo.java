package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.R_POSITION_HIERARCHY;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional(value = "crmTransactionManager")
public interface RPositionHierarchyRepo extends PagingAndSortingRepository<R_POSITION_HIERARCHY, Integer>, JpaSpecificationExecutor<R_POSITION_HIERARCHY> {
    List<R_POSITION_HIERARCHY> findAllByHierId(Integer parent);

    Optional<R_POSITION_HIERARCHY> findByrHierId(Integer rHierId);

    List<R_POSITION_HIERARCHY> findAll();
    
    @Query("SELECT a FROM R_POSITION_HIERARCHY a WHERE a.hierId = :hierId AND a.rHierId NOT IN :rHierId")
    List<R_POSITION_HIERARCHY> findNotIn(Integer hierId, List<Integer> rHierId);

    @Query(value = "SELECT RPH.* FROM R_POSITION_HIERARCHY RPH INNER JOIN M_POSITION_HIERARCHY MPH \n" +
            "ON MPH.HIER_ID=RPH.HIER_ID \n" +
            "WHERE MPH.STATUS = 'ACTIVE' OR RPH.POSITION_ID =:positionId \n" +
            "AND (MPH.START_DATE <= SYSDATE AND PARENT_ID=:positionId)", nativeQuery = true)
    List<R_POSITION_HIERARCHY> findListChildLevelOne(Integer positionId);

    List<R_POSITION_HIERARCHY> findByParentId(Integer parentId);
    List<R_POSITION_HIERARCHY> findByParentIdAndHierId(Integer parentId, Integer hierId);
}

package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.M_COSTCENTER;
import com.dbs.database.crm.entities.usermanagement.R_DATA_ACCESS_HIERARCHY;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional(value = "crmTransactionManager")
public interface RDataAccessHierarchyRepo extends PagingAndSortingRepository<R_DATA_ACCESS_HIERARCHY, Integer>, JpaSpecificationExecutor<R_DATA_ACCESS_HIERARCHY> {
    Optional<R_DATA_ACCESS_HIERARCHY> findByrDahIdAndStatus(Integer rDahId, String status);

    Optional<R_DATA_ACCESS_HIERARCHY> findByrDahId(Integer rDahId);

    Optional<R_DATA_ACCESS_HIERARCHY> findByrDahIdAndDahId(Integer rDahId, Integer dahId);

    List<R_DATA_ACCESS_HIERARCHY> findByDahId(Integer dahId);

    List<R_DATA_ACCESS_HIERARCHY> findAll();
    @Query("SELECT a FROM R_DATA_ACCESS_HIERARCHY a WHERE a.ccId =:ccId AND a.status = 'ACTIVE'")
    List<R_DATA_ACCESS_HIERARCHY> findAllCcId(@Param("ccId") Integer ccId);
    @Query("SELECT a FROM R_DATA_ACCESS_HIERARCHY a WHERE a.ccId =:ccId AND a.status = 'ACTIVE' AND a.dahId =:activeDah")
    List<R_DATA_ACCESS_HIERARCHY> findAllCcIdActiveDah(@Param("ccId") Integer ccId, @Param("activeDah") Integer activeDah);
    @Query("SELECT DISTINCT(a.ccId) FROM R_DATA_ACCESS_HIERARCHY a WHERE a.parentId =:ccId AND a.status = 'ACTIVE' AND a.dahId =:activeDahId")
    List<Integer> findChild(@Param("ccId") Integer ccId, @Param("activeDahId") Integer activeDahId);
    
    @Query("SELECT a FROM R_DATA_ACCESS_HIERARCHY a WHERE a.status = 'ACTIVE' AND a.dahId = :dahId AND a.rDahId = :rDahId")
    List<R_DATA_ACCESS_HIERARCHY> findByrDahIds(Integer dahId, Integer rDahId);
    
    @Query("SELECT a FROM R_DATA_ACCESS_HIERARCHY a WHERE a.dahId = :dahId AND a.rDahId NOT IN :rDahIds")
    List<R_DATA_ACCESS_HIERARCHY> findNotIn(Integer dahId, List<Integer> rDahIds);
    @Query("SELECT DISTINCT(a.parentId) FROM R_DATA_ACCESS_HIERARCHY a WHERE a.ccId = :ccId AND a.status = 'ACTIVE' AND a.parentId IS NOT NULL and a.parentId != :ccId AND a.dahId =:activeDahId")
    List<Integer> findCcId(@Param("ccId") Integer ccId, @Param("activeDahId") Integer activeDahId);
    
    Optional<R_DATA_ACCESS_HIERARCHY> findTopByDahIdAndCcIdAndStatusOrderByCreatedDateDesc(Integer dahId, Integer ccId, String status);

    Optional<R_DATA_ACCESS_HIERARCHY> findByDahIdAndCcIdAndStatus(Integer dahId, Integer ccId, String status);

    List<R_DATA_ACCESS_HIERARCHY> findAllByDahIdAndCcId (Integer dahId, Integer ccId);

    List<R_DATA_ACCESS_HIERARCHY> findAllByDahIdAndParentId (Integer dahId, Integer ccId);

}

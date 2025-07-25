package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.M_DATA_ACCESS_HIERARCHY;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MDataAccessHierarchyRepo extends PagingAndSortingRepository<M_DATA_ACCESS_HIERARCHY, Integer>, JpaSpecificationExecutor<M_DATA_ACCESS_HIERARCHY> {
    Optional<M_DATA_ACCESS_HIERARCHY> findBydahId(Integer dahId);
    Optional<M_DATA_ACCESS_HIERARCHY> findByDahIdAndStatus(Integer dahId, String status);

    List<M_DATA_ACCESS_HIERARCHY> findAll();

    @Query("SELECT u FROM M_DATA_ACCESS_HIERARCHY u WHERE LOWER(u.name) = LOWER(:name)")
    List<M_DATA_ACCESS_HIERARCHY> findAllByName(String name);

    @Query("SELECT u FROM M_DATA_ACCESS_HIERARCHY u WHERE LOWER(u.name) = LOWER(:name)")
    Optional<List<M_DATA_ACCESS_HIERARCHY>> findByName(String name);
    
    Optional<M_DATA_ACCESS_HIERARCHY> findTopByStatusOrderByCreatedDateDesc(String status);

    Optional<M_DATA_ACCESS_HIERARCHY> findByStatus(String status);
    
    boolean existsByNameIgnoreCase(String name);
}

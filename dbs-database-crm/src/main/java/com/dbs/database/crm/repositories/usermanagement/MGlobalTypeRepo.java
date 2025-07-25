package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.M_GLOBAL_TYPE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MGlobalTypeRepo extends JpaRepository<M_GLOBAL_TYPE, Integer> {
    Optional<M_GLOBAL_TYPE> findAllByGroupNameAndStatusAndIsDeleted(String groupName, String status, Boolean isDeleted);

    Optional<M_GLOBAL_TYPE> findAllByGroupName(String groupName);

    Optional<M_GLOBAL_TYPE> findByGroupNameAndEntity(String groupName, Integer entity);

    @Query("SELECT u FROM M_GLOBAL_TYPE u WHERE LOWER(u.groupName) = LOWER(:groupName)")
    Optional<M_GLOBAL_TYPE> findByGroupName(String groupName);

    M_GLOBAL_TYPE findTopByGlbTypeIdAndIsDeleted(Integer globalType, Boolean isDeleted);
    
    Optional<M_GLOBAL_TYPE> findTopByGlbTypeIdAndStatusAndIsDeleted(Integer globalType, String status,Boolean isDeleted);

    M_GLOBAL_TYPE findAllByGlbTypeIdAndIsDeleted(Integer globalType, Boolean isDeleted);

    M_GLOBAL_TYPE findTopByGroupNameIgnoreCaseAndIsDeleted(String groupName, Boolean isDeleted);

    Optional<M_GLOBAL_TYPE> findFirstByGroupNameAndStatus(String groupName, String status);

    Optional<M_GLOBAL_TYPE> findByGlbTypeId(Integer glbTypeId);

    Optional<M_GLOBAL_TYPE> findByGlbTypeIdAndEntity(Integer glbTypeId, Integer entity);
}

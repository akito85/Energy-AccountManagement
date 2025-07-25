package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.M_FILE;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MFileRepo extends PagingAndSortingRepository<M_FILE, String>, JpaSpecificationExecutor<M_FILE> {
    M_FILE findTopByCreatedByAndIsDeletedAndPath(String createdBy, Boolean isDeleted, String path);

    List<M_FILE> findByCreatedByAndIsDeletedAndPath(String createdBy, Boolean isDeleted, String path);

    Optional<M_FILE> findByFileName(String fileName);

    List<M_FILE> findAll();
}

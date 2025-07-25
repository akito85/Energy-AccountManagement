package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.LOG_USER_PASSWORD;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
@Transactional(value = "crmTransactionManager")
public interface LogUserPassRepo extends PagingAndSortingRepository<LOG_USER_PASSWORD, Integer>, JpaSpecificationExecutor<LOG_USER_PASSWORD> {
    Optional<List<LOG_USER_PASSWORD>> findByUserId(String userId);

    Optional<LOG_USER_PASSWORD> findByPassword(String password);
    
    @Query("SELECT a FROM LOG_USER_PASSWORD a WHERE a.createdDate BETWEEN :startDate AND :endDate AND a.userId = :userId")
    Optional<List<LOG_USER_PASSWORD>> findAllByCreatedDateAndUserId(Date startDate, Date endDate, Integer userId);
    
    @Query(value = "SELECT * FROM LOG_USER_PASSWORD a WHERE a.USER_ID = :userId ORDER BY ID DESC FETCH FIRST :top ROWS ONLY", nativeQuery = true)
    Optional<List<LOG_USER_PASSWORD>> findTopByUserId(@Param("userId") Integer userId, @Param("top") Integer top);
}

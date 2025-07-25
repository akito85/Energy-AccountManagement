package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.T_APPROVAL;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface TApprovalRepo extends JpaRepository<T_APPROVAL, Integer> {
    Optional<T_APPROVAL> findFirstByIdTransAndApprovalTypeAndStatus(String id, String type, String status);
    
    Optional<T_APPROVAL> findFirstByIdTransAndCategoryAndStatus(String id, String cat, String status);
    Optional<T_APPROVAL> findFirstByIdTransAndCategoryInAndStatus(String id, List<String> cat, String status);

    List<T_APPROVAL> findBytAppIdAndStatus(Integer tAppId, String status);
    
    Optional<T_APPROVAL> findByIdTransAndCategory(String id, String cat, Sort sort);
    Optional<T_APPROVAL> findFirstByIdTransAndCategory(String id, String cat, Sort sort);
    Optional<List<T_APPROVAL>> findAllByIdTransAndCategory(String id, String cat, Sort sort);
    
    Optional<T_APPROVAL> findFirstByIdTrans(String id, Sort sort);
    @Query("SELECT a FROM T_APPROVAL a WHERE a.idTrans =:idTrans AND a.category IN (:categoryList)")
    List<T_APPROVAL> findAllByIdTransAndCategoryIn(String idTrans, List<String> categoryList);
}

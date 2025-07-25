package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.M_COSTCENTER;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MCostCenterRepo extends PagingAndSortingRepository<M_COSTCENTER, Integer>, JpaSpecificationExecutor<M_COSTCENTER> {
    Optional<M_COSTCENTER> findByccId(Integer integer);

    List<M_COSTCENTER> findAllByIsDeleted(Boolean isDeleted);

    List<M_COSTCENTER> findAllByStatusAndIsDeletedAndEntityId(String status, Boolean isDeleted, Integer entityId);
    
    List<M_COSTCENTER> findAllByStatusAndIsDeleted(String status, Boolean isDeleted);

    List<M_COSTCENTER> findAllByStatusAndEntityId(String status, Integer entityId);

    List<M_COSTCENTER> findAllByStatus(String status);

    M_COSTCENTER findByCodeIgnoreCaseAndIsDeleted(String code, Boolean isDeleted);

    List<M_COSTCENTER> findAllByCcTypeAndIsDeleted(String ccType, Boolean isDeleted);

    @Query(nativeQuery = true, value = "SELECT * FROM M_COSTCENTER \n" +
            "WHERE CC_TYPE NOT IN :ccType AND IS_DELETED = :isDeleted")
    List<M_COSTCENTER> findByCcTypeNotInAndIsDeleted(String ccType, String isDeleted);


//    Optional<M_COSTCENTER> findFirstByFullName(String fullName);

    List<M_COSTCENTER> findAll();

    Optional<List<M_COSTCENTER>> findAllByCcTypeIgnoreCaseAndIsDeleted(String type, Boolean isDeleted);

    M_COSTCENTER findTopByCcIdAndCcTypeAndIsDeleted(Integer id, String ccType, Boolean isDeleted);

    Optional <M_COSTCENTER> findByCcIdAndStatusAndIsDeleted(Integer ccid, String status, Boolean isDeleted);

    List<M_COSTCENTER> findByName(String name);
    
//     Optional<M_COSTCENTER> findByCodeAndIsDeletedAndStatus(String code, Boolean isDeleted, String status);

    Optional<M_COSTCENTER> findByCode(String code);
    Optional<M_COSTCENTER> findByCcIdAndCcType(Integer ccId, String ccType);

     List<M_COSTCENTER> findAllByCcIdAndStatusAndEntityId(Integer ccId, String status, Integer entityId);
     
     @Query("SELECT u FROM M_COSTCENTER u WHERE LOWER(u.name) = LOWER(:name)")
     List<M_COSTCENTER> findFirstByName(String name);
     
     boolean existsByNameIgnoreCase(String name);

     List<M_COSTCENTER> findAllByCcIdIn(List<Integer> ccId);
}

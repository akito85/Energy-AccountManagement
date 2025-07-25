package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.R_GROUPACCESS_MENU;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional(value = "crmTransactionManager")
public interface RGroupAccessMenuRepo extends JpaRepository<R_GROUPACCESS_MENU, Integer> {
    Optional<R_GROUPACCESS_MENU> findByGaMenuId(Integer gaMenuId);

    List<R_GROUPACCESS_MENU> findAllByMenuId(Integer menuId);
    
    List<R_GROUPACCESS_MENU> findAllByMenuIdAndStatus(Integer menuId, String status);

    List<R_GROUPACCESS_MENU> findByGaId(Integer gaId);

    List<R_GROUPACCESS_MENU> findByGaIdAndStatus(Integer gaId, String status);

    List<R_GROUPACCESS_MENU> findByGaIdAndMenuId(Integer gaId, Integer menuId);
    
    @Query("SELECT a FROM R_GROUPACCESS_MENU a WHERE a.gaId = :gaId AND a.status = :status AND a.menuId NOT IN :menuId")
    List<R_GROUPACCESS_MENU> findNotIn(Integer gaId, String status, List<Integer> menuId);
    
    @Query("SELECT a FROM R_GROUPACCESS_MENU a WHERE a.gaId = :gaId AND a.status = :status AND a.menuId IN :menuId")
    List<R_GROUPACCESS_MENU> findIn(Integer gaId, String status, List<Integer> menuId);
    
    Optional<R_GROUPACCESS_MENU> findByMenuIdAndGaId(Integer menuId,Integer gaId);
    
    @Query("SELECT a FROM R_GROUPACCESS_MENU a WHERE a.gaId = :gaId AND a.menuId IN :menuId")
    List<R_GROUPACCESS_MENU> searchInIdAndMenuId(Integer gaId, List<Integer> menuId);
}

package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.R_MENU_ACTION;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional(value = "crmTransactionManager")
public interface RMenuAction extends PagingAndSortingRepository<R_MENU_ACTION, Integer>, JpaSpecificationExecutor<R_MENU_ACTION> {
    List<R_MENU_ACTION> findAllByMenuIdAndStatus(Integer menuId, String status);
    List<R_MENU_ACTION> findAllByMenuId(Integer menuId);

    List<R_MENU_ACTION> findAllByStatus(String status);

    List<R_MENU_ACTION> findAll();

    List<R_MENU_ACTION> findBymenuId(Integer menuId);
    
    @Query("SELECT a FROM R_MENU_ACTION a WHERE a.menuId = :menuId AND a.menuActionId NOT IN :actionId AND status = 'ACTIVE'")
    List<R_MENU_ACTION> findNotIn(Integer menuId, List<Integer> actionId);
    
    @Query("SELECT a FROM R_MENU_ACTION a WHERE a.menuId = :menuId AND a.actionId IN :actionId AND status = 'INACTIVE'")
    List<R_MENU_ACTION> findInStatus(Integer menuId, List<Integer> actionId);
    
    List<R_MENU_ACTION> findBymenuIdAndActionId(Integer menuId, Integer actionId);
    
}


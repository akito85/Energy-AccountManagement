package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.M_MENU;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MMenuRepo extends PagingAndSortingRepository<M_MENU, Integer>, JpaSpecificationExecutor<M_MENU> {
    Optional<M_MENU> findById(Integer menuId);

    @Query("SELECT u FROM M_MENU u WHERE LOWER(u.name) = LOWER(:name)")
    Optional<M_MENU> findByName(@Param("name") String name);

    List<M_MENU> findAll();

    List<M_MENU> findAllByStatusOrderByMenuOrderAscNameAsc(String status);

    Optional<List<M_MENU>> findByPath(String path);
    
    @Query(value = "SELECT \n" +
                "MENU.* \n" +
                "from  \n" +
                "T_USER_GROUPACCESS TUG \n" +
                "JOIN R_GROUPACCESS_MENU RGM ON TUG.GA_ID = RGM.GA_ID AND RGM.STATUS = 'ACTIVE' \n" +
                "JOIN M_MENU MENU ON MENU.MENU_ID = RGM.MENU_ID AND MENU.STATUS = 'ACTIVE' \n" +
                "WHERE \n" +
                "TUG.STATUS = 'ACTIVE' \n" +
                "AND TUG.USER_ID = :userId  \n" +
                "UNION \n" +
                "SELECT \n" +
                "MENU.*  \n" +
                "FROM( \n" +
                "SELECT ( \n" +
                "SELECT USER_ID from M_USER where EMPLOYEE_ID = ud.DELEGATE_TO  \n" +
                ") USER_ID, uga.GA_ID  \n" +
                "from T_USER_GROUPACCESS uga \n" +
                "JOIN M_USER usr ON usr.USER_ID = uga.USER_ID \n" +
                "JOIN M_USER_DELEGATION ud ON ud.DELEGATE_FROM = usr.EMPLOYEE_ID \n" +
                "AND ud.START_DATE <= CAST(TRUNC(SYSDATE) AS TIMESTAMP) AND ud.END_DATE >= CAST(TRUNC(SYSDATE) AS TIMESTAMP) \n" +
                "AND ud.STATUS ='ACTIVE' \n" +
                ") X \n" +
                "JOIN M_GROUPACCESS MG ON MG.GA_ID = X.GA_ID AND MG.STATUS = 'ACTIVE' \n" +
                "JOIN R_GROUPACCESS_MENU RGM ON X.GA_ID = RGM.GA_ID AND RGM.STATUS = 'ACTIVE' \n" +
                "JOIN M_MENU MENU ON MENU.MENU_ID = RGM.MENU_ID AND MENU.STATUS = 'ACTIVE' \n" +
                "WHERE x.USER_ID = :userId", nativeQuery = true)
    List<M_MENU> findByUserId(@Param("userId") Integer userId);
    
    
    boolean existsByNameIgnoreCase(String name);
}

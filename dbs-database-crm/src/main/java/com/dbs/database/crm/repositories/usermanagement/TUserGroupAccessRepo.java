package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.T_USER_GROUPACCESS;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface TUserGroupAccessRepo extends JpaRepository<T_USER_GROUPACCESS, Integer> {
    Optional<T_USER_GROUPACCESS> findByUserGaId(Integer userGaId);

    List<T_USER_GROUPACCESS> findAllByGaId(Integer gaId);

    @Query("SELECT a FROM T_USER_GROUPACCESS a WHERE a.status = 'ACTIVE' AND a.userId = :userId")
    Optional<List<T_USER_GROUPACCESS>> findAllByUser(@Param("userId") Integer userId);

    Optional<T_USER_GROUPACCESS> findByUserId(Integer userId);

    @Query("SELECT a FROM T_USER_GROUPACCESS a WHERE a.status = 'ACTIVE' AND a.userGaId = :userGaId AND a.startDate <= :today AND a.endDate >= :today")
    Optional<T_USER_GROUPACCESS> findByUserGaId(Integer userGaId, Date today);
    
    @Query(value="SELECT *\n" +
            "FROM\n" +
            "T_USER_GROUPACCESS TUG\n" +
            "WHERE\n" +
            "TUG.START_DATE <= SYSDATE AND (TUG.END_DATE >= SYSDATE OR TUG.END_DATE IS NULL)\n" +
            "AND TUG.STATUS ='ACTIVE'\n" +
            "AND TUG.USER_ID = :userId\n" +
            "UNION\n" +
            "SELECT y.* FROM(\n" +
            "        SELECT * FROM (\n" +
            "        SELECT\n" +
            "        uga.USER_GA_ID,\n" +
            "        uga.GA_ID,\n" +
            "(\n" +
            "        SELECT USER_ID AS USER_ID_FROM from M_USER where EMPLOYEE_ID = ud.DELEGATE_TO\n" +
            ") AS USER_ID_FROM\n" +
            "from T_USER_GROUPACCESS uga\n" +
            "JOIN M_USER usr ON usr.USER_ID = uga.USER_ID AND uga.STATUS='ACTIVE'\n" +
            "JOIN M_USER_DELEGATION ud ON ud.DELEGATE_FROM = usr.EMPLOYEE_ID\n" +
            "        where\n" +
            "ud.STATUS ='ACTIVE' AND\n" +
            "ud.START_DATE <= CAST(TRUNC(SYSDATE) AS TIMESTAMP) AND\n" +
            "ud.END_DATE >= CAST(TRUNC(SYSDATE) AS TIMESTAMP) \n" +
            ") XY WHERE XY.USER_ID_FROM = :userId\n" +
            "GROUP BY\n" +
            "XY.USER_GA_ID, XY.GA_ID, XY.USER_ID_FROM\n" +
            ") X JOIN T_USER_GROUPACCESS y ON y.USER_GA_ID = X.USER_GA_ID", nativeQuery = true)
    List<T_USER_GROUPACCESS> findAllGroupByUser(@Param("userId") Integer userId);
}

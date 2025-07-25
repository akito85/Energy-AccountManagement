package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_ACCOUNT_USAGE_PERIODIC;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwAccUsgPeriodicRepo extends PagingAndSortingRepository<VW_ACCOUNT_USAGE_PERIODIC,Integer>, JpaSpecificationExecutor<VW_ACCOUNT_USAGE_PERIODIC> {
    @Query(nativeQuery = true, value = "SELECT X.* FROM(\n" +
            "SELECT\n" +
            "UP.SA_ID, \n" +
            "UP.ACCOUNT_NUMBER, \n" +
            "UP.BILLING_CYCLE_ID, \n" +
            "UP.BILLING_PERIOD, \n" +
            "COALESCE(UP.TOTAL_EST, 0) AS TOTAL_EST,\n" +
            "UP.MIN_USAGE,\n" +
            "UP.MAX_USAGE,\n" +
            "UP.ROW_NUMBER \n" +
            "FROM VW_ACCOUNT_USAGE_PERIODIC UP \n" +
            "WHERE \n" +
            "ACCOUNT_NUMBER = :accNumb \n" +
            ") X \n" +
            " JOIN (\n" +
            " SELECT TOTAL_EST AS TOT_EST, ROW_NUMBER AS ROW_NO, ACCOUNT_NUMBER AS ACC_NO FROM VW_ACCOUNT_USAGE_PERIODIC \n" +
            " WHERE ACCOUNT_NUMBER = :accNumb \n" +
            " AND BILLING_CYCLE_ID = :billCycId \n" +
            " AND BILLING_PERIOD = :billPeriod \n" +
            " AND TOTAL_EST IS NOT NULL \n" +
            " ) B ON B.ACC_NO = X.ACCOUNT_NUMBER \n" +
            " WHERE X.ROW_NUMBER BETWEEN B.ROW_NO - B.TOT_EST AND B.ROW_NO - 1")
    List<VW_ACCOUNT_USAGE_PERIODIC> findPeriodic (
            @Param("accNumb") String accNumb,
            @Param("billCycId") Integer billCycId,
            @Param("billPeriod") Integer billPeriod
    );

    Optional<VW_ACCOUNT_USAGE_PERIODIC> findByAccNumbAndBillCycIdAndBillPeriodAndTotalEstNotNull (
            String accNumb,
            Integer billCycId,
            Integer billPeriod
    );

    Optional<VW_ACCOUNT_USAGE_PERIODIC> findByAccNumbAndBillCycIdAndBillPeriod (
            String accNumb,
            Integer billCycId,
            Integer billPeriod
    );
    
    
    @Query(nativeQuery = true, value = "SELECT X.* FROM(\n" +
            "SELECT\n" +
            "UP.SA_ID, \n" +
            "UP.ACCOUNT_NUMBER, \n" +
            "UP.BILLING_CYCLE_ID, \n" +
            "UP.BILLING_PERIOD, \n" +
            "COALESCE(UP.TOTAL_EST, 0) AS TOTAL_EST,\n" +
            "UP.MIN_USAGE,\n" +
            "UP.MAX_USAGE,\n" +
            "UP.ROW_NUMBER \n" +
            "FROM VW_ACCOUNT_USAGE_PERIODIC UP \n" +
            "WHERE \n" +
            "ACCOUNT_NUMBER = :accNumb \n" +
            ") X \n" +
            " JOIN (\n" +
            " SELECT TOTAL_EST AS TOT_EST, ROW_NUMBER AS ROW_NO, ACCOUNT_NUMBER AS ACC_NO FROM VW_ACCOUNT_USAGE_PERIODIC \n" +
            " WHERE ACCOUNT_NUMBER = :accNumb \n" +
            " AND BILLING_CYCLE_ID = :billCycId \n" +
            " AND BILLING_PERIOD = :billPeriod \n" +
            " AND TOTAL_EST IS NOT NULL \n" +
            " ) B ON B.ACC_NO = X.ACCOUNT_NUMBER \n" +
            " WHERE X.ROW_NUMBER BETWEEN B.ROW_NO - B.TOT_EST AND B.ROW_NO - 1")
    List<Object[]> findPeriodicObj (
            String accNumb,
            Integer billCycId,
            Integer billPeriod
    );
}

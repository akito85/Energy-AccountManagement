package com.dbs.database.crm.repositories.rbi;

import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_CALCULATION_RESULT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface MRbiCalculationSPRepo extends
        JpaRepository<M_RBI_CALCULATION_RESULT, Void>{

    @Transactional
    @Modifying
    @Query(value = "CALL SP_RECALCULATE_SUPPORTING_DATA(:calCode, :accNumb, :userName, :remark, :entityId, :ccId)", nativeQuery = true)
    void recalculateRating(
            @Param("calCode") String calCode,
            @Param("accNumb") String accNumb,
            @Param("userName") String userName,
            @Param("remark") String remark,
            @Param("entityId") Integer entityId,
            @Param("ccId") Integer ccId
    );

    @Transactional
    @Modifying
    @Query(value = "CALL SP_RECALCULATE_BILLING(:bill_cycle, :bill_period, :calCode, :calType, :usr, :accNumb, :entityId, :ccId)", nativeQuery = true)
    void recalculateBilling(
            @Param("bill_cycle") Integer billCyc,
            @Param("bill_period") Integer billPeriod,
            @Param("calCode") String calCode,
            @Param("calType") Integer calType,
            @Param("usr") String usr,
            @Param("accNumb") String accNumb,
            @Param("entityId") Integer entityId,
            @Param("ccId") Integer ccId
    );

    @Transactional
    @Modifying
    @Query(value = "CALL SP_RETRY_SUPPORTING_DATA(:calCode, :accNum, :userName, :remark, :entityId, :ccId)", nativeQuery = true)
    void retryRating(
            @Param("calCode") String calCode,
            @Param("accNum") String accNum,
            @Param("userName") String userName,
            @Param("remark") String remark,
            @Param("entityId") Integer entityId,
            @Param("ccId") Integer ccId
    );

    @Transactional
    @Modifying
    @Query(value = "CALL SP_RECALCULATE_BILLING(:bill_cycle, :bill_period, :calCode, :calType, :usr, :accNumb, :entityId, :ccId)", nativeQuery = true)
    void retryBilling(
            @Param("bill_cycle") Integer billCyc,
            @Param("bill_period") Integer billPeriod,
            @Param("calCode") String calCode,
            @Param("calType") Integer calType,
            @Param("usr") String usr,
            @Param("accNumb") String accNumb,
            @Param("entityId") Integer entityId,
            @Param("ccId") Integer ccId
    );


    @Transactional
    @Modifying
    @Query(value = "CALL SP_CREATE_SUPPORTING_DATA(:calCode, :calType, :usr)", nativeQuery = true)
    void createRating(
            @Param("calCode") String calCode,
            @Param("calType") Integer calType,
            @Param("usr") String usr
    );

    @Transactional
    @Modifying
    @Query(value = "CALL SP_CALCULATE_BILLING_V1(:bill_cycle, :bill_period, :calCode, :calType, :usr)", nativeQuery = true)
    void createBilling(@Param("bill_cycle") Integer billCyc,
                       @Param("bill_period") Integer billPeriod,
                       @Param("calCode") String calCode,
                       @Param("calType") Integer calType,
                       @Param("usr") String usr);

}

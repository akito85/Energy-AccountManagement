package com.dbs.database.crm.repositories.payment;

import com.dbs.database.crm.entities.payment.T_PAY_LATE_CHARGE_V2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface TPayLateChargeRepoV2 extends JpaRepository<T_PAY_LATE_CHARGE_V2, Long> {

    @Query(value = "SELECT SUM(TOTAL_AMOUNT) FROM T_PAY_LATE_CHARGE_V2 WHERE BILLING_CODE=:billingCode GROUP BY BILLING_CODE", nativeQuery = true)
    Optional<BigDecimal> findSumAmountByBillingCode(String billingCode);

    Optional<T_PAY_LATE_CHARGE_V2> findByBillingCode(String billingCode);

    List<T_PAY_LATE_CHARGE_V2> findAllByStatusAndStatusApprovalAndAccountNumber (String status, String statusApp, String accNumb);

}

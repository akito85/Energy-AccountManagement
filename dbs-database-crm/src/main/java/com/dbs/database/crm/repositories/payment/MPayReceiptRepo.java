package com.dbs.database.crm.repositories.payment;

import com.dbs.database.crm.entities.payment.M_PAY_RECEIPT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MPayReceiptRepo extends JpaRepository<M_PAY_RECEIPT, Long> {

    @Query(value = "SELECT RECEIPT_NUMBER FROM M_PAY_RECEIPT ORDER BY RECEIPT_NUMBER DESC FETCH FIRST 1 ROW ONLY", nativeQuery = true)
    String findTopReceiptNumber();

    Optional<M_PAY_RECEIPT> findByReceiptNumber(String receiptNumber);
}

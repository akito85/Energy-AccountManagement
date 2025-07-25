package com.dbs.database.crm.repositories.payment;

import com.dbs.database.crm.entities.payment.T_PAY_GAPURA_PAYMENT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(value = "crmTransactionManager")
public interface GapuraPaymentRepo extends JpaRepository<T_PAY_GAPURA_PAYMENT, Long> {
}

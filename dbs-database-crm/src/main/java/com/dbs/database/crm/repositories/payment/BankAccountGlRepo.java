package com.dbs.database.crm.repositories.payment;

import com.dbs.database.crm.entities.payment.R_PAY_BANK_ACCOUNT_GL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface BankAccountGlRepo extends JpaRepository<R_PAY_BANK_ACCOUNT_GL, Long> {
}

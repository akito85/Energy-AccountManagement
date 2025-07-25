package com.dbs.database.crm.repositories.payment;

import com.dbs.database.crm.entities.payment.R_PAY_BANK_ACCOUNT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface BankAccountRepo extends JpaRepository<R_PAY_BANK_ACCOUNT, Long> {

    boolean existsByAccountNumberAndBankId(String accountNumber, long bankId);

    boolean existsByAccountNameIgnoreCaseAndBankId(String accountName, long bankId);

    Optional<R_PAY_BANK_ACCOUNT> findByAccountNumberAndStatus(String accountNumber, String status);

    boolean existsByIdNotAndAccountNumberAndBankId(Long id, String accountNumber, long bankId);

    boolean existsByIdNotAndAccountNameIgnoreCaseAndBankId(Long id, String accountNumber, long bankId);
}

package com.dbs.database.crm.repositories.payment;

import com.dbs.database.crm.entities.payment.R_PAY_TRANSACTION_CALENDAR_CRITERIA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface TransactionCalendarCriteriaRepo extends JpaRepository<R_PAY_TRANSACTION_CALENDAR_CRITERIA, Long> {

    List<R_PAY_TRANSACTION_CALENDAR_CRITERIA> findAllByTransactionCalendarId(Long transactionCalendarId);

    void deleteAllByIdNotInAndTransactionCalendarId(List<Long> idList, Long transactionCalendarId);
}

package com.dbs.database.crm.repositories.payment;

import com.dbs.database.crm.entities.payment.R_PAY_RECONCILE_RESULT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface ReconcileResultRepo extends JpaRepository<R_PAY_RECONCILE_RESULT, Long> {

}
